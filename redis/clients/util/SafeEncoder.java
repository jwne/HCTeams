package redis.clients.util;

import redis.clients.jedis.exceptions.*;
import java.io.*;

public class SafeEncoder
{
    public static byte[][] encodeMany(final String... strs) {
        final byte[][] many = new byte[strs.length][];
        for (int i = 0; i < strs.length; ++i) {
            many[i] = encode(strs[i]);
        }
        return many;
    }
    
    public static byte[] encode(final String str) {
        try {
            if (str == null) {
                throw new JedisDataException("value sent to redis cannot be null");
            }
            return str.getBytes("UTF-8");
        }
        catch (UnsupportedEncodingException e) {
            throw new JedisException(e);
        }
    }
    
    public static String encode(final byte[] data) {
        try {
            return new String(data, "UTF-8");
        }
        catch (UnsupportedEncodingException e) {
            throw new JedisException(e);
        }
    }
}
