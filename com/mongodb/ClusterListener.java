package com.mongodb;

import java.util.*;

interface ClusterListener extends EventListener
{
    void clusterOpened(ClusterEvent p0);
    
    void clusterClosed(ClusterEvent p0);
    
    void clusterDescriptionChanged(ClusterDescriptionChangedEvent p0);
}
