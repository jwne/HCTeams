package com.mongodb;

@Deprecated
public class MongoServerSelectionException extends MongoClientException
{
    private static final long serialVersionUID = -1497309903649297430L;
    
    public MongoServerSelectionException(final String message) {
        super(message);
    }
}
