package redis.clients.jedis;

import org.apache.commons.pool2.impl.*;
import redis.clients.jedis.exceptions.*;
import java.util.*;

public abstract class JedisClusterConnectionHandler
{
    protected final JedisClusterInfoCache cache;
    
    abstract Jedis getConnection();
    
    public void returnConnection(final Jedis connection) {
        this.cache.getNode(JedisClusterInfoCache.getNodeKey(connection.getClient())).returnResource(connection);
    }
    
    public void returnBrokenConnection(final Jedis connection) {
        this.cache.getNode(JedisClusterInfoCache.getNodeKey(connection.getClient())).returnBrokenResource(connection);
    }
    
    abstract Jedis getConnectionFromSlot(final int p0);
    
    public Jedis getConnectionFromNode(final HostAndPort node) {
        this.cache.setNodeIfNotExist(node);
        return this.cache.getNode(JedisClusterInfoCache.getNodeKey(node)).getResource();
    }
    
    public JedisClusterConnectionHandler(final Set<HostAndPort> nodes, final GenericObjectPoolConfig poolConfig) {
        super();
        this.cache = new JedisClusterInfoCache(poolConfig);
        this.initializeSlotsCache(nodes, poolConfig);
    }
    
    public Map<String, JedisPool> getNodes() {
        return this.cache.getNodes();
    }
    
    public void assignSlotToNode(final int slot, final HostAndPort targetNode) {
        this.cache.assignSlotToNode(slot, targetNode);
    }
    
    private void initializeSlotsCache(final Set<HostAndPort> startNodes, final GenericObjectPoolConfig poolConfig) {
        for (final HostAndPort hostAndPort : startNodes) {
            final JedisPool jp = new JedisPool(poolConfig, hostAndPort.getHost(), hostAndPort.getPort());
            Jedis jedis = null;
            try {
                jedis = jp.getResource();
                this.cache.discoverClusterNodesAndSlots(jedis);
                break;
            }
            catch (JedisConnectionException e) {}
            finally {
                if (jedis != null) {
                    jedis.close();
                }
            }
        }
        for (final HostAndPort node : startNodes) {
            this.cache.setNodeIfNotExist(node);
        }
    }
    
    public void renewSlotCache() {
        final Iterator i$ = this.cache.getNodes().values().iterator();
        if (i$.hasNext()) {
            final JedisPool jp = i$.next();
            Jedis jedis = null;
            try {
                jedis = jp.getResource();
                this.cache.discoverClusterSlots(jedis);
            }
            finally {
                if (jedis != null) {
                    jedis.close();
                }
            }
        }
    }
}
