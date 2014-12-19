package net.frozenorb.foxtrot.jedis.persist;

import net.frozenorb.foxtrot.jedis.*;
import java.util.*;

public class PlaytimeMap extends RedisPersistMap<Long>
{
    private Map<String, Long> joinDate;
    
    public PlaytimeMap() {
        super("PlayerPlaytimes");
        this.joinDate = new HashMap<String, Long>();
    }
    
    @Override
    public String getRedisValue(final Long time) {
        return String.valueOf(time);
    }
    
    @Override
    public Long getJavaObject(final String str) {
        return Long.parseLong(str);
    }
    
    public void playerJoined(final String player) {
        this.joinDate.put(player, System.currentTimeMillis());
        if (!this.contains(player)) {
            this.updateValue(player, 0L);
        }
    }
    
    public void playerQuit(final String player, final boolean async) {
        if (async) {
            this.updateValueAsync(player, this.getPlaytime(player) + (System.currentTimeMillis() - this.joinDate.get(player)) / 1000L);
        }
        else {
            this.updateValue(player, this.getPlaytime(player) + (System.currentTimeMillis() - this.joinDate.get(player)) / 1000L);
        }
    }
    
    public long getCurrentSession(final String player) {
        if (this.joinDate.containsKey(player)) {
            return System.currentTimeMillis() - this.joinDate.get(player);
        }
        return 0L;
    }
    
    public long getPlaytime(final String player) {
        return this.contains(player) ? this.getValue(player) : 0L;
    }
}
