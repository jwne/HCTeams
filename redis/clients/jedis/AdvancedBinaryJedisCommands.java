package redis.clients.jedis;

import java.util.*;

public interface AdvancedBinaryJedisCommands
{
    List<byte[]> configGet(byte[] p0);
    
    byte[] configSet(byte[] p0, byte[] p1);
    
    String slowlogReset();
    
    Long slowlogLen();
    
    List<byte[]> slowlogGetBinary();
    
    List<byte[]> slowlogGetBinary(long p0);
    
    Long objectRefcount(byte[] p0);
    
    byte[] objectEncoding(byte[] p0);
    
    Long objectIdletime(byte[] p0);
}
