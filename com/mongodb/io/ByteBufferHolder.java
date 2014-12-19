package com.mongodb.io;

import java.nio.*;
import java.util.*;

@Deprecated
public class ByteBufferHolder
{
    List<ByteBuffer> _buffers;
    int _pos;
    final int _max;
    static final int _bufSize = 4096;
    
    public ByteBufferHolder() {
        this(1073741824);
    }
    
    public ByteBufferHolder(final int max) {
        super();
        this._buffers = new ArrayList<ByteBuffer>();
        this._pos = 0;
        this._max = max;
    }
    
    public byte get(final int i) {
        if (i >= this._pos) {
            throw new RuntimeException("out of bounds");
        }
        final int num = i / 4096;
        final int pos = i % 4096;
        return this._buffers.get(num).get(pos);
    }
    
    public void get(final int pos, final byte[] b) {
        for (int i = 0; i < b.length; ++i) {
            b[i] = this.get(i + pos);
        }
    }
    
    public void put(final int i, final byte val) {
        if (i >= this._pos) {
            throw new RuntimeException("out of bounds");
        }
        final int num = i / 4096;
        final int pos = i % 4096;
        this._buffers.get(num).put(pos, val);
    }
    
    public int position() {
        return this._pos;
    }
    
    public void position(final int p) {
        this._pos = p;
        final int num = this._pos / 4096;
        final int pos = this._pos % 4096;
        while (this._buffers.size() <= num) {
            this._addBucket();
        }
        final ByteBuffer bb = this._buffers.get(num);
        bb.position(pos);
        for (int i = num + 1; i < this._buffers.size(); ++i) {
            this._buffers.get(i).position(0);
        }
    }
    
    public int remaining() {
        return Integer.MAX_VALUE;
    }
    
    public void put(final ByteBuffer in) {
        while (in.hasRemaining()) {
            final int num = this._pos / 4096;
            if (num >= this._buffers.size()) {
                this._addBucket();
            }
            final ByteBuffer bb = this._buffers.get(num);
            final int canRead = Math.min(bb.remaining(), in.remaining());
            final int oldLimit = in.limit();
            in.limit(in.position() + canRead);
            bb.put(in);
            in.limit(oldLimit);
            this._pos += canRead;
        }
    }
    
    private void _addBucket() {
        if (this.capacity() + 4096 > this._max) {
            throw new RuntimeException("too big current:" + this.capacity());
        }
        this._buffers.add(ByteBuffer.allocateDirect(4096));
    }
    
    public int capacity() {
        return this._buffers.size() * 4096;
    }
    
    public String toString() {
        final StringBuilder buf = new StringBuilder();
        buf.append("{ ByteBufferHolder pos:" + this._pos + " ");
        for (final ByteBuffer bb : this._buffers) {
            buf.append(bb).append(" ");
        }
        return buf.append("}").toString();
    }
}
