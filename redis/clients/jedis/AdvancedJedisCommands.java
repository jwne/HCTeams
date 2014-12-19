package redis.clients.jedis;

import java.util.*;
import redis.clients.util.*;

public interface AdvancedJedisCommands
{
    List<String> configGet(String p0);
    
    String configSet(String p0, String p1);
    
    String slowlogReset();
    
    Long slowlogLen();
    
    List<Slowlog> slowlogGet();
    
    List<Slowlog> slowlogGet(long p0);
    
    Long objectRefcount(String p0);
    
    String objectEncoding(String p0);
    
    Long objectIdletime(String p0);
}
