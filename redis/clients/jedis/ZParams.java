package redis.clients.jedis;

import java.util.*;
import redis.clients.util.*;

public class ZParams
{
    private List<byte[]> params;
    
    public ZParams() {
        super();
        this.params = new ArrayList<byte[]>();
    }
    
    @Deprecated
    public ZParams weights(final int... weights) {
        this.params.add(Protocol.Keyword.WEIGHTS.raw);
        for (final int weight : weights) {
            this.params.add(Protocol.toByteArray(weight));
        }
        return this;
    }
    
    public ZParams weightsByDouble(final double... weights) {
        this.params.add(Protocol.Keyword.WEIGHTS.raw);
        for (final double weight : weights) {
            this.params.add(Protocol.toByteArray(weight));
        }
        return this;
    }
    
    public Collection<byte[]> getParams() {
        return Collections.unmodifiableCollection((Collection<? extends byte[]>)this.params);
    }
    
    public ZParams aggregate(final Aggregate aggregate) {
        this.params.add(Protocol.Keyword.AGGREGATE.raw);
        this.params.add(aggregate.raw);
        return this;
    }
    
    public enum Aggregate
    {
        SUM, 
        MIN, 
        MAX;
        
        public final byte[] raw;
        
        private Aggregate(final int n) {
            this.raw = SafeEncoder.encode(this.name());
        }
    }
}
