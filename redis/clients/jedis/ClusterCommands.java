package redis.clients.jedis;

import java.util.*;

public interface ClusterCommands
{
    String clusterNodes();
    
    String clusterMeet(String p0, int p1);
    
    String clusterAddSlots(int... p0);
    
    String clusterDelSlots(int... p0);
    
    String clusterInfo();
    
    List<String> clusterGetKeysInSlot(int p0, int p1);
    
    String clusterSetSlotNode(int p0, String p1);
    
    String clusterSetSlotMigrating(int p0, String p1);
    
    String clusterSetSlotImporting(int p0, String p1);
    
    String clusterSetSlotStable(int p0);
    
    String clusterForget(String p0);
    
    String clusterFlushSlots();
    
    Long clusterKeySlot(String p0);
    
    Long clusterCountKeysInSlot(int p0);
    
    String clusterSaveConfig();
    
    String clusterReplicate(String p0);
    
    List<String> clusterSlaves(String p0);
    
    String clusterFailover();
    
    List<Object> clusterSlots();
    
    String clusterReset(JedisCluster.Reset p0);
}
