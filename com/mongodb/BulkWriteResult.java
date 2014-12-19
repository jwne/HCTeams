package com.mongodb;

import java.util.*;

public abstract class BulkWriteResult
{
    public abstract boolean isAcknowledged();
    
    public abstract int getInsertedCount();
    
    public abstract int getMatchedCount();
    
    public abstract int getRemovedCount();
    
    public abstract boolean isModifiedCountAvailable();
    
    public abstract int getModifiedCount();
    
    public abstract List<BulkWriteUpsert> getUpserts();
}
