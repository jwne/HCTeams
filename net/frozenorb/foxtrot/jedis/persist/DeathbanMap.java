package net.frozenorb.foxtrot.jedis.persist;

import net.frozenorb.foxtrot.jedis.*;
import net.frozenorb.foxtrot.*;

public class DeathbanMap extends RedisPersistMap<Long>
{
    public DeathbanMap() {
        super("Deathbans");
    }
    
    @Override
    public String getRedisValue(final Long time) {
        return String.valueOf(time);
    }
    
    @Override
    public Long getJavaObject(final String str) {
        return Long.parseLong(str);
    }
    
    public boolean isDeathbanned(final String player) {
        if (this.getValue(player) == null) {
            return false;
        }
        if (buttplug.fdsjfhkdsjfdsjhk().getServerHandler().isPreEOTW()) {
            return this.getValue(player) - System.currentTimeMillis() > 432000000L;
        }
        return this.getValue(player) > System.currentTimeMillis();
    }
    
    public void deathban(final String player, final long seconds) {
        this.updateValue(player, System.currentTimeMillis() + seconds * 1000L);
    }
    
    public void revive(final String player) {
        this.updateValue(player, 0L);
    }
    
    public long getDeathban(final String player) {
        return this.contains(player) ? this.getValue(player) : 0L;
    }
}
