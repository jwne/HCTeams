package com.mongodb;

import org.bson.util.annotations.*;
import javax.net.*;

@Immutable
public class MongoClientOptions
{
    private final String description;
    private final int minConnectionsPerHost;
    private final int connectionsPerHost;
    private final int threadsAllowedToBlockForConnectionMultiplier;
    private final int maxWaitTime;
    private final int maxConnectionIdleTime;
    private final int maxConnectionLifeTime;
    private final int connectTimeout;
    private final int socketTimeout;
    private final boolean socketKeepAlive;
    private final boolean autoConnectRetry;
    private final long maxAutoConnectRetryTime;
    private final ReadPreference readPreference;
    private final DBDecoderFactory dbDecoderFactory;
    private final DBEncoderFactory dbEncoderFactory;
    private final WriteConcern writeConcern;
    private final SocketFactory socketFactory;
    private final boolean cursorFinalizerEnabled;
    private final boolean alwaysUseMBeans;
    private final int heartbeatFrequency;
    private final int minHeartbeatFrequency;
    private final int heartbeatConnectTimeout;
    private final int heartbeatSocketTimeout;
    private final int heartbeatThreadCount;
    private final int acceptableLatencyDifference;
    private final String requiredReplicaSetName;
    
    public static Builder builder() {
        return new Builder();
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public int getMinConnectionsPerHost() {
        return this.minConnectionsPerHost;
    }
    
    public int getConnectionsPerHost() {
        return this.connectionsPerHost;
    }
    
    public int getThreadsAllowedToBlockForConnectionMultiplier() {
        return this.threadsAllowedToBlockForConnectionMultiplier;
    }
    
    public int getMaxWaitTime() {
        return this.maxWaitTime;
    }
    
    public int getMaxConnectionIdleTime() {
        return this.maxConnectionIdleTime;
    }
    
    public int getMaxConnectionLifeTime() {
        return this.maxConnectionLifeTime;
    }
    
    public int getConnectTimeout() {
        return this.connectTimeout;
    }
    
    public int getSocketTimeout() {
        return this.socketTimeout;
    }
    
    public boolean isSocketKeepAlive() {
        return this.socketKeepAlive;
    }
    
    @Deprecated
    public boolean isAutoConnectRetry() {
        return this.autoConnectRetry;
    }
    
    @Deprecated
    public long getMaxAutoConnectRetryTime() {
        return this.maxAutoConnectRetryTime;
    }
    
    public ReadPreference getReadPreference() {
        return this.readPreference;
    }
    
    public DBDecoderFactory getDbDecoderFactory() {
        return this.dbDecoderFactory;
    }
    
    public DBEncoderFactory getDbEncoderFactory() {
        return this.dbEncoderFactory;
    }
    
    public WriteConcern getWriteConcern() {
        return this.writeConcern;
    }
    
    public SocketFactory getSocketFactory() {
        return this.socketFactory;
    }
    
    public boolean isCursorFinalizerEnabled() {
        return this.cursorFinalizerEnabled;
    }
    
    public boolean isAlwaysUseMBeans() {
        return this.alwaysUseMBeans;
    }
    
    public int getHeartbeatFrequency() {
        return this.heartbeatFrequency;
    }
    
    public int getMinHeartbeatFrequency() {
        return this.minHeartbeatFrequency;
    }
    
    @Deprecated
    public int getHeartbeatConnectRetryFrequency() {
        return this.minHeartbeatFrequency;
    }
    
    public int getHeartbeatConnectTimeout() {
        return this.heartbeatConnectTimeout;
    }
    
    public int getHeartbeatSocketTimeout() {
        return this.heartbeatSocketTimeout;
    }
    
    @Deprecated
    public int getHeartbeatThreadCount() {
        return this.heartbeatThreadCount;
    }
    
    public int getAcceptableLatencyDifference() {
        return this.acceptableLatencyDifference;
    }
    
    public String getRequiredReplicaSetName() {
        return this.requiredReplicaSetName;
    }
    
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final MongoClientOptions that = (MongoClientOptions)o;
        if (this.acceptableLatencyDifference != that.acceptableLatencyDifference) {
            return false;
        }
        if (this.alwaysUseMBeans != that.alwaysUseMBeans) {
            return false;
        }
        if (this.autoConnectRetry != that.autoConnectRetry) {
            return false;
        }
        if (this.connectTimeout != that.connectTimeout) {
            return false;
        }
        if (this.connectionsPerHost != that.connectionsPerHost) {
            return false;
        }
        if (this.cursorFinalizerEnabled != that.cursorFinalizerEnabled) {
            return false;
        }
        if (this.minHeartbeatFrequency != that.minHeartbeatFrequency) {
            return false;
        }
        if (this.heartbeatConnectTimeout != that.heartbeatConnectTimeout) {
            return false;
        }
        if (this.heartbeatFrequency != that.heartbeatFrequency) {
            return false;
        }
        if (this.heartbeatSocketTimeout != that.heartbeatSocketTimeout) {
            return false;
        }
        if (this.heartbeatThreadCount != that.heartbeatThreadCount) {
            return false;
        }
        if (this.maxAutoConnectRetryTime != that.maxAutoConnectRetryTime) {
            return false;
        }
        if (this.maxConnectionIdleTime != that.maxConnectionIdleTime) {
            return false;
        }
        if (this.maxConnectionLifeTime != that.maxConnectionLifeTime) {
            return false;
        }
        if (this.maxWaitTime != that.maxWaitTime) {
            return false;
        }
        if (this.minConnectionsPerHost != that.minConnectionsPerHost) {
            return false;
        }
        if (this.socketKeepAlive != that.socketKeepAlive) {
            return false;
        }
        if (this.socketTimeout != that.socketTimeout) {
            return false;
        }
        if (this.threadsAllowedToBlockForConnectionMultiplier != that.threadsAllowedToBlockForConnectionMultiplier) {
            return false;
        }
        if (!this.dbDecoderFactory.equals(that.dbDecoderFactory)) {
            return false;
        }
        if (!this.dbEncoderFactory.equals(that.dbEncoderFactory)) {
            return false;
        }
        Label_0342: {
            if (this.description != null) {
                if (this.description.equals(that.description)) {
                    break Label_0342;
                }
            }
            else if (that.description == null) {
                break Label_0342;
            }
            return false;
        }
        if (!this.readPreference.equals(that.readPreference)) {
            return false;
        }
        if (!this.socketFactory.getClass().equals(that.socketFactory.getClass())) {
            return false;
        }
        if (!this.writeConcern.equals(that.writeConcern)) {
            return false;
        }
        if (this.requiredReplicaSetName != null) {
            if (this.requiredReplicaSetName.equals(that.requiredReplicaSetName)) {
                return true;
            }
        }
        else if (that.requiredReplicaSetName == null) {
            return true;
        }
        return false;
    }
    
    public int hashCode() {
        int result = (this.description != null) ? this.description.hashCode() : 0;
        result = 31 * result + this.minConnectionsPerHost;
        result = 31 * result + this.connectionsPerHost;
        result = 31 * result + this.threadsAllowedToBlockForConnectionMultiplier;
        result = 31 * result + this.maxWaitTime;
        result = 31 * result + this.maxConnectionIdleTime;
        result = 31 * result + this.maxConnectionLifeTime;
        result = 31 * result + this.connectTimeout;
        result = 31 * result + this.socketTimeout;
        result = 31 * result + (this.socketKeepAlive ? 1 : 0);
        result = 31 * result + (this.autoConnectRetry ? 1 : 0);
        result = 31 * result + (int)(this.maxAutoConnectRetryTime ^ this.maxAutoConnectRetryTime >>> 32);
        result = 31 * result + this.readPreference.hashCode();
        result = 31 * result + this.dbDecoderFactory.hashCode();
        result = 31 * result + this.dbEncoderFactory.hashCode();
        result = 31 * result + this.writeConcern.hashCode();
        result = 31 * result + ((this.socketFactory != null) ? this.socketFactory.getClass().hashCode() : 0);
        result = 31 * result + (this.cursorFinalizerEnabled ? 1 : 0);
        result = 31 * result + (this.alwaysUseMBeans ? 1 : 0);
        result = 31 * result + this.heartbeatFrequency;
        result = 31 * result + this.minHeartbeatFrequency;
        result = 31 * result + this.heartbeatConnectTimeout;
        result = 31 * result + this.heartbeatSocketTimeout;
        result = 31 * result + this.heartbeatThreadCount;
        result = 31 * result + this.acceptableLatencyDifference;
        result = 31 * result + ((this.requiredReplicaSetName != null) ? this.requiredReplicaSetName.hashCode() : 0);
        return result;
    }
    
    public String toString() {
        return "MongoClientOptions{description='" + this.description + '\'' + ", connectionsPerHost=" + this.connectionsPerHost + ", threadsAllowedToBlockForConnectionMultiplier=" + this.threadsAllowedToBlockForConnectionMultiplier + ", maxWaitTime=" + this.maxWaitTime + ", connectTimeout=" + this.connectTimeout + ", socketTimeout=" + this.socketTimeout + ", socketKeepAlive=" + this.socketKeepAlive + ", autoConnectRetry=" + this.autoConnectRetry + ", maxAutoConnectRetryTime=" + this.maxAutoConnectRetryTime + ", readPreference=" + this.readPreference + ", dbDecoderFactory=" + this.dbDecoderFactory + ", dbEncoderFactory=" + this.dbEncoderFactory + ", writeConcern=" + this.writeConcern + ", socketFactory=" + this.socketFactory + ", cursorFinalizerEnabled=" + this.cursorFinalizerEnabled + ", alwaysUseMBeans=" + this.alwaysUseMBeans + ", heartbeatFrequency=" + this.heartbeatFrequency + ", minHeartbeatFrequency=" + this.minHeartbeatFrequency + ", heartbeatConnectTimeout=" + this.heartbeatConnectTimeout + ", heartbeatSocketTimeout=" + this.heartbeatSocketTimeout + ", heartbeatThreadCount=" + this.heartbeatThreadCount + ", acceptableLatencyDifference=" + this.acceptableLatencyDifference + ", requiredReplicaSetName=" + this.requiredReplicaSetName + '}';
    }
    
    private MongoClientOptions(final Builder builder) {
        super();
        this.description = builder.description;
        this.minConnectionsPerHost = builder.minConnectionsPerHost;
        this.connectionsPerHost = builder.connectionsPerHost;
        this.threadsAllowedToBlockForConnectionMultiplier = builder.threadsAllowedToBlockForConnectionMultiplier;
        this.maxWaitTime = builder.maxWaitTime;
        this.maxConnectionIdleTime = builder.maxConnectionIdleTime;
        this.maxConnectionLifeTime = builder.maxConnectionLifeTime;
        this.connectTimeout = builder.connectTimeout;
        this.socketTimeout = builder.socketTimeout;
        this.autoConnectRetry = builder.autoConnectRetry;
        this.socketKeepAlive = builder.socketKeepAlive;
        this.maxAutoConnectRetryTime = builder.maxAutoConnectRetryTime;
        this.readPreference = builder.readPreference;
        this.dbDecoderFactory = builder.dbDecoderFactory;
        this.dbEncoderFactory = builder.dbEncoderFactory;
        this.writeConcern = builder.writeConcern;
        this.socketFactory = builder.socketFactory;
        this.cursorFinalizerEnabled = builder.cursorFinalizerEnabled;
        this.alwaysUseMBeans = builder.alwaysUseMBeans;
        this.heartbeatFrequency = builder.heartbeatFrequency;
        this.minHeartbeatFrequency = builder.minHeartbeatFrequency;
        this.heartbeatConnectTimeout = builder.heartbeatConnectTimeout;
        this.heartbeatSocketTimeout = builder.heartbeatSocketTimeout;
        this.heartbeatThreadCount = builder.heartbeatThreadCount;
        this.acceptableLatencyDifference = builder.acceptableLatencyDifference;
        this.requiredReplicaSetName = builder.requiredReplicaSetName;
    }
    
    public static class Builder
    {
        private String description;
        private int minConnectionsPerHost;
        private int connectionsPerHost;
        private int threadsAllowedToBlockForConnectionMultiplier;
        private int maxWaitTime;
        private int maxConnectionIdleTime;
        private int maxConnectionLifeTime;
        private int connectTimeout;
        private int socketTimeout;
        private boolean socketKeepAlive;
        private boolean autoConnectRetry;
        private long maxAutoConnectRetryTime;
        private ReadPreference readPreference;
        private DBDecoderFactory dbDecoderFactory;
        private DBEncoderFactory dbEncoderFactory;
        private WriteConcern writeConcern;
        private SocketFactory socketFactory;
        private boolean cursorFinalizerEnabled;
        private boolean alwaysUseMBeans;
        private int heartbeatFrequency;
        private int minHeartbeatFrequency;
        private int heartbeatConnectTimeout;
        private int heartbeatSocketTimeout;
        private int heartbeatThreadCount;
        private int acceptableLatencyDifference;
        private String requiredReplicaSetName;
        
        public Builder() {
            super();
            this.connectionsPerHost = 100;
            this.threadsAllowedToBlockForConnectionMultiplier = 5;
            this.maxWaitTime = 120000;
            this.connectTimeout = 10000;
            this.socketTimeout = 0;
            this.socketKeepAlive = false;
            this.autoConnectRetry = false;
            this.maxAutoConnectRetryTime = 0L;
            this.readPreference = ReadPreference.primary();
            this.dbDecoderFactory = DefaultDBDecoder.FACTORY;
            this.dbEncoderFactory = DefaultDBEncoder.FACTORY;
            this.writeConcern = WriteConcern.ACKNOWLEDGED;
            this.socketFactory = SocketFactory.getDefault();
            this.cursorFinalizerEnabled = true;
            this.alwaysUseMBeans = false;
            this.heartbeatFrequency = Integer.parseInt(System.getProperty("com.mongodb.updaterIntervalMS", "5000"));
            this.minHeartbeatFrequency = Integer.parseInt(System.getProperty("com.mongodb.updaterIntervalNoMasterMS", "10"));
            this.heartbeatConnectTimeout = Integer.parseInt(System.getProperty("com.mongodb.updaterConnectTimeoutMS", "20000"));
            this.heartbeatSocketTimeout = Integer.parseInt(System.getProperty("com.mongodb.updaterSocketTimeoutMS", "20000"));
            this.acceptableLatencyDifference = Integer.parseInt(System.getProperty("com.mongodb.slaveAcceptableLatencyMS", "15"));
        }
        
        public Builder heartbeatFrequency(final int heartbeatFrequency) {
            if (heartbeatFrequency < 1) {
                throw new IllegalArgumentException("heartbeatFrequency must be greater than 0");
            }
            this.heartbeatFrequency = heartbeatFrequency;
            return this;
        }
        
        public Builder minHeartbeatFrequency(final int minHeartbeatFrequency) {
            if (minHeartbeatFrequency < 1) {
                throw new IllegalArgumentException("minHeartbeatFrequency must be greater than 0");
            }
            this.minHeartbeatFrequency = minHeartbeatFrequency;
            return this;
        }
        
        @Deprecated
        public Builder heartbeatConnectRetryFrequency(final int minHeartbeatFrequency) {
            return this.minHeartbeatFrequency(minHeartbeatFrequency);
        }
        
        public Builder heartbeatConnectTimeout(final int heartbeatConnectTimeout) {
            if (heartbeatConnectTimeout < 0) {
                throw new IllegalArgumentException("heartbeatConnectTimeout must be greater than or equal to 0");
            }
            this.heartbeatConnectTimeout = heartbeatConnectTimeout;
            return this;
        }
        
        public Builder heartbeatSocketTimeout(final int heartbeatSocketTimeout) {
            if (heartbeatSocketTimeout < 0) {
                throw new IllegalArgumentException("heartbeatSocketTimeout must be greater than or equal to 0");
            }
            this.heartbeatSocketTimeout = heartbeatSocketTimeout;
            return this;
        }
        
        @Deprecated
        public Builder heartbeatThreadCount(final int heartbeatThreadCount) {
            if (heartbeatThreadCount < 1) {
                throw new IllegalArgumentException("heartbeatThreadCount must be greater than 0");
            }
            this.heartbeatThreadCount = heartbeatThreadCount;
            return this;
        }
        
        public Builder acceptableLatencyDifference(final int acceptableLatencyDifference) {
            if (acceptableLatencyDifference < 0) {
                throw new IllegalArgumentException("acceptableLatencyDifference must be greater than or equal to 0");
            }
            this.acceptableLatencyDifference = acceptableLatencyDifference;
            return this;
        }
        
        public Builder description(final String description) {
            this.description = description;
            return this;
        }
        
        public Builder minConnectionsPerHost(final int minConnectionsPerHost) {
            if (minConnectionsPerHost < 0) {
                throw new IllegalArgumentException("Minimum value is 0");
            }
            this.minConnectionsPerHost = minConnectionsPerHost;
            return this;
        }
        
        public Builder connectionsPerHost(final int connectionsPerHost) {
            if (connectionsPerHost < 1) {
                throw new IllegalArgumentException("Minimum value is 1");
            }
            this.connectionsPerHost = connectionsPerHost;
            return this;
        }
        
        public Builder threadsAllowedToBlockForConnectionMultiplier(final int threadsAllowedToBlockForConnectionMultiplier) {
            if (threadsAllowedToBlockForConnectionMultiplier < 1) {
                throw new IllegalArgumentException("Minimum value is 1");
            }
            this.threadsAllowedToBlockForConnectionMultiplier = threadsAllowedToBlockForConnectionMultiplier;
            return this;
        }
        
        public Builder maxWaitTime(final int maxWaitTime) {
            if (maxWaitTime < 0) {
                throw new IllegalArgumentException("Minimum value is 0");
            }
            this.maxWaitTime = maxWaitTime;
            return this;
        }
        
        public Builder maxConnectionIdleTime(final int maxConnectionIdleTime) {
            if (maxConnectionIdleTime < 0) {
                throw new IllegalArgumentException("Minimum value is 0");
            }
            this.maxConnectionIdleTime = maxConnectionIdleTime;
            return this;
        }
        
        public Builder maxConnectionLifeTime(final int maxConnectionLifeTime) {
            if (maxConnectionLifeTime < 0) {
                throw new IllegalArgumentException("Minimum value is 0");
            }
            this.maxConnectionLifeTime = maxConnectionLifeTime;
            return this;
        }
        
        public Builder connectTimeout(final int connectTimeout) {
            if (connectTimeout < 0) {
                throw new IllegalArgumentException("Minimum value is 0");
            }
            this.connectTimeout = connectTimeout;
            return this;
        }
        
        public Builder socketTimeout(final int socketTimeout) {
            if (socketTimeout < 0) {
                throw new IllegalArgumentException("Minimum value is 0");
            }
            this.socketTimeout = socketTimeout;
            return this;
        }
        
        public Builder socketKeepAlive(final boolean socketKeepAlive) {
            this.socketKeepAlive = socketKeepAlive;
            return this;
        }
        
        @Deprecated
        public Builder autoConnectRetry(final boolean autoConnectRetry) {
            this.autoConnectRetry = autoConnectRetry;
            return this;
        }
        
        @Deprecated
        public Builder maxAutoConnectRetryTime(final long maxAutoConnectRetryTime) {
            if (maxAutoConnectRetryTime < 0L) {
                throw new IllegalArgumentException("Minimum value is 0");
            }
            this.maxAutoConnectRetryTime = maxAutoConnectRetryTime;
            return this;
        }
        
        public Builder readPreference(final ReadPreference readPreference) {
            if (readPreference == null) {
                throw new IllegalArgumentException("null is not a legal value");
            }
            this.readPreference = readPreference;
            return this;
        }
        
        public Builder dbDecoderFactory(final DBDecoderFactory dbDecoderFactory) {
            if (dbDecoderFactory == null) {
                throw new IllegalArgumentException("null is not a legal value");
            }
            this.dbDecoderFactory = dbDecoderFactory;
            return this;
        }
        
        public Builder dbEncoderFactory(final DBEncoderFactory dbEncoderFactory) {
            if (dbEncoderFactory == null) {
                throw new IllegalArgumentException("null is not a legal value");
            }
            this.dbEncoderFactory = dbEncoderFactory;
            return this;
        }
        
        public Builder writeConcern(final WriteConcern writeConcern) {
            if (writeConcern == null) {
                throw new IllegalArgumentException("null is not a legal value");
            }
            this.writeConcern = writeConcern;
            return this;
        }
        
        public Builder socketFactory(final SocketFactory socketFactory) {
            if (socketFactory == null) {
                throw new IllegalArgumentException("null is not a legal value");
            }
            this.socketFactory = socketFactory;
            return this;
        }
        
        public Builder cursorFinalizerEnabled(final boolean cursorFinalizerEnabled) {
            this.cursorFinalizerEnabled = cursorFinalizerEnabled;
            return this;
        }
        
        public Builder alwaysUseMBeans(final boolean alwaysUseMBeans) {
            this.alwaysUseMBeans = alwaysUseMBeans;
            return this;
        }
        
        public Builder requiredReplicaSetName(final String requiredReplicaSetName) {
            this.requiredReplicaSetName = requiredReplicaSetName;
            return this;
        }
        
        public Builder legacyDefaults() {
            this.connectionsPerHost = 10;
            this.writeConcern = WriteConcern.NORMAL;
            return this;
        }
        
        public MongoClientOptions build() {
            return new MongoClientOptions(this, null);
        }
    }
}
