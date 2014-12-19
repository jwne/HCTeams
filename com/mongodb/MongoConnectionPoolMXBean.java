package com.mongodb;

import com.mongodb.util.*;

@Deprecated
public interface MongoConnectionPoolMXBean
{
    String getName();
    
    int getMaxSize();
    
    String getHost();
    
    int getPort();
    
    ConnectionPoolStatisticsBean getStatistics();
}
