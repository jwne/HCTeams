package net.frozenorb.foxtrot.deathmessage.trackers;

import net.frozenorb.foxtrot.deathmessage.event.*;
import org.bukkit.event.entity.*;
import net.frozenorb.foxtrot.deathmessage.*;
import net.frozenorb.foxtrot.deathmessage.objects.*;
import java.util.*;
import org.bukkit.event.*;
import org.bukkit.*;
import net.frozenorb.foxtrot.*;

public class FallTracker implements Listener
{
    @EventHandler(priority = EventPriority.LOW)
    public void onCustomPlayerDamage(final CustomPlayerDamageEvent event) {
        if (event.getCause().getCause() != EntityDamageEvent.DamageCause.FALL) {
            return;
        }
        final List<Damage> record = DeathMessageHandler.getDamage(event.getPlayer());
        Damage knocker = null;
        long knockerTime = 0L;
        if (record != null) {
            for (final Damage damage : record) {
                if (!(damage instanceof FallDamage)) {
                    if (damage instanceof FallDamageByPlayer) {
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
            event.setTrackerDamage(new FallDamageByPlayer(event.getPlayer().getName(), event.getDamage(), ((PlayerDamage)knocker).getDamager(), Math.round(event.getPlayer().getFallDistance())));
        }
        else {
            event.setTrackerDamage(new FallDamage(event.getPlayer().getName(), event.getDamage(), Math.round(event.getPlayer().getFallDistance())));
        }
    }
    
    public class FallDamage extends Damage
    {
        private int distance;
        
        public FallDamage(final String damaged, final double damage, final int distance) {
            super(damaged, damage);
            this.distance = distance;
        }
        
        @Override
        public String getDescription() {
            if (this.distance == 0) {
                return "Enderpearl";
            }
            return "Fall (" + this.distance + " block" + ((this.distance == 1) ? "" : "s") + ")";
        }
        
        @Override
        public String getDeathMessage() {
            return ChatColor.RED + this.getDamaged() + ChatColor.DARK_RED + "[" + buttplug.fdsjfhkdsjfdsjhk().getKillsMap().getKills(this.getDamaged()) + "] " + ChatColor.YELLOW + "hit the ground too hard.";
        }
    }
    
    public class FallDamageByPlayer extends PlayerDamage
    {
        private int distance;
        
        public FallDamageByPlayer(final String damaged, final double damage, final String damager, final int distance) {
            super(damaged, damage, damager);
            this.distance = distance;
        }
        
        @Override
        public String getDescription() {
            if (this.distance == 0) {
                return "Enderpearl";
            }
            return "Fall (" + this.distance + " block" + ((this.distance == 1) ? "" : "s") + ")";
        }
        
        @Override
        public String getDeathMessage() {
            return ChatColor.RED + this.getDamaged() + ChatColor.DARK_RED + "[" + buttplug.fdsjfhkdsjfdsjhk().getKillsMap().getKills(this.getDamaged()) + "] " + ChatColor.YELLOW + "hit the ground too hard thanks to " + ChatColor.RED + this.getDamager() + ChatColor.DARK_RED + "[" + buttplug.fdsjfhkdsjfdsjhk().getKillsMap().getKills(this.getDamager()) + "]" + ChatColor.YELLOW + ".";
        }
    }
}
