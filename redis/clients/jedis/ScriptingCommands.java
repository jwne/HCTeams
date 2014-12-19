package redis.clients.jedis;

import java.util.*;

public interface ScriptingCommands
{
    Object eval(String p0, int p1, String... p2);
    
    Object eval(String p0, List<String> p1, List<String> p2);
    
    Object eval(String p0);
    
    Object evalsha(String p0);
    
    Object evalsha(String p0, List<String> p1, List<String> p2);
    
    Object evalsha(String p0, int p1, String... p2);
    
    Boolean scriptExists(String p0);
    
    List<Boolean> scriptExists(String... p0);
    
    String scriptLoad(String p0);
}
