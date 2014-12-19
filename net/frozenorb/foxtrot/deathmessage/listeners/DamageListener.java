package net.frozenorb.foxtrot.deathmessage.listeners;

import org.bukkit.entity.*;
import net.frozenorb.foxtrot.deathmessage.event.*;
import net.frozenorb.foxtrot.deathmessage.util.*;
import net.frozenorb.foxtrot.*;
import net.frozenorb.foxtrot.deathmessage.*;
import org.bukkit.event.*;
import org.bukkit.event.entity.*;
import org.bukkit.*;
import net.frozenorb.foxtrot.deathmessage.objects.*;
import org.bukkit.craftbukkit.v1_7_R3.entity.*;
import net.frozenorb.foxtrot.deathtracker.*;
import java.util.*;
import net.minecraft.server.v1_7_R3.*;

public class DamageListener implements Listener
{
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityDamage(final EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        final CustomPlayerDamageEvent event2 = new CustomPlayerDamageEvent(event);
        event2.setTrackerDamage(new UnknownDamage(((Player)event.getEntity()).getName(), event.getDamage()));
        buttplug.fdsjfhkdsjfdsjhk().getServer().getPluginManager().callEvent((Event)event2);
        if (event2.isCancelled()) {
            event.setCancelled(true);
        }
        else {
            event2.getTrackerDamage().setHealthAfter(((Player)event.getEntity()).getHealthScale());
            DeathMessageHandler.addDamage((Player)event.getEntity(), event2.getTrackerDamage());
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDeath(final PlayerDeathEvent event) {
        if (event.getDeathMessage() == null || event.getDeathMessage().isEmpty()) {
            return;
        }
        final List<Damage> record = DeathMessageHandler.getDamage(event.getEntity());
        if (record == null || record.isEmpty()) {
            event.setDeathMessage(ChatColor.RED + event.getEntity().getName() + ChatColor.DARK_RED + "[" + buttplug.fdsjfhkdsjfdsjhk().getKillsMap().getKills(event.getEntity().getName()) + "]" + ChatColor.YELLOW + " died.");
            return;
        }
        final Damage deathCause = record.get(record.size() - 1);
        if (deathCause instanceof PlayerDamage) {
            final Player killer = buttplug.fdsjfhkdsjfdsjhk().getServer().getPlayerExact(((PlayerDamage)deathCause).getDamager());
            ((CraftPlayer)event.getEntity()).getHandle().killer = (EntityHuman)((CraftPlayer)killer).getHandle();
            buttplug.fdsjfhkdsjfdsjhk().getKillsMap().setKills(killer.getName(), buttplug.fdsjfhkdsjfdsjhk().getKillsMap().getKills(killer.getName()) + 1);
        }
        event.setDeathMessage(deathCause.getDeathMessage());
        DeathTracker.logDeath(event.getEntity(), event.getEntity().getKiller());
        DeathMessageHandler.clearDamage(event.getEntity());
    }
}
