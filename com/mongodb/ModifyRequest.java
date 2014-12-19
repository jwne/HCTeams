package com.mongodb;

abstract class ModifyRequest extends WriteRequest
{
    private final DBObject query;
    private final boolean upsert;
    private final DBObject updateDocument;
    
    public ModifyRequest(final DBObject query, final boolean upsert, final DBObject updateDocument) {
        super();
        this.query = query;
        this.upsert = upsert;
        this.updateDocument = updateDocument;
    }
    
    public DBObject getQuery() {
        return this.query;
    }
    
    public boolean isUpsert() {
        return this.upsert;
    }
    
    public DBObject getUpdateDocument() {
        return this.updateDocument;
    }
    
    public boolean isMulti() {
        return false;
    }
}
