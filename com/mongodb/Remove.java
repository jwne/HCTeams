package com.mongodb;

class Remove
{
    private final DBObject filter;
    private boolean isMulti;
    
    public Remove(final DBObject filter) {
        super();
        this.isMulti = true;
        this.filter = filter;
    }
    
    public DBObject getFilter() {
        return this.filter;
    }
    
    public Remove multi(final boolean isMulti) {
        this.isMulti = isMulti;
        return this;
    }
    
    public boolean isMulti() {
        return this.isMulti;
    }
}
