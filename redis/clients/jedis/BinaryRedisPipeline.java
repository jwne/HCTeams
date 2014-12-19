package redis.clients.jedis;

import java.util.*;

public interface BinaryRedisPipeline
{
    Response<Long> append(byte[] p0, byte[] p1);
    
    Response<List<byte[]>> blpop(byte[] p0);
    
    Response<List<byte[]>> brpop(byte[] p0);
    
    Response<Long> decr(byte[] p0);
    
    Response<Long> decrBy(byte[] p0, long p1);
    
    Response<Long> del(byte[] p0);
    
    Response<byte[]> echo(byte[] p0);
    
    Response<Boolean> exists(byte[] p0);
    
    Response<Long> expire(byte[] p0, int p1);
    
    Response<Long> expireAt(byte[] p0, long p1);
    
    Response<byte[]> get(byte[] p0);
    
    Response<Boolean> getbit(byte[] p0, long p1);
    
    Response<byte[]> getSet(byte[] p0, byte[] p1);
    
    Response<Long> getrange(byte[] p0, long p1, long p2);
    
    Response<Long> hdel(byte[] p0, byte[]... p1);
    
    Response<Boolean> hexists(byte[] p0, byte[] p1);
    
    Response<byte[]> hget(byte[] p0, byte[] p1);
    
    Response<Map<byte[], byte[]>> hgetAll(byte[] p0);
    
    Response<Long> hincrBy(byte[] p0, byte[] p1, long p2);
    
    Response<Set<byte[]>> hkeys(byte[] p0);
    
    Response<Long> hlen(byte[] p0);
    
    Response<List<byte[]>> hmget(byte[] p0, byte[]... p1);
    
    Response<String> hmset(byte[] p0, Map<byte[], byte[]> p1);
    
    Response<Long> hset(byte[] p0, byte[] p1, byte[] p2);
    
    Response<Long> hsetnx(byte[] p0, byte[] p1, byte[] p2);
    
    Response<List<byte[]>> hvals(byte[] p0);
    
    Response<Long> incr(byte[] p0);
    
    Response<Long> incrBy(byte[] p0, long p1);
    
    Response<byte[]> lindex(byte[] p0, long p1);
    
    Response<Long> linsert(byte[] p0, BinaryClient.LIST_POSITION p1, byte[] p2, byte[] p3);
    
    Response<Long> llen(byte[] p0);
    
    Response<byte[]> lpop(byte[] p0);
    
    Response<Long> lpush(byte[] p0, byte[]... p1);
    
    Response<Long> lpushx(byte[] p0, byte[]... p1);
    
    Response<List<byte[]>> lrange(byte[] p0, long p1, long p2);
    
    Response<Long> lrem(byte[] p0, long p1, byte[] p2);
    
    Response<String> lset(byte[] p0, long p1, byte[] p2);
    
    Response<String> ltrim(byte[] p0, long p1, long p2);
    
    Response<Long> move(byte[] p0, int p1);
    
    Response<Long> persist(byte[] p0);
    
    Response<byte[]> rpop(byte[] p0);
    
    Response<Long> rpush(byte[] p0, byte[]... p1);
    
    Response<Long> rpushx(byte[] p0, byte[]... p1);
    
    Response<Long> sadd(byte[] p0, byte[]... p1);
    
    Response<Long> scard(byte[] p0);
    
    Response<String> set(byte[] p0, byte[] p1);
    
    Response<Boolean> setbit(byte[] p0, long p1, byte[] p2);
    
    Response<Long> setrange(byte[] p0, long p1, byte[] p2);
    
    Response<String> setex(byte[] p0, int p1, byte[] p2);
    
    Response<Long> setnx(byte[] p0, byte[] p1);
    
    Response<Long> setrange(String p0, long p1, String p2);
    
    Response<Set<byte[]>> smembers(byte[] p0);
    
    Response<Boolean> sismember(byte[] p0, byte[] p1);
    
    Response<List<byte[]>> sort(byte[] p0);
    
    Response<List<byte[]>> sort(byte[] p0, SortingParams p1);
    
    Response<byte[]> spop(byte[] p0);
    
    Response<byte[]> srandmember(byte[] p0);
    
    Response<Long> srem(byte[] p0, byte[]... p1);
    
    Response<Long> strlen(byte[] p0);
    
    Response<String> substr(byte[] p0, int p1, int p2);
    
    Response<Long> ttl(byte[] p0);
    
    Response<String> type(byte[] p0);
    
    Response<Long> zadd(byte[] p0, double p1, byte[] p2);
    
    Response<Long> zcard(byte[] p0);
    
    Response<Long> zcount(byte[] p0, double p1, double p2);
    
    Response<Double> zincrby(byte[] p0, double p1, byte[] p2);
    
    Response<Set<byte[]>> zrange(byte[] p0, long p1, long p2);
    
    Response<Set<byte[]>> zrangeByScore(byte[] p0, double p1, double p2);
    
    Response<Set<byte[]>> zrangeByScore(byte[] p0, byte[] p1, byte[] p2);
    
    Response<Set<byte[]>> zrangeByScore(byte[] p0, double p1, double p2, int p3, int p4);
    
    Response<Set<byte[]>> zrangeByScore(byte[] p0, byte[] p1, byte[] p2, int p3, int p4);
    
    Response<Set<Tuple>> zrangeByScoreWithScores(byte[] p0, double p1, double p2);
    
    Response<Set<Tuple>> zrangeByScoreWithScores(byte[] p0, byte[] p1, byte[] p2);
    
    Response<Set<Tuple>> zrangeByScoreWithScores(byte[] p0, double p1, double p2, int p3, int p4);
    
    Response<Set<Tuple>> zrangeByScoreWithScores(byte[] p0, byte[] p1, byte[] p2, int p3, int p4);
    
    Response<Set<byte[]>> zrevrangeByScore(byte[] p0, double p1, double p2);
    
    Response<Set<byte[]>> zrevrangeByScore(byte[] p0, byte[] p1, byte[] p2);
    
    Response<Set<byte[]>> zrevrangeByScore(byte[] p0, double p1, double p2, int p3, int p4);
    
    Response<Set<byte[]>> zrevrangeByScore(byte[] p0, byte[] p1, byte[] p2, int p3, int p4);
    
    Response<Set<Tuple>> zrevrangeByScoreWithScores(byte[] p0, double p1, double p2);
    
    Response<Set<Tuple>> zrevrangeByScoreWithScores(byte[] p0, byte[] p1, byte[] p2);
    
    Response<Set<Tuple>> zrevrangeByScoreWithScores(byte[] p0, double p1, double p2, int p3, int p4);
    
    Response<Set<Tuple>> zrevrangeByScoreWithScores(byte[] p0, byte[] p1, byte[] p2, int p3, int p4);
    
    Response<Set<Tuple>> zrangeWithScores(byte[] p0, long p1, long p2);
    
    Response<Long> zrank(byte[] p0, byte[] p1);
    
    Response<Long> zrem(byte[] p0, byte[]... p1);
    
    Response<Long> zremrangeByRank(byte[] p0, long p1, long p2);
    
    Response<Long> zremrangeByScore(byte[] p0, double p1, double p2);
    
    Response<Long> zremrangeByScore(byte[] p0, byte[] p1, byte[] p2);
    
    Response<Set<byte[]>> zrevrange(byte[] p0, long p1, long p2);
    
    Response<Set<Tuple>> zrevrangeWithScores(byte[] p0, long p1, long p2);
    
    Response<Long> zrevrank(byte[] p0, byte[] p1);
    
    Response<Double> zscore(byte[] p0, byte[] p1);
    
    Response<Long> zlexcount(byte[] p0, byte[] p1, byte[] p2);
    
    Response<Set<byte[]>> zrangeByLex(byte[] p0, byte[] p1, byte[] p2);
    
    Response<Set<byte[]>> zrangeByLex(byte[] p0, byte[] p1, byte[] p2, int p3, int p4);
    
    Response<Long> zremrangeByLex(byte[] p0, byte[] p1, byte[] p2);
    
    Response<Long> bitcount(byte[] p0);
    
    Response<Long> bitcount(byte[] p0, long p1, long p2);
    
    Response<Long> pfadd(byte[] p0, byte[]... p1);
    
    Response<Long> pfcount(byte[] p0);
}
