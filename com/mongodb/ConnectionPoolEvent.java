package com.mongodb;

class ConnectionPoolEvent extends ClusterEvent
{
    private final ServerAddress serverAddress;
    
    public ConnectionPoolEvent(final String clusterId, final ServerAddress serverAddress) {
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
        final ConnectionPoolEvent that = (ConnectionPoolEvent)o;
        return this.getClusterId().equals(that.getClusterId()) && this.serverAddress.equals(that.serverAddress);
    }
    
    public int hashCode() {
        final int result = super.hashCode();
        return 31 * result + this.serverAddress.hashCode();
    }
}
