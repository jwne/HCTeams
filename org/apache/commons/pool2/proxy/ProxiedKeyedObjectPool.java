package org.apache.commons.pool2.proxy;

import org.apache.commons.pool2.*;
import java.util.*;

public class ProxiedKeyedObjectPool<K, V> implements KeyedObjectPool<K, V>
{
    private final KeyedObjectPool<K, V> pool;
    private final ProxySource<V> proxySource;
    
    public ProxiedKeyedObjectPool(final KeyedObjectPool<K, V> pool, final ProxySource<V> proxySource) {
        super();
        this.pool = pool;
        this.proxySource = proxySource;
    }
    
    @Override
    public V borrowObject(final K key) throws Exception, NoSuchElementException, IllegalStateException {
        UsageTracking<V> usageTracking = null;
        if (this.pool instanceof UsageTracking) {
            usageTracking = (UsageTracking<V>)(UsageTracking)this.pool;
        }
        final V pooledObject = this.pool.borrowObject(key);
        final V proxy = this.proxySource.createProxy(pooledObject, usageTracking);
        return proxy;
    }
    
    @Override
    public void returnObject(final K key, final V proxy) throws Exception {
        final V pooledObject = this.proxySource.resolveProxy(proxy);
        this.pool.returnObject(key, pooledObject);
    }
    
    @Override
    public void invalidateObject(final K key, final V proxy) throws Exception {
        final V pooledObject = this.proxySource.resolveProxy(proxy);
        this.pool.invalidateObject(key, pooledObject);
    }
    
    @Override
    public void addObject(final K key) throws Exception, IllegalStateException, UnsupportedOperationException {
        this.pool.addObject(key);
    }
    
    @Override
    public int getNumIdle(final K key) {
        return this.pool.getNumIdle(key);
    }
    
    @Override
    public int getNumActive(final K key) {
        return this.pool.getNumActive(key);
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
    public void clear(final K key) throws Exception, UnsupportedOperationException {
        this.pool.clear(key);
    }
    
    @Override
    public void close() {
        this.pool.close();
    }
}
