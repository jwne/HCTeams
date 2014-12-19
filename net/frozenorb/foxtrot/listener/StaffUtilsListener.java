package net.frozenorb.foxtrot.listener;

import org.bukkit.event.entity.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.*;
import org.bukkit.event.player.*;
import net.frozenorb.foxtrot.*;
import org.bukkit.scheduler.*;
import net.frozenorb.foxtrot.nametag.*;
import org.bukkit.plugin.*;
import org.bukkit.inventory.*;

public class StaffUtilsListener implements Listener
{
    private static Location lastDamageLocation;
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDamageByEntity(final EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player && !event.isCancelled()) {
            StaffUtilsListener.lastDamageLocation = event.getEntity().getLocation();
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerInteract(final PlayerInteractEvent event) {
        if (StaffUtilsListener.lastDamageLocation != null && event.getItem() != null && event.getItem().getType() == Material.EMERALD && event.getPlayer().getGameMode() == GameMode.CREATIVE) {
            event.getPlayer().teleport(StaffUtilsListener.lastDamageLocation);
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerItemHeld(final PlayerItemHeldEvent event) {
        final ItemStack oldSlot = event.getPlayer().getInventory().getItem(event.getPreviousSlot());
        if (event.getPlayer().getGameMode() == GameMode.CREATIVE && oldSlot != null && oldSlot.getType() == Material.REDSTONE_BLOCK) {
            for (final Player player : buttplug.fdsjfhkdsjfdsjhk().getServer().getOnlinePlayers()) {
                new BukkitRunnable() {
                    public void run() {
                        NametagManager.reloadPlayer(player, event.getPlayer());
                    }
                }.runTaskLater((Plugin)buttplug.fdsjfhkdsjfdsjhk(), 2L);
            }
        }
    }
}
