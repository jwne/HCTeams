package com.mongodb;

public class BulkWriteRequestBuilder
{
    private final BulkWriteOperation bulkWriteOperation;
    private final DBObject query;
    
    BulkWriteRequestBuilder(final BulkWriteOperation bulkWriteOperation, final DBObject query) {
        super();
        this.bulkWriteOperation = bulkWriteOperation;
        this.query = query;
    }
    
    public void remove() {
        this.bulkWriteOperation.addRequest(new RemoveRequest(this.query, true));
    }
    
    public void removeOne() {
        this.bulkWriteOperation.addRequest(new RemoveRequest(this.query, false));
    }
    
    public void replaceOne(final DBObject document) {
        new BulkUpdateRequestBuilder(this.bulkWriteOperation, this.query, false).replaceOne(document);
    }
    
    public void update(final DBObject update) {
        new BulkUpdateRequestBuilder(this.bulkWriteOperation, this.query, false).update(update);
    }
    
    public void updateOne(final DBObject update) {
        new BulkUpdateRequestBuilder(this.bulkWriteOperation, this.query, false).updateOne(update);
    }
    
    public BulkUpdateRequestBuilder upsert() {
        return new BulkUpdateRequestBuilder(this.bulkWriteOperation, this.query, true);
    }
}
