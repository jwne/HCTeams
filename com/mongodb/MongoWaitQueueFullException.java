package com.mongodb;

public class MongoWaitQueueFullException extends MongoClientException
{
    private static final long serialVersionUID = 1482094507852255793L;
    
    MongoWaitQueueFullException(final String msg) {
        super(msg);
    }
}
