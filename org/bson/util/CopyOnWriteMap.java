package org.bson.util;

import org.bson.util.annotations.*;
import java.util.*;

@ThreadSafe
abstract class CopyOnWriteMap<K, V> extends AbstractCopyOnWriteMap<K, V, Map<K, V>>
{
    private static final long serialVersionUID = 7935514534647505917L;
    
    public static <K, V> Builder<K, V> builder() {
        return new Builder<K, V>();
    }
    
    public static <K, V> CopyOnWriteMap<K, V> newHashMap() {
        final Builder<K, V> builder = builder();
        return builder.newHashMap();
    }
    
    public static <K, V> CopyOnWriteMap<K, V> newHashMap(final Map<? extends K, ? extends V> map) {
        final Builder<K, V> builder = builder();
        return builder.addAll(map).newHashMap();
    }
    
    public static <K, V> CopyOnWriteMap<K, V> newLinkedMap() {
        final Builder<K, V> builder = builder();
        return builder.newLinkedMap();
    }
    
    public static <K, V> CopyOnWriteMap<K, V> newLinkedMap(final Map<? extends K, ? extends V> map) {
        final Builder<K, V> builder = builder();
        return builder.addAll(map).newLinkedMap();
    }
    
    protected CopyOnWriteMap(final Map<? extends K, ? extends V> map) {
        this(map, View.Type.LIVE);
    }
    
    protected CopyOnWriteMap() {
        this(Collections.emptyMap(), View.Type.LIVE);
    }
    
    protected CopyOnWriteMap(final Map<? extends K, ? extends V> map, final View.Type viewType) {
        super(map, viewType);
    }
    
    protected CopyOnWriteMap(final View.Type viewType) {
        super(Collections.emptyMap(), viewType);
    }
    
    @GuardedBy("internal-lock")
    protected abstract <N extends Map<? extends K, ? extends V>> Map<K, V> copy(final N p0);
    
    public static class Builder<K, V>
    {
        private View.Type viewType;
        private final Map<K, V> initialValues;
        
        Builder() {
            super();
            this.viewType = View.Type.STABLE;
            this.initialValues = new HashMap<K, V>();
        }
        
        public Builder<K, V> stableViews() {
            this.viewType = View.Type.STABLE;
            return this;
        }
        
        public Builder<K, V> addAll(final Map<? extends K, ? extends V> values) {
            this.initialValues.putAll(values);
            return this;
        }
        
        public Builder<K, V> liveViews() {
            this.viewType = View.Type.LIVE;
            return this;
        }
        
        public CopyOnWriteMap<K, V> newHashMap() {
            return new Hash<K, V>((Map<? extends K, ? extends V>)this.initialValues, this.viewType);
        }
        
        public CopyOnWriteMap<K, V> newLinkedMap() {
            return new Linked<K, V>((Map<? extends K, ? extends V>)this.initialValues, this.viewType);
        }
    }
    
    static class Hash<K, V> extends CopyOnWriteMap<K, V>
    {
        private static final long serialVersionUID = 5221824943734164497L;
        
        Hash(final Map<? extends K, ? extends V> map, final View.Type viewType) {
            super(map, viewType);
        }
        
        public <N extends Map<? extends K, ? extends V>> Map<K, V> copy(final N map) {
            return new HashMap<K, V>(map);
        }
    }
    
    static class Linked<K, V> extends CopyOnWriteMap<K, V>
    {
        private static final long serialVersionUID = -8659999465009072124L;
        
        Linked(final Map<? extends K, ? extends V> map, final View.Type viewType) {
            super(map, viewType);
        }
        
        public <N extends Map<? extends K, ? extends V>> Map<K, V> copy(final N map) {
            return new LinkedHashMap<K, V>(map);
        }
    }
}
