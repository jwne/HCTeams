package redis.clients.jedis;

import java.util.*;

public interface Commands
{
    void set(String p0, String p1);
    
    void set(String p0, String p1, String p2, String p3, long p4);
    
    void get(String p0);
    
    void exists(String p0);
    
    void del(String... p0);
    
    void type(String p0);
    
    void keys(String p0);
    
    void rename(String p0, String p1);
    
    void renamenx(String p0, String p1);
    
    void expire(String p0, int p1);
    
    void expireAt(String p0, long p1);
    
    void ttl(String p0);
    
    void setbit(String p0, long p1, boolean p2);
    
    void setbit(String p0, long p1, String p2);
    
    void getbit(String p0, long p1);
    
    void setrange(String p0, long p1, String p2);
    
    void getrange(String p0, long p1, long p2);
    
    void move(String p0, int p1);
    
    void getSet(String p0, String p1);
    
    void mget(String... p0);
    
    void setnx(String p0, String p1);
    
    void setex(String p0, int p1, String p2);
    
    void mset(String... p0);
    
    void msetnx(String... p0);
    
    void decrBy(String p0, long p1);
    
    void decr(String p0);
    
    void incrBy(String p0, long p1);
    
    void incrByFloat(String p0, double p1);
    
    void incr(String p0);
    
    void append(String p0, String p1);
    
    void substr(String p0, int p1, int p2);
    
    void hset(String p0, String p1, String p2);
    
    void hget(String p0, String p1);
    
    void hsetnx(String p0, String p1, String p2);
    
    void hmset(String p0, Map<String, String> p1);
    
    void hmget(String p0, String... p1);
    
    void hincrBy(String p0, String p1, long p2);
    
    void hincrByFloat(String p0, String p1, double p2);
    
    void hexists(String p0, String p1);
    
    void hdel(String p0, String... p1);
    
    void hlen(String p0);
    
    void hkeys(String p0);
    
    void hvals(String p0);
    
    void hgetAll(String p0);
    
    void rpush(String p0, String... p1);
    
    void lpush(String p0, String... p1);
    
    void llen(String p0);
    
    void lrange(String p0, long p1, long p2);
    
    void ltrim(String p0, long p1, long p2);
    
    void lindex(String p0, long p1);
    
    void lset(String p0, long p1, String p2);
    
    void lrem(String p0, long p1, String p2);
    
    void lpop(String p0);
    
    void rpop(String p0);
    
    void rpoplpush(String p0, String p1);
    
    void sadd(String p0, String... p1);
    
    void smembers(String p0);
    
    void srem(String p0, String... p1);
    
    void spop(String p0);
    
    void smove(String p0, String p1, String p2);
    
    void scard(String p0);
    
    void sismember(String p0, String p1);
    
    void sinter(String... p0);
    
    void sinterstore(String p0, String... p1);
    
    void sunion(String... p0);
    
    void sunionstore(String p0, String... p1);
    
    void sdiff(String... p0);
    
    void sdiffstore(String p0, String... p1);
    
    void srandmember(String p0);
    
    void zadd(String p0, double p1, String p2);
    
    void zadd(String p0, Map<String, Double> p1);
    
    void zrange(String p0, long p1, long p2);
    
    void zrem(String p0, String... p1);
    
    void zincrby(String p0, double p1, String p2);
    
    void zrank(String p0, String p1);
    
    void zrevrank(String p0, String p1);
    
    void zrevrange(String p0, long p1, long p2);
    
    void zrangeWithScores(String p0, long p1, long p2);
    
    void zrevrangeWithScores(String p0, long p1, long p2);
    
    void zcard(String p0);
    
    void zscore(String p0, String p1);
    
    void watch(String... p0);
    
    void sort(String p0);
    
    void sort(String p0, SortingParams p1);
    
    void blpop(String[] p0);
    
    void sort(String p0, SortingParams p1, String p2);
    
    void sort(String p0, String p1);
    
    void brpop(String[] p0);
    
    void brpoplpush(String p0, String p1, int p2);
    
    void zcount(String p0, double p1, double p2);
    
    void zcount(String p0, String p1, String p2);
    
    void zrangeByScore(String p0, double p1, double p2);
    
    void zrangeByScore(String p0, String p1, String p2);
    
    void zrangeByScore(String p0, double p1, double p2, int p3, int p4);
    
    void zrangeByScoreWithScores(String p0, double p1, double p2);
    
    void zrangeByScoreWithScores(String p0, double p1, double p2, int p3, int p4);
    
    void zrangeByScoreWithScores(String p0, String p1, String p2);
    
    void zrangeByScoreWithScores(String p0, String p1, String p2, int p3, int p4);
    
    void zrevrangeByScore(String p0, double p1, double p2);
    
    void zrevrangeByScore(String p0, String p1, String p2);
    
    void zrevrangeByScore(String p0, double p1, double p2, int p3, int p4);
    
    void zrevrangeByScoreWithScores(String p0, double p1, double p2);
    
    void zrevrangeByScoreWithScores(String p0, double p1, double p2, int p3, int p4);
    
    void zrevrangeByScoreWithScores(String p0, String p1, String p2);
    
    void zrevrangeByScoreWithScores(String p0, String p1, String p2, int p3, int p4);
    
    void zremrangeByRank(String p0, long p1, long p2);
    
    void zremrangeByScore(String p0, double p1, double p2);
    
    void zremrangeByScore(String p0, String p1, String p2);
    
    void zunionstore(String p0, String... p1);
    
    void zunionstore(String p0, ZParams p1, String... p2);
    
    void zinterstore(String p0, String... p1);
    
    void zinterstore(String p0, ZParams p1, String... p2);
    
    void strlen(String p0);
    
    void lpushx(String p0, String... p1);
    
    void persist(String p0);
    
    void rpushx(String p0, String... p1);
    
    void echo(String p0);
    
    void linsert(String p0, BinaryClient.LIST_POSITION p1, String p2, String p3);
    
    void bgrewriteaof();
    
    void bgsave();
    
    void lastsave();
    
    void save();
    
    void configSet(String p0, String p1);
    
    void configGet(String p0);
    
    void configResetStat();
    
    void multi();
    
    void exec();
    
    void discard();
    
    void objectRefcount(String p0);
    
    void objectIdletime(String p0);
    
    void objectEncoding(String p0);
    
    void bitcount(String p0);
    
    void bitcount(String p0, long p1, long p2);
    
    void bitop(BitOP p0, String p1, String... p2);
    
    @Deprecated
    void scan(int p0, ScanParams p1);
    
    @Deprecated
    void hscan(String p0, int p1, ScanParams p2);
    
    @Deprecated
    void sscan(String p0, int p1, ScanParams p2);
    
    @Deprecated
    void zscan(String p0, int p1, ScanParams p2);
    
    void scan(String p0, ScanParams p1);
    
    void hscan(String p0, String p1, ScanParams p2);
    
    void sscan(String p0, String p1, ScanParams p2);
    
    void zscan(String p0, String p1, ScanParams p2);
    
    void waitReplicas(int p0, long p1);
}
