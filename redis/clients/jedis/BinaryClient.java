package redis.clients.jedis;

import java.util.*;
import redis.clients.util.*;

public class BinaryClient extends Connection
{
    private boolean isInMulti;
    private String password;
    private long db;
    private boolean isInWatch;
    
    public boolean isInMulti() {
        return this.isInMulti;
    }
    
    public boolean isInWatch() {
        return this.isInWatch;
    }
    
    public BinaryClient(final String host) {
        super(host);
    }
    
    public BinaryClient(final String host, final int port) {
        super(host, port);
    }
    
    private byte[][] joinParameters(final byte[] first, final byte[][] rest) {
        final byte[][] result = new byte[rest.length + 1][];
        result[0] = first;
        for (int i = 0; i < rest.length; ++i) {
            result[i + 1] = rest[i];
        }
        return result;
    }
    
    public void setPassword(final String password) {
        this.password = password;
    }
    
    @Override
    public void connect() {
        if (!this.isConnected()) {
            super.connect();
            if (this.password != null) {
                this.auth(this.password);
                this.getStatusCodeReply();
            }
            if (this.db > 0L) {
                this.select((int)(Object)Long.valueOf(this.db));
                this.getStatusCodeReply();
            }
        }
    }
    
    public void ping() {
        this.sendCommand(Protocol.Command.PING);
    }
    
    public void set(final byte[] key, final byte[] value) {
        this.sendCommand(Protocol.Command.SET, new byte[][] { key, value });
    }
    
    public void set(final byte[] key, final byte[] value, final byte[] nxxx, final byte[] expx, final long time) {
        this.sendCommand(Protocol.Command.SET, new byte[][] { key, value, nxxx, expx, Protocol.toByteArray(time) });
    }
    
    public void get(final byte[] key) {
        this.sendCommand(Protocol.Command.GET, new byte[][] { key });
    }
    
    public void quit() {
        this.db = 0L;
        this.sendCommand(Protocol.Command.QUIT);
    }
    
    public void exists(final byte[] key) {
        this.sendCommand(Protocol.Command.EXISTS, new byte[][] { key });
    }
    
    public void del(final byte[]... keys) {
        this.sendCommand(Protocol.Command.DEL, keys);
    }
    
    public void type(final byte[] key) {
        this.sendCommand(Protocol.Command.TYPE, new byte[][] { key });
    }
    
    public void flushDB() {
        this.sendCommand(Protocol.Command.FLUSHDB);
    }
    
    public void keys(final byte[] pattern) {
        this.sendCommand(Protocol.Command.KEYS, new byte[][] { pattern });
    }
    
    public void randomKey() {
        this.sendCommand(Protocol.Command.RANDOMKEY);
    }
    
    public void rename(final byte[] oldkey, final byte[] newkey) {
        this.sendCommand(Protocol.Command.RENAME, new byte[][] { oldkey, newkey });
    }
    
    public void renamenx(final byte[] oldkey, final byte[] newkey) {
        this.sendCommand(Protocol.Command.RENAMENX, new byte[][] { oldkey, newkey });
    }
    
    public void dbSize() {
        this.sendCommand(Protocol.Command.DBSIZE);
    }
    
    public void expire(final byte[] key, final int seconds) {
        this.sendCommand(Protocol.Command.EXPIRE, new byte[][] { key, Protocol.toByteArray(seconds) });
    }
    
    public void expireAt(final byte[] key, final long unixTime) {
        this.sendCommand(Protocol.Command.EXPIREAT, new byte[][] { key, Protocol.toByteArray(unixTime) });
    }
    
    public void ttl(final byte[] key) {
        this.sendCommand(Protocol.Command.TTL, new byte[][] { key });
    }
    
    public void select(final int index) {
        this.db = index;
        this.sendCommand(Protocol.Command.SELECT, new byte[][] { Protocol.toByteArray(index) });
    }
    
    public void move(final byte[] key, final int dbIndex) {
        this.sendCommand(Protocol.Command.MOVE, new byte[][] { key, Protocol.toByteArray(dbIndex) });
    }
    
    public void flushAll() {
        this.sendCommand(Protocol.Command.FLUSHALL);
    }
    
    public void getSet(final byte[] key, final byte[] value) {
        this.sendCommand(Protocol.Command.GETSET, new byte[][] { key, value });
    }
    
    public void mget(final byte[]... keys) {
        this.sendCommand(Protocol.Command.MGET, keys);
    }
    
    public void setnx(final byte[] key, final byte[] value) {
        this.sendCommand(Protocol.Command.SETNX, new byte[][] { key, value });
    }
    
    public void setex(final byte[] key, final int seconds, final byte[] value) {
        this.sendCommand(Protocol.Command.SETEX, new byte[][] { key, Protocol.toByteArray(seconds), value });
    }
    
    public void mset(final byte[]... keysvalues) {
        this.sendCommand(Protocol.Command.MSET, keysvalues);
    }
    
    public void msetnx(final byte[]... keysvalues) {
        this.sendCommand(Protocol.Command.MSETNX, keysvalues);
    }
    
    public void decrBy(final byte[] key, final long integer) {
        this.sendCommand(Protocol.Command.DECRBY, new byte[][] { key, Protocol.toByteArray(integer) });
    }
    
    public void decr(final byte[] key) {
        this.sendCommand(Protocol.Command.DECR, new byte[][] { key });
    }
    
    public void incrBy(final byte[] key, final long integer) {
        this.sendCommand(Protocol.Command.INCRBY, new byte[][] { key, Protocol.toByteArray(integer) });
    }
    
    public void incrByFloat(final byte[] key, final double value) {
        this.sendCommand(Protocol.Command.INCRBYFLOAT, new byte[][] { key, Protocol.toByteArray(value) });
    }
    
    public void incr(final byte[] key) {
        this.sendCommand(Protocol.Command.INCR, new byte[][] { key });
    }
    
    public void append(final byte[] key, final byte[] value) {
        this.sendCommand(Protocol.Command.APPEND, new byte[][] { key, value });
    }
    
    public void substr(final byte[] key, final int start, final int end) {
        this.sendCommand(Protocol.Command.SUBSTR, new byte[][] { key, Protocol.toByteArray(start), Protocol.toByteArray(end) });
    }
    
    public void hset(final byte[] key, final byte[] field, final byte[] value) {
        this.sendCommand(Protocol.Command.HSET, new byte[][] { key, field, value });
    }
    
    public void hget(final byte[] key, final byte[] field) {
        this.sendCommand(Protocol.Command.HGET, new byte[][] { key, field });
    }
    
    public void hsetnx(final byte[] key, final byte[] field, final byte[] value) {
        this.sendCommand(Protocol.Command.HSETNX, new byte[][] { key, field, value });
    }
    
    public void hmset(final byte[] key, final Map<byte[], byte[]> hash) {
        final List<byte[]> params = new ArrayList<byte[]>();
        params.add(key);
        for (final Map.Entry<byte[], byte[]> entry : hash.entrySet()) {
            params.add(entry.getKey());
            params.add(entry.getValue());
        }
        this.sendCommand(Protocol.Command.HMSET, (byte[][])params.toArray(new byte[params.size()][]));
    }
    
    public void hmget(final byte[] key, final byte[]... fields) {
        final byte[][] params = new byte[fields.length + 1][];
        params[0] = key;
        System.arraycopy(fields, 0, params, 1, fields.length);
        this.sendCommand(Protocol.Command.HMGET, params);
    }
    
    public void hincrBy(final byte[] key, final byte[] field, final long value) {
        this.sendCommand(Protocol.Command.HINCRBY, new byte[][] { key, field, Protocol.toByteArray(value) });
    }
    
    public void hexists(final byte[] key, final byte[] field) {
        this.sendCommand(Protocol.Command.HEXISTS, new byte[][] { key, field });
    }
    
    public void hdel(final byte[] key, final byte[]... fields) {
        this.sendCommand(Protocol.Command.HDEL, this.joinParameters(key, fields));
    }
    
    public void hlen(final byte[] key) {
        this.sendCommand(Protocol.Command.HLEN, new byte[][] { key });
    }
    
    public void hkeys(final byte[] key) {
        this.sendCommand(Protocol.Command.HKEYS, new byte[][] { key });
    }
    
    public void hvals(final byte[] key) {
        this.sendCommand(Protocol.Command.HVALS, new byte[][] { key });
    }
    
    public void hgetAll(final byte[] key) {
        this.sendCommand(Protocol.Command.HGETALL, new byte[][] { key });
    }
    
    public void rpush(final byte[] key, final byte[]... strings) {
        this.sendCommand(Protocol.Command.RPUSH, this.joinParameters(key, strings));
    }
    
    public void lpush(final byte[] key, final byte[]... strings) {
        this.sendCommand(Protocol.Command.LPUSH, this.joinParameters(key, strings));
    }
    
    public void llen(final byte[] key) {
        this.sendCommand(Protocol.Command.LLEN, new byte[][] { key });
    }
    
    public void lrange(final byte[] key, final long start, final long end) {
        this.sendCommand(Protocol.Command.LRANGE, new byte[][] { key, Protocol.toByteArray(start), Protocol.toByteArray(end) });
    }
    
    public void ltrim(final byte[] key, final long start, final long end) {
        this.sendCommand(Protocol.Command.LTRIM, new byte[][] { key, Protocol.toByteArray(start), Protocol.toByteArray(end) });
    }
    
    public void lindex(final byte[] key, final long index) {
        this.sendCommand(Protocol.Command.LINDEX, new byte[][] { key, Protocol.toByteArray(index) });
    }
    
    public void lset(final byte[] key, final long index, final byte[] value) {
        this.sendCommand(Protocol.Command.LSET, new byte[][] { key, Protocol.toByteArray(index), value });
    }
    
    public void lrem(final byte[] key, final long count, final byte[] value) {
        this.sendCommand(Protocol.Command.LREM, new byte[][] { key, Protocol.toByteArray(count), value });
    }
    
    public void lpop(final byte[] key) {
        this.sendCommand(Protocol.Command.LPOP, new byte[][] { key });
    }
    
    public void rpop(final byte[] key) {
        this.sendCommand(Protocol.Command.RPOP, new byte[][] { key });
    }
    
    public void rpoplpush(final byte[] srckey, final byte[] dstkey) {
        this.sendCommand(Protocol.Command.RPOPLPUSH, new byte[][] { srckey, dstkey });
    }
    
    public void sadd(final byte[] key, final byte[]... members) {
        this.sendCommand(Protocol.Command.SADD, this.joinParameters(key, members));
    }
    
    public void smembers(final byte[] key) {
        this.sendCommand(Protocol.Command.SMEMBERS, new byte[][] { key });
    }
    
    public void srem(final byte[] key, final byte[]... members) {
        this.sendCommand(Protocol.Command.SREM, this.joinParameters(key, members));
    }
    
    public void spop(final byte[] key) {
        this.sendCommand(Protocol.Command.SPOP, new byte[][] { key });
    }
    
    public void smove(final byte[] srckey, final byte[] dstkey, final byte[] member) {
        this.sendCommand(Protocol.Command.SMOVE, new byte[][] { srckey, dstkey, member });
    }
    
    public void scard(final byte[] key) {
        this.sendCommand(Protocol.Command.SCARD, new byte[][] { key });
    }
    
    public void sismember(final byte[] key, final byte[] member) {
        this.sendCommand(Protocol.Command.SISMEMBER, new byte[][] { key, member });
    }
    
    public void sinter(final byte[]... keys) {
        this.sendCommand(Protocol.Command.SINTER, keys);
    }
    
    public void sinterstore(final byte[] dstkey, final byte[]... keys) {
        final byte[][] params = new byte[keys.length + 1][];
        params[0] = dstkey;
        System.arraycopy(keys, 0, params, 1, keys.length);
        this.sendCommand(Protocol.Command.SINTERSTORE, params);
    }
    
    public void sunion(final byte[]... keys) {
        this.sendCommand(Protocol.Command.SUNION, keys);
    }
    
    public void sunionstore(final byte[] dstkey, final byte[]... keys) {
        final byte[][] params = new byte[keys.length + 1][];
        params[0] = dstkey;
        System.arraycopy(keys, 0, params, 1, keys.length);
        this.sendCommand(Protocol.Command.SUNIONSTORE, params);
    }
    
    public void sdiff(final byte[]... keys) {
        this.sendCommand(Protocol.Command.SDIFF, keys);
    }
    
    public void sdiffstore(final byte[] dstkey, final byte[]... keys) {
        final byte[][] params = new byte[keys.length + 1][];
        params[0] = dstkey;
        System.arraycopy(keys, 0, params, 1, keys.length);
        this.sendCommand(Protocol.Command.SDIFFSTORE, params);
    }
    
    public void srandmember(final byte[] key) {
        this.sendCommand(Protocol.Command.SRANDMEMBER, new byte[][] { key });
    }
    
    public void zadd(final byte[] key, final double score, final byte[] member) {
        this.sendCommand(Protocol.Command.ZADD, new byte[][] { key, Protocol.toByteArray(score), member });
    }
    
    public void zaddBinary(final byte[] key, final Map<byte[], Double> scoreMembers) {
        final ArrayList<byte[]> args = new ArrayList<byte[]>(scoreMembers.size() * 2 + 1);
        args.add(key);
        for (final Map.Entry<byte[], Double> entry : scoreMembers.entrySet()) {
            args.add(Protocol.toByteArray(entry.getValue()));
            args.add(entry.getKey());
        }
        final byte[][] argsArray = new byte[args.size()][];
        args.toArray(argsArray);
        this.sendCommand(Protocol.Command.ZADD, argsArray);
    }
    
    public void zrange(final byte[] key, final long start, final long end) {
        this.sendCommand(Protocol.Command.ZRANGE, new byte[][] { key, Protocol.toByteArray(start), Protocol.toByteArray(end) });
    }
    
    public void zrem(final byte[] key, final byte[]... members) {
        this.sendCommand(Protocol.Command.ZREM, this.joinParameters(key, members));
    }
    
    public void zincrby(final byte[] key, final double score, final byte[] member) {
        this.sendCommand(Protocol.Command.ZINCRBY, new byte[][] { key, Protocol.toByteArray(score), member });
    }
    
    public void zrank(final byte[] key, final byte[] member) {
        this.sendCommand(Protocol.Command.ZRANK, new byte[][] { key, member });
    }
    
    public void zrevrank(final byte[] key, final byte[] member) {
        this.sendCommand(Protocol.Command.ZREVRANK, new byte[][] { key, member });
    }
    
    public void zrevrange(final byte[] key, final long start, final long end) {
        this.sendCommand(Protocol.Command.ZREVRANGE, new byte[][] { key, Protocol.toByteArray(start), Protocol.toByteArray(end) });
    }
    
    public void zrangeWithScores(final byte[] key, final long start, final long end) {
        this.sendCommand(Protocol.Command.ZRANGE, new byte[][] { key, Protocol.toByteArray(start), Protocol.toByteArray(end), Protocol.Keyword.WITHSCORES.raw });
    }
    
    public void zrevrangeWithScores(final byte[] key, final long start, final long end) {
        this.sendCommand(Protocol.Command.ZREVRANGE, new byte[][] { key, Protocol.toByteArray(start), Protocol.toByteArray(end), Protocol.Keyword.WITHSCORES.raw });
    }
    
    public void zcard(final byte[] key) {
        this.sendCommand(Protocol.Command.ZCARD, new byte[][] { key });
    }
    
    public void zscore(final byte[] key, final byte[] member) {
        this.sendCommand(Protocol.Command.ZSCORE, new byte[][] { key, member });
    }
    
    public void multi() {
        this.sendCommand(Protocol.Command.MULTI);
        this.isInMulti = true;
    }
    
    public void discard() {
        this.sendCommand(Protocol.Command.DISCARD);
        this.isInMulti = false;
        this.isInWatch = false;
    }
    
    public void exec() {
        this.sendCommand(Protocol.Command.EXEC);
        this.isInMulti = false;
        this.isInWatch = false;
    }
    
    public void watch(final byte[]... keys) {
        this.sendCommand(Protocol.Command.WATCH, keys);
        this.isInWatch = true;
    }
    
    public void unwatch() {
        this.sendCommand(Protocol.Command.UNWATCH);
        this.isInWatch = false;
    }
    
    public void sort(final byte[] key) {
        this.sendCommand(Protocol.Command.SORT, new byte[][] { key });
    }
    
    public void sort(final byte[] key, final SortingParams sortingParameters) {
        final List<byte[]> args = new ArrayList<byte[]>();
        args.add(key);
        args.addAll(sortingParameters.getParams());
        this.sendCommand(Protocol.Command.SORT, (byte[][])args.toArray(new byte[args.size()][]));
    }
    
    public void blpop(final byte[][] args) {
        this.sendCommand(Protocol.Command.BLPOP, args);
    }
    
    public void blpop(final int timeout, final byte[]... keys) {
        final List<byte[]> args = new ArrayList<byte[]>();
        for (final byte[] arg : keys) {
            args.add(arg);
        }
        args.add(Protocol.toByteArray(timeout));
        this.blpop(args.toArray(new byte[args.size()][]));
    }
    
    public void sort(final byte[] key, final SortingParams sortingParameters, final byte[] dstkey) {
        final List<byte[]> args = new ArrayList<byte[]>();
        args.add(key);
        args.addAll(sortingParameters.getParams());
        args.add(Protocol.Keyword.STORE.raw);
        args.add(dstkey);
        this.sendCommand(Protocol.Command.SORT, (byte[][])args.toArray(new byte[args.size()][]));
    }
    
    public void sort(final byte[] key, final byte[] dstkey) {
        this.sendCommand(Protocol.Command.SORT, new byte[][] { key, Protocol.Keyword.STORE.raw, dstkey });
    }
    
    public void brpop(final byte[][] args) {
        this.sendCommand(Protocol.Command.BRPOP, args);
    }
    
    public void brpop(final int timeout, final byte[]... keys) {
        final List<byte[]> args = new ArrayList<byte[]>();
        for (final byte[] arg : keys) {
            args.add(arg);
        }
        args.add(Protocol.toByteArray(timeout));
        this.brpop(args.toArray(new byte[args.size()][]));
    }
    
    public void auth(final String password) {
        this.setPassword(password);
        this.sendCommand(Protocol.Command.AUTH, password);
    }
    
    public void subscribe(final byte[]... channels) {
        this.sendCommand(Protocol.Command.SUBSCRIBE, channels);
    }
    
    public void publish(final byte[] channel, final byte[] message) {
        this.sendCommand(Protocol.Command.PUBLISH, new byte[][] { channel, message });
    }
    
    public void unsubscribe() {
        this.sendCommand(Protocol.Command.UNSUBSCRIBE);
    }
    
    public void unsubscribe(final byte[]... channels) {
        this.sendCommand(Protocol.Command.UNSUBSCRIBE, channels);
    }
    
    public void psubscribe(final byte[]... patterns) {
        this.sendCommand(Protocol.Command.PSUBSCRIBE, patterns);
    }
    
    public void punsubscribe() {
        this.sendCommand(Protocol.Command.PUNSUBSCRIBE);
    }
    
    public void punsubscribe(final byte[]... patterns) {
        this.sendCommand(Protocol.Command.PUNSUBSCRIBE, patterns);
    }
    
    public void pubsub(final byte[]... args) {
        this.sendCommand(Protocol.Command.PUBSUB, args);
    }
    
    public void zcount(final byte[] key, final double min, final double max) {
        final byte[] byteArrayMin = (min == Double.NEGATIVE_INFINITY) ? "-inf".getBytes() : Protocol.toByteArray(min);
        final byte[] byteArrayMax = (max == Double.POSITIVE_INFINITY) ? "+inf".getBytes() : Protocol.toByteArray(max);
        this.sendCommand(Protocol.Command.ZCOUNT, new byte[][] { key, byteArrayMin, byteArrayMax });
    }
    
    public void zcount(final byte[] key, final byte[] min, final byte[] max) {
        this.sendCommand(Protocol.Command.ZCOUNT, new byte[][] { key, min, max });
    }
    
    public void zcount(final byte[] key, final String min, final String max) {
        this.sendCommand(Protocol.Command.ZCOUNT, new byte[][] { key, min.getBytes(), max.getBytes() });
    }
    
    public void zrangeByScore(final byte[] key, final double min, final double max) {
        final byte[] byteArrayMin = (min == Double.NEGATIVE_INFINITY) ? "-inf".getBytes() : Protocol.toByteArray(min);
        final byte[] byteArrayMax = (max == Double.POSITIVE_INFINITY) ? "+inf".getBytes() : Protocol.toByteArray(max);
        this.sendCommand(Protocol.Command.ZRANGEBYSCORE, new byte[][] { key, byteArrayMin, byteArrayMax });
    }
    
    public void zrangeByScore(final byte[] key, final byte[] min, final byte[] max) {
        this.sendCommand(Protocol.Command.ZRANGEBYSCORE, new byte[][] { key, min, max });
    }
    
    public void zrangeByScore(final byte[] key, final String min, final String max) {
        this.sendCommand(Protocol.Command.ZRANGEBYSCORE, new byte[][] { key, min.getBytes(), max.getBytes() });
    }
    
    public void zrevrangeByScore(final byte[] key, final double max, final double min) {
        final byte[] byteArrayMin = (min == Double.NEGATIVE_INFINITY) ? "-inf".getBytes() : Protocol.toByteArray(min);
        final byte[] byteArrayMax = (max == Double.POSITIVE_INFINITY) ? "+inf".getBytes() : Protocol.toByteArray(max);
        this.sendCommand(Protocol.Command.ZREVRANGEBYSCORE, new byte[][] { key, byteArrayMax, byteArrayMin });
    }
    
    public void zrevrangeByScore(final byte[] key, final byte[] max, final byte[] min) {
        this.sendCommand(Protocol.Command.ZREVRANGEBYSCORE, new byte[][] { key, max, min });
    }
    
    public void zrevrangeByScore(final byte[] key, final String max, final String min) {
        this.sendCommand(Protocol.Command.ZREVRANGEBYSCORE, new byte[][] { key, max.getBytes(), min.getBytes() });
    }
    
    public void zrangeByScore(final byte[] key, final double min, final double max, final int offset, final int count) {
        final byte[] byteArrayMin = (min == Double.NEGATIVE_INFINITY) ? "-inf".getBytes() : Protocol.toByteArray(min);
        final byte[] byteArrayMax = (max == Double.POSITIVE_INFINITY) ? "+inf".getBytes() : Protocol.toByteArray(max);
        this.sendCommand(Protocol.Command.ZRANGEBYSCORE, new byte[][] { key, byteArrayMin, byteArrayMax, Protocol.Keyword.LIMIT.raw, Protocol.toByteArray(offset), Protocol.toByteArray(count) });
    }
    
    public void zrangeByScore(final byte[] key, final String min, final String max, final int offset, final int count) {
        this.sendCommand(Protocol.Command.ZRANGEBYSCORE, new byte[][] { key, min.getBytes(), max.getBytes(), Protocol.Keyword.LIMIT.raw, Protocol.toByteArray(offset), Protocol.toByteArray(count) });
    }
    
    public void zrevrangeByScore(final byte[] key, final double max, final double min, final int offset, final int count) {
        final byte[] byteArrayMin = (min == Double.NEGATIVE_INFINITY) ? "-inf".getBytes() : Protocol.toByteArray(min);
        final byte[] byteArrayMax = (max == Double.POSITIVE_INFINITY) ? "+inf".getBytes() : Protocol.toByteArray(max);
        this.sendCommand(Protocol.Command.ZREVRANGEBYSCORE, new byte[][] { key, byteArrayMax, byteArrayMin, Protocol.Keyword.LIMIT.raw, Protocol.toByteArray(offset), Protocol.toByteArray(count) });
    }
    
    public void zrevrangeByScore(final byte[] key, final String max, final String min, final int offset, final int count) {
        this.sendCommand(Protocol.Command.ZREVRANGEBYSCORE, new byte[][] { key, max.getBytes(), min.getBytes(), Protocol.Keyword.LIMIT.raw, Protocol.toByteArray(offset), Protocol.toByteArray(count) });
    }
    
    public void zrangeByScoreWithScores(final byte[] key, final double min, final double max) {
        final byte[] byteArrayMin = (min == Double.NEGATIVE_INFINITY) ? "-inf".getBytes() : Protocol.toByteArray(min);
        final byte[] byteArrayMax = (max == Double.POSITIVE_INFINITY) ? "+inf".getBytes() : Protocol.toByteArray(max);
        this.sendCommand(Protocol.Command.ZRANGEBYSCORE, new byte[][] { key, byteArrayMin, byteArrayMax, Protocol.Keyword.WITHSCORES.raw });
    }
    
    public void zrangeByScoreWithScores(final byte[] key, final String min, final String max) {
        this.sendCommand(Protocol.Command.ZRANGEBYSCORE, new byte[][] { key, min.getBytes(), max.getBytes(), Protocol.Keyword.WITHSCORES.raw });
    }
    
    public void zrevrangeByScoreWithScores(final byte[] key, final double max, final double min) {
        final byte[] byteArrayMin = (min == Double.NEGATIVE_INFINITY) ? "-inf".getBytes() : Protocol.toByteArray(min);
        final byte[] byteArrayMax = (max == Double.POSITIVE_INFINITY) ? "+inf".getBytes() : Protocol.toByteArray(max);
        this.sendCommand(Protocol.Command.ZREVRANGEBYSCORE, new byte[][] { key, byteArrayMax, byteArrayMin, Protocol.Keyword.WITHSCORES.raw });
    }
    
    public void zrevrangeByScoreWithScores(final byte[] key, final String max, final String min) {
        this.sendCommand(Protocol.Command.ZREVRANGEBYSCORE, new byte[][] { key, max.getBytes(), min.getBytes(), Protocol.Keyword.WITHSCORES.raw });
    }
    
    public void zrangeByScoreWithScores(final byte[] key, final double min, final double max, final int offset, final int count) {
        final byte[] byteArrayMin = (min == Double.NEGATIVE_INFINITY) ? "-inf".getBytes() : Protocol.toByteArray(min);
        final byte[] byteArrayMax = (max == Double.POSITIVE_INFINITY) ? "+inf".getBytes() : Protocol.toByteArray(max);
        this.sendCommand(Protocol.Command.ZRANGEBYSCORE, new byte[][] { key, byteArrayMin, byteArrayMax, Protocol.Keyword.LIMIT.raw, Protocol.toByteArray(offset), Protocol.toByteArray(count), Protocol.Keyword.WITHSCORES.raw });
    }
    
    public void zrangeByScoreWithScores(final byte[] key, final String min, final String max, final int offset, final int count) {
        this.sendCommand(Protocol.Command.ZRANGEBYSCORE, new byte[][] { key, min.getBytes(), max.getBytes(), Protocol.Keyword.LIMIT.raw, Protocol.toByteArray(offset), Protocol.toByteArray(count), Protocol.Keyword.WITHSCORES.raw });
    }
    
    public void zrevrangeByScoreWithScores(final byte[] key, final double max, final double min, final int offset, final int count) {
        final byte[] byteArrayMin = (min == Double.NEGATIVE_INFINITY) ? "-inf".getBytes() : Protocol.toByteArray(min);
        final byte[] byteArrayMax = (max == Double.POSITIVE_INFINITY) ? "+inf".getBytes() : Protocol.toByteArray(max);
        this.sendCommand(Protocol.Command.ZREVRANGEBYSCORE, new byte[][] { key, byteArrayMax, byteArrayMin, Protocol.Keyword.LIMIT.raw, Protocol.toByteArray(offset), Protocol.toByteArray(count), Protocol.Keyword.WITHSCORES.raw });
    }
    
    public void zrevrangeByScoreWithScores(final byte[] key, final String max, final String min, final int offset, final int count) {
        this.sendCommand(Protocol.Command.ZREVRANGEBYSCORE, new byte[][] { key, max.getBytes(), min.getBytes(), Protocol.Keyword.LIMIT.raw, Protocol.toByteArray(offset), Protocol.toByteArray(count), Protocol.Keyword.WITHSCORES.raw });
    }
    
    public void zrangeByScore(final byte[] key, final byte[] min, final byte[] max, final int offset, final int count) {
        this.sendCommand(Protocol.Command.ZRANGEBYSCORE, new byte[][] { key, min, max, Protocol.Keyword.LIMIT.raw, Protocol.toByteArray(offset), Protocol.toByteArray(count) });
    }
    
    public void zrevrangeByScore(final byte[] key, final byte[] max, final byte[] min, final int offset, final int count) {
        this.sendCommand(Protocol.Command.ZREVRANGEBYSCORE, new byte[][] { key, max, min, Protocol.Keyword.LIMIT.raw, Protocol.toByteArray(offset), Protocol.toByteArray(count) });
    }
    
    public void zrangeByScoreWithScores(final byte[] key, final byte[] min, final byte[] max) {
        this.sendCommand(Protocol.Command.ZRANGEBYSCORE, new byte[][] { key, min, max, Protocol.Keyword.WITHSCORES.raw });
    }
    
    public void zrevrangeByScoreWithScores(final byte[] key, final byte[] max, final byte[] min) {
        this.sendCommand(Protocol.Command.ZREVRANGEBYSCORE, new byte[][] { key, max, min, Protocol.Keyword.WITHSCORES.raw });
    }
    
    public void zrangeByScoreWithScores(final byte[] key, final byte[] min, final byte[] max, final int offset, final int count) {
        this.sendCommand(Protocol.Command.ZRANGEBYSCORE, new byte[][] { key, min, max, Protocol.Keyword.LIMIT.raw, Protocol.toByteArray(offset), Protocol.toByteArray(count), Protocol.Keyword.WITHSCORES.raw });
    }
    
    public void zrevrangeByScoreWithScores(final byte[] key, final byte[] max, final byte[] min, final int offset, final int count) {
        this.sendCommand(Protocol.Command.ZREVRANGEBYSCORE, new byte[][] { key, max, min, Protocol.Keyword.LIMIT.raw, Protocol.toByteArray(offset), Protocol.toByteArray(count), Protocol.Keyword.WITHSCORES.raw });
    }
    
    public void zremrangeByRank(final byte[] key, final long start, final long end) {
        this.sendCommand(Protocol.Command.ZREMRANGEBYRANK, new byte[][] { key, Protocol.toByteArray(start), Protocol.toByteArray(end) });
    }
    
    public void zremrangeByScore(final byte[] key, final byte[] start, final byte[] end) {
        this.sendCommand(Protocol.Command.ZREMRANGEBYSCORE, new byte[][] { key, start, end });
    }
    
    public void zremrangeByScore(final byte[] key, final String start, final String end) {
        this.sendCommand(Protocol.Command.ZREMRANGEBYSCORE, new byte[][] { key, start.getBytes(), end.getBytes() });
    }
    
    public void zunionstore(final byte[] dstkey, final byte[]... sets) {
        final byte[][] params = new byte[sets.length + 2][];
        params[0] = dstkey;
        params[1] = Protocol.toByteArray(sets.length);
        System.arraycopy(sets, 0, params, 2, sets.length);
        this.sendCommand(Protocol.Command.ZUNIONSTORE, params);
    }
    
    public void zunionstore(final byte[] dstkey, final ZParams params, final byte[]... sets) {
        final List<byte[]> args = new ArrayList<byte[]>();
        args.add(dstkey);
        args.add(Protocol.toByteArray(sets.length));
        for (final byte[] set : sets) {
            args.add(set);
        }
        args.addAll(params.getParams());
        this.sendCommand(Protocol.Command.ZUNIONSTORE, (byte[][])args.toArray(new byte[args.size()][]));
    }
    
    public void zinterstore(final byte[] dstkey, final byte[]... sets) {
        final byte[][] params = new byte[sets.length + 2][];
        params[0] = dstkey;
        params[1] = Protocol.toByteArray(sets.length);
        System.arraycopy(sets, 0, params, 2, sets.length);
        this.sendCommand(Protocol.Command.ZINTERSTORE, params);
    }
    
    public void zinterstore(final byte[] dstkey, final ZParams params, final byte[]... sets) {
        final List<byte[]> args = new ArrayList<byte[]>();
        args.add(dstkey);
        args.add(Protocol.toByteArray(sets.length));
        for (final byte[] set : sets) {
            args.add(set);
        }
        args.addAll(params.getParams());
        this.sendCommand(Protocol.Command.ZINTERSTORE, (byte[][])args.toArray(new byte[args.size()][]));
    }
    
    public void zlexcount(final byte[] key, final byte[] min, final byte[] max) {
        this.sendCommand(Protocol.Command.ZLEXCOUNT, new byte[][] { key, min, max });
    }
    
    public void zrangeByLex(final byte[] key, final byte[] min, final byte[] max) {
        this.sendCommand(Protocol.Command.ZRANGEBYLEX, new byte[][] { key, min, max });
    }
    
    public void zrangeByLex(final byte[] key, final byte[] min, final byte[] max, final int offset, final int count) {
        this.sendCommand(Protocol.Command.ZRANGEBYLEX, new byte[][] { key, min, max, Protocol.Keyword.LIMIT.raw, Protocol.toByteArray(offset), Protocol.toByteArray(count) });
    }
    
    public void zremrangeByLex(final byte[] key, final byte[] min, final byte[] max) {
        this.sendCommand(Protocol.Command.ZREMRANGEBYLEX, new byte[][] { key, min, max });
    }
    
    public void save() {
        this.sendCommand(Protocol.Command.SAVE);
    }
    
    public void bgsave() {
        this.sendCommand(Protocol.Command.BGSAVE);
    }
    
    public void bgrewriteaof() {
        this.sendCommand(Protocol.Command.BGREWRITEAOF);
    }
    
    public void lastsave() {
        this.sendCommand(Protocol.Command.LASTSAVE);
    }
    
    public void shutdown() {
        this.sendCommand(Protocol.Command.SHUTDOWN);
    }
    
    public void info() {
        this.sendCommand(Protocol.Command.INFO);
    }
    
    public void info(final String section) {
        this.sendCommand(Protocol.Command.INFO, section);
    }
    
    public void monitor() {
        this.sendCommand(Protocol.Command.MONITOR);
    }
    
    public void slaveof(final String host, final int port) {
        this.sendCommand(Protocol.Command.SLAVEOF, host, String.valueOf(port));
    }
    
    public void slaveofNoOne() {
        this.sendCommand(Protocol.Command.SLAVEOF, new byte[][] { Protocol.Keyword.NO.raw, Protocol.Keyword.ONE.raw });
    }
    
    public void configGet(final byte[] pattern) {
        this.sendCommand(Protocol.Command.CONFIG, new byte[][] { Protocol.Keyword.GET.raw, pattern });
    }
    
    public void configSet(final byte[] parameter, final byte[] value) {
        this.sendCommand(Protocol.Command.CONFIG, new byte[][] { Protocol.Keyword.SET.raw, parameter, value });
    }
    
    public void strlen(final byte[] key) {
        this.sendCommand(Protocol.Command.STRLEN, new byte[][] { key });
    }
    
    public void sync() {
        this.sendCommand(Protocol.Command.SYNC);
    }
    
    public void lpushx(final byte[] key, final byte[]... string) {
        this.sendCommand(Protocol.Command.LPUSHX, this.joinParameters(key, string));
    }
    
    public void persist(final byte[] key) {
        this.sendCommand(Protocol.Command.PERSIST, new byte[][] { key });
    }
    
    public void rpushx(final byte[] key, final byte[]... string) {
        this.sendCommand(Protocol.Command.RPUSHX, this.joinParameters(key, string));
    }
    
    public void echo(final byte[] string) {
        this.sendCommand(Protocol.Command.ECHO, new byte[][] { string });
    }
    
    public void linsert(final byte[] key, final LIST_POSITION where, final byte[] pivot, final byte[] value) {
        this.sendCommand(Protocol.Command.LINSERT, new byte[][] { key, where.raw, pivot, value });
    }
    
    public void debug(final DebugParams params) {
        this.sendCommand(Protocol.Command.DEBUG, params.getCommand());
    }
    
    public void brpoplpush(final byte[] source, final byte[] destination, final int timeout) {
        this.sendCommand(Protocol.Command.BRPOPLPUSH, new byte[][] { source, destination, Protocol.toByteArray(timeout) });
    }
    
    public void configResetStat() {
        this.sendCommand(Protocol.Command.CONFIG, Protocol.Keyword.RESETSTAT.name());
    }
    
    public void setbit(final byte[] key, final long offset, final byte[] value) {
        this.sendCommand(Protocol.Command.SETBIT, new byte[][] { key, Protocol.toByteArray(offset), value });
    }
    
    public void setbit(final byte[] key, final long offset, final boolean value) {
        this.sendCommand(Protocol.Command.SETBIT, new byte[][] { key, Protocol.toByteArray(offset), Protocol.toByteArray(value) });
    }
    
    public void getbit(final byte[] key, final long offset) {
        this.sendCommand(Protocol.Command.GETBIT, new byte[][] { key, Protocol.toByteArray(offset) });
    }
    
    public void bitpos(final byte[] key, final boolean value, final BitPosParams params) {
        final List<byte[]> args = new ArrayList<byte[]>();
        args.add(key);
        args.add(Protocol.toByteArray(value));
        args.addAll(params.getParams());
        this.sendCommand(Protocol.Command.BITPOS, (byte[][])args.toArray(new byte[args.size()][]));
    }
    
    public void setrange(final byte[] key, final long offset, final byte[] value) {
        this.sendCommand(Protocol.Command.SETRANGE, new byte[][] { key, Protocol.toByteArray(offset), value });
    }
    
    public void getrange(final byte[] key, final long startOffset, final long endOffset) {
        this.sendCommand(Protocol.Command.GETRANGE, new byte[][] { key, Protocol.toByteArray(startOffset), Protocol.toByteArray(endOffset) });
    }
    
    public Long getDB() {
        return this.db;
    }
    
    @Override
    public void disconnect() {
        this.db = 0L;
        super.disconnect();
    }
    
    @Override
    public void close() {
        this.db = 0L;
        super.close();
    }
    
    public void resetState() {
        if (this.isInMulti()) {
            this.discard();
        }
        if (this.isInWatch()) {
            this.unwatch();
        }
    }
    
    private void sendEvalCommand(final Protocol.Command command, final byte[] script, final byte[] keyCount, final byte[][] params) {
        final byte[][] allArgs = new byte[params.length + 2][];
        allArgs[0] = script;
        allArgs[1] = keyCount;
        for (int i = 0; i < params.length; ++i) {
            allArgs[i + 2] = params[i];
        }
        this.sendCommand(command, allArgs);
    }
    
    public void eval(final byte[] script, final byte[] keyCount, final byte[][] params) {
        this.sendEvalCommand(Protocol.Command.EVAL, script, keyCount, params);
    }
    
    public void eval(final byte[] script, final int keyCount, final byte[]... params) {
        this.eval(script, Protocol.toByteArray(keyCount), params);
    }
    
    public void evalsha(final byte[] sha1, final byte[] keyCount, final byte[]... params) {
        this.sendEvalCommand(Protocol.Command.EVALSHA, sha1, keyCount, params);
    }
    
    public void evalsha(final byte[] sha1, final int keyCount, final byte[]... params) {
        this.sendEvalCommand(Protocol.Command.EVALSHA, sha1, Protocol.toByteArray(keyCount), params);
    }
    
    public void scriptFlush() {
        this.sendCommand(Protocol.Command.SCRIPT, new byte[][] { Protocol.Keyword.FLUSH.raw });
    }
    
    public void scriptExists(final byte[]... sha1) {
        final byte[][] args = new byte[sha1.length + 1][];
        args[0] = Protocol.Keyword.EXISTS.raw;
        for (int i = 0; i < sha1.length; ++i) {
            args[i + 1] = sha1[i];
        }
        this.sendCommand(Protocol.Command.SCRIPT, args);
    }
    
    public void scriptLoad(final byte[] script) {
        this.sendCommand(Protocol.Command.SCRIPT, new byte[][] { Protocol.Keyword.LOAD.raw, script });
    }
    
    public void scriptKill() {
        this.sendCommand(Protocol.Command.SCRIPT, new byte[][] { Protocol.Keyword.KILL.raw });
    }
    
    public void slowlogGet() {
        this.sendCommand(Protocol.Command.SLOWLOG, new byte[][] { Protocol.Keyword.GET.raw });
    }
    
    public void slowlogGet(final long entries) {
        this.sendCommand(Protocol.Command.SLOWLOG, new byte[][] { Protocol.Keyword.GET.raw, Protocol.toByteArray(entries) });
    }
    
    public void slowlogReset() {
        this.sendCommand(Protocol.Command.SLOWLOG, new byte[][] { Protocol.Keyword.RESET.raw });
    }
    
    public void slowlogLen() {
        this.sendCommand(Protocol.Command.SLOWLOG, new byte[][] { Protocol.Keyword.LEN.raw });
    }
    
    public void objectRefcount(final byte[] key) {
        this.sendCommand(Protocol.Command.OBJECT, new byte[][] { Protocol.Keyword.REFCOUNT.raw, key });
    }
    
    public void objectIdletime(final byte[] key) {
        this.sendCommand(Protocol.Command.OBJECT, new byte[][] { Protocol.Keyword.IDLETIME.raw, key });
    }
    
    public void objectEncoding(final byte[] key) {
        this.sendCommand(Protocol.Command.OBJECT, new byte[][] { Protocol.Keyword.ENCODING.raw, key });
    }
    
    public void bitcount(final byte[] key) {
        this.sendCommand(Protocol.Command.BITCOUNT, new byte[][] { key });
    }
    
    public void bitcount(final byte[] key, final long start, final long end) {
        this.sendCommand(Protocol.Command.BITCOUNT, new byte[][] { key, Protocol.toByteArray(start), Protocol.toByteArray(end) });
    }
    
    public void bitop(final BitOP op, final byte[] destKey, final byte[]... srcKeys) {
        Protocol.Keyword kw = Protocol.Keyword.AND;
        int len = srcKeys.length;
        switch (op) {
            case AND: {
                kw = Protocol.Keyword.AND;
                break;
            }
            case OR: {
                kw = Protocol.Keyword.OR;
                break;
            }
            case XOR: {
                kw = Protocol.Keyword.XOR;
                break;
            }
            case NOT: {
                kw = Protocol.Keyword.NOT;
                len = Math.min(1, len);
                break;
            }
        }
        final byte[][] bargs = new byte[len + 2][];
        bargs[0] = kw.raw;
        bargs[1] = destKey;
        for (int i = 0; i < len; ++i) {
            bargs[i + 2] = srcKeys[i];
        }
        this.sendCommand(Protocol.Command.BITOP, bargs);
    }
    
    public void sentinel(final byte[]... args) {
        this.sendCommand(Protocol.Command.SENTINEL, args);
    }
    
    public void dump(final byte[] key) {
        this.sendCommand(Protocol.Command.DUMP, new byte[][] { key });
    }
    
    public void restore(final byte[] key, final int ttl, final byte[] serializedValue) {
        this.sendCommand(Protocol.Command.RESTORE, new byte[][] { key, Protocol.toByteArray(ttl), serializedValue });
    }
    
    @Deprecated
    public void pexpire(final byte[] key, final int milliseconds) {
        this.pexpire(key, (long)milliseconds);
    }
    
    public void pexpire(final byte[] key, final long milliseconds) {
        this.sendCommand(Protocol.Command.PEXPIRE, new byte[][] { key, Protocol.toByteArray(milliseconds) });
    }
    
    public void pexpireAt(final byte[] key, final long millisecondsTimestamp) {
        this.sendCommand(Protocol.Command.PEXPIREAT, new byte[][] { key, Protocol.toByteArray(millisecondsTimestamp) });
    }
    
    public void pttl(final byte[] key) {
        this.sendCommand(Protocol.Command.PTTL, new byte[][] { key });
    }
    
    public void psetex(final byte[] key, final int milliseconds, final byte[] value) {
        this.sendCommand(Protocol.Command.PSETEX, new byte[][] { key, Protocol.toByteArray(milliseconds), value });
    }
    
    public void set(final byte[] key, final byte[] value, final byte[] nxxx) {
        this.sendCommand(Protocol.Command.SET, new byte[][] { key, value, nxxx });
    }
    
    public void set(final byte[] key, final byte[] value, final byte[] nxxx, final byte[] expx, final int time) {
        this.sendCommand(Protocol.Command.SET, new byte[][] { key, value, nxxx, expx, Protocol.toByteArray(time) });
    }
    
    public void srandmember(final byte[] key, final int count) {
        this.sendCommand(Protocol.Command.SRANDMEMBER, new byte[][] { key, Protocol.toByteArray(count) });
    }
    
    public void clientKill(final byte[] client) {
        this.sendCommand(Protocol.Command.CLIENT, new byte[][] { Protocol.Keyword.KILL.raw, client });
    }
    
    public void clientGetname() {
        this.sendCommand(Protocol.Command.CLIENT, new byte[][] { Protocol.Keyword.GETNAME.raw });
    }
    
    public void clientList() {
        this.sendCommand(Protocol.Command.CLIENT, new byte[][] { Protocol.Keyword.LIST.raw });
    }
    
    public void clientSetname(final byte[] name) {
        this.sendCommand(Protocol.Command.CLIENT, new byte[][] { Protocol.Keyword.SETNAME.raw, name });
    }
    
    public void time() {
        this.sendCommand(Protocol.Command.TIME);
    }
    
    public void migrate(final byte[] host, final int port, final byte[] key, final int destinationDb, final int timeout) {
        this.sendCommand(Protocol.Command.MIGRATE, new byte[][] { host, Protocol.toByteArray(port), key, Protocol.toByteArray(destinationDb), Protocol.toByteArray(timeout) });
    }
    
    public void hincrByFloat(final byte[] key, final byte[] field, final double increment) {
        this.sendCommand(Protocol.Command.HINCRBYFLOAT, new byte[][] { key, field, Protocol.toByteArray(increment) });
    }
    
    @Deprecated
    public void scan(final int cursor, final ScanParams params) {
        final List<byte[]> args = new ArrayList<byte[]>();
        args.add(Protocol.toByteArray(cursor));
        args.addAll(params.getParams());
        this.sendCommand(Protocol.Command.SCAN, (byte[][])args.toArray(new byte[args.size()][]));
    }
    
    @Deprecated
    public void hscan(final byte[] key, final int cursor, final ScanParams params) {
        final List<byte[]> args = new ArrayList<byte[]>();
        args.add(key);
        args.add(Protocol.toByteArray(cursor));
        args.addAll(params.getParams());
        this.sendCommand(Protocol.Command.HSCAN, (byte[][])args.toArray(new byte[args.size()][]));
    }
    
    @Deprecated
    public void sscan(final byte[] key, final int cursor, final ScanParams params) {
        final List<byte[]> args = new ArrayList<byte[]>();
        args.add(key);
        args.add(Protocol.toByteArray(cursor));
        args.addAll(params.getParams());
        this.sendCommand(Protocol.Command.SSCAN, (byte[][])args.toArray(new byte[args.size()][]));
    }
    
    @Deprecated
    public void zscan(final byte[] key, final int cursor, final ScanParams params) {
        final List<byte[]> args = new ArrayList<byte[]>();
        args.add(key);
        args.add(Protocol.toByteArray(cursor));
        args.addAll(params.getParams());
        this.sendCommand(Protocol.Command.ZSCAN, (byte[][])args.toArray(new byte[args.size()][]));
    }
    
    public void scan(final byte[] cursor, final ScanParams params) {
        final List<byte[]> args = new ArrayList<byte[]>();
        args.add(cursor);
        args.addAll(params.getParams());
        this.sendCommand(Protocol.Command.SCAN, (byte[][])args.toArray(new byte[args.size()][]));
    }
    
    public void hscan(final byte[] key, final byte[] cursor, final ScanParams params) {
        final List<byte[]> args = new ArrayList<byte[]>();
        args.add(key);
        args.add(cursor);
        args.addAll(params.getParams());
        this.sendCommand(Protocol.Command.HSCAN, (byte[][])args.toArray(new byte[args.size()][]));
    }
    
    public void sscan(final byte[] key, final byte[] cursor, final ScanParams params) {
        final List<byte[]> args = new ArrayList<byte[]>();
        args.add(key);
        args.add(cursor);
        args.addAll(params.getParams());
        this.sendCommand(Protocol.Command.SSCAN, (byte[][])args.toArray(new byte[args.size()][]));
    }
    
    public void zscan(final byte[] key, final byte[] cursor, final ScanParams params) {
        final List<byte[]> args = new ArrayList<byte[]>();
        args.add(key);
        args.add(cursor);
        args.addAll(params.getParams());
        this.sendCommand(Protocol.Command.ZSCAN, (byte[][])args.toArray(new byte[args.size()][]));
    }
    
    public void waitReplicas(final int replicas, final long timeout) {
        this.sendCommand(Protocol.Command.WAIT, new byte[][] { Protocol.toByteArray(replicas), Protocol.toByteArray(timeout) });
    }
    
    public void cluster(final byte[]... args) {
        this.sendCommand(Protocol.Command.CLUSTER, args);
    }
    
    public void asking() {
        this.sendCommand(Protocol.Command.ASKING);
    }
    
    public void pfadd(final byte[] key, final byte[]... elements) {
        this.sendCommand(Protocol.Command.PFADD, this.joinParameters(key, elements));
    }
    
    public void pfcount(final byte[] key) {
        this.sendCommand(Protocol.Command.PFCOUNT, new byte[][] { key });
    }
    
    public void pfcount(final byte[]... keys) {
        this.sendCommand(Protocol.Command.PFCOUNT, keys);
    }
    
    public void pfmerge(final byte[] destkey, final byte[]... sourcekeys) {
        this.sendCommand(Protocol.Command.PFMERGE, this.joinParameters(destkey, sourcekeys));
    }
    
    public enum LIST_POSITION
    {
        BEFORE, 
        AFTER;
        
        public final byte[] raw;
        
        private LIST_POSITION(final int n) {
            this.raw = SafeEncoder.encode(this.name());
        }
    }
}
