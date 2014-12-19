package org.apache.commons.pool2;

import java.util.*;
import java.io.*;

public interface PooledObject<T> extends Comparable<PooledObject<T>>
{
    T getObject();
    
    long getCreateTime();
    
    long getActiveTimeMillis();
    
    long getIdleTimeMillis();
    
    long getLastBorrowTime();
    
    long getLastReturnTime();
    
    long getLastUsedTime();
    
    int compareTo(PooledObject<T> p0);
    
    boolean equals(Object p0);
    
    int hashCode();
    
    String toString();
    
    boolean startEvictionTest();
    
    boolean endEvictionTest(Deque<PooledObject<T>> p0);
    
    boolean allocate();
    
    boolean deallocate();
    
    void invalidate();
    
    void setLogAbandoned(boolean p0);
    
    void use();
    
    void printStackTrace(PrintWriter p0);
    
    PooledObjectState getState();
    
    void markAbandoned();
    
    void markReturning();
}
