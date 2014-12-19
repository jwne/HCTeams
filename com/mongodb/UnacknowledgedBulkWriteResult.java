package com.mongodb;

import java.util.*;

class UnacknowledgedBulkWriteResult extends BulkWriteResult
{
    public boolean isAcknowledged() {
        return false;
    }
    
    public int getInsertedCount() {
        throw this.getUnacknowledgedWriteException();
    }
    
    public int getMatchedCount() {
        throw this.getUnacknowledgedWriteException();
    }
    
    public int getRemovedCount() {
        throw this.getUnacknowledgedWriteException();
    }
    
    public boolean isModifiedCountAvailable() {
        throw this.getUnacknowledgedWriteException();
    }
    
    public int getModifiedCount() {
        throw this.getUnacknowledgedWriteException();
    }
    
    public List<BulkWriteUpsert> getUpserts() {
        throw this.getUnacknowledgedWriteException();
    }
    
    private UnacknowledgedWriteException getUnacknowledgedWriteException() {
        return new UnacknowledgedWriteException("Can not get information about an unacknowledged write");
    }
    
    public boolean equals(final Object o) {
        return this == o || (o != null && this.getClass() == o.getClass());
    }
    
    public int hashCode() {
        return 0;
    }
    
    public String toString() {
        return "UnacknowledgedBulkWriteResult{}";
    }
}
