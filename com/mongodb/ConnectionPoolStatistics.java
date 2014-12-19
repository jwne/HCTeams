package com.mongodb;

import java.util.concurrent.atomic.*;

final class ConnectionPoolStatistics extends ConnectionPoolListenerAdapter implements ConnectionPoolStatisticsMBean
{
    private final ServerAddress serverAddress;
    private final ConnectionPoolSettings settings;
    private final AtomicInteger size;
    private final AtomicInteger checkedOutCount;
    private final AtomicInteger waitQueueSize;
    
    public ConnectionPoolStatistics(final ConnectionPoolOpenedEvent event) {
        super();
        this.size = new AtomicInteger();
        this.checkedOutCount = new AtomicInteger();
        this.waitQueueSize = new AtomicInteger();
        this.serverAddress = event.getServerAddress();
        this.settings = event.getSettings();
    }
    
    public String getHost() {
        return this.serverAddress.getHost();
    }
    
    public int getPort() {
        return this.serverAddress.getPort();
    }
    
    public int getMinSize() {
        return this.settings.getMinSize();
    }
    
    public int getMaxSize() {
        return this.settings.getMaxSize();
    }
    
    public int getSize() {
        return this.size.get();
    }
    
    public int getCheckedOutCount() {
        return this.checkedOutCount.get();
    }
    
    public int getWaitQueueSize() {
        return this.waitQueueSize.get();
    }
    
    public void connectionCheckedOut(final ConnectionEvent event) {
        this.checkedOutCount.incrementAndGet();
    }
    
    public void connectionCheckedIn(final ConnectionEvent event) {
        this.checkedOutCount.decrementAndGet();
    }
    
    public void connectionAdded(final ConnectionEvent event) {
        this.size.incrementAndGet();
    }
    
    public void connectionRemoved(final ConnectionEvent event) {
        this.size.decrementAndGet();
    }
    
    public void waitQueueEntered(final ConnectionPoolWaitQueueEvent event) {
        this.waitQueueSize.incrementAndGet();
    }
    
    public void waitQueueExited(final ConnectionPoolWaitQueueEvent event) {
        this.waitQueueSize.decrementAndGet();
    }
}
