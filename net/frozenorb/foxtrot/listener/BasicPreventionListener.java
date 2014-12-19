package net.frozenorb.foxtrot.listener;

import net.frozenorb.foxtrot.*;
import net.frozenorb.foxtrot.team.dtr.bitmask.*;
import org.bukkit.event.*;
import org.bukkit.event.inventory.*;
import org.bukkit.*;
import org.bukkit.event.vehicle.*;
import org.bukkit.event.player.*;
import org.bukkit.entity.*;
import org.bukkit.event.block.*;
import net.frozenorb.foxtrot.team.claims.*;
import org.bukkit.event.entity.*;

public class BasicPreventionListener implements Listener
{
    @EventHandler
    public void onPlayerQuit(final PlayerKickEvent event) {
        event.setLeaveMessage((String)null);
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityChangeBlock(final EntityChangeBlockEvent event) {
        if (event.getEntity() instanceof Wither) {
            event.setCancelled(true);
        }
        if (!buttplug.fdsjfhkdsjfdsjhk().getServerHandler().isEOTW() && DTRBitmaskType.SAFE_ZONE.appliesAt(event.getBlock().getLocation())) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onInventoryOpen(final InventoryOpenEvent event) {
        if (event.getInventory().getType() == InventoryType.ENDER_CHEST) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onCommandPreprocess(final PlayerCommandPreprocessEvent event) {
        if ((event.getMessage().toLowerCase().startsWith("/kill") || event.getMessage().toLowerCase().startsWith("/slay") || event.getMessage().toLowerCase().startsWith("/bukkit:kill") || event.getMessage().toLowerCase().startsWith("/bukkit:slay") || event.getMessage().toLowerCase().startsWith("/suicide")) && !event.getPlayer().isOp()) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.RED + "No permission.");
        }
    }
    
    @EventHandler
    public void onVehicleEnter(final VehicleEnterEvent event) {
        if (event.getVehicle() instanceof Horse && event.getEntered() instanceof Player) {
            final Horse horse = (Horse)event.getVehicle();
            final Player player = (Player)event.getEntered();
            if (horse.getOwner() != null && !horse.getOwner().getName().equals(player.getName())) {
                event.setCancelled(true);
                player.sendMessage(ChatColor.RED + "This is not your horse!");
            }
        }
    }
    
    @EventHandler
    public void onFoodLevelChange(final FoodLevelChangeEvent event) {
        if (!buttplug.fdsjfhkdsjfdsjhk().getServerHandler().isEOTW() && DTRBitmaskType.SAFE_ZONE.appliesAt(event.getEntity().getLocation()) && event.getFoodLevel() < ((Player)event.getEntity()).getFoodLevel()) {
            event.setCancelled(true);
        }
        if (event.getFoodLevel() < ((Player)event.getEntity()).getFoodLevel() && buttplug.RANDOM.nextInt(100) > 30) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onPlayerRespawn(final PlayerRespawnEvent event) {
        buttplug.fdsjfhkdsjfdsjhk().getPvPTimerMap().pendingTimer(event.getPlayer().getName());
        event.setRespawnLocation(buttplug.fdsjfhkdsjfdsjhk().getServerHandler().getSpawnLocation());
    }
    
    @EventHandler
    public void onBlockBurn(final BlockBurnEvent event) {
        if (buttplug.fdsjfhkdsjfdsjhk().getServerHandler().isWarzone(event.getBlock().getLocation())) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onCreatureSpawn(final CreatureSpawnEvent event) {
        if (event.getEntity().getType() == EntityType.SQUID) {
            event.setCancelled(true);
        }
        if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.NATURAL && event.getEntity().getType() == EntityType.SKELETON && ((Skeleton)event.getEntity()).getSkeletonType() == Skeleton.SkeletonType.WITHER) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockIgnite(final BlockIgniteEvent event) {
        if (event.getCause() == BlockIgniteEvent.IgniteCause.SPREAD) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onFireBurn(final BlockBurnEvent event) {
        if (buttplug.fdsjfhkdsjfdsjhk().getServerHandler().isWarzone(event.getBlock().getLocation())) {
            event.setCancelled(true);
            return;
        }
        if (buttplug.fdsjfhkdsjfdsjhk().getServerHandler().isUnclaimedOrRaidable(event.getBlock().getLocation())) {
            return;
        }
        if (LandBoard.getInstance().getTeam(event.getBlock().getLocation()) != null) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onEntityExplode(final EntityExplodeEvent event) {
        event.blockList().clear();
    }
}
