package com.mongodb;

interface ClusterableServer extends Server
{
    void addChangeListener(ChangeListener<ServerDescription> p0);
    
    void invalidate();
    
    void close();
    
    boolean isClosed();
    
    void connect();
}
