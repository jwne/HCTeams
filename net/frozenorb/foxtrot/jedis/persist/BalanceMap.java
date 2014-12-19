package net.frozenorb.foxtrot.jedis.persist;

import net.frozenorb.foxtrot.jedis.*;

public class BalanceMap extends RedisPersistMap<Integer>
{
    public BalanceMap() {
        super("Balance");
    }
    
    @Override
    public String getRedisValue(final Integer kills) {
        return String.valueOf(kills);
    }
    
    @Override
    public Integer getJavaObject(final String str) {
        return Integer.parseInt(str);
    }
    
    public int getBalance(final String player) {
        return this.contains(player) ? this.getValue(player) : 0;
    }
    
    public void setBalance(final String player, final int balance) {
        this.updateValueAsync(player, balance);
    }
}
