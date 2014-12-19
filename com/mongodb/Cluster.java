package com.mongodb;

import java.util.concurrent.*;

interface Cluster
{
    ClusterDescription getDescription(long p0, TimeUnit p1);
    
    Server getServer(ServerSelector p0, long p1, TimeUnit p2);
    
    void close();
    
    boolean isClosed();
}
