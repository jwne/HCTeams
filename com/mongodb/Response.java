package com.mongodb;

import org.bson.io.*;
import java.io.*;
import java.util.*;
import org.bson.*;

class Response
{
    final ServerAddress _host;
    final int _len;
    final int _id;
    final int _responseTo;
    final int _operation;
    final int _flags;
    long _cursor;
    final int _startingFrom;
    final int _num;
    final List<DBObject> _objects;
    private static final int MAX_LENGTH = 33554432;
    
    Response(final ServerAddress addr, final DBCollection collection, final InputStream in, final DBDecoder decoder) throws IOException {
        super();
        this._host = addr;
        final byte[] b = new byte[36];
        Bits.readFully(in, b);
        int pos = 0;
        this._len = Bits.readInt(b, pos);
        pos += 4;
        if (this._len > 33554432) {
            throw new IllegalArgumentException("response too long: " + this._len);
        }
        this._id = Bits.readInt(b, pos);
        pos += 4;
        this._responseTo = Bits.readInt(b, pos);
        pos += 4;
        this._operation = Bits.readInt(b, pos);
        pos += 4;
        this._flags = Bits.readInt(b, pos);
        pos += 4;
        this._cursor = Bits.readLong(b, pos);
        pos += 8;
        this._startingFrom = Bits.readInt(b, pos);
        pos += 4;
        this._num = Bits.readInt(b, pos);
        pos += 4;
        final MyInputStream user = new MyInputStream(in, this._len - b.length);
        if (this._num < 2) {
            this._objects = new LinkedList<DBObject>();
        }
        else {
            this._objects = new ArrayList<DBObject>(this._num);
        }
        for (int i = 0; i < this._num; ++i) {
            if (user._toGo < 5) {
                throw new IOException("should have more objects, but only " + user._toGo + " bytes left");
            }
            this._objects.add(decoder.decode(user, collection));
        }
        if (user._toGo != 0) {
            throw new IOException("finished reading objects but still have: " + user._toGo + " bytes to read!' ");
        }
        if (this._num != this._objects.size()) {
            throw new RuntimeException("something is really broken");
        }
    }
    
    public int size() {
        return this._num;
    }
    
    public ServerAddress serverUsed() {
        return this._host;
    }
    
    public DBObject get(final int i) {
        return this._objects.get(i);
    }
    
    public Iterator<DBObject> iterator() {
        return this._objects.iterator();
    }
    
    public long cursor() {
        return this._cursor;
    }
    
    public ServerError getError() {
        if (this._num != 1) {
            return null;
        }
        final DBObject obj = this.get(0);
        if (ServerError.getMsg(obj, null) == null) {
            return null;
        }
        return new ServerError(obj);
    }
    
    public String toString() {
        return "flags:" + this._flags + " _cursor:" + this._cursor + " _startingFrom:" + this._startingFrom + " _num:" + this._num;
    }
    
    static class MyInputStream extends InputStream
    {
        final InputStream _in;
        private int _toGo;
        
        MyInputStream(final InputStream in, final int max) {
            super();
            this._in = in;
            this._toGo = max;
        }
        
        public int available() throws IOException {
            return this._in.available();
        }
        
        public int read() throws IOException {
            if (this._toGo <= 0) {
                return -1;
            }
            final int val = this._in.read();
            --this._toGo;
            return val;
        }
        
        public int read(final byte[] b, final int off, final int len) throws IOException {
            if (this._toGo <= 0) {
                return -1;
            }
            final int n = this._in.read(b, off, Math.min(this._toGo, len));
            this._toGo -= n;
            return n;
        }
        
        public void close() {
            throw new RuntimeException("can't close thos");
        }
    }
}
