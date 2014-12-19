package com.mongodb;

import java.util.logging.*;
import org.bson.util.*;
import java.util.*;

final class SingleServerCluster extends BaseCluster
{
    private static final Logger LOGGER;
    private final ClusterableServer server;
    
    public SingleServerCluster(final String clusterId, final ClusterSettings settings, final ClusterableServerFactory serverFactory, final ClusterListener clusterListener) {
        super(clusterId, settings, serverFactory, clusterListener);
        Assertions.isTrue("one server in a direct cluster", settings.getHosts().size() == 1);
        Assertions.isTrue("connection mode is single", settings.getMode() == ClusterConnectionMode.Single);
        SingleServerCluster.LOGGER.info(String.format("Cluster created with settings %s", settings.getShortDescription()));
        synchronized (this) {
            this.server = this.createServer(settings.getHosts().get(0), new ChangeListener<ServerDescription>() {
                public void stateChanged(final ChangeEvent<ServerDescription> event) {
                    ServerDescription descriptionToPublish = event.getNewValue();
                    if (event.getNewValue().isOk()) {
                        if (SingleServerCluster.this.getSettings().getRequiredClusterType() != ClusterType.Unknown && SingleServerCluster.this.getSettings().getRequiredClusterType() != event.getNewValue().getClusterType()) {
                            descriptionToPublish = null;
                        }
                        else if (SingleServerCluster.this.getSettings().getRequiredClusterType() == ClusterType.ReplicaSet && SingleServerCluster.this.getSettings().getRequiredReplicaSetName() != null && !SingleServerCluster.this.getSettings().getRequiredReplicaSetName().equals(event.getNewValue().getSetName())) {
                            descriptionToPublish = null;
                        }
                    }
                    SingleServerCluster.this.publishDescription(descriptionToPublish);
                }
            });
            this.publishDescription(this.server.getDescription());
        }
    }
    
    protected void connect() {
        this.server.connect();
    }
    
    private void publishDescription(final ServerDescription serverDescription) {
        ClusterType clusterType = this.getSettings().getRequiredClusterType();
        if (clusterType == ClusterType.Unknown && serverDescription != null) {
            clusterType = serverDescription.getClusterType();
        }
        final ClusterDescription description = new ClusterDescription(ClusterConnectionMode.Single, clusterType, (serverDescription == null) ? Collections.emptyList() : Arrays.asList(serverDescription));
        this.updateDescription(description);
        this.fireChangeEvent();
    }
    
    protected ClusterableServer getServer(final ServerAddress serverAddress) {
        Assertions.isTrue("open", !this.isClosed());
        return this.server;
    }
    
    public void close() {
        if (!this.isClosed()) {
            this.server.close();
            super.close();
        }
    }
    
    static {
        LOGGER = Loggers.getLogger("cluster");
    }
}
