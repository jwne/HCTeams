package com.mongodb.util;

import java.util.*;

@Deprecated
public class IdentitySet<T> implements Iterable<T>
{
    final IdentityHashMap<T, String> _map;
    
    public IdentitySet() {
        super();
        this._map = new IdentityHashMap<T, String>();
    }
    
    public IdentitySet(final Iterable<T> copy) {
        super();
        this._map = new IdentityHashMap<T, String>();
        for (final T t : copy) {
            this.add(t);
        }
    }
    
    public boolean add(final T t) {
        return this._map.put(t, "a") == null;
    }
    
    public boolean contains(final T t) {
        return this._map.containsKey(t);
    }
    
    public void remove(final T t) {
        this._map.remove(t);
    }
    
    public void removeall(final Iterable<T> coll) {
        for (final T t : coll) {
            this._map.remove(t);
        }
    }
    
    public void clear() {
        this._map.clear();
    }
    
    public int size() {
        return this._map.size();
    }
    
    public Iterator<T> iterator() {
        return this._map.keySet().iterator();
    }
    
    public void addAll(final Collection<T> c) {
        for (final T t : c) {
            this.add(t);
        }
    }
    
    public void addAll(final IdentitySet<T> c) {
        for (final T t : c) {
            this.add(t);
        }
    }
    
    public void removeAll(final Iterable<T> prev) {
        for (final T t : prev) {
            this.remove(t);
        }
    }
}
