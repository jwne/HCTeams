package com.mongodb;

import java.util.*;

class CompositeServerSelector implements ServerSelector
{
    private final List<ServerSelector> serverSelectors;
    
    CompositeServerSelector(final List<ServerSelector> serverSelectors) {
        super();
        if (serverSelectors.isEmpty()) {
            throw new IllegalArgumentException("Server selectors can not be an empty list");
        }
        this.serverSelectors = new ArrayList<ServerSelector>(serverSelectors);
    }
    
    public List<ServerDescription> choose(final ClusterDescription clusterDescription) {
        ClusterDescription curClusterDescription = clusterDescription;
        List<ServerDescription> choices = null;
        for (final ServerSelector cur : this.serverSelectors) {
            choices = cur.choose(curClusterDescription);
            curClusterDescription = new ClusterDescription(clusterDescription.getConnectionMode(), clusterDescription.getType(), choices);
        }
        return choices;
    }
    
    public String toString() {
        return "{serverSelectors=" + this.serverSelectors + '}';
    }
}
