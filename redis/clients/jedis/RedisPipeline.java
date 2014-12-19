package redis.clients.jedis;

import java.util.*;

public interface RedisPipeline
{
    Response<Long> append(String p0, String p1);
    
    Response<List<String>> blpop(String p0);
    
    Response<List<String>> brpop(String p0);
    
    Response<Long> decr(String p0);
    
    Response<Long> decrBy(String p0, long p1);
    
    Response<Long> del(String p0);
    
    Response<String> echo(String p0);
    
    Response<Boolean> exists(String p0);
    
    Response<Long> expire(String p0, int p1);
    
    Response<Long> expireAt(String p0, long p1);
    
    Response<String> get(String p0);
    
    Response<Boolean> getbit(String p0, long p1);
    
    Response<String> getrange(String p0, long p1, long p2);
    
    Response<String> getSet(String p0, String p1);
    
    Response<Long> hdel(String p0, String... p1);
    
    Response<Boolean> hexists(String p0, String p1);
    
    Response<String> hget(String p0, String p1);
    
    Response<Map<String, String>> hgetAll(String p0);
    
    Response<Long> hincrBy(String p0, String p1, long p2);
    
    Response<Set<String>> hkeys(String p0);
    
    Response<Long> hlen(String p0);
    
    Response<List<String>> hmget(String p0, String... p1);
    
    Response<String> hmset(String p0, Map<String, String> p1);
    
    Response<Long> hset(String p0, String p1, String p2);
    
    Response<Long> hsetnx(String p0, String p1, String p2);
    
    Response<List<String>> hvals(String p0);
    
    Response<Long> incr(String p0);
    
    Response<Long> incrBy(String p0, long p1);
    
    Response<String> lindex(String p0, long p1);
    
    Response<Long> linsert(String p0, BinaryClient.LIST_POSITION p1, String p2, String p3);
    
    Response<Long> llen(String p0);
    
    Response<String> lpop(String p0);
    
    Response<Long> lpush(String p0, String... p1);
    
    Response<Long> lpushx(String p0, String... p1);
    
    Response<List<String>> lrange(String p0, long p1, long p2);
    
    Response<Long> lrem(String p0, long p1, String p2);
    
    Response<String> lset(String p0, long p1, String p2);
    
    Response<String> ltrim(String p0, long p1, long p2);
    
    Response<Long> move(String p0, int p1);
    
    Response<Long> persist(String p0);
    
    Response<String> rpop(String p0);
    
    Response<Long> rpush(String p0, String... p1);
    
    Response<Long> rpushx(String p0, String... p1);
    
    Response<Long> sadd(String p0, String... p1);
    
    Response<Long> scard(String p0);
    
    Response<Boolean> sismember(String p0, String p1);
    
    Response<String> set(String p0, String p1);
    
    Response<Boolean> setbit(String p0, long p1, boolean p2);
    
    Response<String> setex(String p0, int p1, String p2);
    
    Response<Long> setnx(String p0, String p1);
    
    Response<Long> setrange(String p0, long p1, String p2);
    
    Response<Set<String>> smembers(String p0);
    
    Response<List<String>> sort(String p0);
    
    Response<List<String>> sort(String p0, SortingParams p1);
    
    Response<String> spop(String p0);
    
    Response<String> srandmember(String p0);
    
    Response<Long> srem(String p0, String... p1);
    
    Response<Long> strlen(String p0);
    
    Response<String> substr(String p0, int p1, int p2);
    
    Response<Long> ttl(String p0);
    
    Response<String> type(String p0);
    
    Response<Long> zadd(String p0, double p1, String p2);
    
    Response<Long> zcard(String p0);
    
    Response<Long> zcount(String p0, double p1, double p2);
    
    Response<Double> zincrby(String p0, double p1, String p2);
    
    Response<Set<String>> zrange(String p0, long p1, long p2);
    
    Response<Set<String>> zrangeByScore(String p0, double p1, double p2);
    
    Response<Set<String>> zrangeByScore(String p0, String p1, String p2);
    
    Response<Set<String>> zrangeByScore(String p0, double p1, double p2, int p3, int p4);
    
    Response<Set<Tuple>> zrangeByScoreWithScores(String p0, double p1, double p2);
    
    Response<Set<Tuple>> zrangeByScoreWithScores(String p0, double p1, double p2, int p3, int p4);
    
    Response<Set<String>> zrevrangeByScore(String p0, double p1, double p2);
    
    Response<Set<String>> zrevrangeByScore(String p0, String p1, String p2);
    
    Response<Set<String>> zrevrangeByScore(String p0, double p1, double p2, int p3, int p4);
    
    Response<Set<Tuple>> zrevrangeByScoreWithScores(String p0, double p1, double p2);
    
    Response<Set<Tuple>> zrevrangeByScoreWithScores(String p0, double p1, double p2, int p3, int p4);
    
    Response<Set<Tuple>> zrangeWithScores(String p0, long p1, long p2);
    
    Response<Long> zrank(String p0, String p1);
    
    Response<Long> zrem(String p0, String... p1);
    
    Response<Long> zremrangeByRank(String p0, long p1, long p2);
    
    Response<Long> zremrangeByScore(String p0, double p1, double p2);
    
    Response<Set<String>> zrevrange(String p0, long p1, long p2);
    
    Response<Set<Tuple>> zrevrangeWithScores(String p0, long p1, long p2);
    
    Response<Long> zrevrank(String p0, String p1);
    
    Response<Double> zscore(String p0, String p1);
    
    Response<Long> zlexcount(String p0, String p1, String p2);
    
    Response<Set<String>> zrangeByLex(String p0, String p1, String p2);
    
    Response<Set<String>> zrangeByLex(String p0, String p1, String p2, int p3, int p4);
    
    Response<Long> zremrangeByLex(String p0, String p1, String p2);
    
    Response<Long> bitcount(String p0);
    
    Response<Long> bitcount(String p0, long p1, long p2);
    
    Response<Long> pfadd(String p0, String... p1);
    
    Response<Long> pfcount(String p0);
}
