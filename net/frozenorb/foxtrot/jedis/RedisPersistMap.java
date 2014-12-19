package net.frozenorb.foxtrot.jedis;

import lombok.*;
import redis.clients.jedis.*;
import java.util.*;
import net.frozenorb.foxtrot.*;
import org.bukkit.scheduler.*;
import org.bukkit.plugin.*;

public abstract class RedisPersistMap<T>
{
    private HashMap<String, T> wrappedMap;
    @NonNull
    private String keyPrefix;
    
    public void loadFromRedis() {
        final yourmom<Object> jdc = new yourmom<Object>() {
            @Override
            public Object execute(final Jedis jedis) {
                final Map<String, String> results = jedis.hgetAll(RedisPersistMap.this.keyPrefix);
                for (final Map.Entry<String, String> resultEntry : results.entrySet()) {
                    final T object = RedisPersistMap.this.getJavaObjectSafe(resultEntry.getKey(), resultEntry.getValue());
                    if (object != null) {
                        RedisPersistMap.this.wrappedMap.put(resultEntry.getKey(), object);
                    }
                }
                return null;
            }
        };
        buttplug.fdsjfhkdsjfdsjhk().eatmyass(jdc);
    }
    
    public void reloadValue(final String key) {
        final yourmom<Object> jdc = new yourmom<Object>() {
            @Override
            public Object execute(final Jedis jedis) {
                final String result = jedis.hget(RedisPersistMap.this.keyPrefix, key);
                if (result != null) {
                    final T object = RedisPersistMap.this.getJavaObjectSafe(key, result);
                    if (object != null) {
                        RedisPersistMap.this.wrappedMap.put(key, object);
                    }
                }
                return null;
            }
        };
        buttplug.fdsjfhkdsjfdsjhk().eatmyass(jdc);
    }
    
    protected void updateValue(final String key, final T value) {
        this.wrappedMap.put(key.toLowerCase(), value);
        final yourmom<Object> jdc = new yourmom<Object>() {
            @Override
            public Object execute(final Jedis jedis) {
                jedis.hset(RedisPersistMap.this.keyPrefix, key.toLowerCase(), RedisPersistMap.this.getRedisValue(RedisPersistMap.this.getValue(key)));
                return null;
            }
        };
        buttplug.fdsjfhkdsjfdsjhk().eatmyass(jdc);
    }
    
    protected void updateValueAsync(final String key, final T value) {
        this.wrappedMap.put(key.toLowerCase(), value);
        final yourmom<Object> jdc = new yourmom<Object>() {
            @Override
            public Object execute(final Jedis jedis) {
                jedis.hset(RedisPersistMap.this.keyPrefix, key.toLowerCase(), RedisPersistMap.this.getRedisValue(RedisPersistMap.this.getValue(key)));
                return null;
            }
        };
        new BukkitRunnable() {
            public void run() {
                buttplug.fdsjfhkdsjfdsjhk().eatmyass(jdc);
            }
        }.runTaskAsynchronously((Plugin)buttplug.fdsjfhkdsjfdsjhk());
    }
    
    protected T getValue(final String key) {
        return this.wrappedMap.get(key.toLowerCase());
    }
    
    public boolean contains(final String key) {
        return this.wrappedMap.containsKey(key.toLowerCase());
    }
    
    public abstract String getRedisValue(final T p0);
    
    public T getJavaObjectSafe(final String key, final String str) {
        try {
            return this.getJavaObject(str);
        }
        catch (Exception e) {
            System.out.println("Error parsing Redis result.");
            System.out.println(" - Prefix: " + this.keyPrefix);
            System.out.println(" - Key: " + key);
            System.out.println(" - Value: " + str);
            return null;
        }
    }
    
    public abstract T getJavaObject(final String p0);
    
    public RedisPersistMap(@NonNull final String keyPrefix) {
        super();
        this.wrappedMap = new HashMap<String, T>();
        if (keyPrefix == null) {
            throw new NullPointerException("keyPrefix");
        }
        this.keyPrefix = keyPrefix;
    }
}
