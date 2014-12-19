package net.frozenorb.foxtrot.jedis.persist;

import net.frozenorb.foxtrot.jedis.*;
import net.frozenorb.foxtrot.*;

public class PvPTimerMap extends RedisPersistMap<Long>
{
    public static final long PENDING_USE = -10L;
    
    public PvPTimerMap() {
        super("PvPTimers");
    }
    
    @Override
    public String getRedisValue(final Long time) {
        return String.valueOf(time);
    }
    
    @Override
    public Long getJavaObject(final String str) {
        return Long.parseLong(str);
    }
    
    public void pendingTimer(final String player) {
        this.updateValueAsync(player, -10L);
    }
    
    public void createTimer(final String player, final int seconds) {
        this.updateValueAsync(player, System.currentTimeMillis() + seconds * 1000);
    }
    
    public boolean hasTimer(final String player) {
        return this.contains(player) && this.getValue(player) != -10L && this.getValue(player) > System.currentTimeMillis();
    }
    
    public long getTimer(final String player) {
        return this.contains(player) ? this.getValue(player) : -1L;
    }
    
    public void removeTimer(final String player) {
        this.updateValueAsync(player, -1L);
    }
    
    @Override
    public boolean contains(final String player) {
        return super.contains(player);
    }
    
    public Long getValue(final String player) {
        if (buttplug.fdsjfhkdsjfdsjhk().getServerHandler().isPreEOTW()) {
            return -1L;
        }
        return super.getValue(player);
    }
}
