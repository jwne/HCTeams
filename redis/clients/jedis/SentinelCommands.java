package redis.clients.jedis;

import java.util.*;

public interface SentinelCommands
{
    List<Map<String, String>> sentinelMasters();
    
    List<String> sentinelGetMasterAddrByName(String p0);
    
    Long sentinelReset(String p0);
    
    List<Map<String, String>> sentinelSlaves(String p0);
    
    String sentinelFailover(String p0);
    
    String sentinelMonitor(String p0, String p1, int p2, int p3);
    
    String sentinelRemove(String p0);
    
    String sentinelSet(String p0, Map<String, String> p1);
}
