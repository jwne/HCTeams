package net.frozenorb.foxtrot.listener;

import org.bukkit.event.player.*;
import net.frozenorb.foxtrot.*;
import org.bukkit.plugin.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import java.util.*;
import org.bukkit.*;
import org.bukkit.event.entity.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;

public class PvPTimerListener implements Listener
{
    private Set<Integer> droppedItems;
    
    public PvPTimerListener() {
        super();
        this.droppedItems = new HashSet<Integer>();
    }
    
    @EventHandler
    public void onPlayerPickupItem(final PlayerPickupItemEvent event) {
        if (buttplug.fdsjfhkdsjfdsjhk().getPvPTimerMap().hasTimer(event.getPlayer().getName()) && this.droppedItems.contains(event.getItem().getEntityId())) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onItemSpawn(final ItemSpawnEvent event) {
        final ItemStack itemStack = event.getEntity().getItemStack();
        if (itemStack.hasItemMeta() && itemStack.getItemMeta().hasLore() && itemStack.getItemMeta().getLore().contains("§8PVP Loot")) {
            final ItemMeta meta = itemStack.getItemMeta();
            final List<String> lore = (List<String>)meta.getLore();
            lore.remove("§8PVP Loot");
            meta.setLore((List)lore);
            itemStack.setItemMeta(meta);
            event.getEntity().setItemStack(itemStack);
            final int id = event.getEntity().getEntityId();
            this.droppedItems.add(id);
            buttplug.fdsjfhkdsjfdsjhk().getServer().getScheduler().runTaskLater((Plugin)buttplug.fdsjfhkdsjfdsjhk(), (Runnable)new Runnable() {
                @Override
                public void run() {
                    PvPTimerListener.this.droppedItems.remove(id);
                }
            }, 1200L);
        }
    }
    
    @EventHandler
    public void onPlayerDeath(final PlayerDeathEvent event) {
        for (final ItemStack itemStack : event.getDrops()) {
            final ItemMeta meta = itemStack.getItemMeta();
            List<String> lore = new ArrayList<String>();
            if (meta.hasLore()) {
                lore = (List<String>)meta.getLore();
            }
            lore.add("§8PVP Loot");
            meta.setLore((List)lore);
            itemStack.setItemMeta(meta);
        }
    }
    
    @EventHandler
    public void onEntityShootBow(final EntityShootBowEvent event) {
        if (event.getEntity() instanceof Player) {
            final Player player = (Player)event.getEntity();
            if (buttplug.fdsjfhkdsjfdsjhk().getPvPTimerMap().hasTimer(player.getName())) {
                player.sendMessage(ChatColor.RED + "You cannot do this while your PVP Timer is active!");
                player.sendMessage(ChatColor.RED + "Type '" + ChatColor.YELLOW + "/pvp enable" + ChatColor.RED + "' to remove your timer.");
                event.setCancelled(true);
            }
        }
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityDamageByEntity(final EntityDamageByEntityEvent event) {
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
        if (damager == null) {
            return;
        }
        if (!buttplug.fdsjfhkdsjfdsjhk().getServerHandler().isPreEOTW() && buttplug.fdsjfhkdsjfdsjhk().getPvPTimerMap().hasTimer(damager.getName())) {
            damager.sendMessage(ChatColor.RED + "You cannot do this while your PVP Timer is active!");
            damager.sendMessage(ChatColor.RED + "Type '" + ChatColor.YELLOW + "/pvp enable" + ChatColor.RED + "' to remove your timer.");
            event.setCancelled(true);
            return;
        }
        if (!buttplug.fdsjfhkdsjfdsjhk().getServerHandler().isPreEOTW() && buttplug.fdsjfhkdsjfdsjhk().getPvPTimerMap().hasTimer(((Player)event.getEntity()).getName())) {
            damager.sendMessage(ChatColor.RED + "That player currently has their PVP Timer!");
            event.setCancelled(true);
        }
    }
}
