package com.mongodb;

class ClusterDescriptionChangedEvent extends ClusterEvent
{
    private final ClusterDescription clusterDescription;
    
    public ClusterDescriptionChangedEvent(final String clusterId, final ClusterDescription clusterDescription) {
        super(clusterId);
        this.clusterDescription = clusterDescription;
    }
    
    public ClusterDescription getClusterDescription() {
        return this.clusterDescription;
    }
    
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final ClusterDescriptionChangedEvent that = (ClusterDescriptionChangedEvent)o;
        return this.getClusterId().equals(that.getClusterId()) && this.clusterDescription.equals(that.clusterDescription);
    }
    
    public int hashCode() {
        return this.clusterDescription.hashCode();
    }
}
