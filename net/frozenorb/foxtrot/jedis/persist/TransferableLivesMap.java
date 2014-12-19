package net.frozenorb.foxtrot.jedis.persist;

import net.frozenorb.foxtrot.jedis.*;

public class TransferableLivesMap extends RedisPersistMap<Integer>
{
    public TransferableLivesMap() {
        super("TransferableLives");
    }
    
    @Override
    public String getRedisValue(final Integer lives) {
        return String.valueOf(lives);
    }
    
    @Override
    public Integer getJavaObject(final String str) {
        return Integer.parseInt(str);
    }
    
    public int getLives(final String player) {
        return this.contains(player) ? this.getValue(player) : 0;
    }
    
    public void setLives(final String player, final int lives) {
        this.updateValue(player, lives);
    }
}
