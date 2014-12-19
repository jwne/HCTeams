package redis.clients.jedis;

import redis.clients.util.*;
import java.util.*;

public class Client extends BinaryClient implements Commands
{
    public Client(final String host) {
        super(host);
    }
    
    public Client(final String host, final int port) {
        super(host, port);
    }
    
    @Override
    public void set(final String key, final String value) {
        this.set(SafeEncoder.encode(key), SafeEncoder.encode(value));
    }
    
    @Override
    public void set(final String key, final String value, final String nxxx, final String expx, final long time) {
        this.set(SafeEncoder.encode(key), SafeEncoder.encode(value), SafeEncoder.encode(nxxx), SafeEncoder.encode(expx), time);
    }
    
    @Override
    public void get(final String key) {
        this.get(SafeEncoder.encode(key));
    }
    
    @Override
    public void exists(final String key) {
        this.exists(SafeEncoder.encode(key));
    }
    
    @Override
    public void del(final String... keys) {
        final byte[][] bkeys = new byte[keys.length][];
        for (int i = 0; i < keys.length; ++i) {
            bkeys[i] = SafeEncoder.encode(keys[i]);
        }
        this.del(bkeys);
    }
    
    @Override
    public void type(final String key) {
        this.type(SafeEncoder.encode(key));
    }
    
    @Override
    public void keys(final String pattern) {
        this.keys(SafeEncoder.encode(pattern));
    }
    
    @Override
    public void rename(final String oldkey, final String newkey) {
        this.rename(SafeEncoder.encode(oldkey), SafeEncoder.encode(newkey));
    }
    
    @Override
    public void renamenx(final String oldkey, final String newkey) {
        this.renamenx(SafeEncoder.encode(oldkey), SafeEncoder.encode(newkey));
    }
    
    @Override
    public void expire(final String key, final int seconds) {
        this.expire(SafeEncoder.encode(key), seconds);
    }
    
    @Override
    public void expireAt(final String key, final long unixTime) {
        this.expireAt(SafeEncoder.encode(key), unixTime);
    }
    
    @Override
    public void ttl(final String key) {
        this.ttl(SafeEncoder.encode(key));
    }
    
    @Override
    public void move(final String key, final int dbIndex) {
        this.move(SafeEncoder.encode(key), dbIndex);
    }
    
    @Override
    public void getSet(final String key, final String value) {
        this.getSet(SafeEncoder.encode(key), SafeEncoder.encode(value));
    }
    
    @Override
    public void mget(final String... keys) {
        final byte[][] bkeys = new byte[keys.length][];
        for (int i = 0; i < bkeys.length; ++i) {
            bkeys[i] = SafeEncoder.encode(keys[i]);
        }
        this.mget(bkeys);
    }
    
    @Override
    public void setnx(final String key, final String value) {
        this.setnx(SafeEncoder.encode(key), SafeEncoder.encode(value));
    }
    
    @Override
    public void setex(final String key, final int seconds, final String value) {
        this.setex(SafeEncoder.encode(key), seconds, SafeEncoder.encode(value));
    }
    
    @Override
    public void mset(final String... keysvalues) {
        final byte[][] bkeysvalues = new byte[keysvalues.length][];
        for (int i = 0; i < keysvalues.length; ++i) {
            bkeysvalues[i] = SafeEncoder.encode(keysvalues[i]);
        }
        this.mset(bkeysvalues);
    }
    
    @Override
    public void msetnx(final String... keysvalues) {
        final byte[][] bkeysvalues = new byte[keysvalues.length][];
        for (int i = 0; i < keysvalues.length; ++i) {
            bkeysvalues[i] = SafeEncoder.encode(keysvalues[i]);
        }
        this.msetnx(bkeysvalues);
    }
    
    @Override
    public void decrBy(final String key, final long integer) {
        this.decrBy(SafeEncoder.encode(key), integer);
    }
    
    @Override
    public void decr(final String key) {
        this.decr(SafeEncoder.encode(key));
    }
    
    @Override
    public void incrBy(final String key, final long integer) {
        this.incrBy(SafeEncoder.encode(key), integer);
    }
    
    @Override
    public void incr(final String key) {
        this.incr(SafeEncoder.encode(key));
    }
    
    @Override
    public void append(final String key, final String value) {
        this.append(SafeEncoder.encode(key), SafeEncoder.encode(value));
    }
    
    @Override
    public void substr(final String key, final int start, final int end) {
        this.substr(SafeEncoder.encode(key), start, end);
    }
    
    @Override
    public void hset(final String key, final String field, final String value) {
        this.hset(SafeEncoder.encode(key), SafeEncoder.encode(field), SafeEncoder.encode(value));
    }
    
    @Override
    public void hget(final String key, final String field) {
        this.hget(SafeEncoder.encode(key), SafeEncoder.encode(field));
    }
    
    @Override
    public void hsetnx(final String key, final String field, final String value) {
        this.hsetnx(SafeEncoder.encode(key), SafeEncoder.encode(field), SafeEncoder.encode(value));
    }
    
    @Override
    public void hmset(final String key, final Map<String, String> hash) {
        final Map<byte[], byte[]> bhash = new HashMap<byte[], byte[]>(hash.size());
        for (final Map.Entry<String, String> entry : hash.entrySet()) {
            bhash.put(SafeEncoder.encode(entry.getKey()), SafeEncoder.encode(entry.getValue()));
        }
        this.hmset(SafeEncoder.encode(key), bhash);
    }
    
    @Override
    public void hmget(final String key, final String... fields) {
        final byte[][] bfields = new byte[fields.length][];
        for (int i = 0; i < bfields.length; ++i) {
            bfields[i] = SafeEncoder.encode(fields[i]);
        }
        this.hmget(SafeEncoder.encode(key), bfields);
    }
    
    @Override
    public void hincrBy(final String key, final String field, final long value) {
        this.hincrBy(SafeEncoder.encode(key), SafeEncoder.encode(field), value);
    }
    
    @Override
    public void hexists(final String key, final String field) {
        this.hexists(SafeEncoder.encode(key), SafeEncoder.encode(field));
    }
    
    @Override
    public void hdel(final String key, final String... fields) {
        this.hdel(SafeEncoder.encode(key), SafeEncoder.encodeMany(fields));
    }
    
    @Override
    public void hlen(final String key) {
        this.hlen(SafeEncoder.encode(key));
    }
    
    @Override
    public void hkeys(final String key) {
        this.hkeys(SafeEncoder.encode(key));
    }
    
    @Override
    public void hvals(final String key) {
        this.hvals(SafeEncoder.encode(key));
    }
    
    @Override
    public void hgetAll(final String key) {
        this.hgetAll(SafeEncoder.encode(key));
    }
    
    @Override
    public void rpush(final String key, final String... string) {
        this.rpush(SafeEncoder.encode(key), SafeEncoder.encodeMany(string));
    }
    
    @Override
    public void lpush(final String key, final String... string) {
        this.lpush(SafeEncoder.encode(key), SafeEncoder.encodeMany(string));
    }
    
    @Override
    public void llen(final String key) {
        this.llen(SafeEncoder.encode(key));
    }
    
    @Override
    public void lrange(final String key, final long start, final long end) {
        this.lrange(SafeEncoder.encode(key), start, end);
    }
    
    @Override
    public void ltrim(final String key, final long start, final long end) {
        this.ltrim(SafeEncoder.encode(key), start, end);
    }
    
    @Override
    public void lindex(final String key, final long index) {
        this.lindex(SafeEncoder.encode(key), index);
    }
    
    @Override
    public void lset(final String key, final long index, final String value) {
        this.lset(SafeEncoder.encode(key), index, SafeEncoder.encode(value));
    }
    
    @Override
    public void lrem(final String key, final long count, final String value) {
        this.lrem(SafeEncoder.encode(key), count, SafeEncoder.encode(value));
    }
    
    @Override
    public void lpop(final String key) {
        this.lpop(SafeEncoder.encode(key));
    }
    
    @Override
    public void rpop(final String key) {
        this.rpop(SafeEncoder.encode(key));
    }
    
    @Override
    public void rpoplpush(final String srckey, final String dstkey) {
        this.rpoplpush(SafeEncoder.encode(srckey), SafeEncoder.encode(dstkey));
    }
    
    @Override
    public void sadd(final String key, final String... members) {
        this.sadd(SafeEncoder.encode(key), SafeEncoder.encodeMany(members));
    }
    
    @Override
    public void smembers(final String key) {
        this.smembers(SafeEncoder.encode(key));
    }
    
    @Override
    public void srem(final String key, final String... members) {
        this.srem(SafeEncoder.encode(key), SafeEncoder.encodeMany(members));
    }
    
    @Override
    public void spop(final String key) {
        this.spop(SafeEncoder.encode(key));
    }
    
    @Override
    public void smove(final String srckey, final String dstkey, final String member) {
        this.smove(SafeEncoder.encode(srckey), SafeEncoder.encode(dstkey), SafeEncoder.encode(member));
    }
    
    @Override
    public void scard(final String key) {
        this.scard(SafeEncoder.encode(key));
    }
    
    @Override
    public void sismember(final String key, final String member) {
        this.sismember(SafeEncoder.encode(key), SafeEncoder.encode(member));
    }
    
    @Override
    public void sinter(final String... keys) {
        final byte[][] bkeys = new byte[keys.length][];
        for (int i = 0; i < bkeys.length; ++i) {
            bkeys[i] = SafeEncoder.encode(keys[i]);
        }
        this.sinter(bkeys);
    }
    
    @Override
    public void sinterstore(final String dstkey, final String... keys) {
        final byte[][] bkeys = new byte[keys.length][];
        for (int i = 0; i < bkeys.length; ++i) {
            bkeys[i] = SafeEncoder.encode(keys[i]);
        }
        this.sinterstore(SafeEncoder.encode(dstkey), bkeys);
    }
    
    @Override
    public void sunion(final String... keys) {
        final byte[][] bkeys = new byte[keys.length][];
        for (int i = 0; i < bkeys.length; ++i) {
            bkeys[i] = SafeEncoder.encode(keys[i]);
        }
        this.sunion(bkeys);
    }
    
    @Override
    public void sunionstore(final String dstkey, final String... keys) {
        final byte[][] bkeys = new byte[keys.length][];
        for (int i = 0; i < bkeys.length; ++i) {
            bkeys[i] = SafeEncoder.encode(keys[i]);
        }
        this.sunionstore(SafeEncoder.encode(dstkey), bkeys);
    }
    
    @Override
    public void sdiff(final String... keys) {
        final byte[][] bkeys = new byte[keys.length][];
        for (int i = 0; i < bkeys.length; ++i) {
            bkeys[i] = SafeEncoder.encode(keys[i]);
        }
        this.sdiff(bkeys);
    }
    
    @Override
    public void sdiffstore(final String dstkey, final String... keys) {
        final byte[][] bkeys = new byte[keys.length][];
        for (int i = 0; i < bkeys.length; ++i) {
            bkeys[i] = SafeEncoder.encode(keys[i]);
        }
        this.sdiffstore(SafeEncoder.encode(dstkey), bkeys);
    }
    
    @Override
    public void srandmember(final String key) {
        this.srandmember(SafeEncoder.encode(key));
    }
    
    @Override
    public void zadd(final String key, final double score, final String member) {
        this.zadd(SafeEncoder.encode(key), score, SafeEncoder.encode(member));
    }
    
    @Override
    public void zrange(final String key, final long start, final long end) {
        this.zrange(SafeEncoder.encode(key), start, end);
    }
    
    @Override
    public void zrem(final String key, final String... members) {
        this.zrem(SafeEncoder.encode(key), SafeEncoder.encodeMany(members));
    }
    
    @Override
    public void zincrby(final String key, final double score, final String member) {
        this.zincrby(SafeEncoder.encode(key), score, SafeEncoder.encode(member));
    }
    
    @Override
    public void zrank(final String key, final String member) {
        this.zrank(SafeEncoder.encode(key), SafeEncoder.encode(member));
    }
    
    @Override
    public void zrevrank(final String key, final String member) {
        this.zrevrank(SafeEncoder.encode(key), SafeEncoder.encode(member));
    }
    
    @Override
    public void zrevrange(final String key, final long start, final long end) {
        this.zrevrange(SafeEncoder.encode(key), start, end);
    }
    
    @Override
    public void zrangeWithScores(final String key, final long start, final long end) {
        this.zrangeWithScores(SafeEncoder.encode(key), start, end);
    }
    
    @Override
    public void zrevrangeWithScores(final String key, final long start, final long end) {
        this.zrevrangeWithScores(SafeEncoder.encode(key), start, end);
    }
    
    @Override
    public void zcard(final String key) {
        this.zcard(SafeEncoder.encode(key));
    }
    
    @Override
    public void zscore(final String key, final String member) {
        this.zscore(SafeEncoder.encode(key), SafeEncoder.encode(member));
    }
    
    @Override
    public void watch(final String... keys) {
        final byte[][] bargs = new byte[keys.length][];
        for (int i = 0; i < bargs.length; ++i) {
            bargs[i] = SafeEncoder.encode(keys[i]);
        }
        this.watch(bargs);
    }
    
    @Override
    public void sort(final String key) {
        this.sort(SafeEncoder.encode(key));
    }
    
    @Override
    public void sort(final String key, final SortingParams sortingParameters) {
        this.sort(SafeEncoder.encode(key), sortingParameters);
    }
    
    @Override
    public void blpop(final String[] args) {
        final byte[][] bargs = new byte[args.length][];
        for (int i = 0; i < bargs.length; ++i) {
            bargs[i] = SafeEncoder.encode(args[i]);
        }
        this.blpop(bargs);
    }
    
    public void blpop(final int timeout, final String... keys) {
        final List<String> args = new ArrayList<String>();
        for (final String arg : keys) {
            args.add(arg);
        }
        args.add(String.valueOf(timeout));
        this.blpop(args.toArray(new String[args.size()]));
    }
    
    @Override
    public void sort(final String key, final SortingParams sortingParameters, final String dstkey) {
        this.sort(SafeEncoder.encode(key), sortingParameters, SafeEncoder.encode(dstkey));
    }
    
    @Override
    public void sort(final String key, final String dstkey) {
        this.sort(SafeEncoder.encode(key), SafeEncoder.encode(dstkey));
    }
    
    @Override
    public void brpop(final String[] args) {
        final byte[][] bargs = new byte[args.length][];
        for (int i = 0; i < bargs.length; ++i) {
            bargs[i] = SafeEncoder.encode(args[i]);
        }
        this.brpop(bargs);
    }
    
    public void brpop(final int timeout, final String... keys) {
        final List<String> args = new ArrayList<String>();
        for (final String arg : keys) {
            args.add(arg);
        }
        args.add(String.valueOf(timeout));
        this.brpop(args.toArray(new String[args.size()]));
    }
    
    @Override
    public void zcount(final String key, final double min, final double max) {
        this.zcount(SafeEncoder.encode(key), Protocol.toByteArray(min), Protocol.toByteArray(max));
    }
    
    @Override
    public void zcount(final String key, final String min, final String max) {
        this.zcount(SafeEncoder.encode(key), SafeEncoder.encode(min), SafeEncoder.encode(max));
    }
    
    @Override
    public void zrangeByScore(final String key, final double min, final double max) {
        this.zrangeByScore(SafeEncoder.encode(key), Protocol.toByteArray(min), Protocol.toByteArray(max));
    }
    
    @Override
    public void zrangeByScore(final String key, final String min, final String max) {
        this.zrangeByScore(SafeEncoder.encode(key), SafeEncoder.encode(min), SafeEncoder.encode(max));
    }
    
    @Override
    public void zrangeByScore(final String key, final double min, final double max, final int offset, final int count) {
        this.zrangeByScore(SafeEncoder.encode(key), Protocol.toByteArray(min), Protocol.toByteArray(max), offset, count);
    }
    
    @Override
    public void zrangeByScoreWithScores(final String key, final double min, final double max) {
        this.zrangeByScoreWithScores(SafeEncoder.encode(key), Protocol.toByteArray(min), Protocol.toByteArray(max));
    }
    
    @Override
    public void zrangeByScoreWithScores(final String key, final double min, final double max, final int offset, final int count) {
        this.zrangeByScoreWithScores(SafeEncoder.encode(key), Protocol.toByteArray(min), Protocol.toByteArray(max), offset, count);
    }
    
    @Override
    public void zrevrangeByScore(final String key, final double max, final double min) {
        this.zrevrangeByScore(SafeEncoder.encode(key), Protocol.toByteArray(max), Protocol.toByteArray(min));
    }
    
    public void zrangeByScore(final String key, final String min, final String max, final int offset, final int count) {
        this.zrangeByScore(SafeEncoder.encode(key), SafeEncoder.encode(min), SafeEncoder.encode(max), offset, count);
    }
    
    @Override
    public void zrangeByScoreWithScores(final String key, final String min, final String max) {
        this.zrangeByScoreWithScores(SafeEncoder.encode(key), SafeEncoder.encode(min), SafeEncoder.encode(max));
    }
    
    @Override
    public void zrangeByScoreWithScores(final String key, final String min, final String max, final int offset, final int count) {
        this.zrangeByScoreWithScores(SafeEncoder.encode(key), SafeEncoder.encode(min), SafeEncoder.encode(max), offset, count);
    }
    
    @Override
    public void zrevrangeByScore(final String key, final String max, final String min) {
        this.zrevrangeByScore(SafeEncoder.encode(key), SafeEncoder.encode(max), SafeEncoder.encode(min));
    }
    
    @Override
    public void zrevrangeByScore(final String key, final double max, final double min, final int offset, final int count) {
        this.zrevrangeByScore(SafeEncoder.encode(key), Protocol.toByteArray(max), Protocol.toByteArray(min), offset, count);
    }
    
    public void zrevrangeByScore(final String key, final String max, final String min, final int offset, final int count) {
        this.zrevrangeByScore(SafeEncoder.encode(key), SafeEncoder.encode(max), SafeEncoder.encode(min), offset, count);
    }
    
    @Override
    public void zrevrangeByScoreWithScores(final String key, final double max, final double min) {
        this.zrevrangeByScoreWithScores(SafeEncoder.encode(key), Protocol.toByteArray(max), Protocol.toByteArray(min));
    }
    
    @Override
    public void zrevrangeByScoreWithScores(final String key, final String max, final String min) {
        this.zrevrangeByScoreWithScores(SafeEncoder.encode(key), SafeEncoder.encode(max), SafeEncoder.encode(min));
    }
    
    @Override
    public void zrevrangeByScoreWithScores(final String key, final double max, final double min, final int offset, final int count) {
        this.zrevrangeByScoreWithScores(SafeEncoder.encode(key), Protocol.toByteArray(max), Protocol.toByteArray(min), offset, count);
    }
    
    @Override
    public void zrevrangeByScoreWithScores(final String key, final String max, final String min, final int offset, final int count) {
        this.zrevrangeByScoreWithScores(SafeEncoder.encode(key), SafeEncoder.encode(max), SafeEncoder.encode(min), offset, count);
    }
    
    @Override
    public void zremrangeByRank(final String key, final long start, final long end) {
        this.zremrangeByRank(SafeEncoder.encode(key), start, end);
    }
    
    @Override
    public void zremrangeByScore(final String key, final double start, final double end) {
        this.zremrangeByScore(SafeEncoder.encode(key), Protocol.toByteArray(start), Protocol.toByteArray(end));
    }
    
    @Override
    public void zremrangeByScore(final String key, final String start, final String end) {
        this.zremrangeByScore(SafeEncoder.encode(key), SafeEncoder.encode(start), SafeEncoder.encode(end));
    }
    
    @Override
    public void zunionstore(final String dstkey, final String... sets) {
        final byte[][] bsets = new byte[sets.length][];
        for (int i = 0; i < bsets.length; ++i) {
            bsets[i] = SafeEncoder.encode(sets[i]);
        }
        this.zunionstore(SafeEncoder.encode(dstkey), bsets);
    }
    
    @Override
    public void zunionstore(final String dstkey, final ZParams params, final String... sets) {
        final byte[][] bsets = new byte[sets.length][];
        for (int i = 0; i < bsets.length; ++i) {
            bsets[i] = SafeEncoder.encode(sets[i]);
        }
        this.zunionstore(SafeEncoder.encode(dstkey), params, bsets);
    }
    
    @Override
    public void zinterstore(final String dstkey, final String... sets) {
        final byte[][] bsets = new byte[sets.length][];
        for (int i = 0; i < bsets.length; ++i) {
            bsets[i] = SafeEncoder.encode(sets[i]);
        }
        this.zinterstore(SafeEncoder.encode(dstkey), bsets);
    }
    
    @Override
    public void zinterstore(final String dstkey, final ZParams params, final String... sets) {
        final byte[][] bsets = new byte[sets.length][];
        for (int i = 0; i < bsets.length; ++i) {
            bsets[i] = SafeEncoder.encode(sets[i]);
        }
        this.zinterstore(SafeEncoder.encode(dstkey), params, bsets);
    }
    
    public void zlexcount(final String key, final String min, final String max) {
        this.zlexcount(SafeEncoder.encode(key), SafeEncoder.encode(min), SafeEncoder.encode(max));
    }
    
    public void zrangeByLex(final String key, final String min, final String max) {
        this.zrangeByLex(SafeEncoder.encode(key), SafeEncoder.encode(min), SafeEncoder.encode(max));
    }
    
    public void zrangeByLex(final String key, final String min, final String max, final int offset, final int count) {
        this.zrangeByLex(SafeEncoder.encode(key), SafeEncoder.encode(min), SafeEncoder.encode(max), offset, count);
    }
    
    public void zremrangeByLex(final String key, final String min, final String max) {
        this.zremrangeByLex(SafeEncoder.encode(key), SafeEncoder.encode(min), SafeEncoder.encode(max));
    }
    
    @Override
    public void strlen(final String key) {
        this.strlen(SafeEncoder.encode(key));
    }
    
    @Override
    public void lpushx(final String key, final String... string) {
        this.lpushx(SafeEncoder.encode(key), this.getByteParams(string));
    }
    
    @Override
    public void persist(final String key) {
        this.persist(SafeEncoder.encode(key));
    }
    
    @Override
    public void rpushx(final String key, final String... string) {
        this.rpushx(SafeEncoder.encode(key), this.getByteParams(string));
    }
    
    @Override
    public void echo(final String string) {
        this.echo(SafeEncoder.encode(string));
    }
    
    @Override
    public void linsert(final String key, final LIST_POSITION where, final String pivot, final String value) {
        this.linsert(SafeEncoder.encode(key), where, SafeEncoder.encode(pivot), SafeEncoder.encode(value));
    }
    
    @Override
    public void brpoplpush(final String source, final String destination, final int timeout) {
        this.brpoplpush(SafeEncoder.encode(source), SafeEncoder.encode(destination), timeout);
    }
    
    @Override
    public void setbit(final String key, final long offset, final boolean value) {
        this.setbit(SafeEncoder.encode(key), offset, value);
    }
    
    @Override
    public void setbit(final String key, final long offset, final String value) {
        this.setbit(SafeEncoder.encode(key), offset, SafeEncoder.encode(value));
    }
    
    @Override
    public void getbit(final String key, final long offset) {
        this.getbit(SafeEncoder.encode(key), offset);
    }
    
    public void bitpos(final String key, final boolean value, final BitPosParams params) {
        this.bitpos(SafeEncoder.encode(key), value, params);
    }
    
    @Override
    public void setrange(final String key, final long offset, final String value) {
        this.setrange(SafeEncoder.encode(key), offset, SafeEncoder.encode(value));
    }
    
    @Override
    public void getrange(final String key, final long startOffset, final long endOffset) {
        this.getrange(SafeEncoder.encode(key), startOffset, endOffset);
    }
    
    public void publish(final String channel, final String message) {
        this.publish(SafeEncoder.encode(channel), SafeEncoder.encode(message));
    }
    
    public void unsubscribe(final String... channels) {
        final byte[][] cs = new byte[channels.length][];
        for (int i = 0; i < cs.length; ++i) {
            cs[i] = SafeEncoder.encode(channels[i]);
        }
        this.unsubscribe(cs);
    }
    
    public void psubscribe(final String... patterns) {
        final byte[][] ps = new byte[patterns.length][];
        for (int i = 0; i < ps.length; ++i) {
            ps[i] = SafeEncoder.encode(patterns[i]);
        }
        this.psubscribe(ps);
    }
    
    public void punsubscribe(final String... patterns) {
        final byte[][] ps = new byte[patterns.length][];
        for (int i = 0; i < ps.length; ++i) {
            ps[i] = SafeEncoder.encode(patterns[i]);
        }
        this.punsubscribe(ps);
    }
    
    public void subscribe(final String... channels) {
        final byte[][] cs = new byte[channels.length][];
        for (int i = 0; i < cs.length; ++i) {
            cs[i] = SafeEncoder.encode(channels[i]);
        }
        this.subscribe(cs);
    }
    
    public void pubsubChannels(final String pattern) {
        this.pubsub("channels", pattern);
    }
    
    public void pubsubNumPat() {
        this.pubsub("numpat", new String[0]);
    }
    
    public void pubsubNumSub(final String... channels) {
        this.pubsub("numsub", channels);
    }
    
    @Override
    public void configSet(final String parameter, final String value) {
        this.configSet(SafeEncoder.encode(parameter), SafeEncoder.encode(value));
    }
    
    @Override
    public void configGet(final String pattern) {
        this.configGet(SafeEncoder.encode(pattern));
    }
    
    private byte[][] getByteParams(final String... params) {
        final byte[][] p = new byte[params.length][];
        for (int i = 0; i < params.length; ++i) {
            p[i] = SafeEncoder.encode(params[i]);
        }
        return p;
    }
    
    public void eval(final String script, final int keyCount, final String... params) {
        this.eval(SafeEncoder.encode(script), Protocol.toByteArray(keyCount), this.getByteParams(params));
    }
    
    public void evalsha(final String sha1, final int keyCount, final String... params) {
        this.evalsha(SafeEncoder.encode(sha1), Protocol.toByteArray(keyCount), this.getByteParams(params));
    }
    
    public void scriptExists(final String... sha1) {
        final byte[][] bsha1 = new byte[sha1.length][];
        for (int i = 0; i < bsha1.length; ++i) {
            bsha1[i] = SafeEncoder.encode(sha1[i]);
        }
        this.scriptExists(bsha1);
    }
    
    public void scriptLoad(final String script) {
        this.scriptLoad(SafeEncoder.encode(script));
    }
    
    @Override
    public void zadd(final String key, final Map<String, Double> scoreMembers) {
        final HashMap<byte[], Double> binaryScoreMembers = new HashMap<byte[], Double>();
        for (final Map.Entry<String, Double> entry : scoreMembers.entrySet()) {
            binaryScoreMembers.put(SafeEncoder.encode(entry.getKey()), entry.getValue());
        }
        this.zaddBinary(SafeEncoder.encode(key), binaryScoreMembers);
    }
    
    @Override
    public void objectRefcount(final String key) {
        this.objectRefcount(SafeEncoder.encode(key));
    }
    
    @Override
    public void objectIdletime(final String key) {
        this.objectIdletime(SafeEncoder.encode(key));
    }
    
    @Override
    public void objectEncoding(final String key) {
        this.objectEncoding(SafeEncoder.encode(key));
    }
    
    @Override
    public void bitcount(final String key) {
        this.bitcount(SafeEncoder.encode(key));
    }
    
    @Override
    public void bitcount(final String key, final long start, final long end) {
        this.bitcount(SafeEncoder.encode(key), start, end);
    }
    
    @Override
    public void bitop(final BitOP op, final String destKey, final String... srcKeys) {
        this.bitop(op, SafeEncoder.encode(destKey), this.getByteParams(srcKeys));
    }
    
    public void sentinel(final String... args) {
        final byte[][] arg = new byte[args.length][];
        for (int i = 0; i < arg.length; ++i) {
            arg[i] = SafeEncoder.encode(args[i]);
        }
        this.sentinel(arg);
    }
    
    public void dump(final String key) {
        this.dump(SafeEncoder.encode(key));
    }
    
    public void restore(final String key, final int ttl, final byte[] serializedValue) {
        this.restore(SafeEncoder.encode(key), ttl, serializedValue);
    }
    
    @Deprecated
    public void pexpire(final String key, final int milliseconds) {
        this.pexpire(key, (long)milliseconds);
    }
    
    public void pexpire(final String key, final long milliseconds) {
        this.pexpire(SafeEncoder.encode(key), milliseconds);
    }
    
    public void pexpireAt(final String key, final long millisecondsTimestamp) {
        this.pexpireAt(SafeEncoder.encode(key), millisecondsTimestamp);
    }
    
    public void pttl(final String key) {
        this.pttl(SafeEncoder.encode(key));
    }
    
    @Override
    public void incrByFloat(final String key, final double increment) {
        this.incrByFloat(SafeEncoder.encode(key), increment);
    }
    
    public void psetex(final String key, final int milliseconds, final String value) {
        this.psetex(SafeEncoder.encode(key), milliseconds, SafeEncoder.encode(value));
    }
    
    public void set(final String key, final String value, final String nxxx) {
        this.set(SafeEncoder.encode(key), SafeEncoder.encode(value), SafeEncoder.encode(nxxx));
    }
    
    public void set(final String key, final String value, final String nxxx, final String expx, final int time) {
        this.set(SafeEncoder.encode(key), SafeEncoder.encode(value), SafeEncoder.encode(nxxx), SafeEncoder.encode(expx), time);
    }
    
    public void srandmember(final String key, final int count) {
        this.srandmember(SafeEncoder.encode(key), count);
    }
    
    public void clientKill(final String client) {
        this.clientKill(SafeEncoder.encode(client));
    }
    
    public void clientSetname(final String name) {
        this.clientSetname(SafeEncoder.encode(name));
    }
    
    public void migrate(final String host, final int port, final String key, final int destinationDb, final int timeout) {
        this.migrate(SafeEncoder.encode(host), port, SafeEncoder.encode(key), destinationDb, timeout);
    }
    
    @Override
    public void hincrByFloat(final String key, final String field, final double increment) {
        this.hincrByFloat(SafeEncoder.encode(key), SafeEncoder.encode(field), increment);
    }
    
    @Deprecated
    @Override
    public void hscan(final String key, final int cursor, final ScanParams params) {
        this.hscan(SafeEncoder.encode(key), cursor, params);
    }
    
    @Deprecated
    @Override
    public void sscan(final String key, final int cursor, final ScanParams params) {
        this.sscan(SafeEncoder.encode(key), cursor, params);
    }
    
    @Deprecated
    @Override
    public void zscan(final String key, final int cursor, final ScanParams params) {
        this.zscan(SafeEncoder.encode(key), cursor, params);
    }
    
    @Override
    public void scan(final String cursor, final ScanParams params) {
        this.scan(SafeEncoder.encode(cursor), params);
    }
    
    @Override
    public void hscan(final String key, final String cursor, final ScanParams params) {
        this.hscan(SafeEncoder.encode(key), SafeEncoder.encode(cursor), params);
    }
    
    @Override
    public void sscan(final String key, final String cursor, final ScanParams params) {
        this.sscan(SafeEncoder.encode(key), SafeEncoder.encode(cursor), params);
    }
    
    @Override
    public void zscan(final String key, final String cursor, final ScanParams params) {
        this.zscan(SafeEncoder.encode(key), SafeEncoder.encode(cursor), params);
    }
    
    public void cluster(final String subcommand, final int... args) {
        final byte[][] arg = new byte[args.length + 1][];
        for (int i = 1; i < arg.length; ++i) {
            arg[i] = Protocol.toByteArray(args[i - 1]);
        }
        arg[0] = SafeEncoder.encode(subcommand);
        this.cluster(arg);
    }
    
    public void pubsub(final String subcommand, final String... args) {
        final byte[][] arg = new byte[args.length + 1][];
        for (int i = 1; i < arg.length; ++i) {
            arg[i] = SafeEncoder.encode(args[i - 1]);
        }
        arg[0] = SafeEncoder.encode(subcommand);
        this.pubsub(arg);
    }
    
    public void cluster(final String subcommand, final String... args) {
        final byte[][] arg = new byte[args.length + 1][];
        for (int i = 1; i < arg.length; ++i) {
            arg[i] = SafeEncoder.encode(args[i - 1]);
        }
        arg[0] = SafeEncoder.encode(subcommand);
        this.cluster(arg);
    }
    
    public void cluster(final String subcommand) {
        final byte[][] arg = { SafeEncoder.encode(subcommand) };
        this.cluster(arg);
    }
    
    public void clusterNodes() {
        this.cluster("nodes");
    }
    
    public void clusterMeet(final String ip, final int port) {
        this.cluster("meet", ip, String.valueOf(port));
    }
    
    public void clusterReset(final JedisCluster.Reset resetType) {
        this.cluster("reset", resetType.toString());
    }
    
    public void clusterAddSlots(final int... slots) {
        this.cluster("addslots", slots);
    }
    
    public void clusterDelSlots(final int... slots) {
        this.cluster("delslots", slots);
    }
    
    public void clusterInfo() {
        this.cluster("info");
    }
    
    public void clusterGetKeysInSlot(final int slot, final int count) {
        final int[] args = { slot, count };
        this.cluster("getkeysinslot", args);
    }
    
    public void clusterSetSlotNode(final int slot, final String nodeId) {
        this.cluster("setslot", String.valueOf(slot), "node", nodeId);
    }
    
    public void clusterSetSlotMigrating(final int slot, final String nodeId) {
        this.cluster("setslot", String.valueOf(slot), "migrating", nodeId);
    }
    
    public void clusterSetSlotImporting(final int slot, final String nodeId) {
        this.cluster("setslot", String.valueOf(slot), "importing", nodeId);
    }
    
    public void pfadd(final String key, final String... elements) {
        this.pfadd(SafeEncoder.encode(key), SafeEncoder.encodeMany(elements));
    }
    
    public void pfcount(final String key) {
        this.pfcount(SafeEncoder.encode(key));
    }
    
    public void pfcount(final String... keys) {
        this.pfcount(SafeEncoder.encodeMany(keys));
    }
    
    public void pfmerge(final String destkey, final String... sourcekeys) {
        this.pfmerge(SafeEncoder.encode(destkey), SafeEncoder.encodeMany(sourcekeys));
    }
    
    public void clusterSetSlotStable(final int slot) {
        this.cluster("setslot", String.valueOf(slot), "stable");
    }
    
    public void clusterForget(final String nodeId) {
        this.cluster("forget", nodeId);
    }
    
    public void clusterFlushSlots() {
        this.cluster("flushslots");
    }
    
    public void clusterKeySlot(final String key) {
        this.cluster("keyslot", key);
    }
    
    public void clusterCountKeysInSlot(final int slot) {
        this.cluster("countkeysinslot", String.valueOf(slot));
    }
    
    public void clusterSaveConfig() {
        this.cluster("saveconfig");
    }
    
    public void clusterReplicate(final String nodeId) {
        this.cluster("replicate", nodeId);
    }
    
    public void clusterSlaves(final String nodeId) {
        this.cluster("slaves", nodeId);
    }
    
    public void clusterFailover() {
        this.cluster("failover");
    }
    
    public void clusterSlots() {
        this.cluster("slots");
    }
}
