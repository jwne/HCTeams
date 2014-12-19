package org.bson.util;

import java.util.concurrent.*;
import java.util.*;

final class ComputingMap<K, V> implements Map<K, V>, Function<K, V>
{
    private final ConcurrentMap<K, V> map;
    private final Function<K, V> function;
    
    public static <K, V> Map<K, V> create(final Function<K, V> function) {
        return new ComputingMap<K, V>((ConcurrentMap<K, V>)CopyOnWriteMap.newHashMap(), function);
    }
    
    ComputingMap(final ConcurrentMap<K, V> map, final Function<K, V> function) {
        super();
        this.map = Assertions.notNull("map", map);
        this.function = Assertions.notNull("function", function);
    }
    
    public V get(final Object key) {
        while (true) {
            final V v = this.map.get(key);
            if (v != null) {
                return v;
            }
            final V value = this.function.apply((K)key);
            if (value == null) {
                return null;
            }
            this.map.putIfAbsent((K)key, value);
        }
    }
    
    public V apply(final K k) {
        return this.get(k);
    }
    
    public V putIfAbsent(final K key, final V value) {
        return this.map.putIfAbsent(key, value);
    }
    
    public boolean remove(final Object key, final Object value) {
        return this.map.remove(key, value);
    }
    
    public boolean replace(final K key, final V oldValue, final V newValue) {
        return this.map.replace(key, oldValue, newValue);
    }
    
    public V replace(final K key, final V value) {
        return this.map.replace(key, value);
    }
    
    public int size() {
        return this.map.size();
    }
    
    public boolean isEmpty() {
        return this.map.isEmpty();
    }
    
    public boolean containsKey(final Object key) {
        return this.map.containsKey(key);
    }
    
    public boolean containsValue(final Object value) {
        return this.map.containsValue(value);
    }
    
    public V put(final K key, final V value) {
        return this.map.put(key, value);
    }
    
    public V remove(final Object key) {
        return this.map.remove(key);
    }
    
    public void putAll(final Map<? extends K, ? extends V> m) {
        this.map.putAll((Map<?, ?>)m);
    }
    
    public void clear() {
        this.map.clear();
    }
    
    public Set<K> keySet() {
        return this.map.keySet();
    }
    
    public Collection<V> values() {
        return this.map.values();
    }
    
    public Set<Entry<K, V>> entrySet() {
        return this.map.entrySet();
    }
    
    public boolean equals(final Object o) {
        return this.map.equals(o);
    }
    
    public int hashCode() {
        return this.map.hashCode();
    }
}
