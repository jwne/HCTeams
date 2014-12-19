package com.mongodb;

import javax.net.*;

@Deprecated
public class MongoOptions
{
    public String description;
    public int connectionsPerHost;
    public int threadsAllowedToBlockForConnectionMultiplier;
    public int maxWaitTime;
    public int connectTimeout;
    public int socketTimeout;
    public boolean socketKeepAlive;
    @Deprecated
    public boolean autoConnectRetry;
    @Deprecated
    public long maxAutoConnectRetryTime;
    @Deprecated
    public boolean slaveOk;
    public ReadPreference readPreference;
    public DBDecoderFactory dbDecoderFactory;
    public DBEncoderFactory dbEncoderFactory;
    public boolean safe;
    public int w;
    public int wtimeout;
    public boolean fsync;
    public boolean j;
    public SocketFactory socketFactory;
    public boolean cursorFinalizerEnabled;
    public WriteConcern writeConcern;
    public boolean alwaysUseMBeans;
    int minConnectionsPerHost;
    int maxConnectionIdleTime;
    int maxConnectionLifeTime;
    int heartbeatFrequencyMS;
    int heartbeatConnectRetryFrequencyMS;
    int heartbeatConnectTimeoutMS;
    int heartbeatReadTimeoutMS;
    int acceptableLatencyDifferenceMS;
    int heartbeatThreadCount;
    String requiredReplicaSetName;
    
    public MongoOptions() {
        super();
        this.reset();
    }
    
    public MongoOptions(final MongoClientOptions options) {
        super();
        this.connectionsPerHost = options.getConnectionsPerHost();
        this.threadsAllowedToBlockForConnectionMultiplier = options.getThreadsAllowedToBlockForConnectionMultiplier();
        this.maxWaitTime = options.getMaxWaitTime();
        this.connectTimeout = options.getConnectTimeout();
        this.socketTimeout = options.getSocketTimeout();
        this.socketKeepAlive = options.isSocketKeepAlive();
        this.autoConnectRetry = options.isAutoConnectRetry();
        this.maxAutoConnectRetryTime = options.getMaxAutoConnectRetryTime();
        this.readPreference = options.getReadPreference();
        this.dbDecoderFactory = options.getDbDecoderFactory();
        this.dbEncoderFactory = options.getDbEncoderFactory();
        this.socketFactory = options.getSocketFactory();
        this.description = options.getDescription();
        this.cursorFinalizerEnabled = options.isCursorFinalizerEnabled();
        this.writeConcern = options.getWriteConcern();
        this.slaveOk = false;
        this.alwaysUseMBeans = options.isAlwaysUseMBeans();
        this.minConnectionsPerHost = options.getMinConnectionsPerHost();
        this.maxConnectionIdleTime = options.getMaxConnectionIdleTime();
        this.maxConnectionLifeTime = options.getMaxConnectionLifeTime();
        this.heartbeatFrequencyMS = options.getHeartbeatFrequency();
        this.heartbeatConnectRetryFrequencyMS = options.getHeartbeatConnectRetryFrequency();
        this.heartbeatConnectTimeoutMS = options.getHeartbeatConnectTimeout();
        this.heartbeatReadTimeoutMS = options.getHeartbeatSocketTimeout();
        this.heartbeatThreadCount = options.getHeartbeatThreadCount();
        this.acceptableLatencyDifferenceMS = options.getAcceptableLatencyDifference();
        this.requiredReplicaSetName = options.getRequiredReplicaSetName();
    }
    
    public void reset() {
        this.connectionsPerHost = Bytes.CONNECTIONS_PER_HOST;
        this.threadsAllowedToBlockForConnectionMultiplier = 5;
        this.maxWaitTime = 120000;
        this.connectTimeout = 10000;
        this.socketTimeout = 0;
        this.socketKeepAlive = false;
        this.autoConnectRetry = false;
        this.maxAutoConnectRetryTime = 0L;
        this.slaveOk = false;
        this.readPreference = null;
        this.writeConcern = null;
        this.safe = false;
        this.w = 0;
        this.wtimeout = 0;
        this.fsync = false;
        this.j = false;
        this.dbDecoderFactory = DefaultDBDecoder.FACTORY;
        this.dbEncoderFactory = DefaultDBEncoder.FACTORY;
        this.socketFactory = SocketFactory.getDefault();
        this.description = null;
        this.cursorFinalizerEnabled = true;
        this.alwaysUseMBeans = false;
        this.minConnectionsPerHost = 0;
        this.maxConnectionIdleTime = 0;
        this.maxConnectionLifeTime = 0;
        this.heartbeatFrequencyMS = Integer.parseInt(System.getProperty("com.mongodb.updaterIntervalMS", "5000"));
        this.heartbeatConnectRetryFrequencyMS = Integer.parseInt(System.getProperty("com.mongodb.updaterIntervalNoMasterMS", "10"));
        this.heartbeatConnectTimeoutMS = Integer.parseInt(System.getProperty("com.mongodb.updaterConnectTimeoutMS", "20000"));
        this.heartbeatReadTimeoutMS = Integer.parseInt(System.getProperty("com.mongodb.updaterSocketTimeoutMS", "20000"));
        this.heartbeatThreadCount = 0;
        this.acceptableLatencyDifferenceMS = Integer.parseInt(System.getProperty("com.mongodb.slaveAcceptableLatencyMS", "15"));
        this.requiredReplicaSetName = null;
    }
    
    public MongoOptions copy() {
        final MongoOptions m = new MongoOptions();
        m.connectionsPerHost = this.connectionsPerHost;
        m.threadsAllowedToBlockForConnectionMultiplier = this.threadsAllowedToBlockForConnectionMultiplier;
        m.maxWaitTime = this.maxWaitTime;
        m.connectTimeout = this.connectTimeout;
        m.socketTimeout = this.socketTimeout;
        m.socketKeepAlive = this.socketKeepAlive;
        m.autoConnectRetry = this.autoConnectRetry;
        m.maxAutoConnectRetryTime = this.maxAutoConnectRetryTime;
        m.slaveOk = this.slaveOk;
        m.readPreference = this.readPreference;
        m.writeConcern = this.writeConcern;
        m.safe = this.safe;
        m.w = this.w;
        m.wtimeout = this.wtimeout;
        m.fsync = this.fsync;
        m.j = this.j;
        m.dbDecoderFactory = this.dbDecoderFactory;
        m.dbEncoderFactory = this.dbEncoderFactory;
        m.socketFactory = this.socketFactory;
        m.description = this.description;
        m.cursorFinalizerEnabled = this.cursorFinalizerEnabled;
        m.alwaysUseMBeans = this.alwaysUseMBeans;
        m.minConnectionsPerHost = this.minConnectionsPerHost;
        m.maxConnectionIdleTime = this.maxConnectionIdleTime;
        m.maxConnectionLifeTime = this.maxConnectionLifeTime;
        m.heartbeatFrequencyMS = this.heartbeatFrequencyMS;
        m.heartbeatConnectRetryFrequencyMS = this.heartbeatConnectRetryFrequencyMS;
        m.heartbeatConnectTimeoutMS = this.heartbeatConnectTimeoutMS;
        m.heartbeatReadTimeoutMS = this.heartbeatReadTimeoutMS;
        m.heartbeatThreadCount = this.heartbeatThreadCount;
        m.acceptableLatencyDifferenceMS = this.acceptableLatencyDifferenceMS;
        m.requiredReplicaSetName = this.requiredReplicaSetName;
        return m;
    }
    
    public WriteConcern getWriteConcern() {
        if (this.writeConcern != null) {
            return this.writeConcern;
        }
        if (this.w != 0 || this.wtimeout != 0 || (this.fsync | this.j)) {
            return new WriteConcern(this.w, this.wtimeout, this.fsync, this.j);
        }
        if (this.safe) {
            return WriteConcern.SAFE;
        }
        return WriteConcern.NORMAL;
    }
    
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final MongoOptions options = (MongoOptions)o;
        if (this.acceptableLatencyDifferenceMS != options.acceptableLatencyDifferenceMS) {
            return false;
        }
        if (this.alwaysUseMBeans != options.alwaysUseMBeans) {
            return false;
        }
        if (this.autoConnectRetry != options.autoConnectRetry) {
            return false;
        }
        if (this.connectTimeout != options.connectTimeout) {
            return false;
        }
        if (this.connectionsPerHost != options.connectionsPerHost) {
            return false;
        }
        if (this.cursorFinalizerEnabled != options.cursorFinalizerEnabled) {
            return false;
        }
        if (this.fsync != options.fsync) {
            return false;
        }
        if (this.heartbeatConnectRetryFrequencyMS != options.heartbeatConnectRetryFrequencyMS) {
            return false;
        }
        if (this.heartbeatConnectTimeoutMS != options.heartbeatConnectTimeoutMS) {
            return false;
        }
        if (this.heartbeatFrequencyMS != options.heartbeatFrequencyMS) {
            return false;
        }
        if (this.heartbeatReadTimeoutMS != options.heartbeatReadTimeoutMS) {
            return false;
        }
        if (this.heartbeatThreadCount != options.heartbeatThreadCount) {
            return false;
        }
        if (this.j != options.j) {
            return false;
        }
        if (this.maxAutoConnectRetryTime != options.maxAutoConnectRetryTime) {
            return false;
        }
        if (this.maxWaitTime != options.maxWaitTime) {
            return false;
        }
        if (this.safe != options.safe) {
            return false;
        }
        if (this.slaveOk != options.slaveOk) {
            return false;
        }
        if (this.socketKeepAlive != options.socketKeepAlive) {
            return false;
        }
        if (this.socketTimeout != options.socketTimeout) {
            return false;
        }
        if (this.threadsAllowedToBlockForConnectionMultiplier != options.threadsAllowedToBlockForConnectionMultiplier) {
            return false;
        }
        if (this.w != options.w) {
            return false;
        }
        if (this.wtimeout != options.wtimeout) {
            return false;
        }
        if (!this.dbDecoderFactory.equals(options.dbDecoderFactory)) {
            return false;
        }
        if (!this.dbEncoderFactory.equals(options.dbEncoderFactory)) {
            return false;
        }
        Label_0381: {
            if (this.description != null) {
                if (this.description.equals(options.description)) {
                    break Label_0381;
                }
            }
            else if (options.description == null) {
                break Label_0381;
            }
            return false;
        }
        Label_0414: {
            if (this.readPreference != null) {
                if (this.readPreference.equals(options.readPreference)) {
                    break Label_0414;
                }
            }
            else if (options.readPreference == null) {
                break Label_0414;
            }
            return false;
        }
        if (!this.socketFactory.equals(options.socketFactory)) {
            return false;
        }
        Label_0463: {
            if (this.writeConcern != null) {
                if (this.writeConcern.equals(options.writeConcern)) {
                    break Label_0463;
                }
            }
            else if (options.writeConcern == null) {
                break Label_0463;
            }
            return false;
        }
        if (this.requiredReplicaSetName != null) {
            if (this.requiredReplicaSetName.equals(options.requiredReplicaSetName)) {
                return true;
            }
        }
        else if (options.requiredReplicaSetName == null) {
            return true;
        }
        return false;
    }
    
    public int hashCode() {
        int result = (this.description != null) ? this.description.hashCode() : 0;
        result = 31 * result + this.connectionsPerHost;
        result = 31 * result + this.threadsAllowedToBlockForConnectionMultiplier;
        result = 31 * result + this.maxWaitTime;
        result = 31 * result + this.connectTimeout;
        result = 31 * result + this.socketTimeout;
        result = 31 * result + (this.socketKeepAlive ? 1 : 0);
        result = 31 * result + (this.autoConnectRetry ? 1 : 0);
        result = 31 * result + (int)(this.maxAutoConnectRetryTime ^ this.maxAutoConnectRetryTime >>> 32);
        result = 31 * result + (this.slaveOk ? 1 : 0);
        result = 31 * result + ((this.readPreference != null) ? this.readPreference.hashCode() : 0);
        result = 31 * result + this.dbDecoderFactory.hashCode();
        result = 31 * result + this.dbEncoderFactory.hashCode();
        result = 31 * result + (this.safe ? 1 : 0);
        result = 31 * result + this.w;
        result = 31 * result + this.wtimeout;
        result = 31 * result + (this.fsync ? 1 : 0);
        result = 31 * result + (this.j ? 1 : 0);
        result = 31 * result + this.socketFactory.hashCode();
        result = 31 * result + (this.cursorFinalizerEnabled ? 1 : 0);
        result = 31 * result + ((this.writeConcern != null) ? this.writeConcern.hashCode() : 0);
        result = 31 * result + (this.alwaysUseMBeans ? 1 : 0);
        result = 31 * result + this.heartbeatFrequencyMS;
        result = 31 * result + this.heartbeatConnectRetryFrequencyMS;
        result = 31 * result + this.heartbeatConnectTimeoutMS;
        result = 31 * result + this.heartbeatReadTimeoutMS;
        result = 31 * result + this.acceptableLatencyDifferenceMS;
        result = 31 * result + this.heartbeatThreadCount;
        result = 31 * result + ((this.requiredReplicaSetName != null) ? this.requiredReplicaSetName.hashCode() : 0);
        return result;
    }
    
    public synchronized String getDescription() {
        return this.description;
    }
    
    public synchronized void setDescription(final String desc) {
        this.description = desc;
    }
    
    public synchronized int getConnectionsPerHost() {
        return this.connectionsPerHost;
    }
    
    public synchronized void setConnectionsPerHost(final int connections) {
        this.connectionsPerHost = connections;
    }
    
    public synchronized int getThreadsAllowedToBlockForConnectionMultiplier() {
        return this.threadsAllowedToBlockForConnectionMultiplier;
    }
    
    public synchronized void setThreadsAllowedToBlockForConnectionMultiplier(final int threads) {
        this.threadsAllowedToBlockForConnectionMultiplier = threads;
    }
    
    public synchronized int getMaxWaitTime() {
        return this.maxWaitTime;
    }
    
    public synchronized void setMaxWaitTime(final int timeMS) {
        this.maxWaitTime = timeMS;
    }
    
    public synchronized int getConnectTimeout() {
        return this.connectTimeout;
    }
    
    public synchronized void setConnectTimeout(final int timeoutMS) {
        this.connectTimeout = timeoutMS;
    }
    
    public synchronized int getSocketTimeout() {
        return this.socketTimeout;
    }
    
    public synchronized void setSocketTimeout(final int timeoutMS) {
        this.socketTimeout = timeoutMS;
    }
    
    public synchronized boolean isSocketKeepAlive() {
        return this.socketKeepAlive;
    }
    
    public synchronized void setSocketKeepAlive(final boolean keepAlive) {
        this.socketKeepAlive = keepAlive;
    }
    
    @Deprecated
    public synchronized boolean isAutoConnectRetry() {
        return this.autoConnectRetry;
    }
    
    @Deprecated
    public synchronized void setAutoConnectRetry(final boolean retry) {
        this.autoConnectRetry = retry;
    }
    
    @Deprecated
    public synchronized long getMaxAutoConnectRetryTime() {
        return this.maxAutoConnectRetryTime;
    }
    
    @Deprecated
    public synchronized void setMaxAutoConnectRetryTime(final long retryTimeMS) {
        this.maxAutoConnectRetryTime = retryTimeMS;
    }
    
    public synchronized DBDecoderFactory getDbDecoderFactory() {
        return this.dbDecoderFactory;
    }
    
    public synchronized void setDbDecoderFactory(final DBDecoderFactory factory) {
        this.dbDecoderFactory = factory;
    }
    
    public synchronized DBEncoderFactory getDbEncoderFactory() {
        return this.dbEncoderFactory;
    }
    
    public synchronized void setDbEncoderFactory(final DBEncoderFactory factory) {
        this.dbEncoderFactory = factory;
    }
    
    public synchronized boolean isSafe() {
        return this.safe;
    }
    
    public synchronized void setSafe(final boolean isSafe) {
        this.safe = isSafe;
    }
    
    public synchronized int getW() {
        return this.w;
    }
    
    public synchronized void setW(final int val) {
        this.w = val;
    }
    
    public synchronized int getWtimeout() {
        return this.wtimeout;
    }
    
    public synchronized void setWtimeout(final int timeoutMS) {
        this.wtimeout = timeoutMS;
    }
    
    public synchronized boolean isFsync() {
        return this.fsync;
    }
    
    public synchronized void setFsync(final boolean sync) {
        this.fsync = sync;
    }
    
    public synchronized boolean isJ() {
        return this.j;
    }
    
    public synchronized void setJ(final boolean safe) {
        this.j = safe;
    }
    
    public void setWriteConcern(final WriteConcern writeConcern) {
        this.writeConcern = writeConcern;
    }
    
    public synchronized SocketFactory getSocketFactory() {
        return this.socketFactory;
    }
    
    public synchronized void setSocketFactory(final SocketFactory factory) {
        this.socketFactory = factory;
    }
    
    public ReadPreference getReadPreference() {
        return this.readPreference;
    }
    
    public void setReadPreference(final ReadPreference readPreference) {
        this.readPreference = readPreference;
    }
    
    public boolean isCursorFinalizerEnabled() {
        return this.cursorFinalizerEnabled;
    }
    
    public void setCursorFinalizerEnabled(final boolean cursorFinalizerEnabled) {
        this.cursorFinalizerEnabled = cursorFinalizerEnabled;
    }
    
    public boolean isAlwaysUseMBeans() {
        return this.alwaysUseMBeans;
    }
    
    public void setAlwaysUseMBeans(final boolean alwaysUseMBeans) {
        this.alwaysUseMBeans = alwaysUseMBeans;
    }
    
    public String getRequiredReplicaSetName() {
        return this.requiredReplicaSetName;
    }
    
    public String toString() {
        return "MongoOptions{description='" + this.description + '\'' + ", connectionsPerHost=" + this.connectionsPerHost + ", threadsAllowedToBlockForConnectionMultiplier=" + this.threadsAllowedToBlockForConnectionMultiplier + ", maxWaitTime=" + this.maxWaitTime + ", connectTimeout=" + this.connectTimeout + ", socketTimeout=" + this.socketTimeout + ", socketKeepAlive=" + this.socketKeepAlive + ", autoConnectRetry=" + this.autoConnectRetry + ", maxAutoConnectRetryTime=" + this.maxAutoConnectRetryTime + ", slaveOk=" + this.slaveOk + ", readPreference=" + this.readPreference + ", dbDecoderFactory=" + this.dbDecoderFactory + ", dbEncoderFactory=" + this.dbEncoderFactory + ", safe=" + this.safe + ", w=" + this.w + ", wtimeout=" + this.wtimeout + ", fsync=" + this.fsync + ", j=" + this.j + ", socketFactory=" + this.socketFactory + ", cursorFinalizerEnabled=" + this.cursorFinalizerEnabled + ", writeConcern=" + this.writeConcern + ", alwaysUseMBeans=" + this.alwaysUseMBeans + ", requiredReplicaSetName=" + this.requiredReplicaSetName + '}';
    }
}
