package redis.clients.jedis;

import java.util.*;

public interface ClusterPipeline
{
    Response<String> clusterNodes();
    
    Response<String> clusterMeet(String p0, int p1);
    
    Response<String> clusterAddSlots(int... p0);
    
    Response<String> clusterDelSlots(int... p0);
    
    Response<String> clusterInfo();
    
    Response<List<String>> clusterGetKeysInSlot(int p0, int p1);
    
    Response<String> clusterSetSlotNode(int p0, String p1);
    
    Response<String> clusterSetSlotMigrating(int p0, String p1);
    
    Response<String> clusterSetSlotImporting(int p0, String p1);
}
