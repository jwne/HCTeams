package com.mongodb;

@Deprecated
public interface Java5MongoConnectionPoolMBean
{
    String getName();
    
    String getHost();
    
    int getPort();
    
    int getTotal();
    
    int getInUse();
    
    int getMaxSize();
}
