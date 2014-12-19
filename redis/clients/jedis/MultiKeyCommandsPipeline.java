package redis.clients.jedis;

import java.util.*;

public interface MultiKeyCommandsPipeline
{
    Response<Long> del(String... p0);
    
    Response<List<String>> blpop(String... p0);
    
    Response<List<String>> brpop(String... p0);
    
    Response<Set<String>> keys(String p0);
    
    Response<List<String>> mget(String... p0);
    
    Response<String> mset(String... p0);
    
    Response<Long> msetnx(String... p0);
    
    Response<String> rename(String p0, String p1);
    
    Response<Long> renamenx(String p0, String p1);
    
    Response<String> rpoplpush(String p0, String p1);
    
    Response<Set<String>> sdiff(String... p0);
    
    Response<Long> sdiffstore(String p0, String... p1);
    
    Response<Set<String>> sinter(String... p0);
    
    Response<Long> sinterstore(String p0, String... p1);
    
    Response<Long> smove(String p0, String p1, String p2);
    
    Response<Long> sort(String p0, SortingParams p1, String p2);
    
    Response<Long> sort(String p0, String p1);
    
    Response<Set<String>> sunion(String... p0);
    
    Response<Long> sunionstore(String p0, String... p1);
    
    Response<String> watch(String... p0);
    
    Response<Long> zinterstore(String p0, String... p1);
    
    Response<Long> zinterstore(String p0, ZParams p1, String... p2);
    
    Response<Long> zunionstore(String p0, String... p1);
    
    Response<Long> zunionstore(String p0, ZParams p1, String... p2);
    
    Response<String> brpoplpush(String p0, String p1, int p2);
    
    Response<Long> publish(String p0, String p1);
    
    Response<String> randomKey();
    
    Response<Long> bitop(BitOP p0, String p1, String... p2);
    
    Response<String> pfmerge(String p0, String... p1);
    
    Response<Long> pfcount(String... p0);
}
