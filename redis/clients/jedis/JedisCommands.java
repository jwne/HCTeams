package redis.clients.jedis;

import java.util.*;

public interface JedisCommands
{
    String set(String p0, String p1);
    
    String set(String p0, String p1, String p2, String p3, long p4);
    
    String get(String p0);
    
    Boolean exists(String p0);
    
    Long persist(String p0);
    
    String type(String p0);
    
    Long expire(String p0, int p1);
    
    Long expireAt(String p0, long p1);
    
    Long ttl(String p0);
    
    Boolean setbit(String p0, long p1, boolean p2);
    
    Boolean setbit(String p0, long p1, String p2);
    
    Boolean getbit(String p0, long p1);
    
    Long setrange(String p0, long p1, String p2);
    
    String getrange(String p0, long p1, long p2);
    
    String getSet(String p0, String p1);
    
    Long setnx(String p0, String p1);
    
    String setex(String p0, int p1, String p2);
    
    Long decrBy(String p0, long p1);
    
    Long decr(String p0);
    
    Long incrBy(String p0, long p1);
    
    Long incr(String p0);
    
    Long append(String p0, String p1);
    
    String substr(String p0, int p1, int p2);
    
    Long hset(String p0, String p1, String p2);
    
    String hget(String p0, String p1);
    
    Long hsetnx(String p0, String p1, String p2);
    
    String hmset(String p0, Map<String, String> p1);
    
    List<String> hmget(String p0, String... p1);
    
    Long hincrBy(String p0, String p1, long p2);
    
    Boolean hexists(String p0, String p1);
    
    Long hdel(String p0, String... p1);
    
    Long hlen(String p0);
    
    Set<String> hkeys(String p0);
    
    List<String> hvals(String p0);
    
    Map<String, String> hgetAll(String p0);
    
    Long rpush(String p0, String... p1);
    
    Long lpush(String p0, String... p1);
    
    Long llen(String p0);
    
    List<String> lrange(String p0, long p1, long p2);
    
    String ltrim(String p0, long p1, long p2);
    
    String lindex(String p0, long p1);
    
    String lset(String p0, long p1, String p2);
    
    Long lrem(String p0, long p1, String p2);
    
    String lpop(String p0);
    
    String rpop(String p0);
    
    Long sadd(String p0, String... p1);
    
    Set<String> smembers(String p0);
    
    Long srem(String p0, String... p1);
    
    String spop(String p0);
    
    Long scard(String p0);
    
    Boolean sismember(String p0, String p1);
    
    String srandmember(String p0);
    
    List<String> srandmember(String p0, int p1);
    
    Long strlen(String p0);
    
    Long zadd(String p0, double p1, String p2);
    
    Long zadd(String p0, Map<String, Double> p1);
    
    Set<String> zrange(String p0, long p1, long p2);
    
    Long zrem(String p0, String... p1);
    
    Double zincrby(String p0, double p1, String p2);
    
    Long zrank(String p0, String p1);
    
    Long zrevrank(String p0, String p1);
    
    Set<String> zrevrange(String p0, long p1, long p2);
    
    Set<Tuple> zrangeWithScores(String p0, long p1, long p2);
    
    Set<Tuple> zrevrangeWithScores(String p0, long p1, long p2);
    
    Long zcard(String p0);
    
    Double zscore(String p0, String p1);
    
    List<String> sort(String p0);
    
    List<String> sort(String p0, SortingParams p1);
    
    Long zcount(String p0, double p1, double p2);
    
    Long zcount(String p0, String p1, String p2);
    
    Set<String> zrangeByScore(String p0, double p1, double p2);
    
    Set<String> zrangeByScore(String p0, String p1, String p2);
    
    Set<String> zrevrangeByScore(String p0, double p1, double p2);
    
    Set<String> zrangeByScore(String p0, double p1, double p2, int p3, int p4);
    
    Set<String> zrevrangeByScore(String p0, String p1, String p2);
    
    Set<String> zrangeByScore(String p0, String p1, String p2, int p3, int p4);
    
    Set<String> zrevrangeByScore(String p0, double p1, double p2, int p3, int p4);
    
    Set<Tuple> zrangeByScoreWithScores(String p0, double p1, double p2);
    
    Set<Tuple> zrevrangeByScoreWithScores(String p0, double p1, double p2);
    
    Set<Tuple> zrangeByScoreWithScores(String p0, double p1, double p2, int p3, int p4);
    
    Set<String> zrevrangeByScore(String p0, String p1, String p2, int p3, int p4);
    
    Set<Tuple> zrangeByScoreWithScores(String p0, String p1, String p2);
    
    Set<Tuple> zrevrangeByScoreWithScores(String p0, String p1, String p2);
    
    Set<Tuple> zrangeByScoreWithScores(String p0, String p1, String p2, int p3, int p4);
    
    Set<Tuple> zrevrangeByScoreWithScores(String p0, double p1, double p2, int p3, int p4);
    
    Set<Tuple> zrevrangeByScoreWithScores(String p0, String p1, String p2, int p3, int p4);
    
    Long zremrangeByRank(String p0, long p1, long p2);
    
    Long zremrangeByScore(String p0, double p1, double p2);
    
    Long zremrangeByScore(String p0, String p1, String p2);
    
    Long zlexcount(String p0, String p1, String p2);
    
    Set<String> zrangeByLex(String p0, String p1, String p2);
    
    Set<String> zrangeByLex(String p0, String p1, String p2, int p3, int p4);
    
    Long zremrangeByLex(String p0, String p1, String p2);
    
    Long linsert(String p0, BinaryClient.LIST_POSITION p1, String p2, String p3);
    
    Long lpushx(String p0, String... p1);
    
    Long rpushx(String p0, String... p1);
    
    @Deprecated
    List<String> blpop(String p0);
    
    List<String> blpop(int p0, String p1);
    
    @Deprecated
    List<String> brpop(String p0);
    
    List<String> brpop(int p0, String p1);
    
    Long del(String p0);
    
    String echo(String p0);
    
    Long move(String p0, int p1);
    
    Long bitcount(String p0);
    
    Long bitcount(String p0, long p1, long p2);
    
    @Deprecated
    ScanResult<Map.Entry<String, String>> hscan(String p0, int p1);
    
    @Deprecated
    ScanResult<String> sscan(String p0, int p1);
    
    @Deprecated
    ScanResult<Tuple> zscan(String p0, int p1);
    
    ScanResult<Map.Entry<String, String>> hscan(String p0, String p1);
    
    ScanResult<String> sscan(String p0, String p1);
    
    ScanResult<Tuple> zscan(String p0, String p1);
    
    Long pfadd(String p0, String... p1);
    
    long pfcount(String p0);
}
