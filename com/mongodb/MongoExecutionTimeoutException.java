package com.mongodb;

public class MongoExecutionTimeoutException extends MongoException
{
    private static final long serialVersionUID = 5955669123800274594L;
    
    MongoExecutionTimeoutException(final int code, final String errorMessage) {
        super(code, errorMessage);
    }
}
