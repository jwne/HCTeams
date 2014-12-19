package com.mongodb;

public class MongoInternalException extends MongoException
{
    private static final long serialVersionUID = -4415279469780082174L;
    
    public MongoInternalException(final String msg) {
        super(msg);
    }
    
    public MongoInternalException(final String msg, final Throwable t) {
        super(msg, t);
    }
}
