package com.mongodb;

import org.bson.util.*;
import java.util.concurrent.*;
import java.util.*;

class DefaultServer implements ClusterableServer
{
    private final ServerAddress serverAddress;
    private final ServerMonitor serverMonitor;
    private final PooledConnectionProvider connectionProvider;
    private final Map<ChangeListener<ServerDescription>, Boolean> changeListeners;
    private final ChangeListener<ServerDescription> serverStateListener;
    private volatile ServerDescription description;
    private volatile boolean isClosed;
    
    public DefaultServer(final ServerAddress serverAddress, final ServerSettings settings, final String clusterId, final PooledConnectionProvider connectionProvider, final Mongo mongo) {
        super();
        this.changeListeners = new ConcurrentHashMap<ChangeListener<ServerDescription>, Boolean>();
        this.serverAddress = Assertions.notNull("serverAddress", serverAddress);
        this.description = ServerDescription.builder().state(ServerConnectionState.Connecting).address(serverAddress).build();
        this.serverStateListener = new DefaultServerStateListener();
        this.connectionProvider = connectionProvider;
        (this.serverMonitor = new ServerMonitor(serverAddress, this.serverStateListener, settings.getHeartbeatSocketSettings(), settings, clusterId, mongo, connectionProvider)).start();
    }
    
    public ServerDescription getDescription() {
        Assertions.isTrue("open", !this.isClosed());
        return this.description;
    }
    
    public Connection getConnection(final long maxWaitTime, final TimeUnit timeUnit) {
        return this.connectionProvider.get(maxWaitTime, timeUnit);
    }
    
    public void addChangeListener(final ChangeListener<ServerDescription> changeListener) {
        Assertions.isTrue("open", !this.isClosed());
        this.changeListeners.put(changeListener, true);
    }
    
    public void invalidate() {
        Assertions.isTrue("open", !this.isClosed());
        this.serverStateListener.stateChanged(new ChangeEvent<ServerDescription>(this.description, ServerDescription.builder().state(ServerConnectionState.Connecting).address(this.serverAddress).build()));
        this.connectionProvider.invalidate();
    }
    
    public void close() {
        if (!this.isClosed()) {
            this.serverMonitor.close();
            this.connectionProvider.close();
            this.isClosed = true;
        }
    }
    
    public boolean isClosed() {
        return this.isClosed;
    }
    
    public void connect() {
        this.serverMonitor.connect();
    }
    
    private final class DefaultServerStateListener implements ChangeListener<ServerDescription>
    {
        public void stateChanged(final ChangeEvent<ServerDescription> event) {
            DefaultServer.this.description = event.getNewValue();
            for (final ChangeListener<ServerDescription> listener : DefaultServer.this.changeListeners.keySet()) {
                listener.stateChanged(event);
            }
        }
    }
}
