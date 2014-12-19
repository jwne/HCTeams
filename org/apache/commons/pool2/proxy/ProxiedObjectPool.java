package org.apache.commons.pool2.proxy;

import org.apache.commons.pool2.*;
import java.util.*;

public class ProxiedObjectPool<T> implements ObjectPool<T>
{
    private final ObjectPool<T> pool;
    private final ProxySource<T> proxySource;
    
    public ProxiedObjectPool(final ObjectPool<T> pool, final ProxySource<T> proxySource) {
        super();
        this.pool = pool;
        this.proxySource = proxySource;
    }
    
    @Override
    public T borrowObject() throws Exception, NoSuchElementException, IllegalStateException {
        UsageTracking<T> usageTracking = null;
        if (this.pool instanceof UsageTracking) {
            usageTracking = (UsageTracking<T>)(UsageTracking)this.pool;
        }
        final T pooledObject = this.pool.borrowObject();
        final T proxy = this.proxySource.createProxy(pooledObject, usageTracking);
        return proxy;
    }
    
    @Override
    public void returnObject(final T proxy) throws Exception {
        final T pooledObject = this.proxySource.resolveProxy(proxy);
        this.pool.returnObject(pooledObject);
    }
    
    @Override
    public void invalidateObject(final T proxy) throws Exception {
        final T pooledObject = this.proxySource.resolveProxy(proxy);
        this.pool.invalidateObject(pooledObject);
    }
    
    @Override
    public void addObject() throws Exception, IllegalStateException, UnsupportedOperationException {
        this.pool.addObject();
    }
    
    @Override
    public int getNumIdle() {
        return this.pool.getNumIdle();
    }
    
    @Override
    public int getNumActive() {
        return this.pool.getNumActive();
    }
    
    @Override
    public void clear() throws Exception, UnsupportedOperationException {
        this.pool.clear();
    }
    
    @Override
    public void close() {
        this.pool.close();
    }
}
