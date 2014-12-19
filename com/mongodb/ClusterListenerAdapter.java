package com.mongodb;

abstract class ClusterListenerAdapter implements ClusterListener
{
    public void clusterOpened(final ClusterEvent event) {
    }
    
    public void clusterClosed(final ClusterEvent event) {
    }
    
    public void clusterDescriptionChanged(final ClusterDescriptionChangedEvent event) {
    }
}
