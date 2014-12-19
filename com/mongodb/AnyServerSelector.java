package com.mongodb;

import java.util.*;

class AnyServerSelector implements ServerSelector
{
    public List<ServerDescription> choose(final ClusterDescription clusterDescription) {
        return clusterDescription.getAny();
    }
    
    public String toString() {
        return "AnyServerSelector{}";
    }
}
