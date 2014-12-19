package com.mongodb;

import org.bson.util.annotations.*;

@Immutable
final class MessageSettings
{
    private static final int DEFAULT_MAX_DOCUMENT_SIZE = 16777216;
    private static final int DEFAULT_MAX_MESSAGE_SIZE = 33554432;
    private static final int DEFAULT_MAX_WRITE_BATCH_SIZE = 1000;
    private final int maxDocumentSize;
    private final int maxMessageSize;
    private final int maxWriteBatchSize;
    
    public static Builder builder() {
        return new Builder();
    }
    
    public int getMaxDocumentSize() {
        return this.maxDocumentSize;
    }
    
    public int getMaxMessageSize() {
        return this.maxMessageSize;
    }
    
    public int getMaxWriteBatchSize() {
        return this.maxWriteBatchSize;
    }
    
    MessageSettings(final Builder builder) {
        super();
        this.maxDocumentSize = builder.maxDocumentSize;
        this.maxMessageSize = builder.maxMessageSize;
        this.maxWriteBatchSize = builder.maxWriteBatchSize;
    }
    
    static final class Builder
    {
        private int maxDocumentSize;
        private int maxMessageSize;
        private int maxWriteBatchSize;
        
        Builder() {
            super();
            this.maxDocumentSize = 16777216;
            this.maxMessageSize = 33554432;
            this.maxWriteBatchSize = 1000;
        }
        
        public MessageSettings build() {
            return new MessageSettings(this);
        }
        
        public Builder maxDocumentSize(final int maxDocumentSize) {
            this.maxDocumentSize = maxDocumentSize;
            return this;
        }
        
        public Builder maxMessageSize(final int maxMessageSize) {
            this.maxMessageSize = maxMessageSize;
            return this;
        }
        
        public Builder maxWriteBatchSize(final int maxWriteBatchSize) {
            this.maxWriteBatchSize = maxWriteBatchSize;
            return this;
        }
    }
}
