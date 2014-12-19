package com.mongodb.io;

import java.io.*;
import java.nio.*;
import java.util.*;

@Deprecated
public class ByteBufferOutputStream extends OutputStream
{
    final List<ByteBuffer> _lst;
    final ByteBufferFactory _factory;
    static final ByteBufferFactory _defaultFactory;
    
    public ByteBufferOutputStream() {
        this(ByteBufferOutputStream._defaultFactory);
    }
    
    public ByteBufferOutputStream(final int size) {
        this(new ByteBufferFactory.SimpleHeapByteBufferFactory(size));
    }
    
    public ByteBufferOutputStream(final ByteBufferFactory factory) {
        super();
        this._lst = new ArrayList<ByteBuffer>();
        this._factory = factory;
    }
    
    public void close() {
    }
    
    public void flush() {
    }
    
    public void write(final byte[] b) {
        this.write(b, 0, b.length);
    }
    
    public void write(final byte[] b, final int off, final int len) {
        final ByteBuffer cur = this._need(1);
        final int toWrite = Math.min(len, cur.remaining());
        cur.put(b, off, toWrite);
        if (toWrite == len) {
            return;
        }
        this.write(b, off + toWrite, len - toWrite);
    }
    
    public void write(final int b) {
        this._need(1).put((byte)b);
    }
    
    public List<ByteBuffer> getBuffers() {
        return this._lst;
    }
    
    public List<ByteBuffer> getBuffers(final boolean flip) {
        if (flip) {
            for (final ByteBuffer buf : this._lst) {
                buf.flip();
            }
        }
        return this._lst;
    }
    
    private ByteBuffer _need(final int space) {
        if (this._lst.size() == 0) {
            this._lst.add(this._factory.get());
            return this._lst.get(0);
        }
        final ByteBuffer cur = this._lst.get(this._lst.size() - 1);
        if (space <= cur.remaining()) {
            return cur;
        }
        this._lst.add(this._factory.get());
        return this._lst.get(this._lst.size() - 1);
    }
    
    static {
        _defaultFactory = new ByteBufferFactory.SimpleHeapByteBufferFactory(4096);
    }
}
