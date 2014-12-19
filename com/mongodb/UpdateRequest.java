package com.mongodb;

class UpdateRequest extends ModifyRequest
{
    private final boolean multi;
    
    public UpdateRequest(final DBObject query, final boolean upsert, final DBObject update, final boolean multi) {
        super(query, upsert, update);
        this.multi = multi;
    }
    
    public DBObject getUpdate() {
        return this.getUpdateDocument();
    }
    
    public boolean isMulti() {
        return this.multi;
    }
    
    public Type getType() {
        return Type.UPDATE;
    }
}
