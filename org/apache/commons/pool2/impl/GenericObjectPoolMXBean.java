package org.apache.commons.pool2.impl;

import java.util.*;

public interface GenericObjectPoolMXBean
{
    boolean getBlockWhenExhausted();
    
    boolean getLifo();
    
    int getMaxIdle();
    
    int getMaxTotal();
    
    long getMaxWaitMillis();
    
    long getMinEvictableIdleTimeMillis();
    
    int getMinIdle();
    
    int getNumActive();
    
    int getNumIdle();
    
    int getNumTestsPerEvictionRun();
    
    boolean getTestOnCreate();
    
    boolean getTestOnBorrow();
    
    boolean getTestOnReturn();
    
    boolean getTestWhileIdle();
    
    long getTimeBetweenEvictionRunsMillis();
    
    boolean isClosed();
    
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
    
    boolean isAbandonedConfig();
    
    boolean getLogAbandoned();
    
    boolean getRemoveAbandonedOnBorrow();
    
    boolean getRemoveAbandonedOnMaintenance();
    
    int getRemoveAbandonedTimeout();
    
    String getFactoryType();
    
    Set<DefaultPooledObjectInfo> listAllObjects();
}
