package com.mongodb;

public class BulkUpdateRequestBuilder
{
    private final BulkWriteOperation bulkWriteOperation;
    private final DBObject query;
    private final boolean upsert;
    
    BulkUpdateRequestBuilder(final BulkWriteOperation bulkWriteOperation, final DBObject query, final boolean upsert) {
        super();
        this.bulkWriteOperation = bulkWriteOperation;
        this.query = query;
        this.upsert = upsert;
    }
    
    public void replaceOne(final DBObject document) {
        this.bulkWriteOperation.addRequest(new ReplaceRequest(this.query, this.upsert, document));
    }
    
    public void update(final DBObject update) {
        this.bulkWriteOperation.addRequest(new UpdateRequest(this.query, this.upsert, update, true));
    }
    
    public void updateOne(final DBObject update) {
        this.bulkWriteOperation.addRequest(new UpdateRequest(this.query, this.upsert, update, false));
    }
}
