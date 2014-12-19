package net.frozenorb.foxtrot.deathmessage.util;

import net.frozenorb.foxtrot.deathmessage.objects.*;
import org.bukkit.*;
import net.frozenorb.foxtrot.*;

public class UnknownDamage extends Damage
{
    public UnknownDamage(final String damaged, final double damage) {
        super(damaged, damage);
    }
    
    @Override
    public String getDescription() {
        return "Unknown";
    }
    
    @Override
    public String getDeathMessage() {
        return ChatColor.RED + this.getDamaged() + ChatColor.DARK_RED + "[" + buttplug.fdsjfhkdsjfdsjhk().getKillsMap().getKills(this.getDamaged()) + "]" + ChatColor.YELLOW + " died.";
    }
}
