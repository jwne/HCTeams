package net.frozenorb.foxtrot.deathmessage.trackers;

import net.frozenorb.foxtrot.deathmessage.event.*;
import org.bukkit.event.entity.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import net.frozenorb.foxtrot.deathmessage.objects.*;
import org.bukkit.inventory.*;
import net.frozenorb.foxtrot.deathmessage.util.*;
import org.bukkit.*;
import net.frozenorb.foxtrot.*;

public class PVPTracker implements Listener
{
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onCustomPlayerDamage(final CustomPlayerDamageEvent event) {
        if (event.getCause() instanceof EntityDamageByEntityEvent) {
            final EntityDamageByEntityEvent e = (EntityDamageByEntityEvent)event.getCause();
            if (e.getDamager() instanceof Player) {
                final Player damager = (Player)e.getDamager();
                final Player damaged = event.getPlayer();
                event.setTrackerDamage(new PVPDamage(damaged.getName(), event.getDamage(), damager.getName(), damager.getItemInHand()));
            }
        }
    }
    
    public class PVPDamage extends PlayerDamage
    {
        private String itemString;
        
        public PVPDamage(final String damaged, final double damage, final String damager, final ItemStack itemStack) {
            super(damaged, damage, damager);
            this.itemString = "Error";
            if (itemStack.getType() == Material.AIR) {
                this.itemString = "their fists";
            }
            else {
                this.itemString = MobUtil.getItemName(itemStack);
            }
        }
        
        @Override
        public String getDescription() {
            return "Killed by " + this.getDamager();
        }
        
        @Override
        public String getDeathMessage() {
            return ChatColor.RED + this.getDamaged() + ChatColor.DARK_RED + "[" + buttplug.fdsjfhkdsjfdsjhk().getKillsMap().getKills(this.getDamaged()) + "]" + ChatColor.YELLOW + " was slain by " + ChatColor.RED + this.getDamager() + ChatColor.DARK_RED + "[" + buttplug.fdsjfhkdsjfdsjhk().getKillsMap().getKills(this.getDamager()) + "]" + ChatColor.YELLOW + " using " + ChatColor.RED + this.itemString + ChatColor.YELLOW + ".";
        }
    }
}
