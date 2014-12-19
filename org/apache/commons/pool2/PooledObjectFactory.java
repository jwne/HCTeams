package org.apache.commons.pool2;

public interface PooledObjectFactory<T>
{
    PooledObject<T> makeObject() throws Exception;
    
    void destroyObject(PooledObject<T> p0) throws Exception;
    
    boolean validateObject(PooledObject<T> p0);
    
    void activateObject(PooledObject<T> p0) throws Exception;
    
    void passivateObject(PooledObject<T> p0) throws Exception;
}
