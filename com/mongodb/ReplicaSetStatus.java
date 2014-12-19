package com.mongodb;

import java.util.*;

public class ReplicaSetStatus
{
    private final ClusterDescription clusterDescription;
    
    ReplicaSetStatus(final ClusterDescription clusterDescription) {
        super();
        this.clusterDescription = clusterDescription;
    }
    
    public String getName() {
        final List<ServerDescription> any = this.clusterDescription.getAnyPrimaryOrSecondary();
        return any.isEmpty() ? null : any.get(0).getSetName();
    }
    
    public ServerAddress getMaster() {
        final List<ServerDescription> primaries = this.clusterDescription.getPrimaries();
        return primaries.isEmpty() ? null : primaries.get(0).getAddress();
    }
    
    public boolean isMaster(final ServerAddress serverAddress) {
        return this.getMaster().equals(serverAddress);
    }
    
    public int getMaxBsonObjectSize() {
        final List<ServerDescription> primaries = this.clusterDescription.getPrimaries();
        return primaries.isEmpty() ? ServerDescription.getDefaultMaxDocumentSize() : primaries.get(0).getMaxDocumentSize();
    }
    
    public String toString() {
        return "ReplicaSetStatus{name=" + this.getName() + ", cluster=" + this.clusterDescription + '}';
    }
}
