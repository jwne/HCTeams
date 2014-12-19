package com.mongodb;

class ConnectionPoolOpenedEvent extends ConnectionPoolEvent
{
    private final ConnectionPoolSettings settings;
    
    public ConnectionPoolOpenedEvent(final String clusterId, final ServerAddress serverAddress, final ConnectionPoolSettings settings) {
        super(clusterId, serverAddress);
        this.settings = settings;
    }
    
    public ConnectionPoolSettings getSettings() {
        return this.settings;
    }
    
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final ConnectionPoolOpenedEvent that = (ConnectionPoolOpenedEvent)o;
        return this.getClusterId().equals(that.getClusterId()) && this.getServerAddress().equals(that.getServerAddress()) && this.settings.equals(that.getSettings());
    }
    
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + this.settings.hashCode();
        return result;
    }
}
