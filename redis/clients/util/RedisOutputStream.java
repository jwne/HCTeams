package redis.clients.util;

import java.io.*;

public final class RedisOutputStream extends FilterOutputStream
{
    protected final byte[] buf;
    protected int count;
    private static final int[] sizeTable;
    private static final byte[] DigitTens;
    private static final byte[] DigitOnes;
    private static final byte[] digits;
    
    public RedisOutputStream(final OutputStream out) {
        this(out, 8192);
    }
    
    public RedisOutputStream(final OutputStream out, final int size) {
        super(out);
        if (size <= 0) {
            throw new IllegalArgumentException("Buffer size <= 0");
        }
        this.buf = new byte[size];
    }
    
    private void flushBuffer() throws IOException {
        if (this.count > 0) {
            this.out.write(this.buf, 0, this.count);
            this.count = 0;
        }
    }
    
    public void write(final byte b) throws IOException {
        if (this.count == this.buf.length) {
            this.flushBuffer();
        }
        this.buf[this.count++] = b;
    }
    
    @Override
    public void write(final byte[] b) throws IOException {
        this.write(b, 0, b.length);
    }
    
    @Override
    public void write(final byte[] b, final int off, final int len) throws IOException {
        if (len >= this.buf.length) {
            this.flushBuffer();
            this.out.write(b, off, len);
        }
        else {
            if (len >= this.buf.length - this.count) {
                this.flushBuffer();
            }
            System.arraycopy(b, off, this.buf, this.count, len);
            this.count += len;
        }
    }
    
    public void writeAsciiCrLf(final String in) throws IOException {
        for (int size = in.length(), i = 0; i != size; ++i) {
            if (this.count == this.buf.length) {
                this.flushBuffer();
            }
            this.buf[this.count++] = (byte)in.charAt(i);
        }
        this.writeCrLf();
    }
    
    public static boolean isSurrogate(final char ch) {
        return ch >= '\ud800' && ch <= '\udfff';
    }
    
    public static int utf8Length(final String str) {
        final int strLen = str.length();
        int utfLen = 0;
        for (int i = 0; i != strLen; ++i) {
            final char c = str.charAt(i);
            if (c < '\u0080') {
                ++utfLen;
            }
            else if (c < '\u0800') {
                utfLen += 2;
            }
            else if (isSurrogate(c)) {
                ++i;
                utfLen += 4;
            }
            else {
                utfLen += 3;
            }
        }
        return utfLen;
    }
    
    public void writeCrLf() throws IOException {
        if (2 >= this.buf.length - this.count) {
            this.flushBuffer();
        }
        this.buf[this.count++] = 13;
        this.buf[this.count++] = 10;
    }
    
    public void writeUtf8CrLf(final String str) throws IOException {
        int strLen;
        int i;
        for (strLen = str.length(), i = 0; i < strLen; ++i) {
            final char c = str.charAt(i);
            if (c >= '\u0080') {
                break;
            }
            if (this.count == this.buf.length) {
                this.flushBuffer();
            }
            this.buf[this.count++] = (byte)c;
        }
        while (i < strLen) {
            final char c = str.charAt(i);
            if (c < '\u0080') {
                if (this.count == this.buf.length) {
                    this.flushBuffer();
                }
                this.buf[this.count++] = (byte)c;
            }
            else if (c < '\u0800') {
                if (2 >= this.buf.length - this.count) {
                    this.flushBuffer();
                }
                this.buf[this.count++] = (byte)('\u00c0' | c >> 6);
                this.buf[this.count++] = (byte)('\u0080' | (c & '?'));
            }
            else if (isSurrogate(c)) {
                if (4 >= this.buf.length - this.count) {
                    this.flushBuffer();
                }
                final int uc = Character.toCodePoint(c, str.charAt(i++));
                this.buf[this.count++] = (byte)(0xF0 | uc >> 18);
                this.buf[this.count++] = (byte)(0x80 | (uc >> 12 & 0x3F));
                this.buf[this.count++] = (byte)(0x80 | (uc >> 6 & 0x3F));
                this.buf[this.count++] = (byte)(0x80 | (uc & 0x3F));
            }
            else {
                if (3 >= this.buf.length - this.count) {
                    this.flushBuffer();
                }
                this.buf[this.count++] = (byte)('\u00e0' | c >> 12);
                this.buf[this.count++] = (byte)('\u0080' | (c >> 6 & '?'));
                this.buf[this.count++] = (byte)('\u0080' | (c & '?'));
            }
            ++i;
        }
        this.writeCrLf();
    }
    
    public void writeIntCrLf(int value) throws IOException {
        if (value < 0) {
            this.write((byte)45);
            value = -value;
        }
        int size;
        for (size = 0; value > RedisOutputStream.sizeTable[size]; ++size) {}
        if (++size >= this.buf.length - this.count) {
            this.flushBuffer();
        }
        int charPos;
        int q;
        int r;
        for (charPos = this.count + size; value >= 65536; value = q, this.buf[--charPos] = RedisOutputStream.DigitOnes[r], this.buf[--charPos] = RedisOutputStream.DigitTens[r]) {
            q = value / 100;
            r = value - ((q << 6) + (q << 5) + (q << 2));
        }
        do {
            q = value * 52429 >>> 19;
            r = value - ((q << 3) + (q << 1));
            this.buf[--charPos] = RedisOutputStream.digits[r];
            value = q;
        } while (value != 0);
        this.count += size;
        this.writeCrLf();
    }
    
    @Override
    public void flush() throws IOException {
        this.flushBuffer();
        this.out.flush();
    }
    
    static {
        sizeTable = new int[] { 9, 99, 999, 9999, 99999, 999999, 9999999, 99999999, 999999999, Integer.MAX_VALUE };
        DigitTens = new byte[] { 48, 48, 48, 48, 48, 48, 48, 48, 48, 48, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 51, 51, 51, 51, 51, 51, 51, 51, 51, 51, 52, 52, 52, 52, 52, 52, 52, 52, 52, 52, 53, 53, 53, 53, 53, 53, 53, 53, 53, 53, 54, 54, 54, 54, 54, 54, 54, 54, 54, 54, 55, 55, 55, 55, 55, 55, 55, 55, 55, 55, 56, 56, 56, 56, 56, 56, 56, 56, 56, 56, 57, 57, 57, 57, 57, 57, 57, 57, 57, 57 };
        DigitOnes = new byte[] { 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57 };
        digits = new byte[] { 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122 };
    }
}
