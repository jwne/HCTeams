package com.mongodb;

final class Clusters
{
    public static Cluster create(final String clusterId, final ClusterSettings settings, final ServerSettings serverSettings, final ClusterListener clusterListener, final Mongo mongo) {
        final ClusterableServerFactory serverFactory = new DefaultClusterableServerFactory(clusterId, serverSettings, mongo);
        if (settings.getMode() == ClusterConnectionMode.Single) {
            return new SingleServerCluster(clusterId, settings, serverFactory, (clusterListener != null) ? clusterListener : new NoOpClusterListener());
        }
        if (settings.getMode() == ClusterConnectionMode.Multiple) {
            return new MultiServerCluster(clusterId, settings, serverFactory, (clusterListener != null) ? clusterListener : new NoOpClusterListener());
        }
        throw new UnsupportedOperationException("Unsupported cluster mode: " + settings.getMode());
    }
}
