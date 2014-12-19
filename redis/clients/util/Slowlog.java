package redis.clients.util;

import java.util.*;

public class Slowlog
{
    private final long id;
    private final long timeStamp;
    private final long executionTime;
    private final List<String> args;
    
    public static List<Slowlog> from(final List<Object> nestedMultiBulkReply) {
        final List<Slowlog> logs = new ArrayList<Slowlog>(nestedMultiBulkReply.size());
        for (final Object obj : nestedMultiBulkReply) {
            final List<Object> properties = (List<Object>)obj;
            logs.add(new Slowlog(properties));
        }
        return logs;
    }
    
    private Slowlog(final List<Object> properties) {
        super();
        this.id = properties.get(0);
        this.timeStamp = properties.get(1);
        this.executionTime = properties.get(2);
        final List<byte[]> bargs = properties.get(3);
        this.args = new ArrayList<String>(bargs.size());
        for (final byte[] barg : bargs) {
            this.args.add(SafeEncoder.encode(barg));
        }
    }
    
    public long getId() {
        return this.id;
    }
    
    public long getTimeStamp() {
        return this.timeStamp;
    }
    
    public long getExecutionTime() {
        return this.executionTime;
    }
    
    public List<String> getArgs() {
        return this.args;
    }
}
