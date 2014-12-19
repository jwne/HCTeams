package redis.clients.jedis;

import java.io.*;
import java.util.regex.*;
import java.util.*;
import redis.clients.util.*;

public class ShardedJedis extends BinaryShardedJedis implements JedisCommands, Closeable
{
    protected Pool<ShardedJedis> dataSource;
    
    public ShardedJedis(final List<JedisShardInfo> shards) {
        super(shards);
        this.dataSource = null;
    }
    
    public ShardedJedis(final List<JedisShardInfo> shards, final Hashing algo) {
        super(shards, algo);
        this.dataSource = null;
    }
    
    public ShardedJedis(final List<JedisShardInfo> shards, final Pattern keyTagPattern) {
        super(shards, keyTagPattern);
        this.dataSource = null;
    }
    
    public ShardedJedis(final List<JedisShardInfo> shards, final Hashing algo, final Pattern keyTagPattern) {
        super(shards, algo, keyTagPattern);
        this.dataSource = null;
    }
    
    @Override
    public String set(final String key, final String value) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.set(key, value);
    }
    
    @Override
    public String set(final String key, final String value, final String nxxx, final String expx, final long time) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.set(key, value, nxxx, expx, time);
    }
    
    @Override
    public String get(final String key) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.get(key);
    }
    
    @Override
    public String echo(final String string) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(string);
        return j.echo(string);
    }
    
    @Override
    public Boolean exists(final String key) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.exists(key);
    }
    
    @Override
    public String type(final String key) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.type(key);
    }
    
    @Override
    public Long expire(final String key, final int seconds) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.expire(key, seconds);
    }
    
    @Override
    public Long expireAt(final String key, final long unixTime) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.expireAt(key, unixTime);
    }
    
    @Override
    public Long ttl(final String key) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.ttl(key);
    }
    
    @Override
    public Boolean setbit(final String key, final long offset, final boolean value) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.setbit(key, offset, value);
    }
    
    @Override
    public Boolean setbit(final String key, final long offset, final String value) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.setbit(key, offset, value);
    }
    
    @Override
    public Boolean getbit(final String key, final long offset) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.getbit(key, offset);
    }
    
    @Override
    public Long setrange(final String key, final long offset, final String value) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.setrange(key, offset, value);
    }
    
    @Override
    public String getrange(final String key, final long startOffset, final long endOffset) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.getrange(key, startOffset, endOffset);
    }
    
    @Override
    public String getSet(final String key, final String value) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.getSet(key, value);
    }
    
    @Override
    public Long setnx(final String key, final String value) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.setnx(key, value);
    }
    
    @Override
    public String setex(final String key, final int seconds, final String value) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.setex(key, seconds, value);
    }
    
    @Override
    public List<String> blpop(final String arg) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(arg);
        return j.blpop(arg);
    }
    
    @Override
    public List<String> blpop(final int timeout, final String key) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.blpop(timeout, key);
    }
    
    @Override
    public List<String> brpop(final String arg) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(arg);
        return j.brpop(arg);
    }
    
    @Override
    public List<String> brpop(final int timeout, final String key) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.brpop(timeout, key);
    }
    
    @Override
    public Long decrBy(final String key, final long integer) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.decrBy(key, integer);
    }
    
    @Override
    public Long decr(final String key) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.decr(key);
    }
    
    @Override
    public Long incrBy(final String key, final long integer) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.incrBy(key, integer);
    }
    
    public Double incrByFloat(final String key, final double integer) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.incrByFloat(key, integer);
    }
    
    @Override
    public Long incr(final String key) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.incr(key);
    }
    
    @Override
    public Long append(final String key, final String value) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.append(key, value);
    }
    
    @Override
    public String substr(final String key, final int start, final int end) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.substr(key, start, end);
    }
    
    @Override
    public Long hset(final String key, final String field, final String value) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.hset(key, field, value);
    }
    
    @Override
    public String hget(final String key, final String field) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.hget(key, field);
    }
    
    @Override
    public Long hsetnx(final String key, final String field, final String value) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.hsetnx(key, field, value);
    }
    
    @Override
    public String hmset(final String key, final Map<String, String> hash) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.hmset(key, hash);
    }
    
    @Override
    public List<String> hmget(final String key, final String... fields) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.hmget(key, fields);
    }
    
    @Override
    public Long hincrBy(final String key, final String field, final long value) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.hincrBy(key, field, value);
    }
    
    public Double hincrByFloat(final String key, final String field, final double value) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.hincrByFloat(key, field, value);
    }
    
    @Override
    public Boolean hexists(final String key, final String field) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.hexists(key, field);
    }
    
    @Override
    public Long del(final String key) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.del(key);
    }
    
    @Override
    public Long hdel(final String key, final String... fields) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.hdel(key, fields);
    }
    
    @Override
    public Long hlen(final String key) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.hlen(key);
    }
    
    @Override
    public Set<String> hkeys(final String key) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.hkeys(key);
    }
    
    @Override
    public List<String> hvals(final String key) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.hvals(key);
    }
    
    @Override
    public Map<String, String> hgetAll(final String key) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.hgetAll(key);
    }
    
    @Override
    public Long rpush(final String key, final String... strings) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.rpush(key, strings);
    }
    
    @Override
    public Long lpush(final String key, final String... strings) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.lpush(key, strings);
    }
    
    @Override
    public Long lpushx(final String key, final String... string) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.lpushx(key, string);
    }
    
    @Override
    public Long strlen(final String key) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.strlen(key);
    }
    
    @Override
    public Long move(final String key, final int dbIndex) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.move(key, dbIndex);
    }
    
    @Override
    public Long rpushx(final String key, final String... string) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.rpushx(key, string);
    }
    
    @Override
    public Long persist(final String key) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.persist(key);
    }
    
    @Override
    public Long llen(final String key) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.llen(key);
    }
    
    @Override
    public List<String> lrange(final String key, final long start, final long end) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.lrange(key, start, end);
    }
    
    @Override
    public String ltrim(final String key, final long start, final long end) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.ltrim(key, start, end);
    }
    
    @Override
    public String lindex(final String key, final long index) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.lindex(key, index);
    }
    
    @Override
    public String lset(final String key, final long index, final String value) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.lset(key, index, value);
    }
    
    @Override
    public Long lrem(final String key, final long count, final String value) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.lrem(key, count, value);
    }
    
    @Override
    public String lpop(final String key) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.lpop(key);
    }
    
    @Override
    public String rpop(final String key) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.rpop(key);
    }
    
    @Override
    public Long sadd(final String key, final String... members) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.sadd(key, members);
    }
    
    @Override
    public Set<String> smembers(final String key) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.smembers(key);
    }
    
    @Override
    public Long srem(final String key, final String... members) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.srem(key, members);
    }
    
    @Override
    public String spop(final String key) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.spop(key);
    }
    
    @Override
    public Long scard(final String key) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.scard(key);
    }
    
    @Override
    public Boolean sismember(final String key, final String member) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.sismember(key, member);
    }
    
    @Override
    public String srandmember(final String key) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.srandmember(key);
    }
    
    @Override
    public List<String> srandmember(final String key, final int count) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.srandmember(key, count);
    }
    
    @Override
    public Long zadd(final String key, final double score, final String member) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.zadd(key, score, member);
    }
    
    @Override
    public Long zadd(final String key, final Map<String, Double> scoreMembers) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.zadd(key, scoreMembers);
    }
    
    @Override
    public Set<String> zrange(final String key, final long start, final long end) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.zrange(key, start, end);
    }
    
    @Override
    public Long zrem(final String key, final String... members) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.zrem(key, members);
    }
    
    @Override
    public Double zincrby(final String key, final double score, final String member) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.zincrby(key, score, member);
    }
    
    @Override
    public Long zrank(final String key, final String member) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.zrank(key, member);
    }
    
    @Override
    public Long zrevrank(final String key, final String member) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.zrevrank(key, member);
    }
    
    @Override
    public Set<String> zrevrange(final String key, final long start, final long end) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.zrevrange(key, start, end);
    }
    
    @Override
    public Set<Tuple> zrangeWithScores(final String key, final long start, final long end) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.zrangeWithScores(key, start, end);
    }
    
    @Override
    public Set<Tuple> zrevrangeWithScores(final String key, final long start, final long end) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.zrevrangeWithScores(key, start, end);
    }
    
    @Override
    public Long zcard(final String key) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.zcard(key);
    }
    
    @Override
    public Double zscore(final String key, final String member) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.zscore(key, member);
    }
    
    @Override
    public List<String> sort(final String key) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.sort(key);
    }
    
    @Override
    public List<String> sort(final String key, final SortingParams sortingParameters) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.sort(key, sortingParameters);
    }
    
    @Override
    public Long zcount(final String key, final double min, final double max) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.zcount(key, min, max);
    }
    
    @Override
    public Long zcount(final String key, final String min, final String max) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.zcount(key, min, max);
    }
    
    @Override
    public Set<String> zrangeByScore(final String key, final double min, final double max) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.zrangeByScore(key, min, max);
    }
    
    @Override
    public Set<String> zrevrangeByScore(final String key, final double max, final double min) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.zrevrangeByScore(key, max, min);
    }
    
    @Override
    public Set<String> zrangeByScore(final String key, final double min, final double max, final int offset, final int count) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.zrangeByScore(key, min, max, offset, count);
    }
    
    @Override
    public Set<String> zrevrangeByScore(final String key, final double max, final double min, final int offset, final int count) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.zrevrangeByScore(key, max, min, offset, count);
    }
    
    @Override
    public Set<Tuple> zrangeByScoreWithScores(final String key, final double min, final double max) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.zrangeByScoreWithScores(key, min, max);
    }
    
    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(final String key, final double max, final double min) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.zrevrangeByScoreWithScores(key, max, min);
    }
    
    @Override
    public Set<Tuple> zrangeByScoreWithScores(final String key, final double min, final double max, final int offset, final int count) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.zrangeByScoreWithScores(key, min, max, offset, count);
    }
    
    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(final String key, final double max, final double min, final int offset, final int count) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.zrevrangeByScoreWithScores(key, max, min, offset, count);
    }
    
    @Override
    public Set<String> zrangeByScore(final String key, final String min, final String max) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.zrangeByScore(key, min, max);
    }
    
    @Override
    public Set<String> zrevrangeByScore(final String key, final String max, final String min) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.zrevrangeByScore(key, max, min);
    }
    
    @Override
    public Set<String> zrangeByScore(final String key, final String min, final String max, final int offset, final int count) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.zrangeByScore(key, min, max, offset, count);
    }
    
    @Override
    public Set<String> zrevrangeByScore(final String key, final String max, final String min, final int offset, final int count) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.zrevrangeByScore(key, max, min, offset, count);
    }
    
    @Override
    public Set<Tuple> zrangeByScoreWithScores(final String key, final String min, final String max) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.zrangeByScoreWithScores(key, min, max);
    }
    
    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(final String key, final String max, final String min) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.zrevrangeByScoreWithScores(key, max, min);
    }
    
    @Override
    public Set<Tuple> zrangeByScoreWithScores(final String key, final String min, final String max, final int offset, final int count) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.zrangeByScoreWithScores(key, min, max, offset, count);
    }
    
    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(final String key, final String max, final String min, final int offset, final int count) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.zrevrangeByScoreWithScores(key, max, min, offset, count);
    }
    
    @Override
    public Long zremrangeByRank(final String key, final long start, final long end) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.zremrangeByRank(key, start, end);
    }
    
    @Override
    public Long zremrangeByScore(final String key, final double start, final double end) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.zremrangeByScore(key, start, end);
    }
    
    @Override
    public Long zremrangeByScore(final String key, final String start, final String end) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.zremrangeByScore(key, start, end);
    }
    
    @Override
    public Long zlexcount(final String key, final String min, final String max) {
        return ((Sharded<Jedis, S>)this).getShard(key).zlexcount(key, min, max);
    }
    
    @Override
    public Set<String> zrangeByLex(final String key, final String min, final String max) {
        return ((Sharded<Jedis, S>)this).getShard(key).zrangeByLex(key, min, max);
    }
    
    @Override
    public Set<String> zrangeByLex(final String key, final String min, final String max, final int offset, final int count) {
        return ((Sharded<Jedis, S>)this).getShard(key).zrangeByLex(key, min, max, offset, count);
    }
    
    @Override
    public Long zremrangeByLex(final String key, final String min, final String max) {
        return ((Sharded<Jedis, S>)this).getShard(key).zremrangeByLex(key, min, max);
    }
    
    @Override
    public Long linsert(final String key, final BinaryClient.LIST_POSITION where, final String pivot, final String value) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.linsert(key, where, pivot, value);
    }
    
    @Override
    public Long bitcount(final String key) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.bitcount(key);
    }
    
    @Override
    public Long bitcount(final String key, final long start, final long end) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.bitcount(key, start, end);
    }
    
    @Deprecated
    @Override
    public ScanResult<Map.Entry<String, String>> hscan(final String key, final int cursor) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.hscan(key, cursor);
    }
    
    @Deprecated
    @Override
    public ScanResult<String> sscan(final String key, final int cursor) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.sscan(key, cursor);
    }
    
    @Deprecated
    @Override
    public ScanResult<Tuple> zscan(final String key, final int cursor) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.zscan(key, cursor);
    }
    
    @Override
    public ScanResult<Map.Entry<String, String>> hscan(final String key, final String cursor) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.hscan(key, cursor);
    }
    
    @Override
    public ScanResult<String> sscan(final String key, final String cursor) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.sscan(key, cursor);
    }
    
    @Override
    public ScanResult<Tuple> zscan(final String key, final String cursor) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.zscan(key, cursor);
    }
    
    @Override
    public void close() {
        if (this.dataSource != null) {
            boolean broken = false;
            for (final Jedis jedis : ((Sharded<Jedis, S>)this).getAllShards()) {
                if (jedis.getClient().isBroken()) {
                    broken = true;
                }
            }
            if (broken) {
                this.dataSource.returnBrokenResource(this);
            }
            else {
                this.resetState();
                this.dataSource.returnResource(this);
            }
        }
        else {
            this.disconnect();
        }
    }
    
    public void setDataSource(final Pool<ShardedJedis> shardedJedisPool) {
        this.dataSource = shardedJedisPool;
    }
    
    public void resetState() {
        for (final Jedis jedis : ((Sharded<Jedis, S>)this).getAllShards()) {
            jedis.resetState();
        }
    }
    
    @Override
    public Long pfadd(final String key, final String... elements) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.pfadd(key, elements);
    }
    
    @Override
    public long pfcount(final String key) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.pfcount(key);
    }
}
