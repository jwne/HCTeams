package org.bson.util;

import java.util.concurrent.*;
import java.util.*;

class ClassAncestry
{
    private static final ConcurrentMap<Class<?>, List<Class<?>>> _ancestryCache;
    
    public static <T> List<Class<?>> getAncestry(final Class<T> c) {
        final ConcurrentMap<Class<?>, List<Class<?>>> cache = getClassAncestryCache();
        List<Class<?>> cachedResult;
        while (true) {
            cachedResult = cache.get(c);
            if (cachedResult != null) {
                break;
            }
            cache.putIfAbsent(c, computeAncestry(c));
        }
        return cachedResult;
    }
    
    private static List<Class<?>> computeAncestry(final Class<?> c) {
        final List<Class<?>> result = new ArrayList<Class<?>>();
        result.add(Object.class);
        computeAncestry(c, result);
        Collections.reverse(result);
        return Collections.unmodifiableList((List<? extends Class<?>>)new ArrayList<Class<?>>(result));
    }
    
    private static <T> void computeAncestry(final Class<T> c, final List<Class<?>> result) {
        if (c == null || c == Object.class) {
            return;
        }
        final Class<?>[] interfaces = c.getInterfaces();
        for (int i = interfaces.length - 1; i >= 0; --i) {
            computeAncestry(interfaces[i], result);
        }
        computeAncestry(c.getSuperclass(), result);
        if (!result.contains(c)) {
            result.add(c);
        }
    }
    
    private static ConcurrentMap<Class<?>, List<Class<?>>> getClassAncestryCache() {
        return ClassAncestry._ancestryCache;
    }
    
    static {
        _ancestryCache = CopyOnWriteMap.newHashMap();
    }
}
