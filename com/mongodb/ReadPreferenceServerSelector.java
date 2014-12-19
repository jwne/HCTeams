package com.mongodb;

import java.util.*;

class ReadPreferenceServerSelector implements ServerSelector
{
    private final ReadPreference readPreference;
    
    public ReadPreferenceServerSelector(final ReadPreference readPreference) {
        super();
        this.readPreference = readPreference;
    }
    
    public ReadPreference getReadPreference() {
        return this.readPreference;
    }
    
    public List<ServerDescription> choose(final ClusterDescription clusterDescription) {
        return this.readPreference.choose(clusterDescription);
    }
    
    public String toString() {
        return "ReadPreferenceServerSelector{readPreference=" + this.readPreference + '}';
    }
}
