package com.mongodb;

import java.util.concurrent.*;

public class AggregationOptions
{
    private final Integer batchSize;
    private final Boolean allowDiskUse;
    private final OutputMode outputMode;
    private final long maxTimeMS;
    
    AggregationOptions(final Builder builder) {
        super();
        this.batchSize = builder.batchSize;
        this.allowDiskUse = builder.allowDiskUse;
        this.outputMode = builder.outputMode;
        this.maxTimeMS = builder.maxTimeMS;
    }
    
    public Boolean getAllowDiskUse() {
        return this.allowDiskUse;
    }
    
    public Integer getBatchSize() {
        return this.batchSize;
    }
    
    public OutputMode getOutputMode() {
        return this.outputMode;
    }
    
    public long getMaxTime(final TimeUnit timeUnit) {
        return timeUnit.convert(this.maxTimeMS, TimeUnit.MILLISECONDS);
    }
    
    public String toString() {
        return "AggregationOptions{batchSize=" + this.batchSize + ", allowDiskUse=" + this.allowDiskUse + ", outputMode=" + this.outputMode + ", maxTimeMS=" + this.maxTimeMS + '}';
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public enum OutputMode
    {
        INLINE, 
        CURSOR;
    }
    
    public static class Builder
    {
        private Integer batchSize;
        private Boolean allowDiskUse;
        private OutputMode outputMode;
        private long maxTimeMS;
        
        private Builder() {
            super();
            this.outputMode = OutputMode.INLINE;
        }
        
        public Builder batchSize(final Integer size) {
            this.batchSize = size;
            return this;
        }
        
        public Builder allowDiskUse(final Boolean allowDiskUse) {
            this.allowDiskUse = allowDiskUse;
            return this;
        }
        
        public Builder outputMode(final OutputMode mode) {
            this.outputMode = mode;
            return this;
        }
        
        public Builder maxTime(final long maxTime, final TimeUnit timeUnit) {
            this.maxTimeMS = TimeUnit.MILLISECONDS.convert(maxTime, timeUnit);
            return this;
        }
        
        public AggregationOptions build() {
            return new AggregationOptions(this);
        }
    }
}
