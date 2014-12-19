package net.frozenorb.foxtrot.deathmessage.trackers;

import net.frozenorb.foxtrot.deathmessage.event.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.*;
import net.frozenorb.foxtrot.deathmessage.objects.*;
import org.bukkit.entity.*;
import org.bukkit.*;
import net.frozenorb.foxtrot.*;

public class EntityTracker implements Listener
{
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onCustomPlayerDamage(final CustomPlayerDamageEvent event) {
        if (event.getCause() instanceof EntityDamageByEntityEvent) {
            final EntityDamageByEntityEvent e = (EntityDamageByEntityEvent)event.getCause();
            if (!(e.getDamager() instanceof Player) && !(e.getDamager() instanceof Arrow)) {
                event.setTrackerDamage(new EntityDamage(event.getPlayer().getName(), event.getDamage(), e.getDamager()));
            }
        }
    }
    
    public class EntityDamage extends MobDamage
    {
        public EntityDamage(final String damaged, final double damage, final Entity entity) {
            super(damaged, damage, entity.getType());
        }
        
        @Override
        public String getDescription() {
            return this.getMobType().getName();
        }
        
        @Override
        public String getDeathMessage() {
            return ChatColor.RED + this.getDamaged() + ChatColor.DARK_RED + "[" + buttplug.fdsjfhkdsjfdsjhk().getKillsMap().getKills(this.getDamaged()) + "] " + ChatColor.YELLOW + "was slain by a " + ChatColor.RED + this.getMobType().getName() + ChatColor.YELLOW + ".";
        }
    }
}
