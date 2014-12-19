package com.mongodb;

class BSONBinaryWriterSettings
{
    private final int maxDocumentSize;
    
    public BSONBinaryWriterSettings(final int maxDocumentSize) {
        super();
        this.maxDocumentSize = maxDocumentSize;
    }
    
    public BSONBinaryWriterSettings() {
        this(16777216);
    }
    
    public int getMaxDocumentSize() {
        return this.maxDocumentSize;
    }
}
