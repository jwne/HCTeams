package com.mongodb.io;

import java.io.*;
import java.nio.*;
import java.util.*;

@Deprecated
public class ByteBufferInputStream extends InputStream
{
    final List<ByteBuffer> _lst;
    private int _pos;
    
    public ByteBufferInputStream(final List<ByteBuffer> lst) {
        this(lst, false);
    }
    
    public ByteBufferInputStream(final List<ByteBuffer> lst, final boolean flip) {
        super();
        this._pos = 0;
        this._lst = lst;
        if (flip) {
            for (final ByteBuffer buf : this._lst) {
                buf.flip();
            }
        }
    }
    
    public int available() {
        int avail = 0;
        for (int i = this._pos; i < this._lst.size(); ++i) {
            avail += this._lst.get(i).remaining();
        }
        return avail;
    }
    
    public void close() {
    }
    
    public void mark(final int readlimit) {
        throw new RuntimeException("mark not supported");
    }
    
    public void reset() {
        throw new RuntimeException("mark not supported");
    }
    
    public boolean markSupported() {
        return false;
    }
    
    public int read() {
        if (this._pos >= this._lst.size()) {
            return -1;
        }
        final ByteBuffer buf = this._lst.get(this._pos);
        if (buf.remaining() > 0) {
            return buf.get() & 0xFF;
        }
        ++this._pos;
        return this.read();
    }
    
    public int read(final byte[] b) {
        return this.read(b, 0, b.length);
    }
    
    public int read(final byte[] b, final int off, final int len) {
        if (this._pos >= this._lst.size()) {
            return -1;
        }
        final ByteBuffer buf = this._lst.get(this._pos);
        if (buf.remaining() == 0) {
            ++this._pos;
            return this.read(b, off, len);
        }
        final int toRead = Math.min(len, buf.remaining());
        buf.get(b, off, toRead);
        if (toRead == len || this._pos + 1 >= this._lst.size()) {
            return toRead;
        }
        ++this._pos;
        return toRead + this.read(b, off + toRead, len - toRead);
    }
    
    public long skip(long n) {
        long skipped = 0L;
        while (n >= 0L && this._pos < this._lst.size()) {
            final ByteBuffer b = this._lst.get(this._pos);
            if (b.remaining() >= n) {
                skipped += n;
                b.position((int)(b.position() + n));
                return skipped;
            }
            skipped += b.remaining();
            n -= b.remaining();
            b.position(b.limit());
            ++this._pos;
        }
        return skipped;
    }
}
