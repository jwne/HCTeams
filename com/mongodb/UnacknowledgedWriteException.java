package com.mongodb;

public class UnacknowledgedWriteException extends MongoClientException
{
    private static final long serialVersionUID = 6974332938681213965L;
    
    UnacknowledgedWriteException(final String msg) {
        super(msg);
    }
}
