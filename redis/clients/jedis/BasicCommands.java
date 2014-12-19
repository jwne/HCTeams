package redis.clients.jedis;

public interface BasicCommands
{
    String ping();
    
    String quit();
    
    String flushDB();
    
    Long dbSize();
    
    String select(int p0);
    
    String flushAll();
    
    String auth(String p0);
    
    String save();
    
    String bgsave();
    
    String bgrewriteaof();
    
    Long lastsave();
    
    String shutdown();
    
    String info();
    
    String info(String p0);
    
    String slaveof(String p0, int p1);
    
    String slaveofNoOne();
    
    Long getDB();
    
    String debug(DebugParams p0);
    
    String configResetStat();
    
    Long waitReplicas(int p0, long p1);
}
