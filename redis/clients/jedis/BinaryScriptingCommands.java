package redis.clients.jedis;

import java.util.*;

public interface BinaryScriptingCommands
{
    Object eval(byte[] p0, byte[] p1, byte[]... p2);
    
    Object eval(byte[] p0, int p1, byte[]... p2);
    
    Object eval(byte[] p0, List<byte[]> p1, List<byte[]> p2);
    
    Object eval(byte[] p0);
    
    Object evalsha(byte[] p0);
    
    Object evalsha(byte[] p0, List<byte[]> p1, List<byte[]> p2);
    
    Object evalsha(byte[] p0, int p1, byte[]... p2);
    
    List<Long> scriptExists(byte[]... p0);
    
    byte[] scriptLoad(byte[] p0);
    
    String scriptFlush();
    
    String scriptKill();
}
