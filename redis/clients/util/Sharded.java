package redis.clients.util;

import java.util.regex.*;
import java.util.*;

public class Sharded<R, S extends ShardInfo<R>>
{
    public static final int DEFAULT_WEIGHT = 1;
    private TreeMap<Long, S> nodes;
    private final Hashing algo;
    private final Map<ShardInfo<R>, R> resources;
    private Pattern tagPattern;
    public static final Pattern DEFAULT_KEY_TAG_PATTERN;
    
    public Sharded(final List<S> shards) {
        this(shards, Hashing.MURMUR_HASH);
    }
    
    public Sharded(final List<S> shards, final Hashing algo) {
        super();
        this.resources = new LinkedHashMap<ShardInfo<R>, R>();
        this.tagPattern = null;
        this.algo = algo;
        this.initialize(shards);
    }
    
    public Sharded(final List<S> shards, final Pattern tagPattern) {
        this(shards, Hashing.MURMUR_HASH, tagPattern);
    }
    
    public Sharded(final List<S> shards, final Hashing algo, final Pattern tagPattern) {
        super();
        this.resources = new LinkedHashMap<ShardInfo<R>, R>();
        this.tagPattern = null;
        this.algo = algo;
        this.tagPattern = tagPattern;
        this.initialize(shards);
    }
    
    private void initialize(final List<S> shards) {
        this.nodes = new TreeMap<Long, S>();
        for (int i = 0; i != shards.size(); ++i) {
            final S shardInfo = shards.get(i);
            if (shardInfo.getName() == null) {
                for (int n = 0; n < 160 * shardInfo.getWeight(); ++n) {
                    this.nodes.put(this.algo.hash("SHARD-" + i + "-NODE-" + n), shardInfo);
                }
            }
            else {
                for (int n = 0; n < 160 * shardInfo.getWeight(); ++n) {
                    this.nodes.put(this.algo.hash(shardInfo.getName() + "*" + shardInfo.getWeight() + n), shardInfo);
                }
            }
            this.resources.put(shardInfo, shardInfo.createResource());
        }
    }
    
    public R getShard(final byte[] key) {
        return this.resources.get(this.getShardInfo(key));
    }
    
    public R getShard(final String key) {
        return this.resources.get(this.getShardInfo(key));
    }
    
    public S getShardInfo(final byte[] key) {
        final SortedMap<Long, S> tail = this.nodes.tailMap(this.algo.hash(key));
        if (tail.isEmpty()) {
            return this.nodes.get(this.nodes.firstKey());
        }
        return tail.get(tail.firstKey());
    }
    
    public S getShardInfo(final String key) {
        return this.getShardInfo(SafeEncoder.encode(this.getKeyTag(key)));
    }
    
    public String getKeyTag(final String key) {
        if (this.tagPattern != null) {
            final Matcher m = this.tagPattern.matcher(key);
            if (m.find()) {
                return m.group(1);
            }
        }
        return key;
    }
    
    public Collection<S> getAllShardInfo() {
        return Collections.unmodifiableCollection((Collection<? extends S>)this.nodes.values());
    }
    
    public Collection<R> getAllShards() {
        return Collections.unmodifiableCollection((Collection<? extends R>)this.resources.values());
    }
    
    static {
        DEFAULT_KEY_TAG_PATTERN = Pattern.compile("\\{(.+?)\\}");
    }
}
