package com.mongodb;

import java.util.concurrent.*;
import org.bson.util.*;

class ServerSettings
{
    private final long heartbeatFrequencyMS;
    private final long heartbeatConnectRetryFrequencyMS;
    private final SocketSettings heartbeatSocketSettings;
    
    public static Builder builder() {
        return new Builder();
    }
    
    public long getHeartbeatFrequency(final TimeUnit timeUnit) {
        return timeUnit.convert(this.heartbeatFrequencyMS, TimeUnit.MILLISECONDS);
    }
    
    public long getHeartbeatConnectRetryFrequency(final TimeUnit timeUnit) {
        return timeUnit.convert(this.heartbeatConnectRetryFrequencyMS, TimeUnit.MILLISECONDS);
    }
    
    public SocketSettings getHeartbeatSocketSettings() {
        return this.heartbeatSocketSettings;
    }
    
    ServerSettings(final Builder builder) {
        super();
        this.heartbeatFrequencyMS = builder.heartbeatFrequencyMS;
        this.heartbeatConnectRetryFrequencyMS = builder.heartbeatConnectRetryFrequencyMS;
        this.heartbeatSocketSettings = builder.heartbeatSocketSettings;
    }
    
    static class Builder
    {
        private long heartbeatFrequencyMS;
        private long heartbeatConnectRetryFrequencyMS;
        private SocketSettings heartbeatSocketSettings;
        
        Builder() {
            super();
            this.heartbeatFrequencyMS = 5000L;
            this.heartbeatConnectRetryFrequencyMS = 10L;
            this.heartbeatSocketSettings = SocketSettings.builder().build();
        }
        
        public Builder heartbeatFrequency(final long heartbeatFrequency, final TimeUnit timeUnit) {
            this.heartbeatFrequencyMS = TimeUnit.MILLISECONDS.convert(heartbeatFrequency, timeUnit);
            return this;
        }
        
        public Builder heartbeatConnectRetryFrequency(final long heartbeatConnectRetryFrequency, final TimeUnit timeUnit) {
            this.heartbeatConnectRetryFrequencyMS = TimeUnit.MILLISECONDS.convert(heartbeatConnectRetryFrequency, timeUnit);
            return this;
        }
        
        public Builder heartbeatSocketSettings(final SocketSettings heartbeatSocketSettings) {
            this.heartbeatSocketSettings = Assertions.notNull("heartbeatSocketSettings", heartbeatSocketSettings);
            return this;
        }
        
        public ServerSettings build() {
            return new ServerSettings(this);
        }
    }
}
