package redis.clients.util;

import java.io.*;
import org.apache.commons.pool2.impl.*;
import org.apache.commons.pool2.*;
import redis.clients.jedis.exceptions.*;

public abstract class Pool<T> implements Closeable
{
    protected GenericObjectPool<T> internalPool;
    
    public Pool() {
        super();
    }
    
    @Override
    public void close() {
        this.closeInternalPool();
    }
    
    public boolean isClosed() {
        return this.internalPool.isClosed();
    }
    
    public Pool(final GenericObjectPoolConfig poolConfig, final PooledObjectFactory<T> factory) {
        super();
        this.initPool(poolConfig, factory);
    }
    
    public void initPool(final GenericObjectPoolConfig poolConfig, final PooledObjectFactory<T> factory) {
        if (this.internalPool != null) {
            try {
                this.closeInternalPool();
            }
            catch (Exception ex) {}
        }
        this.internalPool = new GenericObjectPool<T>(factory, poolConfig);
    }
    
    public T getResource() {
        try {
            return this.internalPool.borrowObject();
        }
        catch (Exception e) {
            throw new JedisConnectionException("Could not get a resource from the pool", e);
        }
    }
    
    public void returnResourceObject(final T resource) {
        if (resource == null) {
            return;
        }
        try {
            this.internalPool.returnObject(resource);
        }
        catch (Exception e) {
            throw new JedisException("Could not return the resource to the pool", e);
        }
    }
    
    public void returnBrokenResource(final T resource) {
        if (resource != null) {
            this.returnBrokenResourceObject(resource);
        }
    }
    
    public void returnResource(final T resource) {
        if (resource != null) {
            this.returnResourceObject(resource);
        }
    }
    
    public void destroy() {
        this.closeInternalPool();
    }
    
    protected void returnBrokenResourceObject(final T resource) {
        try {
            this.internalPool.invalidateObject(resource);
        }
        catch (Exception e) {
            throw new JedisException("Could not return the resource to the pool", e);
        }
    }
    
    protected void closeInternalPool() {
        try {
            this.internalPool.close();
        }
        catch (Exception e) {
            throw new JedisException("Could not destroy the pool", e);
        }
    }
}
