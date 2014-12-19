package redis.clients.jedis;

import java.net.*;
import redis.clients.util.*;
import org.apache.commons.pool2.impl.*;
import org.apache.commons.pool2.*;

public class JedisPool extends Pool<Jedis>
{
    public JedisPool(final GenericObjectPoolConfig poolConfig, final String host) {
        this(poolConfig, host, 6379, 2000, null, 0, null);
    }
    
    public JedisPool(final String host, final int port) {
        this(new GenericObjectPoolConfig(), host, port, 2000, null, 0, null);
    }
    
    public JedisPool(final String host) {
        super();
        final URI uri = URI.create(host);
        if (uri.getScheme() != null && uri.getScheme().equals("redis")) {
            final String h = uri.getHost();
            final int port = uri.getPort();
            final String password = JedisURIHelper.getPassword(uri);
            int database = 0;
            final Integer dbIndex = JedisURIHelper.getDBIndex(uri);
            if (dbIndex != null) {
                database = dbIndex;
            }
            this.internalPool = (GenericObjectPool<T>)new GenericObjectPool<Object>((PooledObjectFactory<T>)new JedisFactory(h, port, 2000, password, database, null), new GenericObjectPoolConfig());
        }
        else {
            this.internalPool = (GenericObjectPool<T>)new GenericObjectPool<Object>((PooledObjectFactory<T>)new JedisFactory(host, 6379, 2000, null, 0, null), new GenericObjectPoolConfig());
        }
    }
    
    public JedisPool(final URI uri) {
        this(new GenericObjectPoolConfig(), uri, 2000);
    }
    
    public JedisPool(final URI uri, final int timeout) {
        this(new GenericObjectPoolConfig(), uri, timeout);
    }
    
    public JedisPool(final GenericObjectPoolConfig poolConfig, final String host, final int port, final int timeout, final String password) {
        this(poolConfig, host, port, timeout, password, 0, null);
    }
    
    public JedisPool(final GenericObjectPoolConfig poolConfig, final String host, final int port) {
        this(poolConfig, host, port, 2000, null, 0, null);
    }
    
    public JedisPool(final GenericObjectPoolConfig poolConfig, final String host, final int port, final int timeout) {
        this(poolConfig, host, port, timeout, null, 0, null);
    }
    
    public JedisPool(final GenericObjectPoolConfig poolConfig, final String host, final int port, final int timeout, final String password, final int database) {
        this(poolConfig, host, port, timeout, password, database, null);
    }
    
    public JedisPool(final GenericObjectPoolConfig poolConfig, final String host, final int port, final int timeout, final String password, final int database, final String clientName) {
        super(poolConfig, new JedisFactory(host, port, timeout, password, database, clientName));
    }
    
    public JedisPool(final GenericObjectPoolConfig poolConfig, final URI uri) {
        this(poolConfig, uri, 2000);
    }
    
    public JedisPool(final GenericObjectPoolConfig poolConfig, final URI uri, final int timeout) {
        super(poolConfig, new JedisFactory(uri.getHost(), uri.getPort(), timeout, JedisURIHelper.getPassword(uri), (JedisURIHelper.getDBIndex(uri) != null) ? JedisURIHelper.getDBIndex(uri) : 0, null));
    }
    
    @Override
    public Jedis getResource() {
        final Jedis jedis = super.getResource();
        jedis.setDataSource(this);
        return jedis;
    }
    
    @Override
    public void returnBrokenResource(final Jedis resource) {
        if (resource != null) {
            this.returnBrokenResourceObject(resource);
        }
    }
    
    @Override
    public void returnResource(final Jedis resource) {
        if (resource != null) {
            resource.resetState();
            this.returnResourceObject(resource);
        }
    }
    
    public int getNumActive() {
        if (this.internalPool == null || this.internalPool.isClosed()) {
            return -1;
        }
        return this.internalPool.getNumActive();
    }
}
