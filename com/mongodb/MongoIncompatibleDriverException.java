package com.mongodb;

public class MongoIncompatibleDriverException extends MongoException
{
    private static final long serialVersionUID = -5213381354402601890L;
    
    MongoIncompatibleDriverException(final String message) {
        super(message);
    }
}
