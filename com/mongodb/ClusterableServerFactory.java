package com.mongodb;

interface ClusterableServerFactory
{
    ClusterableServer create(ServerAddress p0);
    
    ServerSettings getSettings();
}
