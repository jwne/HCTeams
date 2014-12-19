package com.mongodb;

class ConnectionEvent extends ClusterEvent
{
    private final ServerAddress serverAddress;
    
    public ConnectionEvent(final String clusterId, final ServerAddress serverAddress) {
        super(clusterId);
        this.serverAddress = serverAddress;
    }
    
    public ServerAddress getServerAddress() {
        return this.serverAddress;
    }
    
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        final ConnectionEvent that = (ConnectionEvent)o;
        return this.getClusterId().equals(that.getClusterId()) && this.getServerAddress().equals(that.getServerAddress()) && this.serverAddress.equals(that.serverAddress);
    }
    
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + this.serverAddress.hashCode();
        return result;
    }
}
