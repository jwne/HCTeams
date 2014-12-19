package com.mongodb;

interface Connection
{
    int getGeneration();
    
    long getOpenedAt();
    
    long getLastUsedAt();
    
    boolean isClosed();
    
    void close();
}
