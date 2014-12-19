package org.bson.util;

import java.util.*;

public class ClassMap<T>
{
    private final Map<Class<?>, T> map;
    private final Map<Class<?>, T> cache;
    
    public ClassMap() {
        super();
        this.map = (Map<Class<?>, T>)CopyOnWriteMap.newHashMap();
        this.cache = ComputingMap.create((Function<Class<?>, T>)new ComputeFunction());
    }
    
    public static <T> List<Class<?>> getAncestry(final Class<T> clazz) {
        return ClassAncestry.getAncestry(clazz);
    }
    
    public T get(final Object key) {
        return this.cache.get(key);
    }
    
    public T put(final Class<?> key, final T value) {
        try {
            return this.map.put(key, value);
        }
        finally {
            this.cache.clear();
        }
    }
    
    public T remove(final Object key) {
        try {
            return this.map.remove(key);
        }
        finally {
            this.cache.clear();
        }
    }
    
    public void clear() {
        this.map.clear();
        this.cache.clear();
    }
    
    public int size() {
        return this.map.size();
    }
    
    public boolean isEmpty() {
        return this.map.isEmpty();
    }
    
    private final class ComputeFunction implements Function<Class<?>, T>
    {
        public T apply(final Class<?> a) {
            for (final Class<?> cls : ClassMap.getAncestry(a)) {
                final T result = ClassMap.this.map.get(cls);
                if (result != null) {
                    return result;
                }
            }
            return null;
        }
    }
}
