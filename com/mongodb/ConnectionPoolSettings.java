package com.mongodb;

import java.util.concurrent.*;
import org.bson.util.*;

class ConnectionPoolSettings
{
    private final int maxSize;
    private final int minSize;
    private final int maxWaitQueueSize;
    private final long maxWaitTimeMS;
    private final long maxConnectionLifeTimeMS;
    private final long maxConnectionIdleTimeMS;
    private final long maintenanceInitialDelayMS;
    private final long maintenanceFrequencyMS;
    
    public static Builder builder() {
        return new Builder();
    }
    
    public int getMaxSize() {
        return this.maxSize;
    }
    
    public int getMinSize() {
        return this.minSize;
    }
    
    public int getMaxWaitQueueSize() {
        return this.maxWaitQueueSize;
    }
    
    public long getMaxWaitTime(final TimeUnit timeUnit) {
        return timeUnit.convert(this.maxWaitTimeMS, TimeUnit.MILLISECONDS);
    }
    
    public long getMaxConnectionLifeTime(final TimeUnit timeUnit) {
        return timeUnit.convert(this.maxConnectionLifeTimeMS, TimeUnit.MILLISECONDS);
    }
    
    public long getMaxConnectionIdleTime(final TimeUnit timeUnit) {
        return timeUnit.convert(this.maxConnectionIdleTimeMS, TimeUnit.MILLISECONDS);
    }
    
    public long getMaintenanceInitialDelay(final TimeUnit timeUnit) {
        return timeUnit.convert(this.maintenanceInitialDelayMS, TimeUnit.MILLISECONDS);
    }
    
    public long getMaintenanceFrequency(final TimeUnit timeUnit) {
        return timeUnit.convert(this.maintenanceFrequencyMS, TimeUnit.MILLISECONDS);
    }
    
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final ConnectionPoolSettings that = (ConnectionPoolSettings)o;
        return this.maxConnectionIdleTimeMS == that.maxConnectionIdleTimeMS && this.maxConnectionLifeTimeMS == that.maxConnectionLifeTimeMS && this.maxSize == that.maxSize && this.minSize == that.minSize && this.maintenanceInitialDelayMS == that.maintenanceInitialDelayMS && this.maintenanceFrequencyMS == that.maintenanceFrequencyMS && this.maxWaitQueueSize == that.maxWaitQueueSize && this.maxWaitTimeMS == that.maxWaitTimeMS;
    }
    
    public int hashCode() {
        int result = this.maxSize;
        result = 31 * result + this.minSize;
        result = 31 * result + this.maxWaitQueueSize;
        result = 31 * result + (int)(this.maxWaitTimeMS ^ this.maxWaitTimeMS >>> 32);
        result = 31 * result + (int)(this.maxConnectionLifeTimeMS ^ this.maxConnectionLifeTimeMS >>> 32);
        result = 31 * result + (int)(this.maxConnectionIdleTimeMS ^ this.maxConnectionIdleTimeMS >>> 32);
        result = 31 * result + (int)(this.maintenanceInitialDelayMS ^ this.maintenanceInitialDelayMS >>> 32);
        result = 31 * result + (int)(this.maintenanceFrequencyMS ^ this.maintenanceFrequencyMS >>> 32);
        return result;
    }
    
    public String toString() {
        return "ConnectionPoolSettings{maxSize=" + this.maxSize + ", minSize=" + this.minSize + ", maxWaitQueueSize=" + this.maxWaitQueueSize + ", maxWaitTimeMS=" + this.maxWaitTimeMS + ", maxConnectionLifeTimeMS=" + this.maxConnectionLifeTimeMS + ", maxConnectionIdleTimeMS=" + this.maxConnectionIdleTimeMS + ", maintenanceInitialDelayMS=" + this.maintenanceInitialDelayMS + ", maintenanceFrequencyMS=" + this.maintenanceFrequencyMS + '}';
    }
    
    ConnectionPoolSettings(final Builder builder) {
        super();
        Assertions.isTrue("maxSize > 0", builder.maxSize > 0);
        Assertions.isTrue("minSize >= 0", builder.minSize >= 0);
        Assertions.isTrue("maxWaitQueueSize >= 0", builder.maxWaitQueueSize >= 0);
        Assertions.isTrue("maxConnectionLifeTime >= 0", builder.maxConnectionLifeTimeMS >= 0L);
        Assertions.isTrue("maxConnectionIdleTime >= 0", builder.maxConnectionIdleTimeMS >= 0L);
        Assertions.isTrue("sizeMaintenanceFrequency >= 0", builder.maintenanceFrequencyMS >= 0L);
        Assertions.isTrue("maxSize >= minSize", builder.maxSize >= builder.minSize);
        this.maxSize = builder.maxSize;
        this.minSize = builder.minSize;
        this.maxWaitQueueSize = builder.maxWaitQueueSize;
        this.maxWaitTimeMS = builder.maxWaitTimeMS;
        this.maxConnectionLifeTimeMS = builder.maxConnectionLifeTimeMS;
        this.maxConnectionIdleTimeMS = builder.maxConnectionIdleTimeMS;
        this.maintenanceInitialDelayMS = builder.maintenanceInitialDelayMS;
        this.maintenanceFrequencyMS = builder.maintenanceFrequencyMS;
    }
    
    static class Builder
    {
        private int maxSize;
        private int minSize;
        private int maxWaitQueueSize;
        private long maxWaitTimeMS;
        private long maxConnectionLifeTimeMS;
        private long maxConnectionIdleTimeMS;
        private long maintenanceInitialDelayMS;
        private long maintenanceFrequencyMS;
        
        Builder() {
            super();
            this.maintenanceFrequencyMS = TimeUnit.MILLISECONDS.convert(60L, TimeUnit.SECONDS);
        }
        
        public Builder maxSize(final int maxSize) {
            this.maxSize = maxSize;
            return this;
        }
        
        public Builder minSize(final int minSize) {
            this.minSize = minSize;
            return this;
        }
        
        public Builder maxWaitQueueSize(final int maxWaitQueueSize) {
            this.maxWaitQueueSize = maxWaitQueueSize;
            return this;
        }
        
        public Builder maxWaitTime(final long maxWaitTime, final TimeUnit timeUnit) {
            this.maxWaitTimeMS = TimeUnit.MILLISECONDS.convert(maxWaitTime, timeUnit);
            return this;
        }
        
        public Builder maxConnectionLifeTime(final long maxConnectionLifeTime, final TimeUnit timeUnit) {
            this.maxConnectionLifeTimeMS = TimeUnit.MILLISECONDS.convert(maxConnectionLifeTime, timeUnit);
            return this;
        }
        
        public Builder maxConnectionIdleTime(final long maxConnectionIdleTime, final TimeUnit timeUnit) {
            this.maxConnectionIdleTimeMS = TimeUnit.MILLISECONDS.convert(maxConnectionIdleTime, timeUnit);
            return this;
        }
        
        public Builder maintenanceInitialDelay(final long maintenanceInitialDelay, final TimeUnit timeUnit) {
            this.maintenanceInitialDelayMS = TimeUnit.MILLISECONDS.convert(maintenanceInitialDelay, timeUnit);
            return this;
        }
        
        public Builder maintenanceFrequency(final long maintenanceFrequency, final TimeUnit timeUnit) {
            this.maintenanceFrequencyMS = TimeUnit.MILLISECONDS.convert(maintenanceFrequency, timeUnit);
            return this;
        }
        
        public ConnectionPoolSettings build() {
            return new ConnectionPoolSettings(this);
        }
    }
}
