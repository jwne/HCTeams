package redis.clients.jedis;

import redis.clients.util.*;
import java.util.*;

public class SortingParams
{
    private List<byte[]> params;
    
    public SortingParams() {
        super();
        this.params = new ArrayList<byte[]>();
    }
    
    public SortingParams by(final String pattern) {
        return this.by(SafeEncoder.encode(pattern));
    }
    
    public SortingParams by(final byte[] pattern) {
        this.params.add(Protocol.Keyword.BY.raw);
        this.params.add(pattern);
        return this;
    }
    
    public SortingParams nosort() {
        this.params.add(Protocol.Keyword.BY.raw);
        this.params.add(Protocol.Keyword.NOSORT.raw);
        return this;
    }
    
    public Collection<byte[]> getParams() {
        return Collections.unmodifiableCollection((Collection<? extends byte[]>)this.params);
    }
    
    public SortingParams desc() {
        this.params.add(Protocol.Keyword.DESC.raw);
        return this;
    }
    
    public SortingParams asc() {
        this.params.add(Protocol.Keyword.ASC.raw);
        return this;
    }
    
    public SortingParams limit(final int start, final int count) {
        this.params.add(Protocol.Keyword.LIMIT.raw);
        this.params.add(Protocol.toByteArray(start));
        this.params.add(Protocol.toByteArray(count));
        return this;
    }
    
    public SortingParams alpha() {
        this.params.add(Protocol.Keyword.ALPHA.raw);
        return this;
    }
    
    public SortingParams get(final String... patterns) {
        for (final String pattern : patterns) {
            this.params.add(Protocol.Keyword.GET.raw);
            this.params.add(SafeEncoder.encode(pattern));
        }
        return this;
    }
    
    public SortingParams get(final byte[]... patterns) {
        for (final byte[] pattern : patterns) {
            this.params.add(Protocol.Keyword.GET.raw);
            this.params.add(pattern);
        }
        return this;
    }
}
