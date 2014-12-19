package com.mongodb;

public class MongoTimeoutException extends MongoClientException
{
    private static final long serialVersionUID = -3016560214331826577L;
    
    MongoTimeoutException(final String msg) {
        super(msg);
    }
}
