package net.frozenorb.foxtrot.jedis.persist;

import net.frozenorb.foxtrot.jedis.*;

public class FishingKitMap extends RedisPersistMap<Integer>
{
    public FishingKitMap() {
        super("FishingKitUses");
    }
    
    @Override
    public String getRedisValue(final Integer uses) {
        return String.valueOf(uses);
    }
    
    @Override
    public Integer getJavaObject(final String str) {
        return Integer.parseInt(str);
    }
    
    public int getUses(final String player) {
        return this.contains(player) ? this.getValue(player) : 0;
    }
    
    public void setUses(final String player, final int uses) {
        this.updateValueAsync(player, uses);
    }
}
