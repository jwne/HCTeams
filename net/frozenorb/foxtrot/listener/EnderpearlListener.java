package net.frozenorb.foxtrot.listener;

import org.bukkit.event.entity.*;
import org.bukkit.entity.*;
import net.frozenorb.foxtrot.team.dtr.bitmask.*;
import org.bukkit.event.*;
import org.bukkit.event.block.*;
import org.bukkit.event.player.*;
import net.frozenorb.foxtrot.*;
import net.frozenorb.foxtrot.server.*;
import org.bukkit.*;
import java.util.*;

public class EnderpearlListener implements Listener
{
    private static Map<String, Long> enderpearlCooldown;
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onProjectileLaunch(final ProjectileLaunchEvent event) {
        if (event.isCancelled() || !(event.getEntity().getShooter() instanceof Player)) {
            return;
        }
        final Player shooter = (Player)event.getEntity().getShooter();
        if (event.getEntity() instanceof EnderPearl) {
            if (DTRBitmaskType.THIRTY_SECOND_ENDERPEARL_COOLDOWN.appliesAt(event.getEntity().getLocation())) {
                EnderpearlListener.enderpearlCooldown.put(shooter.getName(), System.currentTimeMillis() + 30000L);
            }
            else {
                EnderpearlListener.enderpearlCooldown.put(shooter.getName(), System.currentTimeMillis() + 16000L);
            }
        }
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerInteract(final PlayerInteractEvent event) {
        if (event.getItem() == null || (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) || event.getItem().getType() != Material.ENDER_PEARL) {
            return;
        }
        if (EnderpearlListener.enderpearlCooldown.containsKey(event.getPlayer().getName()) && EnderpearlListener.enderpearlCooldown.get(event.getPlayer().getName()) > System.currentTimeMillis()) {
            final long millisLeft = EnderpearlListener.enderpearlCooldown.get(event.getPlayer().getName()) - System.currentTimeMillis();
            final double value = millisLeft / 1000.0;
            final double sec = Math.round(10.0 * value) / 10.0;
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.RED + "You cannot use this for another " + ChatColor.BOLD + sec + ChatColor.RED + " seconds!");
            event.getPlayer().updateInventory();
        }
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerTeleport(final PlayerTeleportEvent event) {
        if (event.isCancelled() || event.getCause() != PlayerTeleportEvent.TeleportCause.ENDER_PEARL) {
            return;
        }
        final Location target = event.getTo();
        final Location from = event.getFrom();
        if (!buttplug.fdsjfhkdsjfdsjhk().getServerHandler().isEOTW() && DTRBitmaskType.SAFE_ZONE.appliesAt(target) && !DTRBitmaskType.SAFE_ZONE.appliesAt(from)) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "Invalid Pearl! " + ChatColor.YELLOW + "You cannot Enderpearl into spawn!");
            return;
        }
        if ((!DTRBitmaskType.SAFE_ZONE.appliesAt(target) || !DTRBitmaskType.SAFE_ZONE.appliesAt(from)) && event.getPlayer().getWorld().getEnvironment() != World.Environment.THE_END) {
            SpawnTagHandler.addSeconds(event.getPlayer(), 8);
        }
        final Material mat = event.getTo().getBlock().getType();
        if (((mat == Material.THIN_GLASS || mat == Material.IRON_FENCE) && this.clippingThrough(target, from, 0.65)) || ((mat == Material.FENCE || mat == Material.NETHER_FENCE) && this.clippingThrough(target, from, 0.45))) {
            event.setTo(from);
            return;
        }
        target.setX(target.getBlockX() + 0.5);
        target.setZ(target.getBlockZ() + 0.5);
        event.setTo(target);
    }
    
    public boolean clippingThrough(final Location target, final Location from, final double thickness) {
        return (from.getX() > target.getX() && from.getX() - target.getX() < thickness) || (target.getX() > from.getX() && target.getX() - from.getX() < thickness) || (from.getZ() > target.getZ() && from.getZ() - target.getZ() < thickness) || (target.getZ() > from.getZ() && target.getZ() - from.getZ() < thickness);
    }
    
    public static Map<String, Long> getEnderpearlCooldown() {
        return EnderpearlListener.enderpearlCooldown;
    }
    
    static {
        EnderpearlListener.enderpearlCooldown = new HashMap<String, Long>();
    }
}
