package org.apache.commons.pool2;

public abstract class BaseKeyedPooledObjectFactory<K, V> implements KeyedPooledObjectFactory<K, V>
{
    public abstract V create(final K p0) throws Exception;
    
    public abstract PooledObject<V> wrap(final V p0);
    
    @Override
    public PooledObject<V> makeObject(final K key) throws Exception {
        return this.wrap(this.create(key));
    }
    
    @Override
    public void destroyObject(final K key, final PooledObject<V> p) throws Exception {
    }
    
    @Override
    public boolean validateObject(final K key, final PooledObject<V> p) {
        return true;
    }
    
    @Override
    public void activateObject(final K key, final PooledObject<V> p) throws Exception {
    }
    
    @Override
    public void passivateObject(final K key, final PooledObject<V> p) throws Exception {
    }
}
