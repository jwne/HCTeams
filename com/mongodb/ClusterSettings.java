package com.mongodb;

import org.bson.util.annotations.*;
import org.bson.util.*;
import java.util.*;

@Immutable
final class ClusterSettings
{
    private final List<ServerAddress> hosts;
    private final ClusterConnectionMode mode;
    private final ClusterType requiredClusterType;
    private final String requiredReplicaSetName;
    
    public static Builder builder() {
        return new Builder();
    }
    
    public List<ServerAddress> getHosts() {
        return this.hosts;
    }
    
    public ClusterConnectionMode getMode() {
        return this.mode;
    }
    
    public ClusterType getRequiredClusterType() {
        return this.requiredClusterType;
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
        final ClusterSettings that = (ClusterSettings)o;
        if (!this.hosts.equals(that.hosts)) {
            return false;
        }
        if (this.mode != that.mode) {
            return false;
        }
        if (this.requiredClusterType != that.requiredClusterType) {
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
        int result = this.hosts.hashCode();
        result = 31 * result + this.mode.hashCode();
        result = 31 * result + this.requiredClusterType.hashCode();
        result = 31 * result + ((this.requiredReplicaSetName != null) ? this.requiredReplicaSetName.hashCode() : 0);
        return result;
    }
    
    public String toString() {
        return "{hosts=" + this.hosts + ", mode=" + this.mode + ", requiredClusterType=" + this.requiredClusterType + ", requiredReplicaSetName='" + this.requiredReplicaSetName + '\'' + '}';
    }
    
    public String getShortDescription() {
        return "{hosts=" + this.hosts + ", mode=" + this.mode + ", requiredClusterType=" + this.requiredClusterType + ((this.requiredReplicaSetName == null) ? "" : (", requiredReplicaSetName='" + this.requiredReplicaSetName + '\'')) + '}';
    }
    
    private ClusterSettings(final Builder builder) {
        super();
        Assertions.notNull("hosts", builder.hosts);
        Assertions.isTrueArgument("hosts size > 0", builder.hosts.size() > 0);
        if (builder.hosts.size() > 1 && builder.requiredClusterType == ClusterType.StandAlone) {
            throw new IllegalArgumentException("Multiple hosts cannot be specified when using ClusterType.StandAlone.");
        }
        if (builder.mode == ClusterConnectionMode.Single && builder.hosts.size() > 1) {
            throw new IllegalArgumentException("Can not directly connect to more than one server");
        }
        if (builder.requiredReplicaSetName != null) {
            if (builder.requiredClusterType == ClusterType.Unknown) {
                builder.requiredClusterType = ClusterType.ReplicaSet;
            }
            else if (builder.requiredClusterType != ClusterType.ReplicaSet) {
                throw new IllegalArgumentException("When specifying a replica set name, only ClusterType.Unknown and ClusterType.ReplicaSet are valid.");
            }
        }
        this.hosts = builder.hosts;
        this.mode = builder.mode;
        this.requiredReplicaSetName = builder.requiredReplicaSetName;
        this.requiredClusterType = builder.requiredClusterType;
    }
    
    static final class Builder
    {
        private List<ServerAddress> hosts;
        private ClusterConnectionMode mode;
        private ClusterType requiredClusterType;
        private String requiredReplicaSetName;
        
        private Builder() {
            super();
            this.mode = ClusterConnectionMode.Multiple;
            this.requiredClusterType = ClusterType.Unknown;
        }
        
        public Builder hosts(final List<ServerAddress> hosts) {
            Assertions.notNull("hosts", hosts);
            if (hosts.isEmpty()) {
                throw new IllegalArgumentException("hosts list may not be empty");
            }
            this.hosts = Collections.unmodifiableList((List<? extends ServerAddress>)new ArrayList<ServerAddress>(new LinkedHashSet<ServerAddress>(hosts)));
            return this;
        }
        
        public Builder mode(final ClusterConnectionMode mode) {
            this.mode = Assertions.notNull("mode", mode);
            return this;
        }
        
        public Builder requiredReplicaSetName(final String requiredReplicaSetName) {
            this.requiredReplicaSetName = requiredReplicaSetName;
            return this;
        }
        
        public Builder requiredClusterType(final ClusterType requiredClusterType) {
            this.requiredClusterType = Assertions.notNull("requiredClusterType", requiredClusterType);
            return this;
        }
        
        public ClusterSettings build() {
            return new ClusterSettings(this, null);
        }
    }
}
