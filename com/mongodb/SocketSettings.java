package com.mongodb;

import javax.net.*;
import java.util.concurrent.*;
import org.bson.util.*;

final class SocketSettings
{
    private final long connectTimeoutMS;
    private final long readTimeoutMS;
    private final SocketFactory socketFactory;
    
    public static Builder builder() {
        return new Builder();
    }
    
    public int getConnectTimeout(final TimeUnit timeUnit) {
        return (int)timeUnit.convert(this.connectTimeoutMS, TimeUnit.MILLISECONDS);
    }
    
    public int getReadTimeout(final TimeUnit timeUnit) {
        return (int)timeUnit.convert(this.readTimeoutMS, TimeUnit.MILLISECONDS);
    }
    
    public SocketFactory getSocketFactory() {
        return this.socketFactory;
    }
    
    SocketSettings(final Builder builder) {
        super();
        this.connectTimeoutMS = builder.connectTimeoutMS;
        this.readTimeoutMS = builder.readTimeoutMS;
        this.socketFactory = builder.socketFactory;
    }
    
    static class Builder
    {
        private long connectTimeoutMS;
        private long readTimeoutMS;
        private SocketFactory socketFactory;
        
        Builder() {
            super();
            this.socketFactory = SocketFactory.getDefault();
        }
        
        public Builder connectTimeout(final int connectTimeout, final TimeUnit timeUnit) {
            this.connectTimeoutMS = TimeUnit.MILLISECONDS.convert(connectTimeout, timeUnit);
            return this;
        }
        
        public Builder readTimeout(final int readTimeout, final TimeUnit timeUnit) {
            this.readTimeoutMS = TimeUnit.MILLISECONDS.convert(readTimeout, timeUnit);
            return this;
        }
        
        public Builder socketFactory(final SocketFactory socketFactory) {
            this.socketFactory = Assertions.notNull("socketFactory", socketFactory);
            return this;
        }
        
        public SocketSettings build() {
            return new SocketSettings(this);
        }
    }
}
