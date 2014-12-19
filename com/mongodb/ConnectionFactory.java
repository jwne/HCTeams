package com.mongodb;

interface ConnectionFactory
{
    Connection create(ServerAddress p0, PooledConnectionProvider p1, int p2);
}
