package com.mongodb;

import java.nio.*;
import org.bson.*;
import java.io.*;
import org.bson.types.*;
import java.util.*;

@Deprecated
public class RawDBObject implements DBObject
{
    final ByteBuffer _buf;
    final int _offset;
    final int _end;
    private static final byte[] _cStrBuf;
    
    RawDBObject(final ByteBuffer buf) {
        this(buf, 0);
    }
    
    RawDBObject(final ByteBuffer buf, final int offset) {
        super();
        this._buf = buf;
        this._offset = offset;
        this._end = this._buf.getInt(this._offset);
    }
    
    public Object get(final String key) {
        final Element e = this.findElement(key);
        if (e == null) {
            return null;
        }
        return e.getObject();
    }
    
    public Map toMap() {
        final Map m = new HashMap();
        for (final Object s : this.keySet()) {
            m.put(s, this.get(String.valueOf(s)));
        }
        return m;
    }
    
    public Object put(final String key, final Object v) {
        throw new UnsupportedOperationException("Object is read only");
    }
    
    public void putAll(final BSONObject o) {
        throw new UnsupportedOperationException("Object is read only");
    }
    
    public void putAll(final Map m) {
        throw new UnsupportedOperationException("Object is read only");
    }
    
    public Object removeField(final String key) {
        throw new UnsupportedOperationException("Object is read only");
    }
    
    @Deprecated
    public boolean containsKey(final String key) {
        return this.containsField(key);
    }
    
    public boolean containsField(final String field) {
        return this.findElement(field) != null;
    }
    
    public Set<String> keySet() {
        final Set<String> keys = new HashSet<String>();
        final ElementIter i = new ElementIter();
        while (i.hasNext()) {
            final Element e = i.next();
            if (e.eoo()) {
                break;
            }
            keys.add(e.fieldName());
        }
        return keys;
    }
    
    String _readCStr(final int start) {
        return this._readCStr(start, null);
    }
    
    String _readCStr(final int start, final int[] end) {
        synchronized (RawDBObject._cStrBuf) {
            int pos = 0;
            while (this._buf.get(pos + start) != 0) {
                RawDBObject._cStrBuf[pos] = this._buf.get(pos + start);
                if (++pos >= RawDBObject._cStrBuf.length) {
                    throw new IllegalArgumentException("c string too big for RawDBObject.  so far[" + new String(RawDBObject._cStrBuf) + "]");
                }
                if (pos + start >= this._buf.limit()) {
                    final StringBuilder sb = new StringBuilder();
                    for (int x = 0; x < 10; ++x) {
                        final int y = start + x;
                        if (y >= this._buf.limit()) {
                            break;
                        }
                        sb.append((char)this._buf.get(y));
                    }
                    throw new IllegalArgumentException("can't find end of cstring.  start:" + start + " pos: " + pos + " [" + (Object)sb + "]");
                }
            }
            if (end != null && end.length > 0) {
                end[0] = start + pos;
            }
            return new String(RawDBObject._cStrBuf, 0, pos);
        }
    }
    
    String _readJavaString(final int start) {
        final int size = this._buf.getInt(start) - 1;
        final byte[] b = new byte[size];
        final int old = this._buf.position();
        this._buf.position(start + 4);
        this._buf.get(b, 0, b.length);
        this._buf.position(old);
        try {
            return new String(b, "UTF-8");
        }
        catch (UnsupportedEncodingException uee) {
            return new String(b);
        }
    }
    
    int _cStrLength(final int start) {
        int end;
        for (end = start; this._buf.get(end) != 0; ++end) {}
        return 1 + (end - start);
    }
    
    Element findElement(final String name) {
        final ElementIter i = new ElementIter();
        while (i.hasNext()) {
            final Element e = i.next();
            if (e.fieldName().equals(name)) {
                return e;
            }
        }
        return null;
    }
    
    public boolean isPartialObject() {
        return false;
    }
    
    public void markAsPartialObject() {
        throw new RuntimeException("RawDBObject can't be a partial object");
    }
    
    public String toString() {
        return "Object";
    }
    
    static {
        _cStrBuf = new byte[1024];
    }
    
    class Element
    {
        final int _start;
        final byte _type;
        final String _name;
        final int _dataStart;
        final int _size;
        Object _cached;
        
        Element(final int start) {
            super();
            this._start = start;
            this._type = RawDBObject.this._buf.get(this._start);
            final int[] end = { 0 };
            this._name = (this.eoo() ? "" : RawDBObject.this._readCStr(this._start + 1, end));
            int size = 1 + (end[0] - this._start);
            this._dataStart = this._start + size;
            switch (this._type) {
                case -1:
                case 0:
                case 6:
                case 10:
                case Byte.MAX_VALUE: {
                    break;
                }
                case 8: {
                    ++size;
                    break;
                }
                case 1:
                case 9:
                case 18: {
                    size += 8;
                    break;
                }
                case 16: {
                    size += 4;
                    break;
                }
                case 7: {
                    size += 12;
                    break;
                }
                case 12: {
                    size += 12;
                    size += 4 + RawDBObject.this._buf.getInt(this._dataStart);
                    break;
                }
                case 2:
                case 13:
                case 14: {
                    size += 4 + RawDBObject.this._buf.getInt(this._dataStart);
                    break;
                }
                case 3:
                case 4:
                case 15: {
                    size += RawDBObject.this._buf.getInt(this._dataStart);
                    break;
                }
                case 5: {
                    size += 4 + RawDBObject.this._buf.getInt(this._dataStart) + 1;
                    break;
                }
                case 11: {
                    final int first = RawDBObject.this._cStrLength(this._dataStart);
                    final int second = RawDBObject.this._cStrLength(this._dataStart + first);
                    size += first + second;
                    break;
                }
                case 17: {
                    size += 8;
                    break;
                }
                default: {
                    throw new RuntimeException("RawDBObject can't size type " + this._type);
                }
            }
            this._size = size;
        }
        
        String fieldName() {
            return this._name;
        }
        
        boolean eoo() {
            return this._type == 0 || this._type == 127;
        }
        
        int size() {
            return this._size;
        }
        
        Object getObject() {
            if (this._cached != null) {
                return this._cached;
            }
            switch (this._type) {
                case 1: {
                    return RawDBObject.this._buf.getDouble(this._dataStart);
                }
                case 16: {
                    return RawDBObject.this._buf.getInt(this._dataStart);
                }
                case 7: {
                    return new ObjectId(RawDBObject.this._buf.getInt(this._dataStart), RawDBObject.this._buf.getInt(this._dataStart + 4), RawDBObject.this._buf.getInt(this._dataStart + 8));
                }
                case 13:
                case 15: {
                    throw new RuntimeException("can't handle code");
                }
                case 2:
                case 14: {
                    return RawDBObject.this._readJavaString(this._dataStart);
                }
                case 9: {
                    return new Date(RawDBObject.this._buf.getLong(this._dataStart));
                }
                case 11: {
                    throw new RuntimeException("can't handle regex");
                }
                case 5: {
                    throw new RuntimeException("can't inspect binary in db");
                }
                case 8: {
                    return RawDBObject.this._buf.get(this._dataStart) > 0;
                }
                case 3:
                case 4: {
                    throw new RuntimeException("can't handle emebdded objects");
                }
                case -1:
                case 0:
                case 6:
                case 10:
                case Byte.MAX_VALUE: {
                    return null;
                }
                default: {
                    throw new RuntimeException("can't decode type " + this._type);
                }
            }
        }
    }
    
    class ElementIter
    {
        int _pos;
        boolean _done;
        
        ElementIter() {
            super();
            this._done = false;
            this._pos = RawDBObject.this._offset + 4;
        }
        
        boolean hasNext() {
            return !this._done && this._pos < RawDBObject.this._buf.limit();
        }
        
        Element next() {
            final Element e = new Element(this._pos);
            this._done = e.eoo();
            this._pos += e.size();
            return e;
        }
    }
}
