package redis.clients.jedis;

import java.util.*;

public interface BinaryJedisCommands
{
    String set(byte[] p0, byte[] p1);
    
    byte[] get(byte[] p0);
    
    Boolean exists(byte[] p0);
    
    Long persist(byte[] p0);
    
    String type(byte[] p0);
    
    Long expire(byte[] p0, int p1);
    
    Long expireAt(byte[] p0, long p1);
    
    Long ttl(byte[] p0);
    
    Boolean setbit(byte[] p0, long p1, boolean p2);
    
    Boolean setbit(byte[] p0, long p1, byte[] p2);
    
    Boolean getbit(byte[] p0, long p1);
    
    Long setrange(byte[] p0, long p1, byte[] p2);
    
    byte[] getrange(byte[] p0, long p1, long p2);
    
    byte[] getSet(byte[] p0, byte[] p1);
    
    Long setnx(byte[] p0, byte[] p1);
    
    String setex(byte[] p0, int p1, byte[] p2);
    
    Long decrBy(byte[] p0, long p1);
    
    Long decr(byte[] p0);
    
    Long incrBy(byte[] p0, long p1);
    
    Double incrByFloat(byte[] p0, double p1);
    
    Long incr(byte[] p0);
    
    Long append(byte[] p0, byte[] p1);
    
    byte[] substr(byte[] p0, int p1, int p2);
    
    Long hset(byte[] p0, byte[] p1, byte[] p2);
    
    byte[] hget(byte[] p0, byte[] p1);
    
    Long hsetnx(byte[] p0, byte[] p1, byte[] p2);
    
    String hmset(byte[] p0, Map<byte[], byte[]> p1);
    
    List<byte[]> hmget(byte[] p0, byte[]... p1);
    
    Long hincrBy(byte[] p0, byte[] p1, long p2);
    
    Double hincrByFloat(byte[] p0, byte[] p1, double p2);
    
    Boolean hexists(byte[] p0, byte[] p1);
    
    Long hdel(byte[] p0, byte[]... p1);
    
    Long hlen(byte[] p0);
    
    Set<byte[]> hkeys(byte[] p0);
    
    Collection<byte[]> hvals(byte[] p0);
    
    Map<byte[], byte[]> hgetAll(byte[] p0);
    
    Long rpush(byte[] p0, byte[]... p1);
    
    Long lpush(byte[] p0, byte[]... p1);
    
    Long llen(byte[] p0);
    
    List<byte[]> lrange(byte[] p0, long p1, long p2);
    
    String ltrim(byte[] p0, long p1, long p2);
    
    byte[] lindex(byte[] p0, long p1);
    
    String lset(byte[] p0, long p1, byte[] p2);
    
    Long lrem(byte[] p0, long p1, byte[] p2);
    
    byte[] lpop(byte[] p0);
    
    byte[] rpop(byte[] p0);
    
    Long sadd(byte[] p0, byte[]... p1);
    
    Set<byte[]> smembers(byte[] p0);
    
    Long srem(byte[] p0, byte[]... p1);
    
    byte[] spop(byte[] p0);
    
    Long scard(byte[] p0);
    
    Boolean sismember(byte[] p0, byte[] p1);
    
    byte[] srandmember(byte[] p0);
    
    List<byte[]> srandmember(byte[] p0, int p1);
    
    Long strlen(byte[] p0);
    
    Long zadd(byte[] p0, double p1, byte[] p2);
    
    Long zadd(byte[] p0, Map<byte[], Double> p1);
    
    Set<byte[]> zrange(byte[] p0, long p1, long p2);
    
    Long zrem(byte[] p0, byte[]... p1);
    
    Double zincrby(byte[] p0, double p1, byte[] p2);
    
    Long zrank(byte[] p0, byte[] p1);
    
    Long zrevrank(byte[] p0, byte[] p1);
    
    Set<byte[]> zrevrange(byte[] p0, long p1, long p2);
    
    Set<Tuple> zrangeWithScores(byte[] p0, long p1, long p2);
    
    Set<Tuple> zrevrangeWithScores(byte[] p0, long p1, long p2);
    
    Long zcard(byte[] p0);
    
    Double zscore(byte[] p0, byte[] p1);
    
    List<byte[]> sort(byte[] p0);
    
    List<byte[]> sort(byte[] p0, SortingParams p1);
    
    Long zcount(byte[] p0, double p1, double p2);
    
    Long zcount(byte[] p0, byte[] p1, byte[] p2);
    
    Set<byte[]> zrangeByScore(byte[] p0, double p1, double p2);
    
    Set<byte[]> zrangeByScore(byte[] p0, byte[] p1, byte[] p2);
    
    Set<byte[]> zrevrangeByScore(byte[] p0, double p1, double p2);
    
    Set<byte[]> zrangeByScore(byte[] p0, double p1, double p2, int p3, int p4);
    
    Set<byte[]> zrevrangeByScore(byte[] p0, byte[] p1, byte[] p2);
    
    Set<byte[]> zrangeByScore(byte[] p0, byte[] p1, byte[] p2, int p3, int p4);
    
    Set<byte[]> zrevrangeByScore(byte[] p0, double p1, double p2, int p3, int p4);
    
    Set<Tuple> zrangeByScoreWithScores(byte[] p0, double p1, double p2);
    
    Set<Tuple> zrevrangeByScoreWithScores(byte[] p0, double p1, double p2);
    
    Set<Tuple> zrangeByScoreWithScores(byte[] p0, double p1, double p2, int p3, int p4);
    
    Set<byte[]> zrevrangeByScore(byte[] p0, byte[] p1, byte[] p2, int p3, int p4);
    
    Set<Tuple> zrangeByScoreWithScores(byte[] p0, byte[] p1, byte[] p2);
    
    Set<Tuple> zrevrangeByScoreWithScores(byte[] p0, byte[] p1, byte[] p2);
    
    Set<Tuple> zrangeByScoreWithScores(byte[] p0, byte[] p1, byte[] p2, int p3, int p4);
    
    Set<Tuple> zrevrangeByScoreWithScores(byte[] p0, double p1, double p2, int p3, int p4);
    
    Set<Tuple> zrevrangeByScoreWithScores(byte[] p0, byte[] p1, byte[] p2, int p3, int p4);
    
    Long zremrangeByRank(byte[] p0, long p1, long p2);
    
    Long zremrangeByScore(byte[] p0, double p1, double p2);
    
    Long zremrangeByScore(byte[] p0, byte[] p1, byte[] p2);
    
    Long zlexcount(byte[] p0, byte[] p1, byte[] p2);
    
    Set<byte[]> zrangeByLex(byte[] p0, byte[] p1, byte[] p2);
    
    Set<byte[]> zrangeByLex(byte[] p0, byte[] p1, byte[] p2, int p3, int p4);
    
    Long zremrangeByLex(byte[] p0, byte[] p1, byte[] p2);
    
    Long linsert(byte[] p0, BinaryClient.LIST_POSITION p1, byte[] p2, byte[] p3);
    
    Long lpushx(byte[] p0, byte[]... p1);
    
    Long rpushx(byte[] p0, byte[]... p1);
    
    @Deprecated
    List<byte[]> blpop(byte[] p0);
    
    @Deprecated
    List<byte[]> brpop(byte[] p0);
    
    Long del(byte[] p0);
    
    byte[] echo(byte[] p0);
    
    Long move(byte[] p0, int p1);
    
    Long bitcount(byte[] p0);
    
    Long bitcount(byte[] p0, long p1, long p2);
    
    Long pfadd(byte[] p0, byte[]... p1);
    
    long pfcount(byte[] p0);
}
