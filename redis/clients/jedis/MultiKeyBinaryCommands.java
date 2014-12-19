package redis.clients.jedis;

import java.util.*;

public interface MultiKeyBinaryCommands
{
    Long del(byte[]... p0);
    
    List<byte[]> blpop(int p0, byte[]... p1);
    
    List<byte[]> brpop(int p0, byte[]... p1);
    
    List<byte[]> blpop(byte[]... p0);
    
    List<byte[]> brpop(byte[]... p0);
    
    Set<byte[]> keys(byte[] p0);
    
    List<byte[]> mget(byte[]... p0);
    
    String mset(byte[]... p0);
    
    Long msetnx(byte[]... p0);
    
    String rename(byte[] p0, byte[] p1);
    
    Long renamenx(byte[] p0, byte[] p1);
    
    byte[] rpoplpush(byte[] p0, byte[] p1);
    
    Set<byte[]> sdiff(byte[]... p0);
    
    Long sdiffstore(byte[] p0, byte[]... p1);
    
    Set<byte[]> sinter(byte[]... p0);
    
    Long sinterstore(byte[] p0, byte[]... p1);
    
    Long smove(byte[] p0, byte[] p1, byte[] p2);
    
    Long sort(byte[] p0, SortingParams p1, byte[] p2);
    
    Long sort(byte[] p0, byte[] p1);
    
    Set<byte[]> sunion(byte[]... p0);
    
    Long sunionstore(byte[] p0, byte[]... p1);
    
    String watch(byte[]... p0);
    
    String unwatch();
    
    Long zinterstore(byte[] p0, byte[]... p1);
    
    Long zinterstore(byte[] p0, ZParams p1, byte[]... p2);
    
    Long zunionstore(byte[] p0, byte[]... p1);
    
    Long zunionstore(byte[] p0, ZParams p1, byte[]... p2);
    
    byte[] brpoplpush(byte[] p0, byte[] p1, int p2);
    
    Long publish(byte[] p0, byte[] p1);
    
    void subscribe(BinaryJedisPubSub p0, byte[]... p1);
    
    void psubscribe(BinaryJedisPubSub p0, byte[]... p1);
    
    byte[] randomBinaryKey();
    
    Long bitop(BitOP p0, byte[] p1, byte[]... p2);
    
    String pfmerge(byte[] p0, byte[]... p1);
    
    Long pfcount(byte[]... p0);
}
