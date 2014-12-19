package net.frozenorb.foxtrot.deathmessage.trackers;

import net.frozenorb.foxtrot.*;
import org.bukkit.plugin.*;
import org.bukkit.metadata.*;
import net.frozenorb.foxtrot.deathmessage.event.*;
import org.bukkit.event.entity.*;
import org.bukkit.entity.*;
import java.util.*;
import org.bukkit.event.*;
import org.bukkit.*;
import net.frozenorb.foxtrot.deathmessage.objects.*;

public class ArrowTracker implements Listener
{
    @EventHandler
    public void onEntityShootBow(final EntityShootBowEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        event.getProjectile().setMetadata("ShotFromDistance", (MetadataValue)new FixedMetadataValue((Plugin)buttplug.fdsjfhkdsjfdsjhk(), (Object)event.getProjectile().getLocation()));
    }
    
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onCustomPlayerDamage(final CustomPlayerDamageEvent event) {
        if (event.getCause() instanceof EntityDamageByEntityEvent) {
            final EntityDamageByEntityEvent e = (EntityDamageByEntityEvent)event.getCause();
            if (e.getDamager() instanceof Arrow) {
                final Arrow a = (Arrow)e.getDamager();
                if (a.getShooter() instanceof Player) {
                    final Player shooter = (Player)a.getShooter();
                    for (final MetadataValue value : a.getMetadata("ShotFrom")) {
                        final Location shotFrom = (Location)value.value();
                        final double distance = shotFrom.distance(event.getPlayer().getLocation());
                        event.setTrackerDamage(new ArrowDamageByPlayer(event.getPlayer().getName(), event.getDamage(), shooter.getName(), shotFrom, distance));
                    }
                }
                else if (a.getShooter() instanceof Entity) {
                    final Entity shooter2 = (Entity)a.getShooter();
                    event.setTrackerDamage(new ArrowDamageByMob(event.getPlayer().getName(), event.getDamage(), shooter2));
                }
                else {
                    event.setTrackerDamage(new ArrowDamage(event.getPlayer().getName(), event.getDamage()));
                }
            }
        }
    }
    
    public class ArrowDamage extends Damage
    {
        public ArrowDamage(final String damaged, final double damage) {
            super(damaged, damage);
        }
        
        @Override
        public String getDescription() {
            return "Shot";
        }
        
        @Override
        public String getDeathMessage() {
            return ChatColor.GOLD + this.getDamaged() + ChatColor.RED + " was shot.";
        }
    }
    
    public static class ArrowDamageByPlayer extends PlayerDamage
    {
        private Location shotFrom;
        double distance;
        
        public ArrowDamageByPlayer(final String damaged, final double damage, final String damager, final Location shotFrom, final double distance) {
            super(damaged, damage, damager);
            this.shotFrom = shotFrom;
            this.distance = distance;
        }
        
        @Override
        public String getDescription() {
            return "Shot by " + this.getDamager();
        }
        
        @Override
        public String getDeathMessage() {
            return ChatColor.RED + this.getDamaged() + ChatColor.DARK_RED + "[" + buttplug.fdsjfhkdsjfdsjhk().getKillsMap().getKills(this.getDamaged()) + "]" + ChatColor.YELLOW + " was shot by " + ChatColor.RED + this.getDamager() + ChatColor.DARK_RED + "[" + buttplug.fdsjfhkdsjfdsjhk().getKillsMap().getKills(this.getDamager()) + "]" + ChatColor.YELLOW + " from " + ChatColor.BLUE + (int)this.distance + " blocks" + ChatColor.YELLOW + ".";
        }
        
        public double getDistance() {
            return this.distance;
        }
        
        public Location getShotFrom() {
            return this.shotFrom;
        }
    }
    
    public class ArrowDamageByMob extends MobDamage
    {
        public ArrowDamageByMob(final String damaged, final double damage, final Entity damager) {
            super(damaged, damage, damager.getType());
        }
        
        @Override
        public String getDescription() {
            return "Shot by " + this.getMobType().getName();
        }
        
        @Override
        public String getDeathMessage() {
            return ChatColor.RED + this.getDamaged() + ChatColor.DARK_RED + "[" + buttplug.fdsjfhkdsjfdsjhk().getKillsMap().getKills(this.getDamaged()) + "] " + ChatColor.YELLOW + "was shot by a " + this.getMobType().getName() + ".";
        }
    }
}
