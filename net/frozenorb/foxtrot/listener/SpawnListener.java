package net.frozenorb.foxtrot.listener;

import net.frozenorb.foxtrot.*;
import net.frozenorb.foxtrot.team.dtr.bitmask.*;
import org.bukkit.event.*;
import org.bukkit.event.block.*;
import org.bukkit.*;
import org.bukkit.event.hanging.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.*;
import org.bukkit.event.entity.*;
import org.bukkit.entity.*;

public class SpawnListener implements Listener
{
    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockIgnite(final BlockIgniteEvent event) {
        if (event.getPlayer() != null && buttplug.fdsjfhkdsjfdsjhk().getServerHandler().isAdminOverride(event.getPlayer())) {
            return;
        }
        if (DTRBitmaskType.SAFE_ZONE.appliesAt(event.getBlock().getLocation())) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockPlace(final BlockPlaceEvent event) {
        if (event.isCancelled() || buttplug.fdsjfhkdsjfdsjhk().getServerHandler().isAdminOverride(event.getPlayer())) {
            return;
        }
        if (DTRBitmaskType.SAFE_ZONE.appliesAt(event.getBlock().getLocation())) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.YELLOW + "You cannot build in spawn!");
        }
        else if (buttplug.fdsjfhkdsjfdsjhk().getServerHandler().isSpawnBufferZone(event.getBlock().getLocation()) || buttplug.fdsjfhkdsjfdsjhk().getServerHandler().isNetherBufferZone(event.getBlock().getLocation())) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.YELLOW + "You cannot build this close to spawn!");
        }
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockBreak(final BlockBreakEvent event) {
        if (event.isCancelled() || buttplug.fdsjfhkdsjfdsjhk().getServerHandler().isAdminOverride(event.getPlayer())) {
            return;
        }
        if (DTRBitmaskType.SAFE_ZONE.appliesAt(event.getBlock().getLocation())) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.YELLOW + "You cannot build in spawn!");
        }
        else if (buttplug.fdsjfhkdsjfdsjhk().getServerHandler().isSpawnBufferZone(event.getBlock().getLocation()) || buttplug.fdsjfhkdsjfdsjhk().getServerHandler().isNetherBufferZone(event.getBlock().getLocation())) {
            event.setCancelled(true);
            if (event.getBlock().getType() != Material.LONG_GRASS && event.getBlock().getType() != Material.GRASS) {
                event.getPlayer().sendMessage(ChatColor.YELLOW + "You cannot build this close to spawn!");
            }
        }
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onHangingPlace(final HangingPlaceEvent event) {
        if (buttplug.fdsjfhkdsjfdsjhk().getServerHandler().isAdminOverride(event.getPlayer())) {
            return;
        }
        if (DTRBitmaskType.SAFE_ZONE.appliesAt(event.getEntity().getLocation())) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onHangingBreakByEntity(final HangingBreakByEntityEvent event) {
        if (!(event.getRemover() instanceof Player) || buttplug.fdsjfhkdsjfdsjhk().getServerHandler().isAdminOverride((Player)event.getRemover())) {
            return;
        }
        if (DTRBitmaskType.SAFE_ZONE.appliesAt(event.getEntity().getLocation())) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerInteractEntityEvent(final PlayerInteractEntityEvent event) {
        if (event.isCancelled() || event.getRightClicked().getType() != EntityType.ITEM_FRAME || buttplug.fdsjfhkdsjfdsjhk().getServerHandler().isAdminOverride(event.getPlayer())) {
            return;
        }
        if (DTRBitmaskType.SAFE_ZONE.appliesAt(event.getRightClicked().getLocation())) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityDamageByEntity(final EntityDamageByEntityEvent event) {
        if (event.isCancelled() || !(event.getEntity() instanceof Player) || event.getEntity().getType() != EntityType.ITEM_FRAME || buttplug.fdsjfhkdsjfdsjhk().getServerHandler().isAdminOverride((Player)event.getDamager())) {
            return;
        }
        if (DTRBitmaskType.SAFE_ZONE.appliesAt(event.getEntity().getLocation())) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onBucketEmpty(final PlayerBucketEmptyEvent event) {
        if (buttplug.fdsjfhkdsjfdsjhk().getServerHandler().isAdminOverride(event.getPlayer())) {
            return;
        }
        if (buttplug.fdsjfhkdsjfdsjhk().getServerHandler().isSpawnBufferZone(event.getBlockClicked().getLocation())) {
            event.setCancelled(true);
            event.getBlockClicked().getRelative(event.getBlockFace()).setType(Material.AIR);
            event.setItemStack(new ItemStack(event.getBucket()));
        }
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityDamage(final EntityDamageEvent event) {
        if (event.isCancelled() || buttplug.fdsjfhkdsjfdsjhk().getServerHandler().isEOTW()) {
            return;
        }
        if ((event.getEntity() instanceof Player || event.getEntity() instanceof Horse) && DTRBitmaskType.SAFE_ZONE.appliesAt(event.getEntity().getLocation())) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityDamageByEntity2(final EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player) || event.isCancelled()) {
            return;
        }
        Player damager = null;
        if (event.getDamager() instanceof Player) {
            damager = (Player)event.getDamager();
        }
        else if (event.getDamager() instanceof Projectile) {
            final Projectile projectile = (Projectile)event.getDamager();
            if (projectile.getShooter() instanceof Player) {
                damager = (Player)projectile.getShooter();
            }
        }
        if (damager != null) {
            final Player victim = (Player)event.getEntity();
            if (!buttplug.fdsjfhkdsjfdsjhk().getServerHandler().isEOTW() && (DTRBitmaskType.SAFE_ZONE.appliesAt(victim.getLocation()) || DTRBitmaskType.SAFE_ZONE.appliesAt(damager.getLocation()))) {
                event.setCancelled(true);
            }
        }
    }
}
