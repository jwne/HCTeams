package com.mongodb;

enum ServerType
{
    StandAlone {
        public ClusterType getClusterType() {
            return ClusterType.StandAlone;
        }
    }, 
    ReplicaSetPrimary {
        public ClusterType getClusterType() {
            return ClusterType.ReplicaSet;
        }
    }, 
    ReplicaSetSecondary {
        public ClusterType getClusterType() {
            return ClusterType.ReplicaSet;
        }
    }, 
    ReplicaSetArbiter {
        public ClusterType getClusterType() {
            return ClusterType.ReplicaSet;
        }
    }, 
    ReplicaSetOther {
        public ClusterType getClusterType() {
            return ClusterType.ReplicaSet;
        }
    }, 
    ReplicaSetGhost {
        public ClusterType getClusterType() {
            return ClusterType.ReplicaSet;
        }
    }, 
    ShardRouter {
        public ClusterType getClusterType() {
            return ClusterType.Sharded;
        }
    }, 
    Unknown {
        public ClusterType getClusterType() {
            return ClusterType.Unknown;
        }
    };
    
    public abstract ClusterType getClusterType();
}
