package net.frozenorb.foxtrot.jedis.persist;

import net.frozenorb.foxtrot.jedis.*;

public class FirstJoinMap extends RedisPersistMap<Long>
{
    public FirstJoinMap() {
        super("FirstJoin");
    }
    
    @Override
    public String getRedisValue(final Long time) {
        return String.valueOf(time);
    }
    
    @Override
    public Long getJavaObject(final String str) {
        return Long.parseLong(str);
    }
    
    public void setFirstJoin(final String player) {
        this.updateValueAsync(player, System.currentTimeMillis());
    }
    
    public long getFirstJoin(final String player) {
        return this.contains(player) ? this.getValue(player) : 0L;
    }
}
