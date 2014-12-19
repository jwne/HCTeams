package redis.clients.jedis;

import java.util.*;

abstract class MultiKeyPipelineBase extends PipelineBase implements BasicRedisPipeline, MultiKeyBinaryRedisPipeline, MultiKeyCommandsPipeline, ClusterPipeline
{
    protected Client client;
    
    MultiKeyPipelineBase() {
        super();
        this.client = null;
    }
    
    @Override
    public Response<List<String>> brpop(final String... args) {
        this.client.brpop(args);
        return this.getResponse(BuilderFactory.STRING_LIST);
    }
    
    public Response<List<String>> brpop(final int timeout, final String... keys) {
        this.client.brpop(timeout, keys);
        return this.getResponse(BuilderFactory.STRING_LIST);
    }
    
    @Override
    public Response<List<String>> blpop(final String... args) {
        this.client.blpop(args);
        return this.getResponse(BuilderFactory.STRING_LIST);
    }
    
    public Response<List<String>> blpop(final int timeout, final String... keys) {
        this.client.blpop(timeout, keys);
        return this.getResponse(BuilderFactory.STRING_LIST);
    }
    
    public Response<Map<String, String>> blpopMap(final int timeout, final String... keys) {
        this.client.blpop(timeout, keys);
        return this.getResponse(BuilderFactory.STRING_MAP);
    }
    
    @Override
    public Response<List<byte[]>> brpop(final byte[]... args) {
        this.client.brpop(args);
        return this.getResponse(BuilderFactory.BYTE_ARRAY_LIST);
    }
    
    public Response<List<String>> brpop(final int timeout, final byte[]... keys) {
        this.client.brpop(timeout, keys);
        return this.getResponse(BuilderFactory.STRING_LIST);
    }
    
    public Response<Map<String, String>> brpopMap(final int timeout, final String... keys) {
        this.client.blpop(timeout, keys);
        return this.getResponse(BuilderFactory.STRING_MAP);
    }
    
    @Override
    public Response<List<byte[]>> blpop(final byte[]... args) {
        this.client.blpop(args);
        return this.getResponse(BuilderFactory.BYTE_ARRAY_LIST);
    }
    
    public Response<List<String>> blpop(final int timeout, final byte[]... keys) {
        this.client.blpop(timeout, keys);
        return this.getResponse(BuilderFactory.STRING_LIST);
    }
    
    @Override
    public Response<Long> del(final String... keys) {
        this.client.del(keys);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<Long> del(final byte[]... keys) {
        this.client.del(keys);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<Set<String>> keys(final String pattern) {
        this.getClient(pattern).keys(pattern);
        return this.getResponse(BuilderFactory.STRING_SET);
    }
    
    @Override
    public Response<Set<byte[]>> keys(final byte[] pattern) {
        this.getClient(pattern).keys(pattern);
        return this.getResponse(BuilderFactory.BYTE_ARRAY_ZSET);
    }
    
    @Override
    public Response<List<String>> mget(final String... keys) {
        this.client.mget(keys);
        return this.getResponse(BuilderFactory.STRING_LIST);
    }
    
    @Override
    public Response<List<byte[]>> mget(final byte[]... keys) {
        this.client.mget(keys);
        return this.getResponse(BuilderFactory.BYTE_ARRAY_LIST);
    }
    
    @Override
    public Response<String> mset(final String... keysvalues) {
        this.client.mset(keysvalues);
        return this.getResponse(BuilderFactory.STRING);
    }
    
    @Override
    public Response<String> mset(final byte[]... keysvalues) {
        this.client.mset(keysvalues);
        return this.getResponse(BuilderFactory.STRING);
    }
    
    @Override
    public Response<Long> msetnx(final String... keysvalues) {
        this.client.msetnx(keysvalues);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<Long> msetnx(final byte[]... keysvalues) {
        this.client.msetnx(keysvalues);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<String> rename(final String oldkey, final String newkey) {
        this.client.rename(oldkey, newkey);
        return this.getResponse(BuilderFactory.STRING);
    }
    
    @Override
    public Response<String> rename(final byte[] oldkey, final byte[] newkey) {
        this.client.rename(oldkey, newkey);
        return this.getResponse(BuilderFactory.STRING);
    }
    
    @Override
    public Response<Long> renamenx(final String oldkey, final String newkey) {
        this.client.renamenx(oldkey, newkey);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<Long> renamenx(final byte[] oldkey, final byte[] newkey) {
        this.client.renamenx(oldkey, newkey);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<String> rpoplpush(final String srckey, final String dstkey) {
        this.client.rpoplpush(srckey, dstkey);
        return this.getResponse(BuilderFactory.STRING);
    }
    
    @Override
    public Response<byte[]> rpoplpush(final byte[] srckey, final byte[] dstkey) {
        this.client.rpoplpush(srckey, dstkey);
        return this.getResponse(BuilderFactory.BYTE_ARRAY);
    }
    
    @Override
    public Response<Set<String>> sdiff(final String... keys) {
        this.client.sdiff(keys);
        return this.getResponse(BuilderFactory.STRING_SET);
    }
    
    @Override
    public Response<Set<byte[]>> sdiff(final byte[]... keys) {
        this.client.sdiff(keys);
        return this.getResponse(BuilderFactory.BYTE_ARRAY_ZSET);
    }
    
    @Override
    public Response<Long> sdiffstore(final String dstkey, final String... keys) {
        this.client.sdiffstore(dstkey, keys);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<Long> sdiffstore(final byte[] dstkey, final byte[]... keys) {
        this.client.sdiffstore(dstkey, keys);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<Set<String>> sinter(final String... keys) {
        this.client.sinter(keys);
        return this.getResponse(BuilderFactory.STRING_SET);
    }
    
    @Override
    public Response<Set<byte[]>> sinter(final byte[]... keys) {
        this.client.sinter(keys);
        return this.getResponse(BuilderFactory.BYTE_ARRAY_ZSET);
    }
    
    @Override
    public Response<Long> sinterstore(final String dstkey, final String... keys) {
        this.client.sinterstore(dstkey, keys);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<Long> sinterstore(final byte[] dstkey, final byte[]... keys) {
        this.client.sinterstore(dstkey, keys);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<Long> smove(final String srckey, final String dstkey, final String member) {
        this.client.smove(srckey, dstkey, member);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<Long> smove(final byte[] srckey, final byte[] dstkey, final byte[] member) {
        this.client.smove(srckey, dstkey, member);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<Long> sort(final String key, final SortingParams sortingParameters, final String dstkey) {
        this.client.sort(key, sortingParameters, dstkey);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<Long> sort(final byte[] key, final SortingParams sortingParameters, final byte[] dstkey) {
        this.client.sort(key, sortingParameters, dstkey);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<Long> sort(final String key, final String dstkey) {
        this.client.sort(key, dstkey);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<Long> sort(final byte[] key, final byte[] dstkey) {
        this.client.sort(key, dstkey);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<Set<String>> sunion(final String... keys) {
        this.client.sunion(keys);
        return this.getResponse(BuilderFactory.STRING_SET);
    }
    
    @Override
    public Response<Set<byte[]>> sunion(final byte[]... keys) {
        this.client.sunion(keys);
        return this.getResponse(BuilderFactory.BYTE_ARRAY_ZSET);
    }
    
    @Override
    public Response<Long> sunionstore(final String dstkey, final String... keys) {
        this.client.sunionstore(dstkey, keys);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<Long> sunionstore(final byte[] dstkey, final byte[]... keys) {
        this.client.sunionstore(dstkey, keys);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<String> watch(final String... keys) {
        this.client.watch(keys);
        return this.getResponse(BuilderFactory.STRING);
    }
    
    @Override
    public Response<String> watch(final byte[]... keys) {
        this.client.watch(keys);
        return this.getResponse(BuilderFactory.STRING);
    }
    
    @Override
    public Response<Long> zinterstore(final String dstkey, final String... sets) {
        this.client.zinterstore(dstkey, sets);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<Long> zinterstore(final byte[] dstkey, final byte[]... sets) {
        this.client.zinterstore(dstkey, sets);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<Long> zinterstore(final String dstkey, final ZParams params, final String... sets) {
        this.client.zinterstore(dstkey, params, sets);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<Long> zinterstore(final byte[] dstkey, final ZParams params, final byte[]... sets) {
        this.client.zinterstore(dstkey, params, sets);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<Long> zunionstore(final String dstkey, final String... sets) {
        this.client.zunionstore(dstkey, sets);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<Long> zunionstore(final byte[] dstkey, final byte[]... sets) {
        this.client.zunionstore(dstkey, sets);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<Long> zunionstore(final String dstkey, final ZParams params, final String... sets) {
        this.client.zunionstore(dstkey, params, sets);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<Long> zunionstore(final byte[] dstkey, final ZParams params, final byte[]... sets) {
        this.client.zunionstore(dstkey, params, sets);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<String> bgrewriteaof() {
        this.client.bgrewriteaof();
        return this.getResponse(BuilderFactory.STRING);
    }
    
    @Override
    public Response<String> bgsave() {
        this.client.bgsave();
        return this.getResponse(BuilderFactory.STRING);
    }
    
    @Override
    public Response<String> configGet(final String pattern) {
        this.client.configGet(pattern);
        return this.getResponse(BuilderFactory.STRING);
    }
    
    @Override
    public Response<String> configSet(final String parameter, final String value) {
        this.client.configSet(parameter, value);
        return this.getResponse(BuilderFactory.STRING);
    }
    
    @Override
    public Response<String> brpoplpush(final String source, final String destination, final int timeout) {
        this.client.brpoplpush(source, destination, timeout);
        return this.getResponse(BuilderFactory.STRING);
    }
    
    @Override
    public Response<byte[]> brpoplpush(final byte[] source, final byte[] destination, final int timeout) {
        this.client.brpoplpush(source, destination, timeout);
        return this.getResponse(BuilderFactory.BYTE_ARRAY);
    }
    
    @Override
    public Response<String> configResetStat() {
        this.client.configResetStat();
        return this.getResponse(BuilderFactory.STRING);
    }
    
    @Override
    public Response<String> save() {
        this.client.save();
        return this.getResponse(BuilderFactory.STRING);
    }
    
    @Override
    public Response<Long> lastsave() {
        this.client.lastsave();
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<Long> publish(final String channel, final String message) {
        this.client.publish(channel, message);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<Long> publish(final byte[] channel, final byte[] message) {
        this.client.publish(channel, message);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<String> randomKey() {
        this.client.randomKey();
        return this.getResponse(BuilderFactory.STRING);
    }
    
    @Override
    public Response<byte[]> randomKeyBinary() {
        this.client.randomKey();
        return this.getResponse(BuilderFactory.BYTE_ARRAY);
    }
    
    @Override
    public Response<String> flushDB() {
        this.client.flushDB();
        return this.getResponse(BuilderFactory.STRING);
    }
    
    @Override
    public Response<String> flushAll() {
        this.client.flushAll();
        return this.getResponse(BuilderFactory.STRING);
    }
    
    @Override
    public Response<String> info() {
        this.client.info();
        return this.getResponse(BuilderFactory.STRING);
    }
    
    @Override
    public Response<List<String>> time() {
        this.client.time();
        return this.getResponse(BuilderFactory.STRING_LIST);
    }
    
    @Override
    public Response<Long> dbSize() {
        this.client.dbSize();
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<String> shutdown() {
        this.client.shutdown();
        return this.getResponse(BuilderFactory.STRING);
    }
    
    @Override
    public Response<String> ping() {
        this.client.ping();
        return this.getResponse(BuilderFactory.STRING);
    }
    
    @Override
    public Response<String> select(final int index) {
        this.client.select(index);
        return this.getResponse(BuilderFactory.STRING);
    }
    
    @Override
    public Response<Long> bitop(final BitOP op, final byte[] destKey, final byte[]... srcKeys) {
        this.client.bitop(op, destKey, srcKeys);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<Long> bitop(final BitOP op, final String destKey, final String... srcKeys) {
        this.client.bitop(op, destKey, srcKeys);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<String> clusterNodes() {
        this.client.clusterNodes();
        return this.getResponse(BuilderFactory.STRING);
    }
    
    @Override
    public Response<String> clusterMeet(final String ip, final int port) {
        this.client.clusterMeet(ip, port);
        return this.getResponse(BuilderFactory.STRING);
    }
    
    @Override
    public Response<String> clusterAddSlots(final int... slots) {
        this.client.clusterAddSlots(slots);
        return this.getResponse(BuilderFactory.STRING);
    }
    
    @Override
    public Response<String> clusterDelSlots(final int... slots) {
        this.client.clusterDelSlots(slots);
        return this.getResponse(BuilderFactory.STRING);
    }
    
    @Override
    public Response<String> clusterInfo() {
        this.client.clusterInfo();
        return this.getResponse(BuilderFactory.STRING);
    }
    
    @Override
    public Response<List<String>> clusterGetKeysInSlot(final int slot, final int count) {
        this.client.clusterGetKeysInSlot(slot, count);
        return this.getResponse(BuilderFactory.STRING_LIST);
    }
    
    @Override
    public Response<String> clusterSetSlotNode(final int slot, final String nodeId) {
        this.client.clusterSetSlotNode(slot, nodeId);
        return this.getResponse(BuilderFactory.STRING);
    }
    
    @Override
    public Response<String> clusterSetSlotMigrating(final int slot, final String nodeId) {
        this.client.clusterSetSlotMigrating(slot, nodeId);
        return this.getResponse(BuilderFactory.STRING);
    }
    
    @Override
    public Response<String> clusterSetSlotImporting(final int slot, final String nodeId) {
        this.client.clusterSetSlotImporting(slot, nodeId);
        return this.getResponse(BuilderFactory.STRING);
    }
    
    @Override
    public Response<String> pfmerge(final byte[] destkey, final byte[]... sourcekeys) {
        this.client.pfmerge(destkey, sourcekeys);
        return this.getResponse(BuilderFactory.STRING);
    }
    
    @Override
    public Response<String> pfmerge(final String destkey, final String... sourcekeys) {
        this.client.pfmerge(destkey, sourcekeys);
        return this.getResponse(BuilderFactory.STRING);
    }
    
    @Override
    public Response<Long> pfcount(final String... keys) {
        this.client.pfcount(keys);
        return this.getResponse(BuilderFactory.LONG);
    }
    
    @Override
    public Response<Long> pfcount(final byte[]... keys) {
        this.client.pfcount(keys);
        return this.getResponse(BuilderFactory.LONG);
    }
}
