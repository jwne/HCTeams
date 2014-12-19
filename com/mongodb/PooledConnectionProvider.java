package com.mongodb;

import java.util.logging.*;
import java.util.concurrent.atomic.*;
import org.bson.util.*;
import java.util.concurrent.*;

class PooledConnectionProvider
{
    private static final Logger LOGGER;
    private final ConcurrentPool<Connection> pool;
    private final ConnectionPoolSettings settings;
    private final AtomicInteger waitQueueSize;
    private final AtomicInteger generation;
    private final ExecutorService sizeMaintenanceTimer;
    private final String clusterId;
    private final ServerAddress serverAddress;
    private final Runnable maintenanceTask;
    private final ConnectionPoolListener connectionPoolListener;
    private final ConnectionFactory connectionFactory;
    private volatile boolean closed;
    private volatile boolean hasWorked;
    
    public PooledConnectionProvider(final String clusterId, final ServerAddress serverAddress, final ConnectionFactory connectionFactory, final ConnectionPoolSettings settings, final ConnectionPoolListener connectionPoolListener) {
        super();
        this.waitQueueSize = new AtomicInteger(0);
        this.generation = new AtomicInteger(0);
        this.connectionFactory = connectionFactory;
        this.clusterId = Assertions.notNull("clusterId", clusterId);
        this.serverAddress = Assertions.notNull("serverAddress", serverAddress);
        this.settings = Assertions.notNull("settings", settings);
        final ConnectionItemFactory connectionItemFactory = new ConnectionItemFactory();
        this.pool = new ConcurrentPool<Connection>(settings.getMaxSize(), connectionItemFactory);
        this.maintenanceTask = this.createMaintenanceTask();
        this.sizeMaintenanceTimer = this.createTimer();
        this.connectionPoolListener = Assertions.notNull("connectionPoolListener", connectionPoolListener);
        connectionPoolListener.connectionPoolOpened(new ConnectionPoolOpenedEvent(clusterId, serverAddress, settings));
    }
    
    public Connection get() {
        return this.get(this.settings.getMaxWaitTime(TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS);
    }
    
    public Connection get(final long timeout, final TimeUnit timeUnit) {
        try {
            if (this.waitQueueSize.incrementAndGet() > this.settings.getMaxWaitQueueSize()) {
                throw new MongoWaitQueueFullException(String.format("Too many threads are already waiting for a connection. Max number of threads (maxWaitQueueSize) of %d has been exceeded.", this.settings.getMaxWaitQueueSize()));
            }
            this.connectionPoolListener.waitQueueEntered(new ConnectionPoolWaitQueueEvent(this.clusterId, this.serverAddress, Thread.currentThread().getId()));
            Connection connection = this.pool.get(timeout, timeUnit);
            this.hasWorked = true;
            while (this.shouldPrune(connection)) {
                this.pool.release(connection, true);
                connection = this.pool.get(timeout, timeUnit);
            }
            this.connectionPoolListener.connectionCheckedOut(new ConnectionEvent(this.clusterId, this.serverAddress));
            return connection;
        }
        finally {
            this.waitQueueSize.decrementAndGet();
            this.connectionPoolListener.waitQueueExited(new ConnectionPoolWaitQueueEvent(this.clusterId, this.serverAddress, Thread.currentThread().getId()));
        }
    }
    
    public void release(final Connection connection) {
        if (!this.closed) {
            this.connectionPoolListener.connectionCheckedIn(new ConnectionEvent(this.clusterId, this.serverAddress));
        }
        this.pool.release(connection, connection.isClosed() || this.shouldPrune(connection));
    }
    
    public boolean hasWorked() {
        return this.hasWorked;
    }
    
    public void close() {
        if (!this.closed) {
            this.pool.close();
            if (this.sizeMaintenanceTimer != null) {
                this.sizeMaintenanceTimer.shutdownNow();
            }
            this.closed = true;
            this.connectionPoolListener.connectionPoolClosed(new ConnectionPoolEvent(this.clusterId, this.serverAddress));
        }
    }
    
    public void doMaintenance() {
        if (this.maintenanceTask != null) {
            this.maintenanceTask.run();
        }
    }
    
    private Runnable createMaintenanceTask() {
        Runnable newMaintenanceTask = null;
        if (this.shouldPrune() || this.shouldEnsureMinSize()) {
            newMaintenanceTask = new Runnable() {
                public synchronized void run() {
                    if (PooledConnectionProvider.this.shouldPrune()) {
                        PooledConnectionProvider.LOGGER.fine(String.format("Pruning pooled connections to %s", PooledConnectionProvider.this.serverAddress));
                        PooledConnectionProvider.this.pool.prune();
                    }
                    if (PooledConnectionProvider.this.shouldEnsureMinSize()) {
                        PooledConnectionProvider.LOGGER.fine(String.format("Ensuring minimum pooled connections to %s", PooledConnectionProvider.this.serverAddress));
                        PooledConnectionProvider.this.pool.ensureMinSize(PooledConnectionProvider.this.settings.getMinSize());
                    }
                }
            };
        }
        return newMaintenanceTask;
    }
    
    private ExecutorService createTimer() {
        if (this.maintenanceTask == null) {
            return null;
        }
        final ScheduledExecutorService newTimer = Executors.newSingleThreadScheduledExecutor();
        newTimer.scheduleAtFixedRate(this.maintenanceTask, this.settings.getMaintenanceInitialDelay(TimeUnit.MILLISECONDS), this.settings.getMaintenanceFrequency(TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS);
        return newTimer;
    }
    
    private boolean shouldEnsureMinSize() {
        return this.settings.getMinSize() > 0;
    }
    
    private boolean shouldPrune() {
        return this.settings.getMaxConnectionIdleTime(TimeUnit.MILLISECONDS) > 0L || this.settings.getMaxConnectionLifeTime(TimeUnit.MILLISECONDS) > 0L;
    }
    
    private boolean shouldPrune(final Connection connection) {
        return this.fromPreviousGeneration(connection) || this.pastMaxLifeTime(connection) || this.pastMaxIdleTime(connection);
    }
    
    private boolean pastMaxIdleTime(final Connection connection) {
        return this.expired(connection.getLastUsedAt(), System.currentTimeMillis(), this.settings.getMaxConnectionIdleTime(TimeUnit.MILLISECONDS));
    }
    
    private boolean pastMaxLifeTime(final Connection connection) {
        return this.expired(connection.getOpenedAt(), System.currentTimeMillis(), this.settings.getMaxConnectionLifeTime(TimeUnit.MILLISECONDS));
    }
    
    private boolean fromPreviousGeneration(final Connection connection) {
        return this.generation.get() > connection.getGeneration();
    }
    
    private boolean expired(final long startTime, final long curTime, final long maxTime) {
        return maxTime != 0L && curTime - startTime > maxTime;
    }
    
    public void invalidate() {
        this.generation.incrementAndGet();
    }
    
    static {
        LOGGER = Loggers.getLogger("connection");
    }
    
    private class ConnectionItemFactory implements ConcurrentPool.ItemFactory<Connection>
    {
        public Connection create() {
            final Connection connection = PooledConnectionProvider.this.connectionFactory.create(PooledConnectionProvider.this.serverAddress, PooledConnectionProvider.this, PooledConnectionProvider.this.generation.get());
            PooledConnectionProvider.LOGGER.fine(String.format("Opened connection to %s", PooledConnectionProvider.this.serverAddress));
            PooledConnectionProvider.this.connectionPoolListener.connectionAdded(new ConnectionEvent(PooledConnectionProvider.this.clusterId, PooledConnectionProvider.this.serverAddress));
            return connection;
        }
        
        public void close(final Connection connection) {
            String reason;
            if (PooledConnectionProvider.this.fromPreviousGeneration(connection)) {
                reason = "there was a socket exception raised on another connection from this pool";
            }
            else if (PooledConnectionProvider.this.pastMaxLifeTime(connection)) {
                reason = "it is past its maximum allowed life time";
            }
            else if (PooledConnectionProvider.this.pastMaxIdleTime(connection)) {
                reason = "it is past its maximum allowed idle time";
            }
            else {
                reason = "the pool has been closed";
            }
            if (!PooledConnectionProvider.this.closed) {
                PooledConnectionProvider.this.connectionPoolListener.connectionRemoved(new ConnectionEvent(PooledConnectionProvider.this.clusterId, PooledConnectionProvider.this.serverAddress));
            }
            connection.close();
            PooledConnectionProvider.LOGGER.fine(String.format("Closed connection to %s because %s.", PooledConnectionProvider.this.serverAddress, reason));
        }
        
        public boolean shouldPrune(final Connection connection) {
            return PooledConnectionProvider.this.shouldPrune(connection);
        }
    }
}
