package com.mongodb;

import java.util.*;

class ServerAddressSelector implements ServerSelector
{
    private final ServerAddress address;
    
    public ServerAddressSelector(final ServerAddress address) {
        super();
        this.address = address;
    }
    
    public List<ServerDescription> choose(final ClusterDescription clusterDescription) {
        return Arrays.asList(clusterDescription.getByServerAddress(this.address));
    }
}
