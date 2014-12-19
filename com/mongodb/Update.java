package com.mongodb;

class Update
{
    private final DBObject updateOperations;
    private final DBObject filter;
    private boolean isUpsert;
    private boolean isMulti;
    
    public Update(final DBObject filter, final DBObject updateOperations) {
        super();
        this.isUpsert = false;
        this.isMulti = false;
        this.filter = filter;
        this.updateOperations = updateOperations;
    }
    
    DBObject getFilter() {
        return this.filter;
    }
    
    public DBObject getUpdateOperations() {
        return this.updateOperations;
    }
    
    public boolean isMulti() {
        return this.isMulti;
    }
    
    boolean isUpsert() {
        return this.isUpsert;
    }
    
    public Update multi(final boolean isMulti) {
        this.isMulti = isMulti;
        return this;
    }
    
    public Update upsert(final boolean isUpsert) {
        this.isUpsert = isUpsert;
        return this;
    }
}
