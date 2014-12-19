package org.bson.io;

import org.bson.*;
import java.io.*;
import java.nio.*;

@Deprecated
public class BSONByteBuffer
{
    protected ByteBuffer buf;
    
    private BSONByteBuffer(final ByteBuffer buf) {
        super();
        (this.buf = buf).order(ByteOrder.LITTLE_ENDIAN);
    }
    
    public static BSONByteBuffer wrap(final byte[] bytes, final int offset, final int length) {
        return new BSONByteBuffer(ByteBuffer.wrap(bytes, offset, length));
    }
    
    public static BSONByteBuffer wrap(final byte[] bytes) {
        return new BSONByteBuffer(ByteBuffer.wrap(bytes));
    }
    
    public byte get(final int i) {
        return this.buf.get(i);
    }
    
    public ByteBuffer get(final byte[] bytes, final int offset, final int length) {
        return this.buf.get(bytes, offset, length);
    }
    
    public ByteBuffer get(final byte[] bytes) {
        return this.buf.get(bytes);
    }
    
    public byte[] array() {
        return this.buf.array();
    }
    
    public String toString() {
        return this.buf.toString();
    }
    
    public int hashCode() {
        return this.buf.hashCode();
    }
    
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final BSONByteBuffer that = (BSONByteBuffer)o;
        if (this.buf != null) {
            if (this.buf.equals(that.buf)) {
                return true;
            }
        }
        else if (that.buf == null) {
            return true;
        }
        return false;
    }
    
    public int getInt(final int i) {
        return this.getIntLE(i);
    }
    
    public int getIntLE(final int i) {
        int x = 0;
        x |= (0xFF & this.buf.get(i + 0)) << 0;
        x |= (0xFF & this.buf.get(i + 1)) << 8;
        x |= (0xFF & this.buf.get(i + 2)) << 16;
        x |= (0xFF & this.buf.get(i + 3)) << 24;
        return x;
    }
    
    public int getIntBE(final int i) {
        int x = 0;
        x |= (0xFF & this.buf.get(i + 0)) << 24;
        x |= (0xFF & this.buf.get(i + 1)) << 16;
        x |= (0xFF & this.buf.get(i + 2)) << 8;
        x |= (0xFF & this.buf.get(i + 3)) << 0;
        return x;
    }
    
    public long getLong(final int i) {
        return this.buf.getLong(i);
    }
    
    public String getCString(final int offset) {
        int end;
        for (end = offset; this.get(end) != 0; ++end) {}
        final int len = end - offset;
        return new String(this.array(), offset, len);
    }
    
    public String getUTF8String(final int valueOffset) {
        final int size = this.getInt(valueOffset) - 1;
        try {
            return new String(this.array(), valueOffset + 4, size, "UTF-8");
        }
        catch (UnsupportedEncodingException e) {
            throw new BSONException("Cannot decode string as UTF-8.");
        }
    }
    
    public Buffer position(final int i) {
        return this.buf.position(i);
    }
    
    public Buffer reset() {
        return this.buf.reset();
    }
    
    public int size() {
        return this.getInt(0);
    }
}
