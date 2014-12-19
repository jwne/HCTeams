package net.frozenorb.foxtrot.deathmessage.trackers;

import net.frozenorb.foxtrot.deathmessage.event.*;
import net.frozenorb.foxtrot.deathmessage.objects.*;
import org.bukkit.event.*;
import org.bukkit.*;
import net.frozenorb.foxtrot.*;
import org.bukkit.event.entity.*;

public class GeneralTracker implements Listener
{
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onCustomPlayerDamage(final CustomPlayerDamageEvent event) {
        switch (event.getCause().getCause()) {
            case SUFFOCATION: {
                event.setTrackerDamage(new GeneralDamage(event.getPlayer().getName(), event.getDamage(), "Suffocation", "suffocated"));
                break;
            }
            case DROWNING: {
                event.setTrackerDamage(new GeneralDamage(event.getPlayer().getName(), event.getDamage(), "Water", "drowned"));
                break;
            }
            case STARVATION: {
                event.setTrackerDamage(new GeneralDamage(event.getPlayer().getName(), event.getDamage(), "Food", "starved to death"));
                break;
            }
            case FIRE_TICK: {
                event.setTrackerDamage(new GeneralDamage(event.getPlayer().getName(), event.getDamage(), "Fire", "burned to death"));
                break;
            }
            case LAVA: {
                event.setTrackerDamage(new GeneralDamage(event.getPlayer().getName(), event.getDamage(), "Lava", "burned to death"));
                break;
            }
            case LIGHTNING: {
                event.setTrackerDamage(new GeneralDamage(event.getPlayer().getName(), event.getDamage(), "Lightning", "was struck by lightning"));
                break;
            }
            case POISON: {
                event.setTrackerDamage(new GeneralDamage(event.getPlayer().getName(), event.getDamage(), "Poison", "was poisoned"));
                break;
            }
            case WITHER: {
                event.setTrackerDamage(new GeneralDamage(event.getPlayer().getName(), event.getDamage(), "Wither", "withered away"));
                break;
            }
        }
    }
    
    public class GeneralDamage extends Damage
    {
        private String description;
        private String message;
        
        public GeneralDamage(final String damaged, final double damage, final String description, final String message) {
            super(damaged, damage);
            this.description = description;
            this.message = message;
        }
        
        @Override
        public String getDescription() {
            return this.description;
        }
        
        @Override
        public String getDeathMessage() {
            return ChatColor.RED + this.getDamaged() + ChatColor.DARK_RED + "[" + buttplug.fdsjfhkdsjfdsjhk().getKillsMap().getKills(this.getDamaged()) + "] " + ChatColor.YELLOW + this.message + ".";
        }
    }
}
