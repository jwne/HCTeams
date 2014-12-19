package com.mongodb.util;

public interface ObjectSerializer
{
    void serialize(Object p0, StringBuilder p1);
    
    String serialize(Object p0);
}
