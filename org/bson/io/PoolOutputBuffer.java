package org.bson.io;

import org.bson.util.*;
import java.util.*;
import java.io.*;

@Deprecated
public class PoolOutputBuffer extends OutputBuffer
{
    public static final int BUF_SIZE = 16384;
    final byte[] _mine;
    final char[] _chars;
    final List<byte[]> _fromPool;
    final UTF8Encoding _encoding;
    private static final String DEFAULT_ENCODING_1 = "UTF-8";
    private static final String DEFAULT_ENCODING_2 = "UTF8";
    private final Position _cur;
    private final Position _end;
    private static SimplePool<byte[]> _extra;
    
    public PoolOutputBuffer() {
        super();
        this._mine = new byte[16384];
        this._chars = new char[16384];
        this._fromPool = new ArrayList<byte[]>();
        this._encoding = new UTF8Encoding();
        this._cur = new Position();
        this._end = new Position();
        this.reset();
    }
    
    public void reset() {
        this._cur.reset();
        this._end.reset();
        for (int i = 0; i < this._fromPool.size(); ++i) {
            PoolOutputBuffer._extra.done(this._fromPool.get(i));
        }
        this._fromPool.clear();
    }
    
    public int getPosition() {
        return this._cur.pos();
    }
    
    @Deprecated
    public void setPosition(final int position) {
        this._cur.reset(position);
    }
    
    @Deprecated
    public void seekEnd() {
        this._cur.reset(this._end);
    }
    
    @Deprecated
    public void seekStart() {
        this._cur.reset();
    }
    
    public int size() {
        return this._end.pos();
    }
    
    public void write(final byte[] b) {
        this.write(b, 0, b.length);
    }
    
    public void write(final byte[] b, int off, int len) {
        while (len > 0) {
            final byte[] bs = this._cur();
            final int space = Math.min(bs.length - this._cur.y, len);
            System.arraycopy(b, off, bs, this._cur.y, space);
            this._cur.inc(space);
            len -= space;
            off += space;
            this._afterWrite();
        }
    }
    
    public void write(final int b) {
        final byte[] bs = this._cur();
        bs[this._cur.getAndInc()] = (byte)(b & 0xFF);
        this._afterWrite();
    }
    
    public void truncateToPosition(final int newPosition) {
        this.setPosition(newPosition);
        this._end.reset(this._cur);
    }
    
    void _afterWrite() {
        if (this._cur.pos() < this._end.pos()) {
            if (this._cur.y == 16384) {
                this._cur.nextBuffer();
            }
            return;
        }
        this._end.reset(this._cur);
        if (this._end.y < 16384) {
            return;
        }
        this._fromPool.add(PoolOutputBuffer._extra.get());
        this._end.nextBuffer();
        this._cur.reset(this._end);
    }
    
    byte[] _cur() {
        return this._get(this._cur.x);
    }
    
    byte[] _get(final int z) {
        if (z < 0) {
            return this._mine;
        }
        return this._fromPool.get(z);
    }
    
    public int pipe(final OutputStream out) throws IOException {
        if (out == null) {
            throw new NullPointerException("out is null");
        }
        int total = 0;
        for (int i = -1; i < this._fromPool.size(); ++i) {
            final byte[] b = this._get(i);
            final int amt = this._end.len(i);
            if (amt == 0) {
                break;
            }
            out.write(b, 0, amt);
            total += amt;
        }
        return total;
    }
    
    public String asAscii() {
        if (this._fromPool.size() > 0) {
            return super.asString();
        }
        final int m = this.size();
        final char[] c = (m < this._chars.length) ? this._chars : new char[m];
        for (int i = 0; i < m; ++i) {
            c[i] = (char)this._mine[i];
        }
        return new String(c, 0, m);
    }
    
    @Deprecated
    public String asString(final String encoding) throws UnsupportedEncodingException {
        if (this._fromPool.size() > 0) {
            return super.asString(encoding);
        }
        if (!encoding.equals("UTF-8")) {
            if (!encoding.equals("UTF8")) {
                return new String(this._mine, 0, this.size(), encoding);
            }
        }
        try {
            return this._encoding.decode(this._mine, 0, this.size());
        }
        catch (IOException ex) {}
        return new String(this._mine, 0, this.size(), encoding);
    }
    
    static {
        PoolOutputBuffer._extra = new SimplePool<byte[]>(640) {
            protected byte[] createNew() {
                return new byte[16384];
            }
        };
    }
    
    static class Position
    {
        int x;
        int y;
        
        Position() {
            super();
            this.reset();
        }
        
        void reset() {
            this.x = -1;
            this.y = 0;
        }
        
        void reset(final Position other) {
            this.x = other.x;
            this.y = other.y;
        }
        
        void reset(final int pos) {
            this.x = pos / 16384 - 1;
            this.y = pos % 16384;
        }
        
        int pos() {
            return (this.x + 1) * 16384 + this.y;
        }
        
        int getAndInc() {
            return this.y++;
        }
        
        void inc(final int amt) {
            this.y += amt;
            if (this.y > 16384) {
                throw new IllegalArgumentException("something is wrong");
            }
        }
        
        void nextBuffer() {
            if (this.y != 16384) {
                throw new IllegalArgumentException("broken");
            }
            ++this.x;
            this.y = 0;
        }
        
        int len(final int which) {
            if (which < this.x) {
                return 16384;
            }
            if (which == this.x) {
                return this.y;
            }
            return 0;
        }
        
        public String toString() {
            return this.x + "," + this.y;
        }
    }
}
