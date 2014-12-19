package com.mongodb;

abstract class ConnectionPoolListenerAdapter implements ConnectionPoolListener
{
    public void connectionPoolOpened(final ConnectionPoolOpenedEvent event) {
    }
    
    public void connectionPoolClosed(final ConnectionPoolEvent event) {
    }
    
    public void connectionCheckedOut(final ConnectionEvent event) {
    }
    
    public void connectionCheckedIn(final ConnectionEvent event) {
    }
    
    public void waitQueueEntered(final ConnectionPoolWaitQueueEvent event) {
    }
    
    public void waitQueueExited(final ConnectionPoolWaitQueueEvent event) {
    }
    
    public void connectionAdded(final ConnectionEvent event) {
    }
    
    public void connectionRemoved(final ConnectionEvent event) {
    }
}
