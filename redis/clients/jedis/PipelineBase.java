package redis.clients.jedis;

import java.util.*;

abstract class PipelineBase extends Queable implements BinaryRedisPipeline, RedisPipeline
{
    protected abstract Client getClient(final String p0);
    
    protected abstract Client getClient(final byte[] p0);
    
    @Override
    public Response<Long> append(final String key, final String value) {
        this.getClient(key).append(key, value);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<Long> append(final byte[] key, final byte[] value) {
        this.getClient(key).append(key, value);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<List<String>> blpop(final String key) {
        final String[] temp = { key };
        this.getClient(key).blpop(temp);
        return this.getResponse(BuilderFactory.STRING_LIST);
    }
    
    @Override
    public Response<List<String>> brpop(final String key) {
        final String[] temp = { key };
        this.getClient(key).brpop(temp);
        return this.getResponse(BuilderFactory.STRING_LIST);
    }
    
    @Override
    public Response<List<byte[]>> blpop(final byte[] key) {
        final byte[][] temp = { key };
        this.getClient(key).blpop(temp);
        return this.getResponse(BuilderFactory.BYTE_ARRAY_LIST);
    }
    
    @Override
    public Response<List<byte[]>> brpop(final byte[] key) {
        final byte[][] temp = { key };
        this.getClient(key).brpop(temp);
        return this.getResponse(BuilderFactory.BYTE_ARRAY_LIST);
    }
    
    @Override
    public Response<Long> decr(final String key) {
        this.getClient(key).decr(key);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<Long> decr(final byte[] key) {
        this.getClient(key).decr(key);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<Long> decrBy(final String key, final long integer) {
        this.getClient(key).decrBy(key, integer);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<Long> decrBy(final byte[] key, final long integer) {
        this.getClient(key).decrBy(key, integer);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<Long> del(final String key) {
        this.getClient(key).del(key);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<Long> del(final byte[] key) {
        this.getClient(key).del(new byte[][] { key });
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<String> echo(final String string) {
        this.getClient(string).echo(string);
        return this.getResponse(BuilderFactory.STRING);
    }
    
    @Override
    public Response<byte[]> echo(final byte[] string) {
        this.getClient(string).echo(string);
        return this.getResponse(BuilderFactory.BYTE_ARRAY);
    }
    
    @Override
    public Response<Boolean> exists(final String key) {
        this.getClient(key).exists(key);
        return this.getResponse(BuilderFactory.BOOLEAN);
    }
    
    @Override
    public Response<Boolean> exists(final byte[] key) {
        this.getClient(key).exists(key);
        return this.getResponse(BuilderFactory.BOOLEAN);
    }
    
    @Override
    public Response<Long> expire(final String key, final int seconds) {
        this.getClient(key).expire(key, seconds);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<Long> expire(final byte[] key, final int seconds) {
        this.getClient(key).expire(key, seconds);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<Long> expireAt(final String key, final long unixTime) {
        this.getClient(key).expireAt(key, unixTime);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<Long> expireAt(final byte[] key, final long unixTime) {
        this.getClient(key).expireAt(key, unixTime);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<String> get(final String key) {
        this.getClient(key).get(key);
        return this.getResponse(BuilderFactory.STRING);
    }
    
    @Override
    public Response<byte[]> get(final byte[] key) {
        this.getClient(key).get(key);
        return this.getResponse(BuilderFactory.BYTE_ARRAY);
    }
    
    @Override
    public Response<Boolean> getbit(final String key, final long offset) {
        this.getClient(key).getbit(key, offset);
        return this.getResponse(BuilderFactory.BOOLEAN);
    }
    
    @Override
    public Response<Boolean> getbit(final byte[] key, final long offset) {
        this.getClient(key).getbit(key, offset);
        return this.getResponse(BuilderFactory.BOOLEAN);
    }
    
    public Response<Long> bitpos(final String key, final boolean value) {
        return this.bitpos(key, value, new BitPosParams());
    }
    
    public Response<Long> bitpos(final String key, final boolean value, final BitPosParams params) {
        this.getClient(key).bitpos(key, value, params);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    public Response<Long> bitpos(final byte[] key, final boolean value) {
        return this.bitpos(key, value, new BitPosParams());
    }
    
    public Response<Long> bitpos(final byte[] key, final boolean value, final BitPosParams params) {
        this.getClient(key).bitpos(key, value, params);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<String> getrange(final String key, final long startOffset, final long endOffset) {
        this.getClient(key).getrange(key, startOffset, endOffset);
        return this.getResponse(BuilderFactory.STRING);
    }
    
    @Override
    public Response<String> getSet(final String key, final String value) {
        this.getClient(key).getSet(key, value);
        return this.getResponse(BuilderFactory.STRING);
    }
    
    @Override
    public Response<byte[]> getSet(final byte[] key, final byte[] value) {
        this.getClient(key).getSet(key, value);
        return this.getResponse(BuilderFactory.BYTE_ARRAY);
    }
    
    @Override
    public Response<Long> getrange(final byte[] key, final long startOffset, final long endOffset) {
        this.getClient(key).getrange(key, startOffset, endOffset);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<Long> hdel(final String key, final String... field) {
        this.getClient(key).hdel(key, field);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<Long> hdel(final byte[] key, final byte[]... field) {
        this.getClient(key).hdel(key, field);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<Boolean> hexists(final String key, final String field) {
        this.getClient(key).hexists(key, field);
        return this.getResponse(BuilderFactory.BOOLEAN);
    }
    
    @Override
    public Response<Boolean> hexists(final byte[] key, final byte[] field) {
        this.getClient(key).hexists(key, field);
        return this.getResponse(BuilderFactory.BOOLEAN);
    }
    
    @Override
    public Response<String> hget(final String key, final String field) {
        this.getClient(key).hget(key, field);
        return this.getResponse(BuilderFactory.STRING);
    }
    
    @Override
    public Response<byte[]> hget(final byte[] key, final byte[] field) {
        this.getClient(key).hget(key, field);
        return this.getResponse(BuilderFactory.BYTE_ARRAY);
    }
    
    @Override
    public Response<Map<String, String>> hgetAll(final String key) {
        this.getClient(key).hgetAll(key);
        return this.getResponse(BuilderFactory.STRING_MAP);
    }
    
    @Override
    public Response<Map<byte[], byte[]>> hgetAll(final byte[] key) {
        this.getClient(key).hgetAll(key);
        return this.getResponse(BuilderFactory.BYTE_ARRAY_MAP);
    }
    
    @Override
    public Response<Long> hincrBy(final String key, final String field, final long value) {
        this.getClient(key).hincrBy(key, field, value);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<Long> hincrBy(final byte[] key, final byte[] field, final long value) {
        this.getClient(key).hincrBy(key, field, value);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<Set<String>> hkeys(final String key) {
        this.getClient(key).hkeys(key);
        return this.getResponse(BuilderFactory.STRING_SET);
    }
    
    @Override
    public Response<Set<byte[]>> hkeys(final byte[] key) {
        this.getClient(key).hkeys(key);
        return this.getResponse(BuilderFactory.BYTE_ARRAY_ZSET);
    }
    
    @Override
    public Response<Long> hlen(final String key) {
        this.getClient(key).hlen(key);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<Long> hlen(final byte[] key) {
        this.getClient(key).hlen(key);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<List<String>> hmget(final String key, final String... fields) {
        this.getClient(key).hmget(key, fields);
        return this.getResponse(BuilderFactory.STRING_LIST);
    }
    
    @Override
    public Response<List<byte[]>> hmget(final byte[] key, final byte[]... fields) {
        this.getClient(key).hmget(key, fields);
        return this.getResponse(BuilderFactory.BYTE_ARRAY_LIST);
    }
    
    @Override
    public Response<String> hmset(final String key, final Map<String, String> hash) {
        this.getClient(key).hmset(key, hash);
        return this.getResponse(BuilderFactory.STRING);
    }
    
    @Override
    public Response<String> hmset(final byte[] key, final Map<byte[], byte[]> hash) {
        this.getClient(key).hmset(key, hash);
        return this.getResponse(BuilderFactory.STRING);
    }
    
    @Override
    public Response<Long> hset(final String key, final String field, final String value) {
        this.getClient(key).hset(key, field, value);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<Long> hset(final byte[] key, final byte[] field, final byte[] value) {
        this.getClient(key).hset(key, field, value);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<Long> hsetnx(final String key, final String field, final String value) {
        this.getClient(key).hsetnx(key, field, value);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<Long> hsetnx(final byte[] key, final byte[] field, final byte[] value) {
        this.getClient(key).hsetnx(key, field, value);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<List<String>> hvals(final String key) {
        this.getClient(key).hvals(key);
        return this.getResponse(BuilderFactory.STRING_LIST);
    }
    
    @Override
    public Response<List<byte[]>> hvals(final byte[] key) {
        this.getClient(key).hvals(key);
        return this.getResponse(BuilderFactory.BYTE_ARRAY_LIST);
    }
    
    @Override
    public Response<Long> incr(final String key) {
        this.getClient(key).incr(key);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<Long> incr(final byte[] key) {
        this.getClient(key).incr(key);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<Long> incrBy(final String key, final long integer) {
        this.getClient(key).incrBy(key, integer);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<Long> incrBy(final byte[] key, final long integer) {
        this.getClient(key).incrBy(key, integer);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<String> lindex(final String key, final long index) {
        this.getClient(key).lindex(key, index);
        return this.getResponse(BuilderFactory.STRING);
    }
    
    @Override
    public Response<byte[]> lindex(final byte[] key, final long index) {
        this.getClient(key).lindex(key, index);
        return this.getResponse(BuilderFactory.BYTE_ARRAY);
    }
    
    @Override
    public Response<Long> linsert(final String key, final BinaryClient.LIST_POSITION where, final String pivot, final String value) {
        this.getClient(key).linsert(key, where, pivot, value);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<Long> linsert(final byte[] key, final BinaryClient.LIST_POSITION where, final byte[] pivot, final byte[] value) {
        this.getClient(key).linsert(key, where, pivot, value);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<Long> llen(final String key) {
        this.getClient(key).llen(key);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<Long> llen(final byte[] key) {
        this.getClient(key).llen(key);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<String> lpop(final String key) {
        this.getClient(key).lpop(key);
        return this.getResponse(BuilderFactory.STRING);
    }
    
    @Override
    public Response<byte[]> lpop(final byte[] key) {
        this.getClient(key).lpop(key);
        return this.getResponse(BuilderFactory.BYTE_ARRAY);
    }
    
    @Override
    public Response<Long> lpush(final String key, final String... string) {
        this.getClient(key).lpush(key, string);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<Long> lpush(final byte[] key, final byte[]... string) {
        this.getClient(key).lpush(key, string);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<Long> lpushx(final String key, final String... string) {
        this.getClient(key).lpushx(key, string);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<Long> lpushx(final byte[] key, final byte[]... bytes) {
        this.getClient(key).lpushx(key, bytes);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<List<String>> lrange(final String key, final long start, final long end) {
        this.getClient(key).lrange(key, start, end);
        return this.getResponse(BuilderFactory.STRING_LIST);
    }
    
    @Override
    public Response<List<byte[]>> lrange(final byte[] key, final long start, final long end) {
        this.getClient(key).lrange(key, start, end);
        return this.getResponse(BuilderFactory.BYTE_ARRAY_LIST);
    }
    
    @Override
    public Response<Long> lrem(final String key, final long count, final String value) {
        this.getClient(key).lrem(key, count, value);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<Long> lrem(final byte[] key, final long count, final byte[] value) {
        this.getClient(key).lrem(key, count, value);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<String> lset(final String key, final long index, final String value) {
        this.getClient(key).lset(key, index, value);
        return this.getResponse(BuilderFactory.STRING);
    }
    
    @Override
    public Response<String> lset(final byte[] key, final long index, final byte[] value) {
        this.getClient(key).lset(key, index, value);
        return this.getResponse(BuilderFactory.STRING);
    }
    
    @Override
    public Response<String> ltrim(final String key, final long start, final long end) {
        this.getClient(key).ltrim(key, start, end);
        return this.getResponse(BuilderFactory.STRING);
    }
    
    @Override
    public Response<String> ltrim(final byte[] key, final long start, final long end) {
        this.getClient(key).ltrim(key, start, end);
        return this.getResponse(BuilderFactory.STRING);
    }
    
    @Override
    public Response<Long> move(final String key, final int dbIndex) {
        this.getClient(key).move(key, dbIndex);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<Long> move(final byte[] key, final int dbIndex) {
        this.getClient(key).move(key, dbIndex);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<Long> persist(final String key) {
        this.getClient(key).persist(key);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<Long> persist(final byte[] key) {
        this.getClient(key).persist(key);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<String> rpop(final String key) {
        this.getClient(key).rpop(key);
        return this.getResponse(BuilderFactory.STRING);
    }
    
    @Override
    public Response<byte[]> rpop(final byte[] key) {
        this.getClient(key).rpop(key);
        return this.getResponse(BuilderFactory.BYTE_ARRAY);
    }
    
    @Override
    public Response<Long> rpush(final String key, final String... string) {
        this.getClient(key).rpush(key, string);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<Long> rpush(final byte[] key, final byte[]... string) {
        this.getClient(key).rpush(key, string);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<Long> rpushx(final String key, final String... string) {
        this.getClient(key).rpushx(key, string);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<Long> rpushx(final byte[] key, final byte[]... string) {
        this.getClient(key).rpushx(key, string);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<Long> sadd(final String key, final String... member) {
        this.getClient(key).sadd(key, member);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<Long> sadd(final byte[] key, final byte[]... member) {
        this.getClient(key).sadd(key, member);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<Long> scard(final String key) {
        this.getClient(key).scard(key);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<Long> scard(final byte[] key) {
        this.getClient(key).scard(key);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<String> set(final String key, final String value) {
        this.getClient(key).set(key, value);
        return this.getResponse(BuilderFactory.STRING);
    }
    
    @Override
    public Response<String> set(final byte[] key, final byte[] value) {
        this.getClient(key).set(key, value);
        return this.getResponse(BuilderFactory.STRING);
    }
    
    @Override
    public Response<Boolean> setbit(final String key, final long offset, final boolean value) {
        this.getClient(key).setbit(key, offset, value);
        return this.getResponse(BuilderFactory.BOOLEAN);
    }
    
    @Override
    public Response<Boolean> setbit(final byte[] key, final long offset, final byte[] value) {
        this.getClient(key).setbit(key, offset, value);
        return this.getResponse(BuilderFactory.BOOLEAN);
    }
    
    @Override
    public Response<String> setex(final String key, final int seconds, final String value) {
        this.getClient(key).setex(key, seconds, value);
        return this.getResponse(BuilderFactory.STRING);
    }
    
    @Override
    public Response<String> setex(final byte[] key, final int seconds, final byte[] value) {
        this.getClient(key).setex(key, seconds, value);
        return this.getResponse(BuilderFactory.STRING);
    }
    
    @Override
    public Response<Long> setnx(final String key, final String value) {
        this.getClient(key).setnx(key, value);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<Long> setnx(final byte[] key, final byte[] value) {
        this.getClient(key).setnx(key, value);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<Long> setrange(final String key, final long offset, final String value) {
        this.getClient(key).setrange(key, offset, value);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<Long> setrange(final byte[] key, final long offset, final byte[] value) {
        this.getClient(key).setrange(key, offset, value);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<Boolean> sismember(final String key, final String member) {
        this.getClient(key).sismember(key, member);
        return this.getResponse(BuilderFactory.BOOLEAN);
    }
    
    @Override
    public Response<Boolean> sismember(final byte[] key, final byte[] member) {
        this.getClient(key).sismember(key, member);
        return this.getResponse(BuilderFactory.BOOLEAN);
    }
    
    @Override
    public Response<Set<String>> smembers(final String key) {
        this.getClient(key).smembers(key);
        return this.getResponse(BuilderFactory.STRING_SET);
    }
    
    @Override
    public Response<Set<byte[]>> smembers(final byte[] key) {
        this.getClient(key).smembers(key);
        return this.getResponse(BuilderFactory.BYTE_ARRAY_ZSET);
    }
    
    @Override
    public Response<List<String>> sort(final String key) {
        this.getClient(key).sort(key);
        return this.getResponse(BuilderFactory.STRING_LIST);
    }
    
    @Override
    public Response<List<byte[]>> sort(final byte[] key) {
        this.getClient(key).sort(key);
        return this.getResponse(BuilderFactory.BYTE_ARRAY_LIST);
    }
    
    @Override
    public Response<List<String>> sort(final String key, final SortingParams sortingParameters) {
        this.getClient(key).sort(key, sortingParameters);
        return this.getResponse(BuilderFactory.STRING_LIST);
    }
    
    @Override
    public Response<List<byte[]>> sort(final byte[] key, final SortingParams sortingParameters) {
        this.getClient(key).sort(key, sortingParameters);
        return this.getResponse(BuilderFactory.BYTE_ARRAY_LIST);
    }
    
    @Override
    public Response<String> spop(final String key) {
        this.getClient(key).spop(key);
        return this.getResponse(BuilderFactory.STRING);
    }
    
    @Override
    public Response<byte[]> spop(final byte[] key) {
        this.getClient(key).spop(key);
        return this.getResponse(BuilderFactory.BYTE_ARRAY);
    }
    
    @Override
    public Response<String> srandmember(final String key) {
        this.getClient(key).srandmember(key);
        return this.getResponse(BuilderFactory.STRING);
    }
    
    public Response<List<String>> srandmember(final String key, final int count) {
        this.getClient(key).srandmember(key, count);
        return this.getResponse(BuilderFactory.STRING_LIST);
    }
    
    @Override
    public Response<byte[]> srandmember(final byte[] key) {
        this.getClient(key).srandmember(key);
        return this.getResponse(BuilderFactory.BYTE_ARRAY);
    }
    
    public Response<List<byte[]>> srandmember(final byte[] key, final int count) {
        this.getClient(key).srandmember(key, count);
        return this.getResponse(BuilderFactory.BYTE_ARRAY_LIST);
    }
    
    @Override
    public Response<Long> srem(final String key, final String... member) {
        this.getClient(key).srem(key, member);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<Long> srem(final byte[] key, final byte[]... member) {
        this.getClient(key).srem(key, member);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<Long> strlen(final String key) {
        this.getClient(key).strlen(key);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<Long> strlen(final byte[] key) {
        this.getClient(key).strlen(key);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<String> substr(final String key, final int start, final int end) {
        this.getClient(key).substr(key, start, end);
        return this.getResponse(BuilderFactory.STRING);
    }
    
    @Override
    public Response<String> substr(final byte[] key, final int start, final int end) {
        this.getClient(key).substr(key, start, end);
        return this.getResponse(BuilderFactory.STRING);
    }
    
    @Override
    public Response<Long> ttl(final String key) {
        this.getClient(key).ttl(key);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<Long> ttl(final byte[] key) {
        this.getClient(key).ttl(key);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<String> type(final String key) {
        this.getClient(key).type(key);
        return this.getResponse(BuilderFactory.STRING);
    }
    
    @Override
    public Response<String> type(final byte[] key) {
        this.getClient(key).type(key);
        return this.getResponse(BuilderFactory.STRING);
    }
    
    @Override
    public Response<Long> zadd(final String key, final double score, final String member) {
        this.getClient(key).zadd(key, score, member);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    public Response<Long> zadd(final String key, final Map<String, Double> scoreMembers) {
        this.getClient(key).zadd(key, scoreMembers);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<Long> zadd(final byte[] key, final double score, final byte[] member) {
        this.getClient(key).zadd(key, score, member);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<Long> zcard(final String key) {
        this.getClient(key).zcard(key);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<Long> zcard(final byte[] key) {
        this.getClient(key).zcard(key);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<Long> zcount(final String key, final double min, final double max) {
        this.getClient(key).zcount(key, min, max);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    public Response<Long> zcount(final String key, final String min, final String max) {
        this.getClient(key).zcount(key, min, max);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<Long> zcount(final byte[] key, final double min, final double max) {
        this.getClient(key).zcount(key, Protocol.toByteArray(min), Protocol.toByteArray(max));
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<Double> zincrby(final String key, final double score, final String member) {
        this.getClient(key).zincrby(key, score, member);
        return this.getResponse(BuilderFactory.DOUBLE);
    }
    
    @Override
    public Response<Double> zincrby(final byte[] key, final double score, final byte[] member) {
        this.getClient(key).zincrby(key, score, member);
        return this.getResponse(BuilderFactory.DOUBLE);
    }
    
    @Override
    public Response<Set<String>> zrange(final String key, final long start, final long end) {
        this.getClient(key).zrange(key, start, end);
        return this.getResponse(BuilderFactory.STRING_ZSET);
    }
    
    @Override
    public Response<Set<byte[]>> zrange(final byte[] key, final long start, final long end) {
        this.getClient(key).zrange(key, start, end);
        return this.getResponse(BuilderFactory.BYTE_ARRAY_ZSET);
    }
    
    @Override
    public Response<Set<String>> zrangeByScore(final String key, final double min, final double max) {
        this.getClient(key).zrangeByScore(key, min, max);
        return this.getResponse(BuilderFactory.STRING_ZSET);
    }
    
    @Override
    public Response<Set<byte[]>> zrangeByScore(final byte[] key, final double min, final double max) {
        return this.zrangeByScore(key, Protocol.toByteArray(min), Protocol.toByteArray(max));
    }
    
    @Override
    public Response<Set<String>> zrangeByScore(final String key, final String min, final String max) {
        this.getClient(key).zrangeByScore(key, min, max);
        return this.getResponse(BuilderFactory.STRING_ZSET);
    }
    
    @Override
    public Response<Set<byte[]>> zrangeByScore(final byte[] key, final byte[] min, final byte[] max) {
        this.getClient(key).zrangeByScore(key, min, max);
        return this.getResponse(BuilderFactory.BYTE_ARRAY_ZSET);
    }
    
    @Override
    public Response<Set<String>> zrangeByScore(final String key, final double min, final double max, final int offset, final int count) {
        this.getClient(key).zrangeByScore(key, min, max, offset, count);
        return this.getResponse(BuilderFactory.STRING_ZSET);
    }
    
    public Response<Set<String>> zrangeByScore(final String key, final String min, final String max, final int offset, final int count) {
        this.getClient(key).zrangeByScore(key, min, max, offset, count);
        return this.getResponse(BuilderFactory.STRING_ZSET);
    }
    
    @Override
    public Response<Set<byte[]>> zrangeByScore(final byte[] key, final double min, final double max, final int offset, final int count) {
        return this.zrangeByScore(key, Protocol.toByteArray(min), Protocol.toByteArray(max), offset, count);
    }
    
    @Override
    public Response<Set<byte[]>> zrangeByScore(final byte[] key, final byte[] min, final byte[] max, final int offset, final int count) {
        this.getClient(key).zrangeByScore(key, min, max, offset, count);
        return this.getResponse(BuilderFactory.BYTE_ARRAY_ZSET);
    }
    
    @Override
    public Response<Set<Tuple>> zrangeByScoreWithScores(final String key, final double min, final double max) {
        this.getClient(key).zrangeByScoreWithScores(key, min, max);
        return this.getResponse(BuilderFactory.TUPLE_ZSET);
    }
    
    public Response<Set<Tuple>> zrangeByScoreWithScores(final String key, final String min, final String max) {
        this.getClient(key).zrangeByScoreWithScores(key, min, max);
        return this.getResponse(BuilderFactory.TUPLE_ZSET);
    }
    
    @Override
    public Response<Set<Tuple>> zrangeByScoreWithScores(final byte[] key, final double min, final double max) {
        return this.zrangeByScoreWithScores(key, Protocol.toByteArray(min), Protocol.toByteArray(max));
    }
    
    @Override
    public Response<Set<Tuple>> zrangeByScoreWithScores(final byte[] key, final byte[] min, final byte[] max) {
        this.getClient(key).zrangeByScoreWithScores(key, min, max);
        return this.getResponse(BuilderFactory.TUPLE_ZSET_BINARY);
    }
    
    @Override
    public Response<Set<Tuple>> zrangeByScoreWithScores(final String key, final double min, final double max, final int offset, final int count) {
        this.getClient(key).zrangeByScoreWithScores(key, min, max, offset, count);
        return this.getResponse(BuilderFactory.TUPLE_ZSET);
    }
    
    public Response<Set<Tuple>> zrangeByScoreWithScores(final String key, final String min, final String max, final int offset, final int count) {
        this.getClient(key).zrangeByScoreWithScores(key, min, max, offset, count);
        return this.getResponse(BuilderFactory.TUPLE_ZSET);
    }
    
    @Override
    public Response<Set<Tuple>> zrangeByScoreWithScores(final byte[] key, final double min, final double max, final int offset, final int count) {
        this.getClient(key).zrangeByScoreWithScores(key, Protocol.toByteArray(min), Protocol.toByteArray(max), offset, count);
        return this.getResponse(BuilderFactory.TUPLE_ZSET_BINARY);
    }
    
    @Override
    public Response<Set<Tuple>> zrangeByScoreWithScores(final byte[] key, final byte[] min, final byte[] max, final int offset, final int count) {
        this.getClient(key).zrangeByScoreWithScores(key, min, max, offset, count);
        return this.getResponse(BuilderFactory.TUPLE_ZSET_BINARY);
    }
    
    @Override
    public Response<Set<String>> zrevrangeByScore(final String key, final double max, final double min) {
        this.getClient(key).zrevrangeByScore(key, max, min);
        return this.getResponse(BuilderFactory.STRING_ZSET);
    }
    
    @Override
    public Response<Set<byte[]>> zrevrangeByScore(final byte[] key, final double max, final double min) {
        this.getClient(key).zrevrangeByScore(key, Protocol.toByteArray(max), Protocol.toByteArray(min));
        return this.getResponse(BuilderFactory.BYTE_ARRAY_ZSET);
    }
    
    @Override
    public Response<Set<String>> zrevrangeByScore(final String key, final String max, final String min) {
        this.getClient(key).zrevrangeByScore(key, max, min);
        return this.getResponse(BuilderFactory.STRING_ZSET);
    }
    
    @Override
    public Response<Set<byte[]>> zrevrangeByScore(final byte[] key, final byte[] max, final byte[] min) {
        this.getClient(key).zrevrangeByScore(key, max, min);
        return this.getResponse(BuilderFactory.BYTE_ARRAY_ZSET);
    }
    
    @Override
    public Response<Set<String>> zrevrangeByScore(final String key, final double max, final double min, final int offset, final int count) {
        this.getClient(key).zrevrangeByScore(key, max, min, offset, count);
        return this.getResponse(BuilderFactory.STRING_ZSET);
    }
    
    public Response<Set<String>> zrevrangeByScore(final String key, final String max, final String min, final int offset, final int count) {
        this.getClient(key).zrevrangeByScore(key, max, min, offset, count);
        return this.getResponse(BuilderFactory.STRING_ZSET);
    }
    
    @Override
    public Response<Set<byte[]>> zrevrangeByScore(final byte[] key, final double max, final double min, final int offset, final int count) {
        this.getClient(key).zrevrangeByScore(key, Protocol.toByteArray(max), Protocol.toByteArray(min), offset, count);
        return this.getResponse(BuilderFactory.BYTE_ARRAY_ZSET);
    }
    
    @Override
    public Response<Set<byte[]>> zrevrangeByScore(final byte[] key, final byte[] max, final byte[] min, final int offset, final int count) {
        this.getClient(key).zrevrangeByScore(key, max, min, offset, count);
        return this.getResponse(BuilderFactory.BYTE_ARRAY_ZSET);
    }
    
    @Override
    public Response<Set<Tuple>> zrevrangeByScoreWithScores(final String key, final double max, final double min) {
        this.getClient(key).zrevrangeByScoreWithScores(key, max, min);
        return this.getResponse(BuilderFactory.TUPLE_ZSET);
    }
    
    public Response<Set<Tuple>> zrevrangeByScoreWithScores(final String key, final String max, final String min) {
        this.getClient(key).zrevrangeByScoreWithScores(key, max, min);
        return this.getResponse(BuilderFactory.TUPLE_ZSET);
    }
    
    @Override
    public Response<Set<Tuple>> zrevrangeByScoreWithScores(final byte[] key, final double max, final double min) {
        this.getClient(key).zrevrangeByScoreWithScores(key, Protocol.toByteArray(max), Protocol.toByteArray(min));
        return this.getResponse(BuilderFactory.TUPLE_ZSET_BINARY);
    }
    
    @Override
    public Response<Set<Tuple>> zrevrangeByScoreWithScores(final byte[] key, final byte[] max, final byte[] min) {
        this.getClient(key).zrevrangeByScoreWithScores(key, max, min);
        return this.getResponse(BuilderFactory.TUPLE_ZSET_BINARY);
    }
    
    @Override
    public Response<Set<Tuple>> zrevrangeByScoreWithScores(final String key, final double max, final double min, final int offset, final int count) {
        this.getClient(key).zrevrangeByScoreWithScores(key, max, min, offset, count);
        return this.getResponse(BuilderFactory.TUPLE_ZSET);
    }
    
    public Response<Set<Tuple>> zrevrangeByScoreWithScores(final String key, final String max, final String min, final int offset, final int count) {
        this.getClient(key).zrevrangeByScoreWithScores(key, max, min, offset, count);
        return this.getResponse(BuilderFactory.TUPLE_ZSET);
    }
    
    @Override
    public Response<Set<Tuple>> zrevrangeByScoreWithScores(final byte[] key, final double max, final double min, final int offset, final int count) {
        this.getClient(key).zrevrangeByScoreWithScores(key, Protocol.toByteArray(max), Protocol.toByteArray(min), offset, count);
        return this.getResponse(BuilderFactory.TUPLE_ZSET_BINARY);
    }
    
    @Override
    public Response<Set<Tuple>> zrevrangeByScoreWithScores(final byte[] key, final byte[] max, final byte[] min, final int offset, final int count) {
        this.getClient(key).zrevrangeByScoreWithScores(key, max, min, offset, count);
        return this.getResponse(BuilderFactory.TUPLE_ZSET_BINARY);
    }
    
    @Override
    public Response<Set<Tuple>> zrangeWithScores(final String key, final long start, final long end) {
        this.getClient(key).zrangeWithScores(key, start, end);
        return this.getResponse(BuilderFactory.TUPLE_ZSET);
    }
    
    @Override
    public Response<Set<Tuple>> zrangeWithScores(final byte[] key, final long start, final long end) {
        this.getClient(key).zrangeWithScores(key, start, end);
        return this.getResponse(BuilderFactory.TUPLE_ZSET_BINARY);
    }
    
    @Override
    public Response<Long> zrank(final String key, final String member) {
        this.getClient(key).zrank(key, member);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<Long> zrank(final byte[] key, final byte[] member) {
        this.getClient(key).zrank(key, member);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<Long> zrem(final String key, final String... member) {
        this.getClient(key).zrem(key, member);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<Long> zrem(final byte[] key, final byte[]... member) {
        this.getClient(key).zrem(key, member);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<Long> zremrangeByRank(final String key, final long start, final long end) {
        this.getClient(key).zremrangeByRank(key, start, end);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<Long> zremrangeByRank(final byte[] key, final long start, final long end) {
        this.getClient(key).zremrangeByRank(key, start, end);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<Long> zremrangeByScore(final String key, final double start, final double end) {
        this.getClient(key).zremrangeByScore(key, start, end);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    public Response<Long> zremrangeByScore(final String key, final String start, final String end) {
        this.getClient(key).zremrangeByScore(key, start, end);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<Long> zremrangeByScore(final byte[] key, final double start, final double end) {
        this.getClient(key).zremrangeByScore(key, Protocol.toByteArray(start), Protocol.toByteArray(end));
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<Long> zremrangeByScore(final byte[] key, final byte[] start, final byte[] end) {
        this.getClient(key).zremrangeByScore(key, start, end);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<Set<String>> zrevrange(final String key, final long start, final long end) {
        this.getClient(key).zrevrange(key, start, end);
        return this.getResponse(BuilderFactory.STRING_ZSET);
    }
    
    @Override
    public Response<Set<byte[]>> zrevrange(final byte[] key, final long start, final long end) {
        this.getClient(key).zrevrange(key, start, end);
        return this.getResponse(BuilderFactory.BYTE_ARRAY_ZSET);
    }
    
    @Override
    public Response<Set<Tuple>> zrevrangeWithScores(final String key, final long start, final long end) {
        this.getClient(key).zrevrangeWithScores(key, start, end);
        return this.getResponse(BuilderFactory.TUPLE_ZSET);
    }
    
    @Override
    public Response<Set<Tuple>> zrevrangeWithScores(final byte[] key, final long start, final long end) {
        this.getClient(key).zrevrangeWithScores(key, start, end);
        return this.getResponse(BuilderFactory.TUPLE_ZSET);
    }
    
    @Override
    public Response<Long> zrevrank(final String key, final String member) {
        this.getClient(key).zrevrank(key, member);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<Long> zrevrank(final byte[] key, final byte[] member) {
        this.getClient(key).zrevrank(key, member);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<Double> zscore(final String key, final String member) {
        this.getClient(key).zscore(key, member);
        return this.getResponse(BuilderFactory.DOUBLE);
    }
    
    @Override
    public Response<Double> zscore(final byte[] key, final byte[] member) {
        this.getClient(key).zscore(key, member);
        return this.getResponse(BuilderFactory.DOUBLE);
    }
    
    @Override
    public Response<Long> zlexcount(final byte[] key, final byte[] min, final byte[] max) {
        this.getClient(key).zlexcount(key, min, max);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<Long> zlexcount(final String key, final String min, final String max) {
        this.getClient(key).zlexcount(key, min, max);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<Set<byte[]>> zrangeByLex(final byte[] key, final byte[] min, final byte[] max) {
        this.getClient(key).zrangeByLex(key, min, max);
        return this.getResponse(BuilderFactory.BYTE_ARRAY_ZSET);
    }
    
    @Override
    public Response<Set<String>> zrangeByLex(final String key, final String min, final String max) {
        this.getClient(key).zrangeByLex(key, min, max);
        return this.getResponse(BuilderFactory.STRING_ZSET);
    }
    
    @Override
    public Response<Set<byte[]>> zrangeByLex(final byte[] key, final byte[] min, final byte[] max, final int offset, final int count) {
        this.getClient(key).zrangeByLex(key, min, max, offset, count);
        return this.getResponse(BuilderFactory.BYTE_ARRAY_ZSET);
    }
    
    @Override
    public Response<Set<String>> zrangeByLex(final String key, final String min, final String max, final int offset, final int count) {
        this.getClient(key).zrangeByLex(key, min, max, offset, count);
        return this.getResponse(BuilderFactory.STRING_ZSET);
    }
    
    @Override
    public Response<Long> zremrangeByLex(final byte[] key, final byte[] min, final byte[] max) {
        this.getClient(key).zremrangeByLex(key, min, max);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<Long> zremrangeByLex(final String key, final String min, final String max) {
        this.getClient(key).zremrangeByLex(key, min, max);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<Long> bitcount(final String key) {
        this.getClient(key).bitcount(key);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<Long> bitcount(final String key, final long start, final long end) {
        this.getClient(key).bitcount(key, start, end);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<Long> bitcount(final byte[] key) {
        this.getClient(key).bitcount(key);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<Long> bitcount(final byte[] key, final long start, final long end) {
        this.getClient(key).bitcount(key, start, end);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    public Response<byte[]> dump(final String key) {
        this.getClient(key).dump(key);
        return this.getResponse(BuilderFactory.BYTE_ARRAY);
    }
    
    public Response<byte[]> dump(final byte[] key) {
        this.getClient(key).dump(key);
        return this.getResponse(BuilderFactory.BYTE_ARRAY);
    }
    
    public Response<String> migrate(final String host, final int port, final String key, final int destinationDb, final int timeout) {
        this.getClient(key).migrate(host, port, key, destinationDb, timeout);
        return this.getResponse(BuilderFactory.STRING);
    }
    
    public Response<String> migrate(final byte[] host, final int port, final byte[] key, final int destinationDb, final int timeout) {
        this.getClient(key).migrate(host, port, key, destinationDb, timeout);
        return this.getResponse(BuilderFactory.STRING);
    }
    
    public Response<Long> objectRefcount(final String key) {
        this.getClient(key).objectRefcount(key);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    public Response<Long> objectRefcount(final byte[] key) {
        this.getClient(key).objectRefcount(key);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    public Response<String> objectEncoding(final String key) {
        this.getClient(key).objectEncoding(key);
        return this.getResponse(BuilderFactory.STRING);
    }
    
    public Response<byte[]> objectEncoding(final byte[] key) {
        this.getClient(key).objectEncoding(key);
        return this.getResponse(BuilderFactory.BYTE_ARRAY);
    }
    
    public Response<Long> objectIdletime(final String key) {
        this.getClient(key).objectIdletime(key);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    public Response<Long> objectIdletime(final byte[] key) {
        this.getClient(key).objectIdletime(key);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Deprecated
    public Response<Long> pexpire(final String key, final int milliseconds) {
        return this.pexpire(key, (long)milliseconds);
    }
    
    @Deprecated
    public Response<Long> pexpire(final byte[] key, final int milliseconds) {
        return this.pexpire(key, (long)milliseconds);
    }
    
    public Response<Long> pexpire(final String key, final long milliseconds) {
        this.getClient(key).pexpire(key, milliseconds);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    public Response<Long> pexpire(final byte[] key, final long milliseconds) {
        this.getClient(key).pexpire(key, milliseconds);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    public Response<Long> pexpireAt(final String key, final long millisecondsTimestamp) {
        this.getClient(key).pexpireAt(key, millisecondsTimestamp);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    public Response<Long> pexpireAt(final byte[] key, final long millisecondsTimestamp) {
        this.getClient(key).pexpireAt(key, millisecondsTimestamp);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    public Response<Long> pttl(final String key) {
        this.getClient(key).pttl(key);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    public Response<Long> pttl(final byte[] key) {
        this.getClient(key).pttl(key);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    public Response<String> restore(final String key, final int ttl, final byte[] serializedValue) {
        this.getClient(key).restore(key, ttl, serializedValue);
        return this.getResponse(BuilderFactory.STRING);
    }
    
    public Response<String> restore(final byte[] key, final int ttl, final byte[] serializedValue) {
        this.getClient(key).restore(key, ttl, serializedValue);
        return this.getResponse(BuilderFactory.STRING);
    }
    
    public Response<Double> incrByFloat(final String key, final double increment) {
        this.getClient(key).incrByFloat(key, increment);
        return this.getResponse(BuilderFactory.DOUBLE);
    }
    
    public Response<Double> incrByFloat(final byte[] key, final double increment) {
        this.getClient(key).incrByFloat(key, increment);
        return this.getResponse(BuilderFactory.DOUBLE);
    }
    
    public Response<String> psetex(final String key, final int milliseconds, final String value) {
        this.getClient(key).psetex(key, milliseconds, value);
        return this.getResponse(BuilderFactory.STRING);
    }
    
    public Response<String> psetex(final byte[] key, final int milliseconds, final byte[] value) {
        this.getClient(key).psetex(key, milliseconds, value);
        return this.getResponse(BuilderFactory.STRING);
    }
    
    public Response<String> set(final String key, final String value, final String nxxx) {
        this.getClient(key).set(key, value, nxxx);
        return this.getResponse(BuilderFactory.STRING);
    }
    
    public Response<String> set(final byte[] key, final byte[] value, final byte[] nxxx) {
        this.getClient(key).set(key, value, nxxx);
        return this.getResponse(BuilderFactory.STRING);
    }
    
    public Response<String> set(final String key, final String value, final String nxxx, final String expx, final int time) {
        this.getClient(key).set(key, value, nxxx, expx, time);
        return this.getResponse(BuilderFactory.STRING);
    }
    
    public Response<String> set(final byte[] key, final byte[] value, final byte[] nxxx, final byte[] expx, final int time) {
        this.getClient(key).set(key, value, nxxx, expx, time);
        return this.getResponse(BuilderFactory.STRING);
    }
    
    public Response<Double> hincrByFloat(final String key, final String field, final double increment) {
        this.getClient(key).hincrByFloat(key, field, increment);
        return this.getResponse(BuilderFactory.DOUBLE);
    }
    
    public Response<Double> hincrByFloat(final byte[] key, final byte[] field, final double increment) {
        this.getClient(key).hincrByFloat(key, field, increment);
        return this.getResponse(BuilderFactory.DOUBLE);
    }
    
    public Response<String> eval(final String script) {
        return this.eval(script, 0, new String[0]);
    }
    
    public Response<String> eval(final String script, final List<String> keys, final List<String> args) {
        final String[] argv = Jedis.getParams(keys, args);
        return this.eval(script, keys.size(), argv);
    }
    
    public Response<String> eval(final String script, final int numKeys, final String[] argv) {
        this.getClient(script).eval(script, numKeys, argv);
        return this.getResponse(BuilderFactory.STRING);
    }
    
    public Response<String> evalsha(final String script) {
        return this.evalsha(script, 0, new String[0]);
    }
    
    public Response<String> evalsha(final String sha1, final List<String> keys, final List<String> args) {
        final String[] argv = Jedis.getParams(keys, args);
        return this.evalsha(sha1, keys.size(), argv);
    }
    
    public Response<String> evalsha(final String sha1, final int numKeys, final String[] argv) {
        this.getClient(sha1).evalsha(sha1, numKeys, argv);
        return this.getResponse(BuilderFactory.STRING);
    }
    
    @Override
    public Response<Long> pfadd(final byte[] key, final byte[]... elements) {
        this.getClient(key).pfadd(key, elements);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<Long> pfcount(final byte[] key) {
        this.getClient(key).pfcount(key);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<Long> pfadd(final String key, final String... elements) {
        this.getClient(key).pfadd(key, elements);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<Long> pfcount(final String key) {
        this.getClient(key).pfcount(key);
        return this.getResponse(BuilderFactory.LONG);
    }
}
