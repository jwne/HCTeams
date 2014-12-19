package org.apache.commons.pool2;

public interface KeyedPooledObjectFactory<K, V>
{
    PooledObject<V> makeObject(K p0) throws Exception;
    
    void destroyObject(K p0, PooledObject<V> p1) throws Exception;
    
    boolean validateObject(K p0, PooledObject<V> p1);
    
    void activateObject(K p0, PooledObject<V> p1) throws Exception;
    
    void passivateObject(K p0, PooledObject<V> p1) throws Exception;
}
