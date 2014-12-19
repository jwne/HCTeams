package redis.clients.util;

import java.io.*;
import java.util.*;

public class JedisByteHashMap implements Map<byte[], byte[]>, Cloneable, Serializable
{
    private static final long serialVersionUID = -6971431362627219416L;
    private Map<ByteArrayWrapper, byte[]> internalMap;
    
    public JedisByteHashMap() {
        super();
        this.internalMap = new HashMap<ByteArrayWrapper, byte[]>();
    }
    
    @Override
    public void clear() {
        this.internalMap.clear();
    }
    
    @Override
    public boolean containsKey(final Object key) {
        if (key instanceof byte[]) {
            return this.internalMap.containsKey(new ByteArrayWrapper((byte[])key));
        }
        return this.internalMap.containsKey(key);
    }
    
    @Override
    public boolean containsValue(final Object value) {
        return this.internalMap.containsValue(value);
    }
    
    @Override
    public Set<Entry<byte[], byte[]>> entrySet() {
        final Iterator<Entry<ByteArrayWrapper, byte[]>> iterator = this.internalMap.entrySet().iterator();
        final HashSet<Entry<byte[], byte[]>> hashSet = new HashSet<Entry<byte[], byte[]>>();
        while (iterator.hasNext()) {
            final Entry<ByteArrayWrapper, byte[]> entry = iterator.next();
            hashSet.add(new JedisByteEntry(entry.getKey().data, entry.getValue()));
        }
        return hashSet;
    }
    
    @Override
    public byte[] get(final Object key) {
        if (key instanceof byte[]) {
            return this.internalMap.get(new ByteArrayWrapper((byte[])key));
        }
        return this.internalMap.get(key);
    }
    
    @Override
    public boolean isEmpty() {
        return this.internalMap.isEmpty();
    }
    
    @Override
    public Set<byte[]> keySet() {
        final Set<byte[]> keySet = new HashSet<byte[]>();
        final Iterator<ByteArrayWrapper> iterator = this.internalMap.keySet().iterator();
        while (iterator.hasNext()) {
            keySet.add(iterator.next().data);
        }
        return keySet;
    }
    
    @Override
    public byte[] put(final byte[] key, final byte[] value) {
        return this.internalMap.put(new ByteArrayWrapper(key), value);
    }
    
    @Override
    public void putAll(final Map<? extends byte[], ? extends byte[]> m) {
        for (final Entry<? extends byte[], ? extends byte[]> next : m.entrySet()) {
            this.internalMap.put(new ByteArrayWrapper((byte[])(Object)next.getKey()), (byte[])(Object)next.getValue());
        }
    }
    
    @Override
    public byte[] remove(final Object key) {
        if (key instanceof byte[]) {
            return this.internalMap.remove(new ByteArrayWrapper((byte[])key));
        }
        return this.internalMap.remove(key);
    }
    
    @Override
    public int size() {
        return this.internalMap.size();
    }
    
    @Override
    public Collection<byte[]> values() {
        return this.internalMap.values();
    }
    
    private static final class ByteArrayWrapper
    {
        private final byte[] data;
        
        public ByteArrayWrapper(final byte[] data) {
            super();
            if (data == null) {
                throw new NullPointerException();
            }
            this.data = data;
        }
        
        @Override
        public boolean equals(final Object other) {
            return other instanceof ByteArrayWrapper && Arrays.equals(this.data, ((ByteArrayWrapper)other).data);
        }
        
        @Override
        public int hashCode() {
            return Arrays.hashCode(this.data);
        }
    }
    
    private static final class JedisByteEntry implements Entry<byte[], byte[]>
    {
        private byte[] value;
        private byte[] key;
        
        public JedisByteEntry(final byte[] key, final byte[] value) {
            super();
            this.key = key;
            this.value = value;
        }
        
        @Override
        public byte[] getKey() {
            return this.key;
        }
        
        @Override
        public byte[] getValue() {
            return this.value;
        }
        
        @Override
        public byte[] setValue(final byte[] value) {
            return this.value = value;
        }
    }
}
