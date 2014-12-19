package com.mongodb;

import org.bson.util.annotations.*;
import java.util.concurrent.locks.*;
import java.util.concurrent.*;
import java.io.*;
import java.util.*;
import java.util.logging.*;

@ThreadSafe
class ServerMonitor
{
    private static final Logger LOGGER;
    private ServerAddress serverAddress;
    private final ChangeListener<ServerDescription> serverStateListener;
    private final SocketSettings socketSettings;
    private final ServerSettings settings;
    private final Mongo mongo;
    private final PooledConnectionProvider connectionProvider;
    private int count;
    private long elapsedNanosSum;
    private volatile boolean isClosed;
    private final Thread monitorThread;
    private final Lock lock;
    private final Condition condition;
    
    ServerMonitor(final ServerAddress serverAddress, final ChangeListener<ServerDescription> serverStateListener, final SocketSettings socketSettings, final ServerSettings settings, final String clusterId, final Mongo mongo, final PooledConnectionProvider connectionProvider) {
        super();
        this.lock = new ReentrantLock();
        this.condition = this.lock.newCondition();
        this.serverAddress = serverAddress;
        this.serverStateListener = serverStateListener;
        this.socketSettings = socketSettings;
        this.settings = settings;
        this.mongo = mongo;
        this.connectionProvider = connectionProvider;
        (this.monitorThread = new Thread(new ServerMonitorRunnable(), "cluster-" + clusterId + "-" + serverAddress)).setDaemon(true);
    }
    
    void start() {
        this.monitorThread.start();
    }
    
    public void connect() {
        this.lock.lock();
        try {
            this.condition.signal();
        }
        finally {
            this.lock.unlock();
        }
    }
    
    public void close() {
        this.isClosed = true;
        this.monitorThread.interrupt();
    }
    
    private MongoOptions getOptions() {
        final MongoOptions options = new MongoOptions();
        options.setConnectTimeout(this.socketSettings.getConnectTimeout(TimeUnit.MILLISECONDS));
        options.setSocketTimeout(this.socketSettings.getReadTimeout(TimeUnit.MILLISECONDS));
        options.setSocketFactory(this.socketSettings.getSocketFactory());
        return options;
    }
    
    static boolean descriptionHasChanged(final ServerDescription previousServerDescription, final ServerDescription currentServerDescription) {
        return !previousServerDescription.equals(currentServerDescription);
    }
    
    static boolean stateHasChanged(final ServerDescription previousServerDescription, final ServerDescription currentServerDescription) {
        return descriptionHasChanged(previousServerDescription, currentServerDescription) || previousServerDescription.getAverageLatencyNanos() != currentServerDescription.getAverageLatencyNanos();
    }
    
    static boolean exceptionHasChanged(final Throwable previousException, final Throwable currentException) {
        if (currentException == null) {
            return previousException != null;
        }
        if (previousException == null) {
            return true;
        }
        if (!currentException.getClass().equals(previousException.getClass())) {
            return true;
        }
        if (currentException.getMessage() == null) {
            return previousException.getMessage() != null;
        }
        return !currentException.getMessage().equals(previousException.getMessage());
    }
    
    private ServerDescription lookupServerDescription(final DBPort connection) throws IOException {
        ServerMonitor.LOGGER.fine(String.format("Checking status of %s", this.serverAddress));
        final long startNanoTime = System.nanoTime();
        final CommandResult isMasterResult = connection.runCommand(this.mongo.getDB("admin"), new BasicDBObject("ismaster", 1));
        ++this.count;
        this.elapsedNanosSum += System.nanoTime() - startNanoTime;
        final CommandResult buildInfoResult = connection.runCommand(this.mongo.getDB("admin"), new BasicDBObject("buildinfo", 1));
        return this.createDescription(isMasterResult, buildInfoResult, this.elapsedNanosSum / this.count);
    }
    
    private ServerDescription createDescription(final CommandResult commandResult, final CommandResult buildInfoResult, final long averageLatencyNanos) {
        return ServerDescription.builder().state(ServerConnectionState.Connected).version(getVersion(buildInfoResult)).address(commandResult.getServerUsed()).type(getServerType(commandResult)).hosts(this.listToSet((List<String>)commandResult.get("hosts"))).passives(this.listToSet((List<String>)commandResult.get("passives"))).arbiters(this.listToSet((List<String>)commandResult.get("arbiters"))).primary(commandResult.getString("primary")).maxDocumentSize(commandResult.getInt("maxBsonObjectSize", ServerDescription.getDefaultMaxDocumentSize())).maxMessageSize(commandResult.getInt("maxMessageSizeBytes", ServerDescription.getDefaultMaxMessageSize())).maxWriteBatchSize(commandResult.getInt("maxWriteBatchSize", ServerDescription.getDefaultMaxWriteBatchSize())).tagSet(getTagSetFromDocument((DBObject)commandResult.get("tags"))).setName(commandResult.getString("setName")).minWireVersion(commandResult.getInt("minWireVersion", ServerDescription.getDefaultMinWireVersion())).maxWireVersion(commandResult.getInt("maxWireVersion", ServerDescription.getDefaultMaxWireVersion())).averageLatency(averageLatencyNanos, TimeUnit.NANOSECONDS).ok(commandResult.ok()).build();
    }
    
    private static ServerVersion getVersion(final CommandResult buildInfoResult) {
        return new ServerVersion(((List)buildInfoResult.get("versionArray")).subList(0, 3));
    }
    
    private Set<String> listToSet(final List<String> list) {
        if (list == null || list.isEmpty()) {
            return Collections.emptySet();
        }
        return new HashSet<String>(list);
    }
    
    private static ServerType getServerType(final BasicDBObject isMasterResult) {
        if (isReplicaSetMember(isMasterResult)) {
            if (isMasterResult.getBoolean("ismaster", false)) {
                return ServerType.ReplicaSetPrimary;
            }
            if (isMasterResult.getBoolean("secondary", false)) {
                return ServerType.ReplicaSetSecondary;
            }
            if (isMasterResult.getBoolean("arbiterOnly", false)) {
                return ServerType.ReplicaSetArbiter;
            }
            if (isMasterResult.containsKey("setName") && isMasterResult.containsField("hosts")) {
                return ServerType.ReplicaSetOther;
            }
            return ServerType.ReplicaSetGhost;
        }
        else {
            if (isMasterResult.containsKey("msg") && isMasterResult.get("msg").equals("isdbgrid")) {
                return ServerType.ShardRouter;
            }
            return ServerType.StandAlone;
        }
    }
    
    private static boolean isReplicaSetMember(final BasicDBObject isMasterResult) {
        return isMasterResult.containsKey("setName") || isMasterResult.getBoolean("isreplicaset", false);
    }
    
    private static TagSet getTagSetFromDocument(final DBObject tagsDocument) {
        if (tagsDocument == null) {
            return new TagSet();
        }
        final List<Tag> tagList = new ArrayList<Tag>();
        for (final String key : tagsDocument.keySet()) {
            tagList.add(new Tag(key, tagsDocument.get(key).toString()));
        }
        return new TagSet(tagList);
    }
    
    private ServerDescription getConnectingServerDescription(final Throwable throwable) {
        return ServerDescription.builder().type(ServerType.Unknown).state(ServerConnectionState.Connecting).address(this.serverAddress).exception(throwable).build();
    }
    
    static {
        LOGGER = Loggers.getLogger("cluster");
    }
    
    class ServerMonitorRunnable implements Runnable
    {
        public void run() {
            DBPort connection = null;
            try {
                ServerDescription currentServerDescription = ServerMonitor.this.getConnectingServerDescription(null);
                Throwable currentException = null;
                while (!ServerMonitor.this.isClosed) {
                    final ServerDescription previousServerDescription = currentServerDescription;
                    final Throwable previousException = currentException;
                    try {
                        if (connection == null) {
                            connection = new DBPort(ServerMonitor.this.serverAddress, null, ServerMonitor.this.getOptions(), 0);
                        }
                        try {
                            currentServerDescription = ServerMonitor.this.lookupServerDescription(connection);
                        }
                        catch (IOException e2) {
                            ServerMonitor.this.count = 0;
                            ServerMonitor.this.elapsedNanosSum = 0L;
                            if (connection != null) {
                                connection.close();
                                connection = null;
                                ServerMonitor.this.connectionProvider.invalidate();
                            }
                            connection = new DBPort(ServerMonitor.this.serverAddress, null, ServerMonitor.this.getOptions(), 0);
                            try {
                                currentServerDescription = ServerMonitor.this.lookupServerDescription(connection);
                            }
                            catch (IOException e1) {
                                connection.close();
                                connection = null;
                                throw e1;
                            }
                        }
                    }
                    catch (Throwable t) {
                        currentException = t;
                        currentServerDescription = ServerMonitor.this.getConnectingServerDescription(t);
                    }
                    if (!ServerMonitor.this.isClosed) {
                        try {
                            this.logStateChange(previousServerDescription, previousException, currentServerDescription, currentException);
                            this.sendStateChangedEvent(previousServerDescription, currentServerDescription);
                        }
                        catch (Throwable t) {
                            ServerMonitor.LOGGER.log(Level.WARNING, "Exception in monitor thread during notification of server state change", t);
                        }
                    }
                    this.waitForNext();
                }
            }
            finally {
                if (connection != null) {
                    connection.close();
                }
            }
        }
        
        private void sendStateChangedEvent(final ServerDescription previousServerDescription, final ServerDescription currentServerDescription) {
            if (ServerMonitor.stateHasChanged(previousServerDescription, currentServerDescription)) {
                ServerMonitor.this.serverStateListener.stateChanged(new ChangeEvent<ServerDescription>(previousServerDescription, currentServerDescription));
            }
        }
        
        private void logStateChange(final ServerDescription previousServerDescription, final Throwable previousException, final ServerDescription currentServerDescription, final Throwable currentException) {
            if (ServerMonitor.descriptionHasChanged(previousServerDescription, currentServerDescription) || ServerMonitor.exceptionHasChanged(previousException, currentException)) {
                if (currentException != null) {
                    ServerMonitor.LOGGER.log(Level.INFO, String.format("Exception in monitor thread while connecting to server %s", ServerMonitor.this.serverAddress), currentException);
                }
                else {
                    ServerMonitor.LOGGER.info(String.format("Monitor thread successfully connected to server with description %s", currentServerDescription));
                }
            }
        }
        
        private void waitForNext() {
            try {
                final long timeRemaining = this.waitForSignalOrTimeout();
                if (timeRemaining > 0L) {
                    final long timeWaiting = ServerMonitor.this.settings.getHeartbeatFrequency(TimeUnit.NANOSECONDS) - timeRemaining;
                    final long minimumNanosToWait = ServerMonitor.this.settings.getHeartbeatConnectRetryFrequency(TimeUnit.NANOSECONDS);
                    if (timeWaiting < minimumNanosToWait) {
                        final long millisToSleep = TimeUnit.MILLISECONDS.convert(minimumNanosToWait - timeWaiting, TimeUnit.NANOSECONDS);
                        if (millisToSleep > 0L) {
                            Thread.sleep(millisToSleep);
                        }
                    }
                }
            }
            catch (InterruptedException ex) {}
        }
        
        private long waitForSignalOrTimeout() throws InterruptedException {
            ServerMonitor.this.lock.lock();
            try {
                return ServerMonitor.this.condition.awaitNanos(ServerMonitor.this.settings.getHeartbeatFrequency(TimeUnit.NANOSECONDS));
            }
            finally {
                ServerMonitor.this.lock.unlock();
            }
        }
    }
}
