package redis.clients.jedis;

import org.apache.commons.pool2.impl.*;
import redis.clients.jedis.exceptions.*;
import java.util.*;

public class JedisSlotBasedConnectionHandler extends JedisClusterConnectionHandler
{
    public JedisSlotBasedConnectionHandler(final Set<HostAndPort> nodes, final GenericObjectPoolConfig poolConfig) {
        super(nodes, poolConfig);
    }
    
    public Jedis getConnection() {
        final List<JedisPool> pools = this.getShuffledNodesPool();
        for (final JedisPool pool : pools) {
            Jedis jedis = null;
            try {
                jedis = pool.getResource();
                if (jedis == null) {
                    continue;
                }
                final String result = jedis.ping();
                if (result.equalsIgnoreCase("pong")) {
                    return jedis;
                }
                pool.returnBrokenResource(jedis);
            }
            catch (JedisConnectionException ex) {
                if (jedis == null) {
                    continue;
                }
                pool.returnBrokenResource(jedis);
            }
        }
        throw new JedisConnectionException("no reachable node in cluster");
    }
    
    public Jedis getConnectionFromSlot(final int slot) {
        final JedisPool connectionPool = this.cache.getSlotPool(slot);
        if (connectionPool != null) {
            return connectionPool.getResource();
        }
        return this.getConnection();
    }
    
    private List<JedisPool> getShuffledNodesPool() {
        final List<JedisPool> pools = new ArrayList<JedisPool>();
        pools.addAll(this.cache.getNodes().values());
        Collections.shuffle(pools);
        return pools;
    }
}
