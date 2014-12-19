package net.frozenorb.foxtrot.server;

import java.util.*;
import org.bukkit.entity.*;
import org.bukkit.*;

public class PlayerDamagePair
{
    public static final int FALL_DAMAGE_ASSIST_SECONDS = 30;
    private UUID victimUUID;
    private UUID damagerUUID;
    
    public Player getVictim() {
        return Bukkit.getPlayer(this.victimUUID);
    }
    
    public Player getDamager() {
        return Bukkit.getPlayer(this.damagerUUID);
    }
    
    public PlayerDamagePair(final UUID victimUUID, final UUID damagerUUID) {
        super();
        this.victimUUID = victimUUID;
        this.damagerUUID = damagerUUID;
    }
    
    public UUID getVictimUUID() {
        return this.victimUUID;
    }
    
    public UUID getDamagerUUID() {
        return this.damagerUUID;
    }
    
    public void setVictimUUID(final UUID victimUUID) {
        this.victimUUID = victimUUID;
    }
    
    public void setDamagerUUID(final UUID damagerUUID) {
        this.damagerUUID = damagerUUID;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PlayerDamagePair)) {
            return false;
        }
        final PlayerDamagePair other = (PlayerDamagePair)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$victimUUID = this.getVictimUUID();
        final Object other$victimUUID = other.getVictimUUID();
        Label_0065: {
            if (this$victimUUID == null) {
                if (other$victimUUID == null) {
                    break Label_0065;
                }
            }
            else if (this$victimUUID.equals(other$victimUUID)) {
                break Label_0065;
            }
            return false;
        }
        final Object this$damagerUUID = this.getDamagerUUID();
        final Object other$damagerUUID = other.getDamagerUUID();
        if (this$damagerUUID == null) {
            if (other$damagerUUID == null) {
                return true;
            }
        }
        else if (this$damagerUUID.equals(other$damagerUUID)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof PlayerDamagePair;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $victimUUID = this.getVictimUUID();
        result = result * 59 + (($victimUUID == null) ? 0 : $victimUUID.hashCode());
        final Object $damagerUUID = this.getDamagerUUID();
        result = result * 59 + (($damagerUUID == null) ? 0 : $damagerUUID.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "PlayerDamagePair(victimUUID=" + this.getVictimUUID() + ", damagerUUID=" + this.getDamagerUUID() + ")";
    }
}
