package com.mongodb;

class BSONWriterSettings
{
    private final int maxSerializationDepth;
    
    public BSONWriterSettings(final int maxSerializationDepth) {
        super();
        this.maxSerializationDepth = maxSerializationDepth;
    }
    
    public BSONWriterSettings() {
        this(1024);
    }
    
    public int getMaxSerializationDepth() {
        return this.maxSerializationDepth;
    }
}
