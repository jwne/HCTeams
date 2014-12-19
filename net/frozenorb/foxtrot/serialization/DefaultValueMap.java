package net.frozenorb.foxtrot.serialization;

import java.util.*;

public class DefaultValueMap extends HashMap<Class<?>, Object>
{
    private static final long serialVersionUID = 8693766426388395238L;
    private static volatile DefaultValueMap instance;
    
    DefaultValueMap() {
        super();
        ((HashMap<Class<String>, String>)this).put(String.class, "");
        ((HashMap<Class<Integer>, Integer>)this).put(Integer.class, 0);
        ((HashMap<Class<Integer>, Integer>)this).put(Integer.TYPE, 0);
        ((HashMap<Class<Long>, Long>)this).put(Long.class, 0L);
        ((HashMap<Class<Long>, Long>)this).put(Long.TYPE, 0L);
        ((HashMap<Class<Character>, Character>)this).put(Character.class, '\0');
        ((HashMap<Class<Character>, Character>)this).put(Character.TYPE, '\0');
        ((HashMap<Class<Boolean>, Boolean>)this).put(Boolean.class, false);
        ((HashMap<Class<Boolean>, Boolean>)this).put(Boolean.TYPE, false);
    }
    
    public static DefaultValueMap getInstance() {
        if (DefaultValueMap.instance == null) {
            synchronized (DefaultValueMap.class) {
                if (DefaultValueMap.instance == null) {
                    DefaultValueMap.instance = new DefaultValueMap();
                }
            }
        }
        return DefaultValueMap.instance;
    }
}
