package com.mongodb;

import org.bson.*;

public interface DBObject extends BSONObject
{
    void markAsPartialObject();
    
    boolean isPartialObject();
}
