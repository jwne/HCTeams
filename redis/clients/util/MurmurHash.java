package redis.clients.util;

import java.nio.*;

public class MurmurHash implements Hashing
{
    public static int hash(final byte[] data, final int seed) {
        return hash(ByteBuffer.wrap(data), seed);
    }
    
    public static int hash(final byte[] data, final int offset, final int length, final int seed) {
        return hash(ByteBuffer.wrap(data, offset, length), seed);
    }
    
    public static int hash(final ByteBuffer buf, final int seed) {
        final ByteOrder byteOrder = buf.order();
        buf.order(ByteOrder.LITTLE_ENDIAN);
        final int m = 1540483477;
        final int r = 24;
        int h = seed ^ buf.remaining();
        while (buf.remaining() >= 4) {
            int k = buf.getInt();
            k *= m;
            k ^= k >>> r;
            k *= m;
            h *= m;
            h ^= k;
        }
        if (buf.remaining() > 0) {
            final ByteBuffer finish = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
            finish.put(buf).rewind();
            h ^= finish.getInt();
            h *= m;
        }
        h ^= h >>> 13;
        h *= m;
        h ^= h >>> 15;
        buf.order(byteOrder);
        return h;
    }
    
    public static long hash64A(final byte[] data, final int seed) {
        return hash64A(ByteBuffer.wrap(data), seed);
    }
    
    public static long hash64A(final byte[] data, final int offset, final int length, final int seed) {
        return hash64A(ByteBuffer.wrap(data, offset, length), seed);
    }
    
    public static long hash64A(final ByteBuffer buf, final int seed) {
        final ByteOrder byteOrder = buf.order();
        buf.order(ByteOrder.LITTLE_ENDIAN);
        final long m = -4132994306676758123L;
        final int r = 47;
        long h = seed ^ buf.remaining() * m;
        while (buf.remaining() >= 8) {
            long k = buf.getLong();
            k *= m;
            k ^= k >>> r;
            k *= m;
            h ^= k;
            h *= m;
        }
        if (buf.remaining() > 0) {
            final ByteBuffer finish = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN);
            finish.put(buf).rewind();
            h ^= finish.getLong();
            h *= m;
        }
        h ^= h >>> r;
        h *= m;
        h ^= h >>> r;
        buf.order(byteOrder);
        return h;
    }
    
    @Override
    public long hash(final byte[] key) {
        return hash64A(key, 305441741);
    }
    
    @Override
    public long hash(final String key) {
        return this.hash(SafeEncoder.encode(key));
    }
}
