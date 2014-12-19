package redis.clients.jedis;

import java.util.*;

public class BitPosParams
{
    private List<byte[]> params;
    
    protected BitPosParams() {
        super();
        this.params = new ArrayList<byte[]>();
    }
    
    public BitPosParams(final long start) {
        super();
        (this.params = new ArrayList<byte[]>()).add(Protocol.toByteArray(start));
    }
    
    public BitPosParams(final long start, final long end) {
        this(start);
        this.params.add(Protocol.toByteArray(end));
    }
    
    public Collection<byte[]> getParams() {
        return Collections.unmodifiableCollection((Collection<? extends byte[]>)this.params);
    }
}
