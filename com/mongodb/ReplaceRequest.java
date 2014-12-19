package com.mongodb;

class ReplaceRequest extends ModifyRequest
{
    public ReplaceRequest(final DBObject query, final boolean upsert, final DBObject document) {
        super(query, upsert, document);
    }
    
    public DBObject getDocument() {
        return this.getUpdateDocument();
    }
    
    public Type getType() {
        return Type.REPLACE;
    }
}
