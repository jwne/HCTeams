package com.mongodb;

class RemoveRequest extends WriteRequest
{
    private final DBObject query;
    private final boolean multi;
    
    public RemoveRequest(final DBObject query, final boolean multi) {
        super();
        this.query = query;
        this.multi = multi;
    }
    
    public DBObject getQuery() {
        return this.query;
    }
    
    public boolean isMulti() {
        return this.multi;
    }
    
    public Type getType() {
        return Type.REMOVE;
    }
}
