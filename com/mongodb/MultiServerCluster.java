package com.mongodb;

import java.util.logging.*;
import java.util.concurrent.*;
import org.bson.util.*;
import java.net.*;
import java.util.*;

final class MultiServerCluster extends BaseCluster
{
    private static final Logger LOGGER;
    private ClusterType clusterType;
    private String replicaSetName;
    private final ConcurrentMap<ServerAddress, ServerTuple> addressToServerTupleMap;
    
    public MultiServerCluster(final String clusterId, final ClusterSettings settings, final ClusterableServerFactory serverFactory, final ClusterListener clusterListener) {
        super(clusterId, settings, serverFactory, clusterListener);
        this.addressToServerTupleMap = new ConcurrentHashMap<ServerAddress, ServerTuple>();
        Assertions.isTrue("connection mode is multiple", settings.getMode() == ClusterConnectionMode.Multiple);
        this.clusterType = settings.getRequiredClusterType();
        this.replicaSetName = settings.getRequiredReplicaSetName();
        MultiServerCluster.LOGGER.info(String.format("Cluster created with settings %s", settings.getShortDescription()));
        synchronized (this) {
            for (final ServerAddress serverAddress : settings.getHosts()) {
                this.addServer(serverAddress);
            }
            this.updateDescription();
        }
        this.fireChangeEvent();
    }
    
    protected void connect() {
        for (final ServerTuple cur : this.addressToServerTupleMap.values()) {
            cur.server.connect();
        }
    }
    
    public void close() {
        if (!this.isClosed()) {
            synchronized (this) {
                for (final ServerTuple serverTuple : this.addressToServerTupleMap.values()) {
                    serverTuple.server.close();
                }
            }
            super.close();
        }
    }
    
    protected ClusterableServer getServer(final ServerAddress serverAddress) {
        Assertions.isTrue("is open", !this.isClosed());
        final ServerTuple serverTuple = this.addressToServerTupleMap.get(serverAddress);
        if (serverTuple == null) {
            return null;
        }
        return serverTuple.server;
    }
    
    private void onChange(final ChangeEvent<ServerDescription> event) {
        if (this.isClosed()) {
            return;
        }
        synchronized (this) {
            final ServerDescription newDescription = event.getNewValue();
            final ServerTuple serverTuple = this.addressToServerTupleMap.get(newDescription.getAddress());
            if (serverTuple == null) {
                return;
            }
            if (event.getNewValue().isOk()) {
                if (this.clusterType == ClusterType.Unknown && newDescription.getType() != ServerType.ReplicaSetGhost) {
                    this.clusterType = newDescription.getClusterType();
                    MultiServerCluster.LOGGER.info(String.format("Discovered cluster type of %s", this.clusterType));
                }
                switch (this.clusterType) {
                    case ReplicaSet: {
                        this.handleReplicaSetMemberChanged(newDescription);
                        break;
                    }
                    case Sharded: {
                        this.handleShardRouterChanged(newDescription);
                        break;
                    }
                    case StandAlone: {
                        this.handleStandAloneChanged(newDescription);
                        break;
                    }
                }
            }
            serverTuple.description = newDescription;
            this.updateDescription();
        }
        this.fireChangeEvent();
    }
    
    private void handleReplicaSetMemberChanged(final ServerDescription newDescription) {
        if (!newDescription.isReplicaSetMember()) {
            MultiServerCluster.LOGGER.severe(String.format("Expecting replica set member, but found a %s.  Removing %s from client view of cluster.", newDescription.getType(), newDescription.getAddress()));
            this.removeServer(newDescription.getAddress());
            return;
        }
        if (newDescription.getType() == ServerType.ReplicaSetGhost) {
            MultiServerCluster.LOGGER.info(String.format("Server %s does not appear to be a member of an initiated replica set.", newDescription.getAddress()));
            return;
        }
        if (this.replicaSetName == null) {
            this.replicaSetName = newDescription.getSetName();
        }
        if (!this.replicaSetName.equals(newDescription.getSetName())) {
            MultiServerCluster.LOGGER.severe(String.format("Expecting replica set member from set '%s', but found one from set '%s'.  Removing %s from client view of cluster.", this.replicaSetName, newDescription.getSetName(), newDescription.getAddress()));
            this.removeServer(newDescription.getAddress());
            return;
        }
        this.ensureServers(newDescription);
        if (newDescription.isPrimary()) {
            if (this.isNotAlreadyPrimary(newDescription.getAddress())) {
                MultiServerCluster.LOGGER.info(String.format("Discovered replica set primary %s", newDescription.getAddress()));
            }
            this.invalidateOldPrimaries(newDescription.getAddress());
        }
    }
    
    private boolean isNotAlreadyPrimary(final ServerAddress address) {
        final ServerTuple serverTuple = this.addressToServerTupleMap.get(address);
        return serverTuple == null || !serverTuple.description.isPrimary();
    }
    
    private void handleShardRouterChanged(final ServerDescription newDescription) {
        if (newDescription.getClusterType() != ClusterType.Sharded) {
            MultiServerCluster.LOGGER.severe(String.format("Expecting a %s, but found a %s.  Removing %s from client view of cluster.", ServerType.ShardRouter, newDescription.getType(), newDescription.getAddress()));
            this.removeServer(newDescription.getAddress());
        }
    }
    
    private void handleStandAloneChanged(final ServerDescription newDescription) {
        if (this.getSettings().getHosts().size() > 1) {
            MultiServerCluster.LOGGER.severe(String.format("Expecting a single %s, but found more than one.  Removing %s from client view of cluster.", ServerType.StandAlone, newDescription.getAddress()));
            this.clusterType = ClusterType.Unknown;
            this.removeServer(newDescription.getAddress());
        }
    }
    
    private void addServer(final ServerAddress serverAddress) {
        if (!this.addressToServerTupleMap.containsKey(serverAddress)) {
            MultiServerCluster.LOGGER.info(String.format("Adding discovered server %s to client view of cluster", serverAddress));
            final ClusterableServer server = this.createServer(serverAddress, new DefaultServerStateListener());
            this.addressToServerTupleMap.put(serverAddress, new ServerTuple(server, this.getConnectingServerDescription(serverAddress)));
        }
    }
    
    private void removeServer(final ServerAddress serverAddress) {
        this.addressToServerTupleMap.remove(serverAddress).server.close();
    }
    
    private void invalidateOldPrimaries(final ServerAddress newPrimary) {
        for (final ServerTuple serverTuple : this.addressToServerTupleMap.values()) {
            if (!serverTuple.description.getAddress().equals(newPrimary) && serverTuple.description.isPrimary()) {
                MultiServerCluster.LOGGER.info(String.format("Rediscovering type of existing primary %s", serverTuple.description.getAddress()));
                serverTuple.server.invalidate();
            }
        }
    }
    
    private ServerDescription getConnectingServerDescription(final ServerAddress serverAddress) {
        return ServerDescription.builder().state(ServerConnectionState.Connecting).address(serverAddress).build();
    }
    
    private void updateDescription() {
        final List<ServerDescription> newServerDescriptionList = this.getNewServerDescriptionList();
        this.updateDescription(new ClusterDescription(ClusterConnectionMode.Multiple, this.clusterType, newServerDescriptionList));
    }
    
    private List<ServerDescription> getNewServerDescriptionList() {
        final List<ServerDescription> serverDescriptions = new ArrayList<ServerDescription>();
        for (final ServerTuple cur : this.addressToServerTupleMap.values()) {
            serverDescriptions.add(cur.description);
        }
        return serverDescriptions;
    }
    
    private void ensureServers(final ServerDescription description) {
        if (description.isPrimary() || !this.hasPrimary()) {
            this.addNewHosts(description.getHosts());
            this.addNewHosts(description.getPassives());
            this.addNewHosts(description.getArbiters());
        }
        if (description.isPrimary()) {
            this.removeExtraHosts(description);
        }
    }
    
    private boolean hasPrimary() {
        for (final ServerTuple serverTuple : this.addressToServerTupleMap.values()) {
            if (serverTuple.description.isPrimary()) {
                return true;
            }
        }
        return false;
    }
    
    private void addNewHosts(final Set<String> hosts) {
        for (final String cur : hosts) {
            try {
                this.addServer(new ServerAddress(cur));
            }
            catch (UnknownHostException ex) {}
        }
    }
    
    private void removeExtraHosts(final ServerDescription serverDescription) {
        final Set<ServerAddress> allServerAddresses = this.getAllServerAddresses(serverDescription);
        for (final ServerTuple cur : this.addressToServerTupleMap.values()) {
            if (!allServerAddresses.contains(cur.description.getAddress())) {
                MultiServerCluster.LOGGER.info(String.format("Server %s is no longer a member of the replica set.  Removing from client view of cluster.", cur.description.getAddress()));
                this.removeServer(cur.description.getAddress());
            }
        }
    }
    
    private Set<ServerAddress> getAllServerAddresses(final ServerDescription serverDescription) {
        final Set<ServerAddress> retVal = new HashSet<ServerAddress>();
        this.addHostsToSet(serverDescription.getHosts(), retVal);
        this.addHostsToSet(serverDescription.getPassives(), retVal);
        this.addHostsToSet(serverDescription.getArbiters(), retVal);
        return retVal;
    }
    
    private void addHostsToSet(final Set<String> hosts, final Set<ServerAddress> retVal) {
        for (final String host : hosts) {
            try {
                retVal.add(new ServerAddress(host));
            }
            catch (UnknownHostException ex) {}
        }
    }
    
    static {
        LOGGER = Loggers.getLogger("cluster");
    }
    
    private static final class ServerTuple
    {
        private final ClusterableServer server;
        private ServerDescription description;
        
        private ServerTuple(final ClusterableServer server, final ServerDescription description) {
            super();
            this.server = server;
            this.description = description;
        }
    }
    
    private final class DefaultServerStateListener implements ChangeListener<ServerDescription>
    {
        public void stateChanged(final ChangeEvent<ServerDescription> event) {
            MultiServerCluster.this.onChange(event);
        }
    }
}
