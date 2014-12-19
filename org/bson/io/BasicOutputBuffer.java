package org.bson.io;

import java.io.*;

public class BasicOutputBuffer extends OutputBuffer
{
    private int _cur;
    private int _size;
    private byte[] _buffer;
    
    public BasicOutputBuffer() {
        super();
        this._buffer = new byte[512];
    }
    
    public void write(final byte[] b) {
        this.write(b, 0, b.length);
    }
    
    public void write(final byte[] b, final int off, final int len) {
        this._ensure(len);
        System.arraycopy(b, off, this._buffer, this._cur, len);
        this._cur += len;
        this._size = Math.max(this._cur, this._size);
    }
    
    public void write(final int b) {
        this._ensure(1);
        this._buffer[this._cur++] = (byte)(0xFF & b);
        this._size = Math.max(this._cur, this._size);
    }
    
    public int getPosition() {
        return this._cur;
    }
    
    @Deprecated
    public void setPosition(final int position) {
        this._cur = position;
    }
    
    @Deprecated
    public void seekEnd() {
        this._cur = this._size;
    }
    
    @Deprecated
    public void seekStart() {
        this._cur = 0;
    }
    
    public int size() {
        return this._size;
    }
    
    public int pipe(final OutputStream out) throws IOException {
        out.write(this._buffer, 0, this._size);
        return this._size;
    }
    
    @Deprecated
    public int pipe(final DataOutput out) throws IOException {
        out.write(this._buffer, 0, this._size);
        return this._size;
    }
    
    void _ensure(final int more) {
        final int need = this._cur + more;
        if (need < this._buffer.length) {
            return;
        }
        int newSize = this._buffer.length * 2;
        if (newSize <= need) {
            newSize = need + 128;
        }
        final byte[] n = new byte[newSize];
        System.arraycopy(this._buffer, 0, n, 0, this._size);
        this._buffer = n;
    }
    
    @Deprecated
    public String asString() {
        return new String(this._buffer, 0, this._size);
    }
    
    @Deprecated
    public String asString(final String encoding) throws UnsupportedEncodingException {
        return new String(this._buffer, 0, this._size, encoding);
    }
}
