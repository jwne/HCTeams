package com.mongodb;

import java.util.concurrent.*;

class DefaultClusterableServerFactory implements ClusterableServerFactory
{
    private final String clusterId;
    private ServerSettings settings;
    private final Mongo mongo;
    
    public DefaultClusterableServerFactory(final String clusterId, final ServerSettings settings, final Mongo mongo) {
        super();
        this.clusterId = clusterId;
        this.settings = settings;
        this.mongo = mongo;
    }
    
    public ClusterableServer create(final ServerAddress serverAddress) {
        final MongoOptions options = this.mongo.getMongoOptions();
        final ConnectionPoolSettings connectionPoolSettings = ConnectionPoolSettings.builder().minSize(options.minConnectionsPerHost).maxSize(options.getConnectionsPerHost()).maxConnectionIdleTime(options.maxConnectionIdleTime, TimeUnit.MILLISECONDS).maxConnectionLifeTime(options.maxConnectionLifeTime, TimeUnit.MILLISECONDS).maxWaitQueueSize(options.getConnectionsPerHost() * options.getThreadsAllowedToBlockForConnectionMultiplier()).maxWaitTime(options.getMaxWaitTime(), TimeUnit.MILLISECONDS).build();
        return new DefaultServer(serverAddress, this.settings, this.clusterId, new PooledConnectionProvider(this.clusterId, serverAddress, new DBPortFactory(options), connectionPoolSettings, new JMXConnectionPoolListener(this.mongo.getMongoOptions().getDescription())), this.mongo);
    }
    
    public ServerSettings getSettings() {
        return this.settings;
    }
}
