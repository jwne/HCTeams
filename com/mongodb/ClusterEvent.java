package com.mongodb;

class ClusterEvent
{
    private final String clusterId;
    
    public ClusterEvent(final String clusterId) {
        super();
        this.clusterId = clusterId;
    }
    
    public String getClusterId() {
        return this.clusterId;
    }
    
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final ClusterEvent that = (ClusterEvent)o;
        return this.clusterId.equals(that.clusterId);
    }
    
    public int hashCode() {
        return this.clusterId.hashCode();
    }
}
