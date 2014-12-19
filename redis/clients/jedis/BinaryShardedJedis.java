package redis.clients.jedis;

import redis.clients.util.*;
import java.util.regex.*;
import java.util.*;

public class BinaryShardedJedis extends Sharded<Jedis, JedisShardInfo> implements BinaryJedisCommands
{
    public BinaryShardedJedis(final List<JedisShardInfo> shards) {
        super(shards);
    }
    
    public BinaryShardedJedis(final List<JedisShardInfo> shards, final Hashing algo) {
        super(shards, algo);
    }
    
    public BinaryShardedJedis(final List<JedisShardInfo> shards, final Pattern keyTagPattern) {
        super(shards, keyTagPattern);
    }
    
    public BinaryShardedJedis(final List<JedisShardInfo> shards, final Hashing algo, final Pattern keyTagPattern) {
        super(shards, algo, keyTagPattern);
    }
    
    public void disconnect() {
        for (final Jedis jedis : ((Sharded<Jedis, S>)this).getAllShards()) {
            jedis.quit();
            jedis.disconnect();
        }
    }
    
    protected Jedis create(final JedisShardInfo shard) {
        return new Jedis(shard);
    }
    
    @Override
    public String set(final byte[] key, final byte[] value) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.set(key, value);
    }
    
    @Override
    public byte[] get(final byte[] key) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.get(key);
    }
    
    @Override
    public Boolean exists(final byte[] key) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.exists(key);
    }
    
    @Override
    public String type(final byte[] key) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.type(key);
    }
    
    @Override
    public Long expire(final byte[] key, final int seconds) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.expire(key, seconds);
    }
    
    @Override
    public Long expireAt(final byte[] key, final long unixTime) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.expireAt(key, unixTime);
    }
    
    @Override
    public Long ttl(final byte[] key) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.ttl(key);
    }
    
    @Override
    public byte[] getSet(final byte[] key, final byte[] value) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.getSet(key, value);
    }
    
    @Override
    public Long setnx(final byte[] key, final byte[] value) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.setnx(key, value);
    }
    
    @Override
    public String setex(final byte[] key, final int seconds, final byte[] value) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.setex(key, seconds, value);
    }
    
    @Override
    public Long decrBy(final byte[] key, final long integer) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.decrBy(key, integer);
    }
    
    @Override
    public Long decr(final byte[] key) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.decr(key);
    }
    
    @Override
    public Long del(final byte[] key) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.del(key);
    }
    
    @Override
    public Long incrBy(final byte[] key, final long integer) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.incrBy(key, integer);
    }
    
    @Override
    public Double incrByFloat(final byte[] key, final double integer) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.incrByFloat(key, integer);
    }
    
    @Override
    public Long incr(final byte[] key) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.incr(key);
    }
    
    @Override
    public Long append(final byte[] key, final byte[] value) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.append(key, value);
    }
    
    @Override
    public byte[] substr(final byte[] key, final int start, final int end) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.substr(key, start, end);
    }
    
    @Override
    public Long hset(final byte[] key, final byte[] field, final byte[] value) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.hset(key, field, value);
    }
    
    @Override
    public byte[] hget(final byte[] key, final byte[] field) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.hget(key, field);
    }
    
    @Override
    public Long hsetnx(final byte[] key, final byte[] field, final byte[] value) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.hsetnx(key, field, value);
    }
    
    @Override
    public String hmset(final byte[] key, final Map<byte[], byte[]> hash) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.hmset(key, hash);
    }
    
    @Override
    public List<byte[]> hmget(final byte[] key, final byte[]... fields) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.hmget(key, fields);
    }
    
    @Override
    public Long hincrBy(final byte[] key, final byte[] field, final long value) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.hincrBy(key, field, value);
    }
    
    @Override
    public Double hincrByFloat(final byte[] key, final byte[] field, final double value) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.hincrByFloat(key, field, value);
    }
    
    @Override
    public Boolean hexists(final byte[] key, final byte[] field) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.hexists(key, field);
    }
    
    @Override
    public Long hdel(final byte[] key, final byte[]... fields) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.hdel(key, fields);
    }
    
    @Override
    public Long hlen(final byte[] key) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.hlen(key);
    }
    
    @Override
    public Set<byte[]> hkeys(final byte[] key) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.hkeys(key);
    }
    
    @Override
    public Collection<byte[]> hvals(final byte[] key) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.hvals(key);
    }
    
    @Override
    public Map<byte[], byte[]> hgetAll(final byte[] key) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.hgetAll(key);
    }
    
    @Override
    public Long rpush(final byte[] key, final byte[]... strings) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.rpush(key, strings);
    }
    
    @Override
    public Long lpush(final byte[] key, final byte[]... strings) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.lpush(key, strings);
    }
    
    @Override
    public Long strlen(final byte[] key) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.strlen(key);
    }
    
    @Override
    public Long lpushx(final byte[] key, final byte[]... string) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.lpushx(key, string);
    }
    
    @Override
    public Long persist(final byte[] key) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.persist(key);
    }
    
    @Override
    public Long rpushx(final byte[] key, final byte[]... string) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.rpushx(key, string);
    }
    
    @Override
    public Long llen(final byte[] key) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.llen(key);
    }
    
    @Override
    public List<byte[]> lrange(final byte[] key, final long start, final long end) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.lrange(key, start, end);
    }
    
    @Override
    public String ltrim(final byte[] key, final long start, final long end) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.ltrim(key, start, end);
    }
    
    @Override
    public byte[] lindex(final byte[] key, final long index) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.lindex(key, index);
    }
    
    @Override
    public String lset(final byte[] key, final long index, final byte[] value) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.lset(key, index, value);
    }
    
    @Override
    public Long lrem(final byte[] key, final long count, final byte[] value) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.lrem(key, count, value);
    }
    
    @Override
    public byte[] lpop(final byte[] key) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.lpop(key);
    }
    
    @Override
    public byte[] rpop(final byte[] key) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.rpop(key);
    }
    
    @Override
    public Long sadd(final byte[] key, final byte[]... members) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.sadd(key, members);
    }
    
    @Override
    public Set<byte[]> smembers(final byte[] key) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.smembers(key);
    }
    
    @Override
    public Long srem(final byte[] key, final byte[]... members) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.srem(key, members);
    }
    
    @Override
    public byte[] spop(final byte[] key) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.spop(key);
    }
    
    @Override
    public Long scard(final byte[] key) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.scard(key);
    }
    
    @Override
    public Boolean sismember(final byte[] key, final byte[] member) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.sismember(key, member);
    }
    
    @Override
    public byte[] srandmember(final byte[] key) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.srandmember(key);
    }
    
    @Override
    public List srandmember(final byte[] key, final int count) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.srandmember(key, count);
    }
    
    @Override
    public Long zadd(final byte[] key, final double score, final byte[] member) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.zadd(key, score, member);
    }
    
    @Override
    public Long zadd(final byte[] key, final Map<byte[], Double> scoreMembers) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.zadd(key, scoreMembers);
    }
    
    @Override
    public Set<byte[]> zrange(final byte[] key, final long start, final long end) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.zrange(key, start, end);
    }
    
    @Override
    public Long zrem(final byte[] key, final byte[]... members) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.zrem(key, members);
    }
    
    @Override
    public Double zincrby(final byte[] key, final double score, final byte[] member) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.zincrby(key, score, member);
    }
    
    @Override
    public Long zrank(final byte[] key, final byte[] member) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.zrank(key, member);
    }
    
    @Override
    public Long zrevrank(final byte[] key, final byte[] member) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.zrevrank(key, member);
    }
    
    @Override
    public Set<byte[]> zrevrange(final byte[] key, final long start, final long end) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.zrevrange(key, start, end);
    }
    
    @Override
    public Set<Tuple> zrangeWithScores(final byte[] key, final long start, final long end) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.zrangeWithScores(key, start, end);
    }
    
    @Override
    public Set<Tuple> zrevrangeWithScores(final byte[] key, final long start, final long end) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.zrevrangeWithScores(key, start, end);
    }
    
    @Override
    public Long zcard(final byte[] key) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.zcard(key);
    }
    
    @Override
    public Double zscore(final byte[] key, final byte[] member) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.zscore(key, member);
    }
    
    @Override
    public List<byte[]> sort(final byte[] key) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.sort(key);
    }
    
    @Override
    public List<byte[]> sort(final byte[] key, final SortingParams sortingParameters) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.sort(key, sortingParameters);
    }
    
    @Override
    public Long zcount(final byte[] key, final double min, final double max) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.zcount(key, min, max);
    }
    
    @Override
    public Long zcount(final byte[] key, final byte[] min, final byte[] max) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.zcount(key, min, max);
    }
    
    @Override
    public Set<byte[]> zrangeByScore(final byte[] key, final double min, final double max) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.zrangeByScore(key, min, max);
    }
    
    @Override
    public Set<byte[]> zrangeByScore(final byte[] key, final double min, final double max, final int offset, final int count) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.zrangeByScore(key, min, max, offset, count);
    }
    
    @Override
    public Set<Tuple> zrangeByScoreWithScores(final byte[] key, final double min, final double max) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.zrangeByScoreWithScores(key, min, max);
    }
    
    @Override
    public Set<Tuple> zrangeByScoreWithScores(final byte[] key, final double min, final double max, final int offset, final int count) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.zrangeByScoreWithScores(key, min, max, offset, count);
    }
    
    @Override
    public Set<byte[]> zrangeByScore(final byte[] key, final byte[] min, final byte[] max) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.zrangeByScore(key, min, max);
    }
    
    @Override
    public Set<Tuple> zrangeByScoreWithScores(final byte[] key, final byte[] min, final byte[] max) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.zrangeByScoreWithScores(key, min, max);
    }
    
    @Override
    public Set<Tuple> zrangeByScoreWithScores(final byte[] key, final byte[] min, final byte[] max, final int offset, final int count) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.zrangeByScoreWithScores(key, min, max, offset, count);
    }
    
    @Override
    public Set<byte[]> zrangeByScore(final byte[] key, final byte[] min, final byte[] max, final int offset, final int count) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.zrangeByScore(key, min, max, offset, count);
    }
    
    @Override
    public Set<byte[]> zrevrangeByScore(final byte[] key, final double max, final double min) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.zrevrangeByScore(key, max, min);
    }
    
    @Override
    public Set<byte[]> zrevrangeByScore(final byte[] key, final double max, final double min, final int offset, final int count) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.zrevrangeByScore(key, max, min, offset, count);
    }
    
    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(final byte[] key, final double max, final double min) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.zrevrangeByScoreWithScores(key, max, min);
    }
    
    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(final byte[] key, final double max, final double min, final int offset, final int count) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.zrevrangeByScoreWithScores(key, max, min, offset, count);
    }
    
    @Override
    public Set<byte[]> zrevrangeByScore(final byte[] key, final byte[] max, final byte[] min) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.zrevrangeByScore(key, max, min);
    }
    
    @Override
    public Set<byte[]> zrevrangeByScore(final byte[] key, final byte[] max, final byte[] min, final int offset, final int count) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.zrevrangeByScore(key, max, min, offset, count);
    }
    
    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(final byte[] key, final byte[] max, final byte[] min) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.zrevrangeByScoreWithScores(key, max, min);
    }
    
    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(final byte[] key, final byte[] max, final byte[] min, final int offset, final int count) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.zrevrangeByScoreWithScores(key, max, min, offset, count);
    }
    
    @Override
    public Long zremrangeByRank(final byte[] key, final long start, final long end) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.zremrangeByRank(key, start, end);
    }
    
    @Override
    public Long zremrangeByScore(final byte[] key, final double start, final double end) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.zremrangeByScore(key, start, end);
    }
    
    @Override
    public Long zremrangeByScore(final byte[] key, final byte[] start, final byte[] end) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.zremrangeByScore(key, start, end);
    }
    
    @Override
    public Long zlexcount(final byte[] key, final byte[] min, final byte[] max) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.zlexcount(key, min, max);
    }
    
    @Override
    public Set<byte[]> zrangeByLex(final byte[] key, final byte[] min, final byte[] max) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.zrangeByLex(key, min, max);
    }
    
    @Override
    public Set<byte[]> zrangeByLex(final byte[] key, final byte[] min, final byte[] max, final int offset, final int count) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.zrangeByLex(key, min, max, offset, count);
    }
    
    @Override
    public Long zremrangeByLex(final byte[] key, final byte[] min, final byte[] max) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.zremrangeByLex(key, min, max);
    }
    
    @Override
    public Long linsert(final byte[] key, final BinaryClient.LIST_POSITION where, final byte[] pivot, final byte[] value) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.linsert(key, where, pivot, value);
    }
    
    @Deprecated
    public List<Object> pipelined(final ShardedJedisPipeline shardedJedisPipeline) {
        shardedJedisPipeline.setShardedJedis(this);
        shardedJedisPipeline.execute();
        return shardedJedisPipeline.getResults();
    }
    
    public ShardedJedisPipeline pipelined() {
        final ShardedJedisPipeline pipeline = new ShardedJedisPipeline();
        pipeline.setShardedJedis(this);
        return pipeline;
    }
    
    public Long objectRefcount(final byte[] key) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.objectRefcount(key);
    }
    
    public byte[] objectEncoding(final byte[] key) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.objectEncoding(key);
    }
    
    public Long objectIdletime(final byte[] key) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.objectIdletime(key);
    }
    
    @Override
    public Boolean setbit(final byte[] key, final long offset, final boolean value) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.setbit(key, offset, value);
    }
    
    @Override
    public Boolean setbit(final byte[] key, final long offset, final byte[] value) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.setbit(key, offset, value);
    }
    
    @Override
    public Boolean getbit(final byte[] key, final long offset) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.getbit(key, offset);
    }
    
    @Override
    public Long setrange(final byte[] key, final long offset, final byte[] value) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.setrange(key, offset, value);
    }
    
    @Override
    public byte[] getrange(final byte[] key, final long startOffset, final long endOffset) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.getrange(key, startOffset, endOffset);
    }
    
    @Override
    public Long move(final byte[] key, final int dbIndex) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.move(key, dbIndex);
    }
    
    @Override
    public byte[] echo(final byte[] arg) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(arg);
        return j.echo(arg);
    }
    
    @Override
    public List<byte[]> brpop(final byte[] arg) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(arg);
        return j.brpop(arg);
    }
    
    @Override
    public List<byte[]> blpop(final byte[] arg) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(arg);
        return j.blpop(arg);
    }
    
    @Override
    public Long bitcount(final byte[] key) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.bitcount(key);
    }
    
    @Override
    public Long bitcount(final byte[] key, final long start, final long end) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.bitcount(key, start, end);
    }
    
    @Override
    public Long pfadd(final byte[] key, final byte[]... elements) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.pfadd(key, elements);
    }
    
    @Override
    public long pfcount(final byte[] key) {
        final Jedis j = ((Sharded<Jedis, S>)this).getShard(key);
        return j.pfcount(key);
    }
}
