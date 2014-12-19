package com.mongodb;

class InsertRequest extends WriteRequest
{
    private final DBObject document;
    
    public InsertRequest(final DBObject document) {
        super();
        this.document = document;
    }
    
    public DBObject getDocument() {
        return this.document;
    }
    
    public Type getType() {
        return Type.INSERT;
    }
}
