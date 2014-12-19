package redis.clients.jedis;

import org.apache.commons.pool2.impl.*;

public class JedisPoolConfig extends GenericObjectPoolConfig
{
    public JedisPoolConfig() {
        super();
        this.setTestWhileIdle(true);
        this.setMinEvictableIdleTimeMillis(60000L);
        this.setTimeBetweenEvictionRunsMillis(30000L);
        this.setNumTestsPerEvictionRun(-1);
    }
}
