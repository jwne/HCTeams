package com.mongodb;

import java.util.*;
import org.bson.util.*;

public class BulkWriteOperation
{
    private final boolean ordered;
    private final DBCollection collection;
    private final List<WriteRequest> requests;
    private boolean closed;
    
    BulkWriteOperation(final boolean ordered, final DBCollection collection) {
        super();
        this.requests = new ArrayList<WriteRequest>();
        this.ordered = ordered;
        this.collection = collection;
    }
    
    public boolean isOrdered() {
        return this.ordered;
    }
    
    public void insert(final DBObject document) {
        Assertions.isTrue("already executed", !this.closed);
        this.addRequest(new InsertRequest(document));
    }
    
    public BulkWriteRequestBuilder find(final DBObject query) {
        Assertions.isTrue("already executed", !this.closed);
        return new BulkWriteRequestBuilder(this, query);
    }
    
    public BulkWriteResult execute() {
        Assertions.isTrue("already executed", !this.closed);
        this.closed = true;
        return this.collection.executeBulkWriteOperation(this.ordered, this.requests);
    }
    
    public BulkWriteResult execute(final WriteConcern writeConcern) {
        Assertions.isTrue("already executed", !this.closed);
        this.closed = true;
        return this.collection.executeBulkWriteOperation(this.ordered, this.requests, writeConcern);
    }
    
    void addRequest(final WriteRequest request) {
        Assertions.isTrue("already executed", !this.closed);
        this.requests.add(request);
    }
}
