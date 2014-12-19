package org.apache.commons.pool2;

import java.util.*;

public interface KeyedObjectPool<K, V>
{
    V borrowObject(K p0) throws Exception, NoSuchElementException, IllegalStateException;
    
    void returnObject(K p0, V p1) throws Exception;
    
    void invalidateObject(K p0, V p1) throws Exception;
    
    void addObject(K p0) throws Exception, IllegalStateException, UnsupportedOperationException;
    
    int getNumIdle(K p0);
    
    int getNumActive(K p0);
    
    int getNumIdle();
    
    int getNumActive();
    
    void clear() throws Exception, UnsupportedOperationException;
    
    void clear(K p0) throws Exception, UnsupportedOperationException;
    
    void close();
}
