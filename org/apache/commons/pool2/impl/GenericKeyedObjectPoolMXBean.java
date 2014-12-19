package org.apache.commons.pool2.impl;

import java.util.*;

public interface GenericKeyedObjectPoolMXBean<K>
{
    boolean getBlockWhenExhausted();
    
    boolean getLifo();
    
    int getMaxIdlePerKey();
    
    int getMaxTotal();
    
    int getMaxTotalPerKey();
    
    long getMaxWaitMillis();
    
    long getMinEvictableIdleTimeMillis();
    
    int getMinIdlePerKey();
    
    int getNumActive();
    
    int getNumIdle();
    
    int getNumTestsPerEvictionRun();
    
    boolean getTestOnCreate();
    
    boolean getTestOnBorrow();
    
    boolean getTestOnReturn();
    
    boolean getTestWhileIdle();
    
    long getTimeBetweenEvictionRunsMillis();
    
    boolean isClosed();
    
    Map<String, Integer> getNumActivePerKey();
    
    long getBorrowedCount();
    
    long getReturnedCount();
    
    long getCreatedCount();
    
    long getDestroyedCount();
    
    long getDestroyedByEvictorCount();
    
    long getDestroyedByBorrowValidationCount();
    
    long getMeanActiveTimeMillis();
    
    long getMeanIdleTimeMillis();
    
    long getMeanBorrowWaitTimeMillis();
    
    long getMaxBorrowWaitTimeMillis();
    
    String getCreationStackTrace();
    
    int getNumWaiters();
    
    Map<String, Integer> getNumWaitersByKey();
    
    Map<String, List<DefaultPooledObjectInfo>> listAllObjects();
}
