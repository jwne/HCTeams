package com.mongodb;

public interface ConnectionPoolStatisticsMBean
{
    String getHost();
    
    int getPort();
    
    int getMinSize();
    
    int getMaxSize();
    
    int getSize();
    
    int getCheckedOutCount();
    
    int getWaitQueueSize();
}
