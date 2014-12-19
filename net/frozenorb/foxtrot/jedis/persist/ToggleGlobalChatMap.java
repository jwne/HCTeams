package net.frozenorb.foxtrot.jedis.persist;

import net.frozenorb.foxtrot.jedis.*;

public class ToggleGlobalChatMap extends RedisPersistMap<Boolean>
{
    public ToggleGlobalChatMap() {
        super("GlobalChatToggles");
    }
    
    @Override
    public String getRedisValue(final Boolean toggled) {
        return String.valueOf(toggled);
    }
    
    @Override
    public Boolean getJavaObject(final String str) {
        return Boolean.valueOf(str);
    }
    
    public void setGlobalChatToggled(final String player, final boolean toggled) {
        this.updateValueAsync(player, toggled);
    }
    
    public boolean isGlobalChatToggled(final String player) {
        return !this.contains(player) || this.getValue(player);
    }
}
