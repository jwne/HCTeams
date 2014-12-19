package org.bson.io;

import java.io.*;
import java.security.*;
import com.mongodb.util.*;
import org.bson.*;

public abstract class OutputBuffer extends OutputStream
{
    public abstract void write(final byte[] p0);
    
    public abstract void write(final byte[] p0, final int p1, final int p2);
    
    public abstract void write(final int p0);
    
    public abstract int getPosition();
    
    @Deprecated
    public abstract void setPosition(final int p0);
    
    @Deprecated
    public abstract void seekEnd();
    
    @Deprecated
    public abstract void seekStart();
    
    public abstract int size();
    
    public abstract int pipe(final OutputStream p0) throws IOException;
    
    public byte[] toByteArray() {
        try {
            final ByteArrayOutputStream bout = new ByteArrayOutputStream(this.size());
            this.pipe(bout);
            return bout.toByteArray();
        }
        catch (IOException ioe) {
            throw new RuntimeException("should be impossible", ioe);
        }
    }
    
    @Deprecated
    public String asString() {
        return new String(this.toByteArray());
    }
    
    @Deprecated
    public String asString(final String encoding) throws UnsupportedEncodingException {
        return new String(this.toByteArray(), encoding);
    }
    
    @Deprecated
    public String hex() {
        final StringBuilder buf = new StringBuilder();
        try {
            this.pipe(new OutputStream() {
                public void write(final int b) {
                    final String s = Integer.toHexString(0xFF & b);
                    if (s.length() < 2) {
                        buf.append("0");
                    }
                    buf.append(s);
                }
            });
        }
        catch (IOException ioe) {
            throw new RuntimeException("impossible");
        }
        return buf.toString();
    }
    
    @Deprecated
    public String md5() {
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error - this implementation of Java doesn't support MD5.");
        }
        md5.reset();
        try {
            this.pipe(new OutputStream() {
                public void write(final byte[] b, final int off, final int len) {
                    md5.update(b, off, len);
                }
                
                public void write(final int b) {
                    md5.update((byte)(b & 0xFF));
                }
            });
        }
        catch (IOException ioe) {
            throw new RuntimeException("impossible");
        }
        return Util.toHex(md5.digest());
    }
    
    public void writeInt(final int x) {
        this.write(x >> 0);
        this.write(x >> 8);
        this.write(x >> 16);
        this.write(x >> 24);
    }
    
    @Deprecated
    public void writeIntBE(final int x) {
        this.write(x >> 24);
        this.write(x >> 16);
        this.write(x >> 8);
        this.write(x);
    }
    
    @Deprecated
    public void writeInt(final int pos, final int x) {
        final int save = this.getPosition();
        this.setPosition(pos);
        this.writeInt(x);
        this.setPosition(save);
    }
    
    public void writeLong(final long x) {
        this.write((byte)(0xFFL & x >> 0));
        this.write((byte)(0xFFL & x >> 8));
        this.write((byte)(0xFFL & x >> 16));
        this.write((byte)(0xFFL & x >> 24));
        this.write((byte)(0xFFL & x >> 32));
        this.write((byte)(0xFFL & x >> 40));
        this.write((byte)(0xFFL & x >> 48));
        this.write((byte)(0xFFL & x >> 56));
    }
    
    public void writeDouble(final double x) {
        this.writeLong(Double.doubleToRawLongBits(x));
    }
    
    public void writeString(final String str) {
        this.writeInt(0);
        final int strLen = this.writeCString(str, false);
        this.backpatchSize(strLen, 4);
    }
    
    public int writeCString(final String str) {
        return this.writeCString(str, true);
    }
    
    private int writeCString(final String str, final boolean checkForNullCharacters) {
        final int len = str.length();
        int total = 0;
        int c;
        for (int i = 0; i < len; i += Character.charCount(c)) {
            c = Character.codePointAt(str, i);
            if (checkForNullCharacters && c == 0) {
                throw new BSONException(String.format("BSON cstring '%s' is not valid because it contains a null character at index %d", str, i));
            }
            if (c < 128) {
                this.write((byte)c);
                ++total;
            }
            else if (c < 2048) {
                this.write((byte)(192 + (c >> 6)));
                this.write((byte)(128 + (c & 0x3F)));
                total += 2;
            }
            else if (c < 65536) {
                this.write((byte)(224 + (c >> 12)));
                this.write((byte)(128 + (c >> 6 & 0x3F)));
                this.write((byte)(128 + (c & 0x3F)));
                total += 3;
            }
            else {
                this.write((byte)(240 + (c >> 18)));
                this.write((byte)(128 + (c >> 12 & 0x3F)));
                this.write((byte)(128 + (c >> 6 & 0x3F)));
                this.write((byte)(128 + (c & 0x3F)));
                total += 4;
            }
        }
        this.write(0);
        return ++total;
    }
    
    public String toString() {
        return this.getClass().getName() + " size: " + this.size() + " pos: " + this.getPosition();
    }
    
    public void backpatchSize(final int size) {
        this.writeInt(this.getPosition() - size, size);
    }
    
    protected void backpatchSize(final int size, final int additionalOffset) {
        this.writeInt(this.getPosition() - size - additionalOffset, size);
    }
    
    public void truncateToPosition(final int newPosition) {
        this.setPosition(newPosition);
    }
}
