package org.bson.io;

import java.io.*;

public class Bits
{
    public static void readFully(final InputStream in, final byte[] b) throws IOException {
        readFully(in, b, b.length);
    }
    
    public static void readFully(final InputStream in, final byte[] b, final int length) throws IOException {
        readFully(in, b, 0, length);
    }
    
    public static void readFully(final InputStream in, final byte[] b, final int startOffset, final int length) throws IOException {
        if (b.length < length + startOffset) {
            throw new IllegalArgumentException("Buffer is too small");
        }
        int bytesRead;
        for (int offset = startOffset, toRead = length; toRead > 0; toRead -= bytesRead, offset += bytesRead) {
            bytesRead = in.read(b, offset, toRead);
            if (bytesRead < 0) {
                throw new EOFException();
            }
        }
    }
    
    public static int readInt(final InputStream in) throws IOException {
        return readInt(in, new byte[4]);
    }
    
    public static int readInt(final InputStream in, final byte[] data) throws IOException {
        readFully(in, data, 4);
        return readInt(data);
    }
    
    public static int readInt(final byte[] data) {
        return readInt(data, 0);
    }
    
    public static int readInt(final byte[] data, final int offset) {
        int x = 0;
        x |= (0xFF & data[offset + 0]) << 0;
        x |= (0xFF & data[offset + 1]) << 8;
        x |= (0xFF & data[offset + 2]) << 16;
        x |= (0xFF & data[offset + 3]) << 24;
        return x;
    }
    
    public static int readIntBE(final byte[] data, final int offset) {
        int x = 0;
        x |= (0xFF & data[offset + 0]) << 24;
        x |= (0xFF & data[offset + 1]) << 16;
        x |= (0xFF & data[offset + 2]) << 8;
        x |= (0xFF & data[offset + 3]) << 0;
        return x;
    }
    
    public static long readLong(final InputStream in) throws IOException {
        return readLong(in, new byte[8]);
    }
    
    public static long readLong(final InputStream in, final byte[] data) throws IOException {
        readFully(in, data, 8);
        return readLong(data);
    }
    
    public static long readLong(final byte[] data) {
        return readLong(data, 0);
    }
    
    public static long readLong(final byte[] data, final int offset) {
        long x = 0L;
        x |= (0xFFL & data[offset + 0]) << 0;
        x |= (0xFFL & data[offset + 1]) << 8;
        x |= (0xFFL & data[offset + 2]) << 16;
        x |= (0xFFL & data[offset + 3]) << 24;
        x |= (0xFFL & data[offset + 4]) << 32;
        x |= (0xFFL & data[offset + 5]) << 40;
        x |= (0xFFL & data[offset + 6]) << 48;
        x |= (0xFFL & data[offset + 7]) << 56;
        return x;
    }
}
