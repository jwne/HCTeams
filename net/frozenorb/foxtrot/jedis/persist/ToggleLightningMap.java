package net.frozenorb.foxtrot.jedis.persist;

import net.frozenorb.foxtrot.jedis.*;

public class ToggleLightningMap extends RedisPersistMap<Boolean>
{
    public ToggleLightningMap() {
        super("LightningToggles");
    }
    
    @Override
    public String getRedisValue(final Boolean toggled) {
        return String.valueOf(toggled);
    }
    
    @Override
    public Boolean getJavaObject(final String str) {
        return Boolean.valueOf(str);
    }
    
    public void setLightningToggled(final String player, final boolean toggled) {
        this.updateValueAsync(player, toggled);
    }
    
    public boolean isLightningToggled(final String player) {
        return !this.contains(player) || this.getValue(player);
    }
}
