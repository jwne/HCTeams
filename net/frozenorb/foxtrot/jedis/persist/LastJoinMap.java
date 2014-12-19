package net.frozenorb.foxtrot.jedis.persist;

import net.frozenorb.foxtrot.jedis.*;

public class LastJoinMap extends RedisPersistMap<Long>
{
    public LastJoinMap() {
        super("LastJoin");
    }
    
    @Override
    public String getRedisValue(final Long time) {
        return String.valueOf(time);
    }
    
    @Override
    public Long getJavaObject(final String str) {
        return Long.parseLong(str);
    }
    
    public void setLastJoin(final String player) {
        this.updateValueAsync(player, System.currentTimeMillis());
    }
    
    public long getLastJoin(final String player) {
        return this.contains(player) ? this.getValue(player) : 0L;
    }
}
