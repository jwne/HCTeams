package net.frozenorb.foxtrot.listener;

import org.bukkit.event.*;
import org.bukkit.event.block.*;
import org.bukkit.*;
import org.bukkit.event.player.*;

public class BorderListener implements Listener
{
    public static int BORDER_SIZE;
    
    @EventHandler
    public void onBlockPlace(final BlockPlaceEvent event) {
        if (Math.abs(event.getBlock().getX()) > BorderListener.BORDER_SIZE || Math.abs(event.getBlock().getZ()) > BorderListener.BORDER_SIZE) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onBlockBreak(final BlockBreakEvent event) {
        if (Math.abs(event.getBlock().getX()) > BorderListener.BORDER_SIZE || Math.abs(event.getBlock().getZ()) > BorderListener.BORDER_SIZE) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onPlayerPortal(final PlayerPortalEvent event) {
        if (Math.abs(event.getTo().getBlockX()) > BorderListener.BORDER_SIZE || Math.abs(event.getTo().getBlockZ()) > BorderListener.BORDER_SIZE) {
            final Location newLocation = event.getTo().clone();
            while (Math.abs(newLocation.getX()) > BorderListener.BORDER_SIZE) {
                newLocation.setX(newLocation.getX() - ((newLocation.getX() > 0.0) ? 1 : -1));
            }
            while (Math.abs(newLocation.getZ()) > BorderListener.BORDER_SIZE) {
                newLocation.setZ(newLocation.getZ() - ((newLocation.getZ() > 0.0) ? 1 : -1));
            }
            event.setTo(newLocation);
            event.getPlayer().sendMessage(ChatColor.RED + "That portal's location is past the border. It has been moved inwards.");
        }
    }
    
    @EventHandler
    public void onPlayerTeleport(final PlayerTeleportEvent event) {
        if (!event.getTo().getWorld().equals(event.getFrom().getWorld())) {
            return;
        }
        if (event.getTo().distance(event.getFrom()) < 0.0 || event.getCause() == PlayerTeleportEvent.TeleportCause.PLUGIN) {
            return;
        }
        if (Math.abs(event.getTo().getBlockX()) > BorderListener.BORDER_SIZE || Math.abs(event.getTo().getBlockZ()) > BorderListener.BORDER_SIZE) {
            final Location newLocation = event.getTo().clone();
            while (Math.abs(newLocation.getX()) > BorderListener.BORDER_SIZE) {
                newLocation.setX(newLocation.getX() - ((newLocation.getX() > 0.0) ? 1 : -1));
            }
            while (Math.abs(newLocation.getZ()) > BorderListener.BORDER_SIZE) {
                newLocation.setZ(newLocation.getZ() - ((newLocation.getZ() > 0.0) ? 1 : -1));
            }
            event.setTo(newLocation);
            event.getPlayer().sendMessage(ChatColor.RED + "That location is past the border.");
        }
    }
    
    @EventHandler
    public void onPlayerMove(final PlayerMoveEvent event) {
        final Location from = event.getFrom();
        final Location to = event.getTo();
        if ((from.getX() != to.getX() || from.getZ() != to.getZ() || from.getY() != to.getY()) && (Math.abs(event.getTo().getBlockX()) > BorderListener.BORDER_SIZE || Math.abs(event.getTo().getBlockZ()) > BorderListener.BORDER_SIZE)) {
            if (event.getPlayer().getVehicle() != null) {
                event.getPlayer().getVehicle().eject();
            }
            final Location newLocation = event.getTo().clone();
            while (Math.abs(newLocation.getX()) > BorderListener.BORDER_SIZE) {
                newLocation.setX(newLocation.getX() - ((newLocation.getX() > 0.0) ? 1 : -1));
            }
            while (Math.abs(newLocation.getZ()) > BorderListener.BORDER_SIZE) {
                newLocation.setZ(newLocation.getZ() - ((newLocation.getZ() > 0.0) ? 1 : -1));
            }
            event.setTo(newLocation);
            event.getPlayer().sendMessage(ChatColor.RED + "You have hit the border!");
        }
    }
    
    static {
        BorderListener.BORDER_SIZE = 3000;
    }
}
