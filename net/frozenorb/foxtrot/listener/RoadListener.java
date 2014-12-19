package net.frozenorb.foxtrot.listener;

import net.frozenorb.foxtrot.team.dtr.bitmask.*;
import org.bukkit.event.*;
import org.bukkit.event.entity.*;
import net.frozenorb.foxtrot.*;
import org.bukkit.event.block.*;
import org.bukkit.event.player.*;
import org.bukkit.*;

public class RoadListener implements Listener
{
    private boolean isRoad(final Location loc) {
        return DTRBitmaskType.ROAD.appliesAt(loc);
    }
    
    @EventHandler
    public void onPistonRetract(final BlockPistonRetractEvent event) {
        if (event.isSticky() && this.isRoad(event.getRetractLocation())) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onPistonExtend(final BlockPistonExtendEvent event) {
        if (this.isRoad(event.getBlock().getRelative(event.getDirection(), event.getLength() + 1).getLocation())) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onEntityChangeBlock(final EntityChangeBlockEvent event) {
        if (this.isRoad(event.getBlock().getLocation())) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onBlockPlace(final BlockPlaceEvent event) {
        if (buttplug.fdsjfhkdsjfdsjhk().getServerHandler().isAdminOverride(event.getPlayer())) {
            return;
        }
        if (this.isRoad(event.getBlock().getLocation())) {
            event.getPlayer().sendMessage(ChatColor.YELLOW + "You cannot build on the road!");
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onBlockBreak(final BlockBreakEvent event) {
        if (buttplug.fdsjfhkdsjfdsjhk().getServerHandler().isAdminOverride(event.getPlayer())) {
            return;
        }
        if (this.isRoad(event.getBlock().getLocation())) {
            event.getPlayer().sendMessage(ChatColor.YELLOW + "You cannot build on the road!");
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onBlockBurn(final BlockBurnEvent event) {
        if (this.isRoad(event.getBlock().getLocation())) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent event) {
        if (buttplug.fdsjfhkdsjfdsjhk().getServerHandler().isAdminOverride(event.getPlayer())) {
            return;
        }
        if (event.getClickedBlock() != null && this.isRoad(event.getClickedBlock().getRelative(event.getBlockFace()).getLocation()) && (event.getPlayer().getItemInHand().getType() == Material.FLINT_AND_STEEL || event.getPlayer().getItemInHand().getType() == Material.LAVA_BUCKET || (event.getPlayer().getItemInHand().getType() == Material.INK_SACK && event.getPlayer().getItemInHand().getData().getData() == 15))) {
            event.getPlayer().sendMessage(ChatColor.YELLOW + "You cannot build on the road!");
            event.setCancelled(true);
        }
    }
}
