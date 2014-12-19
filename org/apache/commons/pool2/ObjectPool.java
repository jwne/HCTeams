package org.apache.commons.pool2;

import java.util.*;

public interface ObjectPool<T>
{
    T borrowObject() throws Exception, NoSuchElementException, IllegalStateException;
    
    void returnObject(T p0) throws Exception;
    
    void invalidateObject(T p0) throws Exception;
    
    void addObject() throws Exception, IllegalStateException, UnsupportedOperationException;
    
    int getNumIdle();
    
    int getNumActive();
    
    void clear() throws Exception, UnsupportedOperationException;
    
    void close();
}
