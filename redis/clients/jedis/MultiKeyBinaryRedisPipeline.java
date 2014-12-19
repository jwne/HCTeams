package redis.clients.jedis;

import java.util.*;

public interface MultiKeyBinaryRedisPipeline
{
    Response<Long> del(byte[]... p0);
    
    Response<List<byte[]>> blpop(byte[]... p0);
    
    Response<List<byte[]>> brpop(byte[]... p0);
    
    Response<Set<byte[]>> keys(byte[] p0);
    
    Response<List<byte[]>> mget(byte[]... p0);
    
    Response<String> mset(byte[]... p0);
    
    Response<Long> msetnx(byte[]... p0);
    
    Response<String> rename(byte[] p0, byte[] p1);
    
    Response<Long> renamenx(byte[] p0, byte[] p1);
    
    Response<byte[]> rpoplpush(byte[] p0, byte[] p1);
    
    Response<Set<byte[]>> sdiff(byte[]... p0);
    
    Response<Long> sdiffstore(byte[] p0, byte[]... p1);
    
    Response<Set<byte[]>> sinter(byte[]... p0);
    
    Response<Long> sinterstore(byte[] p0, byte[]... p1);
    
    Response<Long> smove(byte[] p0, byte[] p1, byte[] p2);
    
    Response<Long> sort(byte[] p0, SortingParams p1, byte[] p2);
    
    Response<Long> sort(byte[] p0, byte[] p1);
    
    Response<Set<byte[]>> sunion(byte[]... p0);
    
    Response<Long> sunionstore(byte[] p0, byte[]... p1);
    
    Response<String> watch(byte[]... p0);
    
    Response<Long> zinterstore(byte[] p0, byte[]... p1);
    
    Response<Long> zinterstore(byte[] p0, ZParams p1, byte[]... p2);
    
    Response<Long> zunionstore(byte[] p0, byte[]... p1);
    
    Response<Long> zunionstore(byte[] p0, ZParams p1, byte[]... p2);
    
    Response<byte[]> brpoplpush(byte[] p0, byte[] p1, int p2);
    
    Response<Long> publish(byte[] p0, byte[] p1);
    
    Response<byte[]> randomKeyBinary();
    
    Response<Long> bitop(BitOP p0, byte[] p1, byte[]... p2);
    
    Response<String> pfmerge(byte[] p0, byte[]... p1);
    
    Response<Long> pfcount(byte[]... p0);
}
