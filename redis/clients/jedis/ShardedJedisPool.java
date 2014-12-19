package redis.clients.jedis;

import java.util.regex.*;
import org.apache.commons.pool2.*;
import org.apache.commons.pool2.impl.*;
import java.util.*;
import redis.clients.util.*;

public class ShardedJedisPool extends Pool<ShardedJedis>
{
    public ShardedJedisPool(final GenericObjectPoolConfig poolConfig, final List<JedisShardInfo> shards) {
        this(poolConfig, shards, Hashing.MURMUR_HASH);
    }
    
    public ShardedJedisPool(final GenericObjectPoolConfig poolConfig, final List<JedisShardInfo> shards, final Hashing algo) {
        this(poolConfig, shards, algo, null);
    }
    
    public ShardedJedisPool(final GenericObjectPoolConfig poolConfig, final List<JedisShardInfo> shards, final Pattern keyTagPattern) {
        this(poolConfig, shards, Hashing.MURMUR_HASH, keyTagPattern);
    }
    
    public ShardedJedisPool(final GenericObjectPoolConfig poolConfig, final List<JedisShardInfo> shards, final Hashing algo, final Pattern keyTagPattern) {
        super(poolConfig, new ShardedJedisFactory(shards, algo, keyTagPattern));
    }
    
    @Override
    public ShardedJedis getResource() {
        final ShardedJedis jedis = super.getResource();
        jedis.setDataSource(this);
        return jedis;
    }
    
    @Override
    public void returnBrokenResource(final ShardedJedis resource) {
        if (resource != null) {
            this.returnBrokenResourceObject(resource);
        }
    }
    
    @Override
    public void returnResource(final ShardedJedis resource) {
        if (resource != null) {
            resource.resetState();
            this.returnResourceObject(resource);
        }
    }
    
    private static class ShardedJedisFactory implements PooledObjectFactory<ShardedJedis>
    {
        private List<JedisShardInfo> shards;
        private Hashing algo;
        private Pattern keyTagPattern;
        
        public ShardedJedisFactory(final List<JedisShardInfo> shards, final Hashing algo, final Pattern keyTagPattern) {
            super();
            this.shards = shards;
            this.algo = algo;
            this.keyTagPattern = keyTagPattern;
        }
        
        @Override
        public PooledObject<ShardedJedis> makeObject() throws Exception {
            final ShardedJedis jedis = new ShardedJedis(this.shards, this.algo, this.keyTagPattern);
            return new DefaultPooledObject<ShardedJedis>(jedis);
        }
        
        @Override
        public void destroyObject(final PooledObject<ShardedJedis> pooledShardedJedis) throws Exception {
            final ShardedJedis shardedJedis = pooledShardedJedis.getObject();
            for (final Jedis jedis : ((Sharded<Jedis, S>)shardedJedis).getAllShards()) {
                try {
                    try {
                        jedis.quit();
                    }
                    catch (Exception ex) {}
                    jedis.disconnect();
                }
                catch (Exception ex2) {}
            }
        }
        
        @Override
        public boolean validateObject(final PooledObject<ShardedJedis> pooledShardedJedis) {
            try {
                final ShardedJedis jedis = pooledShardedJedis.getObject();
                for (final Jedis shard : ((Sharded<Jedis, S>)jedis).getAllShards()) {
                    if (!shard.ping().equals("PONG")) {
                        return false;
                    }
                }
                return true;
            }
            catch (Exception ex) {
                return false;
            }
        }
        
        @Override
        public void activateObject(final PooledObject<ShardedJedis> p) throws Exception {
        }
        
        @Override
        public void passivateObject(final PooledObject<ShardedJedis> p) throws Exception {
        }
    }
}
