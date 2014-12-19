package com.mongodb;

abstract class WriteRequest
{
    public abstract Type getType();
    
    enum Type
    {
        INSERT, 
        UPDATE, 
        REPLACE, 
        REMOVE;
    }
}
