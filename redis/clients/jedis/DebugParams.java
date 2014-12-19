package redis.clients.jedis;

public class DebugParams
{
    private String[] command;
    
    public String[] getCommand() {
        return this.command;
    }
    
    public static DebugParams SEGFAULT() {
        final DebugParams debugParams = new DebugParams();
        debugParams.command = new String[] { "SEGFAULT" };
        return debugParams;
    }
    
    public static DebugParams OBJECT(final String key) {
        final DebugParams debugParams = new DebugParams();
        debugParams.command = new String[] { "OBJECT", key };
        return debugParams;
    }
    
    public static DebugParams RELOAD() {
        final DebugParams debugParams = new DebugParams();
        debugParams.command = new String[] { "RELOAD" };
        return debugParams;
    }
}
