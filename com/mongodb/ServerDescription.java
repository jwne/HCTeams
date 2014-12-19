package com.mongodb;

import org.bson.util.annotations.*;
import java.text.*;
import org.bson.util.*;
import java.util.*;
import java.util.concurrent.*;

@Immutable
class ServerDescription
{
    static final int MIN_DRIVER_WIRE_VERSION = 0;
    static final int MAX_DRIVER_WIRE_VERSION = 3;
    private static final int DEFAULT_MAX_DOCUMENT_SIZE = 16777216;
    private static final int DEFAULT_MAX_MESSAGE_SIZE = 33554432;
    private static final int DEFAULT_MAX_WRITE_BATCH_SIZE = 512;
    private final ServerAddress address;
    private final ServerType type;
    private final Set<String> hosts;
    private final Set<String> passives;
    private final Set<String> arbiters;
    private final String primary;
    private final int maxDocumentSize;
    private final int maxWriteBatchSize;
    private final int maxMessageSize;
    private final TagSet tagSet;
    private final String setName;
    private final long averageLatencyNanos;
    private final boolean ok;
    private final ServerConnectionState state;
    private final ServerVersion version;
    private final int minWireVersion;
    private final int maxWireVersion;
    private Throwable exception;
    
    public boolean isCompatibleWithDriver() {
        return !this.ok || (this.minWireVersion <= 3 && this.maxWireVersion >= 0);
    }
    
    public static int getDefaultMaxDocumentSize() {
        return 16777216;
    }
    
    public static int getDefaultMaxMessageSize() {
        return 33554432;
    }
    
    public static int getDefaultMinWireVersion() {
        return 0;
    }
    
    public static int getDefaultMaxWireVersion() {
        return 0;
    }
    
    public static int getDefaultMaxWriteBatchSize() {
        return 512;
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public ServerAddress getAddress() {
        return this.address;
    }
    
    public boolean isReplicaSetMember() {
        return this.type == ServerType.ReplicaSetPrimary || this.type == ServerType.ReplicaSetSecondary || this.type == ServerType.ReplicaSetArbiter || this.type == ServerType.ReplicaSetOther || this.type == ServerType.ReplicaSetGhost;
    }
    
    public boolean isShardRouter() {
        return this.type == ServerType.ShardRouter;
    }
    
    public boolean isStandAlone() {
        return this.type == ServerType.StandAlone;
    }
    
    public boolean isPrimary() {
        return this.ok && (this.type == ServerType.ReplicaSetPrimary || this.type == ServerType.ShardRouter || this.type == ServerType.StandAlone);
    }
    
    public boolean isSecondary() {
        return this.ok && (this.type == ServerType.ReplicaSetSecondary || this.type == ServerType.ShardRouter || this.type == ServerType.StandAlone);
    }
    
    public Set<String> getHosts() {
        return this.hosts;
    }
    
    public Set<String> getPassives() {
        return this.passives;
    }
    
    public Set<String> getArbiters() {
        return this.arbiters;
    }
    
    public String getPrimary() {
        return this.primary;
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
    
    public TagSet getTagSet() {
        return this.tagSet;
    }
    
    public int getMinWireVersion() {
        return this.minWireVersion;
    }
    
    public int getMaxWireVersion() {
        return this.maxWireVersion;
    }
    
    public boolean hasTags(final TagSet desiredTags) {
        return this.ok && (this.type == ServerType.StandAlone || this.type == ServerType.ShardRouter || this.tagSet.containsAll(desiredTags));
    }
    
    public String getSetName() {
        return this.setName;
    }
    
    public boolean isOk() {
        return this.ok;
    }
    
    public ServerConnectionState getState() {
        return this.state;
    }
    
    public ServerType getType() {
        return this.type;
    }
    
    public ClusterType getClusterType() {
        return this.type.getClusterType();
    }
    
    public ServerVersion getVersion() {
        return this.version;
    }
    
    public long getAverageLatencyNanos() {
        return this.averageLatencyNanos;
    }
    
    public Throwable getException() {
        return this.exception;
    }
    
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final ServerDescription that = (ServerDescription)o;
        if (this.maxDocumentSize != that.maxDocumentSize) {
            return false;
        }
        if (this.maxMessageSize != that.maxMessageSize) {
            return false;
        }
        if (this.maxWriteBatchSize != that.maxWriteBatchSize) {
            return false;
        }
        if (this.ok != that.ok) {
            return false;
        }
        if (!this.address.equals(that.address)) {
            return false;
        }
        if (!this.arbiters.equals(that.arbiters)) {
            return false;
        }
        if (!this.hosts.equals(that.hosts)) {
            return false;
        }
        if (!this.passives.equals(that.passives)) {
            return false;
        }
        Label_0184: {
            if (this.primary != null) {
                if (this.primary.equals(that.primary)) {
                    break Label_0184;
                }
            }
            else if (that.primary == null) {
                break Label_0184;
            }
            return false;
        }
        Label_0217: {
            if (this.setName != null) {
                if (this.setName.equals(that.setName)) {
                    break Label_0217;
                }
            }
            else if (that.setName == null) {
                break Label_0217;
            }
            return false;
        }
        if (this.state != that.state) {
            return false;
        }
        if (!this.tagSet.equals(that.tagSet)) {
            return false;
        }
        if (this.type != that.type) {
            return false;
        }
        if (!this.version.equals(that.version)) {
            return false;
        }
        if (this.minWireVersion != that.minWireVersion) {
            return false;
        }
        if (this.maxWireVersion != that.maxWireVersion) {
            return false;
        }
        final Class thisExceptionClass = (this.exception != null) ? this.exception.getClass() : null;
        final Class thatExceptionClass = (that.exception != null) ? that.exception.getClass() : null;
        Label_0363: {
            if (thisExceptionClass != null) {
                if (thisExceptionClass.equals(thatExceptionClass)) {
                    break Label_0363;
                }
            }
            else if (thatExceptionClass == null) {
                break Label_0363;
            }
            return false;
        }
        final String thisExceptionMessage = (this.exception != null) ? this.exception.getMessage() : null;
        final String thatExceptionMessage = (that.exception != null) ? that.exception.getMessage() : null;
        if (thisExceptionMessage != null) {
            if (thisExceptionMessage.equals(thatExceptionMessage)) {
                return true;
            }
        }
        else if (thatExceptionMessage == null) {
            return true;
        }
        return false;
    }
    
    public int hashCode() {
        int result = this.address.hashCode();
        result = 31 * result + this.type.hashCode();
        result = 31 * result + this.hosts.hashCode();
        result = 31 * result + this.passives.hashCode();
        result = 31 * result + this.arbiters.hashCode();
        result = 31 * result + ((this.primary != null) ? this.primary.hashCode() : 0);
        result = 31 * result + this.maxDocumentSize;
        result = 31 * result + this.maxMessageSize;
        result = 31 * result + this.maxWriteBatchSize;
        result = 31 * result + this.tagSet.hashCode();
        result = 31 * result + ((this.setName != null) ? this.setName.hashCode() : 0);
        result = 31 * result + (this.ok ? 1 : 0);
        result = 31 * result + this.state.hashCode();
        result = 31 * result + this.version.hashCode();
        result = 31 * result + this.minWireVersion;
        result = 31 * result + this.maxWireVersion;
        result = 31 * result + ((this.exception == null) ? 0 : this.exception.getClass().hashCode());
        result = 31 * result + ((this.exception == null) ? 0 : this.exception.getMessage().hashCode());
        return result;
    }
    
    public String toString() {
        return "ServerDescription{address=" + this.address + ", type=" + this.type + ", hosts=" + this.hosts + ", passives=" + this.passives + ", arbiters=" + this.arbiters + ", primary='" + this.primary + '\'' + ", maxDocumentSize=" + this.maxDocumentSize + ", maxMessageSize=" + this.maxMessageSize + ", maxWriteBatchSize=" + this.maxWriteBatchSize + ", tagSet=" + this.tagSet + ", setName='" + this.setName + '\'' + ", averageLatencyNanos=" + this.averageLatencyNanos + ", ok=" + this.ok + ", state=" + this.state + ", version=" + this.version + ", minWireVersion=" + this.minWireVersion + ", maxWireVersion=" + this.maxWireVersion + '}';
    }
    
    public String getShortDescription() {
        return "{address=" + this.address + ", type=" + this.type + (this.tagSet.iterator().hasNext() ? (", " + this.tagSet) : "") + ((this.state == ServerConnectionState.Connected) ? (", averageLatency=" + this.getAverageLatencyFormattedInMilliseconds() + " ms") : "") + ", state=" + this.state + ((this.exception == null) ? "" : (", exception=" + this.translateExceptionToString())) + '}';
    }
    
    private String translateExceptionToString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("{");
        builder.append(this.exception);
        builder.append("}");
        for (Throwable cur = this.exception.getCause(); cur != null; cur = cur.getCause()) {
            builder.append(", caused by ");
            builder.append("{");
            builder.append(cur);
            builder.append("}");
        }
        return builder.toString();
    }
    
    private String getAverageLatencyFormattedInMilliseconds() {
        return new DecimalFormat("#0.0").format(this.averageLatencyNanos / 1000.0 / 1000.0);
    }
    
    ServerDescription(final Builder builder) {
        super();
        this.address = Assertions.notNull("address", builder.address);
        this.type = Assertions.notNull("type", builder.type);
        this.state = Assertions.notNull("state", builder.state);
        this.version = Assertions.notNull("version", builder.version);
        this.hosts = builder.hosts;
        this.passives = builder.passives;
        this.arbiters = builder.arbiters;
        this.primary = builder.primary;
        this.maxDocumentSize = builder.maxDocumentSize;
        this.maxMessageSize = builder.maxMessageSize;
        this.maxWriteBatchSize = builder.maxWriteBatchSize;
        this.tagSet = builder.tags;
        this.setName = builder.setName;
        this.averageLatencyNanos = builder.averageLatency;
        this.ok = builder.ok;
        this.minWireVersion = builder.minWireVersion;
        this.maxWireVersion = builder.maxWireVersion;
        this.exception = builder.exception;
    }
    
    static class Builder
    {
        private ServerAddress address;
        private ServerType type;
        private Set<String> hosts;
        private Set<String> passives;
        private Set<String> arbiters;
        private String primary;
        private int maxDocumentSize;
        private int maxMessageSize;
        private int maxWriteBatchSize;
        private TagSet tags;
        private String setName;
        private long averageLatency;
        private boolean ok;
        private ServerConnectionState state;
        private ServerVersion version;
        private int minWireVersion;
        private int maxWireVersion;
        private Throwable exception;
        
        Builder() {
            super();
            this.type = ServerType.Unknown;
            this.hosts = Collections.emptySet();
            this.passives = Collections.emptySet();
            this.arbiters = Collections.emptySet();
            this.maxDocumentSize = 16777216;
            this.maxMessageSize = 33554432;
            this.maxWriteBatchSize = 512;
            this.tags = new TagSet();
            this.version = new ServerVersion();
            this.minWireVersion = 0;
            this.maxWireVersion = 0;
        }
        
        public Builder address(final ServerAddress address) {
            this.address = address;
            return this;
        }
        
        public Builder type(final ServerType type) {
            this.type = Assertions.notNull("type", type);
            return this;
        }
        
        public Builder hosts(final Set<String> hosts) {
            this.hosts = ((hosts == null) ? Collections.emptySet() : Collections.unmodifiableSet((Set<? extends String>)new HashSet<String>(hosts)));
            return this;
        }
        
        public Builder passives(final Set<String> passives) {
            this.passives = ((passives == null) ? Collections.emptySet() : Collections.unmodifiableSet((Set<? extends String>)new HashSet<String>(passives)));
            return this;
        }
        
        public Builder arbiters(final Set<String> arbiters) {
            this.arbiters = ((arbiters == null) ? Collections.emptySet() : Collections.unmodifiableSet((Set<? extends String>)new HashSet<String>(arbiters)));
            return this;
        }
        
        public Builder primary(final String primary) {
            this.primary = primary;
            return this;
        }
        
        public Builder maxDocumentSize(final int maxBSONObjectSize) {
            this.maxDocumentSize = maxBSONObjectSize;
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
        
        public Builder tagSet(final TagSet tags) {
            this.tags = ((tags == null) ? new TagSet() : tags);
            return this;
        }
        
        public Builder averageLatency(final long averageLatency, final TimeUnit timeUnit) {
            this.averageLatency = timeUnit.toNanos(averageLatency);
            return this;
        }
        
        public Builder setName(final String setName) {
            this.setName = setName;
            return this;
        }
        
        public Builder ok(final boolean ok) {
            this.ok = ok;
            return this;
        }
        
        public Builder state(final ServerConnectionState state) {
            this.state = state;
            return this;
        }
        
        public Builder version(final ServerVersion version) {
            Assertions.notNull("version", version);
            this.version = version;
            return this;
        }
        
        public Builder minWireVersion(final int minWireVersion) {
            this.minWireVersion = minWireVersion;
            return this;
        }
        
        public Builder maxWireVersion(final int maxWireVersion) {
            this.maxWireVersion = maxWireVersion;
            return this;
        }
        
        public Builder exception(final Throwable exception) {
            this.exception = exception;
            return this;
        }
        
        public ServerDescription build() {
            return new ServerDescription(this);
        }
    }
}
