package com.mongodb;

class ConnectionPoolWaitQueueEvent extends ConnectionPoolEvent
{
    private final long threadId;
    
    public ConnectionPoolWaitQueueEvent(final String clusterId, final ServerAddress serverAddress, final long threadId) {
        super(clusterId, serverAddress);
        this.threadId = threadId;
    }
    
    public long getThreadId() {
        return this.threadId;
    }
    
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final ConnectionPoolWaitQueueEvent that = (ConnectionPoolWaitQueueEvent)o;
        return this.getClusterId().equals(that.getClusterId()) && this.getServerAddress().equals(that.getServerAddress()) && this.threadId == that.threadId;
    }
    
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (int)(this.threadId ^ this.threadId >>> 32);
        return result;
    }
}
