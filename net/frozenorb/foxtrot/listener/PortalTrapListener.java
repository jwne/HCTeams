package net.frozenorb.foxtrot.listener;

import org.bukkit.event.player.*;
import net.frozenorb.foxtrot.team.dtr.bitmask.*;
import net.frozenorb.foxtrot.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;

public class PortalTrapListener implements Listener
{
    @EventHandler
    public void onPortal(final PlayerPortalEvent event) {
        final Player player = event.getPlayer();
        if (event.getCause() != PlayerTeleportEvent.TeleportCause.NETHER_PORTAL) {
            return;
        }
        if (event.getTo().getWorld().getEnvironment() == World.Environment.NORMAL) {
            if (DTRBitmaskType.SAFE_ZONE.appliesAt(event.getFrom())) {
                event.setCancelled(true);
                player.teleport(buttplug.fdsjfhkdsjfdsjhk().getServer().getWorld("world").getSpawnLocation());
                player.sendMessage(ChatColor.GREEN + "Teleported to overworld spawn!");
            }
        }
        else if (event.getTo().getWorld().getEnvironment() == World.Environment.NETHER) {}
    }
}
