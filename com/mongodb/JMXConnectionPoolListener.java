package com.mongodb;

import java.util.concurrent.*;
import com.mongodb.util.management.*;

class JMXConnectionPoolListener implements ConnectionPoolListener
{
    private final ConcurrentMap<ClusterIdServerAddressPair, ConnectionPoolStatistics> map;
    private final String clusterDescription;
    
    public JMXConnectionPoolListener(final String clusterDescription) {
        super();
        this.map = new ConcurrentHashMap<ClusterIdServerAddressPair, ConnectionPoolStatistics>();
        this.clusterDescription = clusterDescription;
    }
    
    public String getMBeanObjectName(final String clusterId, final ServerAddress serverAddress) {
        final String adjustedClusterId = clusterId.replace(":", "%3A");
        final String adjustedHost = serverAddress.getHost().replace(":", "%3A");
        String objectName = String.format("org.mongodb.driver:type=ConnectionPool,clusterId=%s,host=%s,port=%s", adjustedClusterId, adjustedHost, serverAddress.getPort());
        if (this.clusterDescription != null) {
            final String adjustedClusterDescription = this.clusterDescription.replace(":", "%3A");
            objectName += String.format(",description=%s", adjustedClusterDescription);
        }
        return objectName;
    }
    
    public ConnectionPoolStatisticsMBean getMBean(final String clusterId, final ServerAddress serverAddress) {
        return this.getStatistics(clusterId, serverAddress);
    }
    
    public void connectionPoolOpened(final ConnectionPoolOpenedEvent event) {
        final ConnectionPoolStatistics statistics = new ConnectionPoolStatistics(event);
        this.map.put(new ClusterIdServerAddressPair(event.getClusterId(), event.getServerAddress()), statistics);
        MBeanServerFactory.getMBeanServer().registerMBean(statistics, this.getMBeanObjectName(event.getClusterId(), event.getServerAddress()));
    }
    
    public void connectionPoolClosed(final ConnectionPoolEvent event) {
        this.map.remove(new ClusterIdServerAddressPair(event.getClusterId(), event.getServerAddress()));
        MBeanServerFactory.getMBeanServer().unregisterMBean(this.getMBeanObjectName(event.getClusterId(), event.getServerAddress()));
    }
    
    public void connectionCheckedOut(final ConnectionEvent event) {
        final ConnectionPoolStatistics statistics = this.getStatistics(event);
        if (statistics != null) {
            statistics.connectionCheckedOut(event);
        }
    }
    
    public void connectionCheckedIn(final ConnectionEvent event) {
        final ConnectionPoolStatistics statistics = this.getStatistics(event);
        if (statistics != null) {
            statistics.connectionCheckedIn(event);
        }
    }
    
    public void waitQueueEntered(final ConnectionPoolWaitQueueEvent event) {
        final ConnectionPoolListener statistics = this.getStatistics(event);
        if (statistics != null) {
            statistics.waitQueueEntered(event);
        }
    }
    
    public void waitQueueExited(final ConnectionPoolWaitQueueEvent event) {
        final ConnectionPoolListener statistics = this.getStatistics(event);
        if (statistics != null) {
            statistics.waitQueueExited(event);
        }
    }
    
    public void connectionAdded(final ConnectionEvent event) {
        final ConnectionPoolStatistics statistics = this.getStatistics(event);
        if (statistics != null) {
            statistics.connectionAdded(event);
        }
    }
    
    public void connectionRemoved(final ConnectionEvent event) {
        final ConnectionPoolStatistics statistics = this.getStatistics(event);
        if (statistics != null) {
            statistics.connectionRemoved(event);
        }
    }
    
    private ConnectionPoolStatistics getStatistics(final ConnectionEvent event) {
        return this.getStatistics(event.getClusterId(), event.getServerAddress());
    }
    
    private ConnectionPoolListener getStatistics(final ConnectionPoolEvent event) {
        return this.getStatistics(event.getClusterId(), event.getServerAddress());
    }
    
    private ConnectionPoolStatistics getStatistics(final String clusterId, final ServerAddress serverAddress) {
        return this.map.get(new ClusterIdServerAddressPair(clusterId, serverAddress));
    }
    
    private static final class ClusterIdServerAddressPair
    {
        private final String clusterId;
        private final ServerAddress serverAddress;
        
        private ClusterIdServerAddressPair(final String clusterId, final ServerAddress serverAddress) {
            super();
            this.clusterId = clusterId;
            this.serverAddress = serverAddress;
        }
        
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || this.getClass() != o.getClass()) {
                return false;
            }
            final ClusterIdServerAddressPair that = (ClusterIdServerAddressPair)o;
            return this.clusterId.equals(that.clusterId) && this.serverAddress.equals(that.serverAddress);
        }
        
        public int hashCode() {
            int result = this.clusterId.hashCode();
            result = 31 * result + this.serverAddress.hashCode();
            return result;
        }
    }
}
