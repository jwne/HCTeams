package com.mongodb;

public class MongoInterruptedException extends MongoException
{
    private static final long serialVersionUID = -4110417867718417860L;
    
    public MongoInterruptedException(final InterruptedException e) {
        super("A driver operation has been interrupted", e);
    }
    
    public MongoInterruptedException(final String message, final InterruptedException e) {
        super(message, e);
    }
}
