package com.mongodb;

import java.util.*;

class MongosHAServerSelector implements ServerSelector
{
    private ServerAddress stickTo;
    private Set<ServerAddress> consideredServers;
    
    MongosHAServerSelector() {
        super();
        this.consideredServers = new HashSet<ServerAddress>();
    }
    
    public List<ServerDescription> choose(final ClusterDescription clusterDescription) {
        if (clusterDescription.getConnectionMode() != ClusterConnectionMode.Multiple || clusterDescription.getType() != ClusterType.Sharded) {
            throw new IllegalArgumentException("This is not a sharded cluster with multiple mongos servers");
        }
        final Set<ServerAddress> okServers = this.getOkServers(clusterDescription);
        synchronized (this) {
            if (!this.consideredServers.containsAll(okServers) || !okServers.contains(this.stickTo)) {
                if (this.stickTo != null && !okServers.contains(this.stickTo)) {
                    this.stickTo = null;
                    this.consideredServers.clear();
                }
                ServerDescription fastestServer = null;
                for (final ServerDescription cur : clusterDescription.getAny()) {
                    if (fastestServer == null || cur.getAverageLatencyNanos() < fastestServer.getAverageLatencyNanos()) {
                        fastestServer = cur;
                    }
                }
                if (fastestServer != null) {
                    this.stickTo = fastestServer.getAddress();
                    this.consideredServers.addAll(okServers);
                }
            }
            if (this.stickTo == null) {
                return Collections.emptyList();
            }
            return Arrays.asList(clusterDescription.getByServerAddress(this.stickTo));
        }
    }
    
    public String toString() {
        return "MongosHAServerSelector{" + ((this.stickTo == null) ? "" : ("stickTo=" + this.stickTo)) + '}';
    }
    
    private Set<ServerAddress> getOkServers(final ClusterDescription clusterDescription) {
        final Set<ServerAddress> okServers = new HashSet<ServerAddress>();
        for (final ServerDescription cur : clusterDescription.getAny()) {
            okServers.add(cur.getAddress());
        }
        return okServers;
    }
}
