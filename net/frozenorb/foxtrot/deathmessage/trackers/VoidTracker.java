package net.frozenorb.foxtrot.deathmessage.trackers;

import net.frozenorb.foxtrot.deathmessage.event.*;
import org.bukkit.event.entity.*;
import net.frozenorb.foxtrot.deathmessage.*;
import net.frozenorb.foxtrot.deathmessage.objects.*;
import java.util.*;
import org.bukkit.event.*;
import org.bukkit.*;
import net.frozenorb.foxtrot.*;

public class VoidTracker implements Listener
{
    @EventHandler(priority = EventPriority.LOW)
    public void onCustomPlayerDamage(final CustomPlayerDamageEvent event) {
        if (event.getCause().getCause() != EntityDamageEvent.DamageCause.VOID) {
            return;
        }
        final List<Damage> record = DeathMessageHandler.getDamage(event.getPlayer());
        Damage knocker = null;
        long knockerTime = 0L;
        if (record != null) {
            for (final Damage damage : record) {
                if (!(damage instanceof VoidDamage)) {
                    if (damage instanceof VoidDamageByPlayer) {
                        continue;
                    }
                    if (!(damage instanceof PlayerDamage) || (knocker != null && damage.getTime() <= knockerTime)) {
                        continue;
                    }
                    knocker = damage;
                    knockerTime = damage.getTime();
                }
            }
        }
        if (knocker != null) {
            event.setTrackerDamage(new VoidDamageByPlayer(event.getPlayer().getName(), event.getDamage(), ((PlayerDamage)knocker).getDamager(), !(knocker instanceof ArrowTracker.ArrowDamageByPlayer)));
        }
        else {
            event.setTrackerDamage(new VoidDamage(event.getPlayer().getName(), event.getDamage()));
        }
    }
    
    public class VoidDamage extends Damage
    {
        VoidDamage(final String damaged, final double damage) {
            super(damaged, damage);
        }
        
        @Override
        public String getDescription() {
            return "Void";
        }
        
        @Override
        public String getDeathMessage() {
            return ChatColor.RED + this.getDamaged() + ChatColor.DARK_RED + "[" + buttplug.fdsjfhkdsjfdsjhk().getKillsMap().getKills(this.getDamaged()) + "]" + ChatColor.YELLOW + " fell into the void.";
        }
    }
    
    public class VoidDamageByPlayer extends PlayerDamage
    {
        private boolean knocked;
        
        VoidDamageByPlayer(final String damaged, final double damage, final String damager, final boolean knocked) {
            super(damaged, damage, damager);
            this.knocked = knocked;
        }
        
        @Override
        public String getDescription() {
            return "Void";
        }
        
        @Override
        public String getDeathMessage() {
            return ChatColor.RED + this.getDamaged() + ChatColor.DARK_RED + "[" + buttplug.fdsjfhkdsjfdsjhk().getKillsMap().getKills(this.getDamaged()) + "]" + ChatColor.YELLOW + " fell into the void thanks to " + ChatColor.RED + this.getDamager() + ChatColor.DARK_RED + "[" + buttplug.fdsjfhkdsjfdsjhk().getKillsMap().getKills(this.getDamager()) + "]" + ChatColor.YELLOW + ".";
        }
    }
}
