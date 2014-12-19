package redis.clients.jedis;

import java.util.*;

public interface MultiKeyCommands
{
    Long del(String... p0);
    
    List<String> blpop(int p0, String... p1);
    
    List<String> brpop(int p0, String... p1);
    
    List<String> blpop(String... p0);
    
    List<String> brpop(String... p0);
    
    Set<String> keys(String p0);
    
    List<String> mget(String... p0);
    
    String mset(String... p0);
    
    Long msetnx(String... p0);
    
    String rename(String p0, String p1);
    
    Long renamenx(String p0, String p1);
    
    String rpoplpush(String p0, String p1);
    
    Set<String> sdiff(String... p0);
    
    Long sdiffstore(String p0, String... p1);
    
    Set<String> sinter(String... p0);
    
    Long sinterstore(String p0, String... p1);
    
    Long smove(String p0, String p1, String p2);
    
    Long sort(String p0, SortingParams p1, String p2);
    
    Long sort(String p0, String p1);
    
    Set<String> sunion(String... p0);
    
    Long sunionstore(String p0, String... p1);
    
    String watch(String... p0);
    
    String unwatch();
    
    Long zinterstore(String p0, String... p1);
    
    Long zinterstore(String p0, ZParams p1, String... p2);
    
    Long zunionstore(String p0, String... p1);
    
    Long zunionstore(String p0, ZParams p1, String... p2);
    
    String brpoplpush(String p0, String p1, int p2);
    
    Long publish(String p0, String p1);
    
    void subscribe(JedisPubSub p0, String... p1);
    
    void psubscribe(JedisPubSub p0, String... p1);
    
    String randomKey();
    
    Long bitop(BitOP p0, String p1, String... p2);
    
    @Deprecated
    ScanResult<String> scan(int p0);
    
    ScanResult<String> scan(String p0);
    
    String pfmerge(String p0, String... p1);
    
    long pfcount(String... p0);
}
