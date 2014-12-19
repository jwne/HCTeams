package com.mongodb;

import java.util.logging.*;
import java.util.concurrent.atomic.*;
import org.bson.util.*;
import java.util.concurrent.*;
import java.util.*;

abstract class BaseCluster implements Cluster
{
    private static final Logger LOGGER;
    private final AtomicReference<CountDownLatch> phase;
    private final ClusterableServerFactory serverFactory;
    private final ThreadLocal<Random> random;
    private final String clusterId;
    private final ClusterSettings settings;
    private final ClusterListener clusterListener;
    private volatile boolean isClosed;
    private volatile ClusterDescription description;
    
    public BaseCluster(final String clusterId, final ClusterSettings settings, final ClusterableServerFactory serverFactory, final ClusterListener clusterListener) {
        super();
        this.phase = new AtomicReference<CountDownLatch>(new CountDownLatch(1));
        this.random = new ThreadLocal<Random>();
        this.clusterId = Assertions.notNull("clusterId", clusterId);
        this.settings = Assertions.notNull("settings", settings);
        this.serverFactory = Assertions.notNull("serverFactory", serverFactory);
        this.clusterListener = Assertions.notNull("clusterListener", clusterListener);
        clusterListener.clusterOpened(new ClusterEvent(clusterId));
    }
    
    public Server getServer(final ServerSelector serverSelector, final long maxWaitTime, final TimeUnit timeUnit) {
        Assertions.isTrue("open", !this.isClosed());
        try {
            CountDownLatch currentPhase = this.phase.get();
            ClusterDescription curDescription = this.description;
            List<ServerDescription> serverDescriptions = serverSelector.choose(curDescription);
            boolean selectionFailureLogged = false;
            final long startTimeNanos = System.nanoTime();
            final long endTimeNanos = startTimeNanos + TimeUnit.NANOSECONDS.convert(maxWaitTime, timeUnit);
            long curTimeNanos = startTimeNanos;
            while (true) {
                this.throwIfIncompatible(curDescription);
                if (!serverDescriptions.isEmpty()) {
                    final ClusterableServer server = this.getRandomServer(new ArrayList<ServerDescription>(serverDescriptions));
                    if (server != null) {
                        return new WrappedServer(server);
                    }
                }
                if (curTimeNanos > endTimeNanos) {
                    throw new MongoTimeoutException(String.format("Timed out after %d ms while waiting for a server that matches %s. Client view of cluster state is %s", TimeUnit.MILLISECONDS.convert(maxWaitTime, timeUnit), serverSelector, curDescription.getShortDescription()));
                }
                if (!selectionFailureLogged) {
                    BaseCluster.LOGGER.info(String.format("No server chosen by %s from cluster description %s. Waiting for %d ms before timing out", serverSelector, curDescription, TimeUnit.MILLISECONDS.convert(maxWaitTime, timeUnit)));
                    selectionFailureLogged = true;
                }
                this.connect();
                currentPhase.await(Math.min(endTimeNanos - curTimeNanos, this.serverFactory.getSettings().getHeartbeatConnectRetryFrequency(TimeUnit.NANOSECONDS)), TimeUnit.NANOSECONDS);
                curTimeNanos = System.nanoTime();
                currentPhase = this.phase.get();
                curDescription = this.description;
                serverDescriptions = serverSelector.choose(curDescription);
            }
        }
        catch (InterruptedException e) {
            throw new MongoInterruptedException(String.format("Interrupted while waiting for a server that matches %s ", serverSelector), e);
        }
    }
    
    public ClusterDescription getDescription(final long maxWaitTime, final TimeUnit timeUnit) {
        Assertions.isTrue("open", !this.isClosed());
        try {
            CountDownLatch currentPhase = this.phase.get();
            ClusterDescription curDescription = this.description;
            boolean selectionFailureLogged = false;
            final long startTimeNanos = System.nanoTime();
            final long endTimeNanos = startTimeNanos + TimeUnit.NANOSECONDS.convert(maxWaitTime, timeUnit);
            long curTimeNanos = startTimeNanos;
            while (curDescription.getType() == ClusterType.Unknown) {
                if (curTimeNanos > endTimeNanos) {
                    throw new MongoTimeoutException(String.format("Timed out after %d ms while waiting to connect. Client view of cluster state is %s", TimeUnit.MILLISECONDS.convert(maxWaitTime, timeUnit), curDescription.getShortDescription()));
                }
                if (!selectionFailureLogged) {
                    BaseCluster.LOGGER.info(String.format("Cluster description not yet available. Waiting for %d ms before timing out", TimeUnit.MILLISECONDS.convert(maxWaitTime, timeUnit)));
                    selectionFailureLogged = true;
                }
                this.connect();
                currentPhase.await(Math.min(endTimeNanos - curTimeNanos, this.serverFactory.getSettings().getHeartbeatConnectRetryFrequency(TimeUnit.NANOSECONDS)), TimeUnit.NANOSECONDS);
                curTimeNanos = System.nanoTime();
                currentPhase = this.phase.get();
                curDescription = this.description;
            }
            return curDescription;
        }
        catch (InterruptedException e) {
            throw new MongoInterruptedException(String.format("Interrupted while waiting to connect", new Object[0]), e);
        }
    }
    
    public ClusterSettings getSettings() {
        return this.settings;
    }
    
    public void close() {
        if (!this.isClosed()) {
            this.isClosed = true;
            this.phase.get().countDown();
            this.clusterListener.clusterClosed(new ClusterEvent(this.clusterId));
        }
    }
    
    public boolean isClosed() {
        return this.isClosed;
    }
    
    protected abstract ClusterableServer getServer(final ServerAddress p0);
    
    protected abstract void connect();
    
    protected synchronized void updateDescription(final ClusterDescription newDescription) {
        BaseCluster.LOGGER.fine(String.format("Updating cluster description to  %s", newDescription.getShortDescription()));
        this.description = newDescription;
        final CountDownLatch current = this.phase.getAndSet(new CountDownLatch(1));
        current.countDown();
    }
    
    protected void fireChangeEvent() {
        this.clusterListener.clusterDescriptionChanged(new ClusterDescriptionChangedEvent(this.clusterId, this.description));
    }
    
    private ClusterableServer getRandomServer(final List<ServerDescription> serverDescriptions) {
        while (!serverDescriptions.isEmpty()) {
            final int serverPos = this.getRandom().nextInt(serverDescriptions.size());
            final ClusterableServer server = this.getServer(serverDescriptions.get(serverPos).getAddress());
            if (server != null) {
                return server;
            }
            serverDescriptions.remove(serverPos);
        }
        return null;
    }
    
    private void throwIfIncompatible(final ClusterDescription curDescription) {
        if (!curDescription.isCompatibleWithDriver()) {
            throw new MongoIncompatibleDriverException(String.format("This version of the driver is not compatible with one or more of the servers to which it is connected: %s", curDescription));
        }
    }
    
    private Random getRandom() {
        Random result = this.random.get();
        if (result == null) {
            result = new Random();
            this.random.set(result);
        }
        return result;
    }
    
    protected ClusterableServer createServer(final ServerAddress serverAddress, final ChangeListener<ServerDescription> serverStateListener) {
        final ClusterableServer server = this.serverFactory.create(serverAddress);
        server.addChangeListener(serverStateListener);
        return server;
    }
    
    static {
        LOGGER = Loggers.getLogger("cluster");
    }
    
    private static final class WrappedServer implements Server
    {
        private final ClusterableServer wrapped;
        
        public WrappedServer(final ClusterableServer server) {
            super();
            this.wrapped = server;
        }
        
        public ServerDescription getDescription() {
            return this.wrapped.getDescription();
        }
        
        public Connection getConnection(final long maxWaitTime, final TimeUnit timeUnit) {
            return this.wrapped.getConnection(maxWaitTime, timeUnit);
        }
        
        public void invalidate() {
            this.wrapped.invalidate();
        }
    }
}
