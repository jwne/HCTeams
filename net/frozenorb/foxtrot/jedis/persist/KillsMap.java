package net.frozenorb.foxtrot.jedis.persist;

import net.frozenorb.foxtrot.jedis.*;

public class KillsMap extends RedisPersistMap<Integer>
{
    public KillsMap() {
        super("Kills");
    }
    
    @Override
    public String getRedisValue(final Integer kills) {
        return String.valueOf(kills);
    }
    
    @Override
    public Integer getJavaObject(final String str) {
        return Integer.parseInt(str);
    }
    
    public int getKills(final String player) {
        return this.contains(player) ? this.getValue(player) : 0;
    }
    
    public void setKills(final String player, final int kills) {
        this.updateValueAsync(player, kills);
    }
}
