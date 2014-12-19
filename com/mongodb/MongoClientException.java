package com.mongodb;

public class MongoClientException extends MongoInternalException
{
    private static final long serialVersionUID = -5127414714432646066L;
    
    MongoClientException(final String msg) {
        super(msg);
    }
}
