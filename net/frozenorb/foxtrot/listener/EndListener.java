package net.frozenorb.foxtrot.listener;

import net.frozenorb.foxtrot.*;
import org.bukkit.inventory.*;
import net.frozenorb.foxtrot.team.*;
import org.bukkit.inventory.meta.*;
import java.text.*;
import org.bukkit.event.*;
import org.bukkit.event.block.*;
import org.bukkit.*;
import org.bukkit.event.player.*;
import net.frozenorb.foxtrot.server.*;
import org.bukkit.potion.*;
import org.bukkit.entity.*;
import java.util.*;
import org.bukkit.event.entity.*;

public class EndListener implements Listener
{
    public static boolean endActive;
    private Map<String, Long> msgCooldown;
    
    public EndListener() {
        super();
        this.msgCooldown = new HashMap<String, Long>();
    }
    
    @EventHandler
    public void onEntityDeath(final EntityDeathEvent event) {
        if (event.getEntity() instanceof EnderDragon) {
            final faggot team = buttplug.fdsjfhkdsjfdsjhk().getDsfjhkdsjhdsjkhfds().getPlayerTeam(event.getEntity().getKiller().getName());
            String teamName = ChatColor.GOLD + "[" + ChatColor.YELLOW + "-" + ChatColor.GOLD + "]";
            if (team != null) {
                teamName = ChatColor.GOLD + "[" + ChatColor.YELLOW + team.getName() + ChatColor.GOLD + "]";
            }
            for (int i = 0; i < 6; ++i) {
                Bukkit.broadcastMessage("");
            }
            Bukkit.broadcastMessage(ChatColor.BLACK + "\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588");
            Bukkit.broadcastMessage(ChatColor.BLACK + "\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588");
            Bukkit.broadcastMessage(ChatColor.BLACK + "\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588" + ChatColor.GOLD + " [Enderdragon]");
            Bukkit.broadcastMessage(ChatColor.BLACK + "\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588" + ChatColor.YELLOW + " killed by");
            Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE + "\u2588" + ChatColor.DARK_PURPLE + "\u2588" + ChatColor.LIGHT_PURPLE + "\u2588" + ChatColor.BLACK + "\u2588\u2588" + ChatColor.LIGHT_PURPLE + "\u2588" + ChatColor.DARK_PURPLE + "\u2588" + ChatColor.LIGHT_PURPLE + "\u2588" + " " + teamName);
            Bukkit.broadcastMessage(ChatColor.BLACK + "\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588" + " " + event.getEntity().getKiller().getDisplayName());
            Bukkit.broadcastMessage(ChatColor.BLACK + "\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588");
            Bukkit.broadcastMessage(ChatColor.BLACK + "\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588");
            final ItemStack dragonEgg = new ItemStack(Material.DRAGON_EGG);
            final ItemMeta itemMeta = dragonEgg.getItemMeta();
            final DateFormat sdf = new SimpleDateFormat("M/d HH:mm:ss");
            itemMeta.setLore((List)Arrays.asList(ChatColor.DARK_PURPLE.toString() + ChatColor.BOLD + "Enderdragon " + ChatColor.WHITE + "slain by " + ChatColor.YELLOW + event.getEntity().getKiller().getName(), ChatColor.WHITE + sdf.format(new Date()).replace(" AM", "").replace(" PM", "")));
            dragonEgg.setItemMeta(itemMeta);
            event.getEntity().getKiller().getInventory().addItem(new ItemStack[] { dragonEgg });
            if (!event.getEntity().getKiller().getInventory().contains(Material.DRAGON_EGG)) {
                event.getDrops().add(dragonEgg);
            }
        }
    }
    
    @EventHandler
    public void onEntityCreatePortal(final EntityCreatePortalEvent event) {
        if (event.getEntity() instanceof Item && event.getPortalType() == PortalType.ENDER) {
            event.getBlocks().clear();
        }
    }
    
    @EventHandler
    public void onEntityDamage(final EntityDamageEvent event) {
        if (event.getEntity() instanceof EnderDragon && event.getEntity().getWorld().getEnvironment() == World.Environment.THE_END) {
            ((EnderDragon)event.getEntity()).setCustomName("Ender Dragon " + ChatColor.YELLOW.toString() + ChatColor.BOLD + Math.round(((EnderDragon)event.getEntity()).getHealth() / ((EnderDragon)event.getEntity()).getMaxHealth() * 100.0) + "% Health");
        }
    }
    
    @EventHandler
    public void onBlockPlace(final BlockPlaceEvent event) {
        if (event.getPlayer().getWorld().getEnvironment() == World.Environment.THE_END) {
            if (event.getPlayer().isOp() && event.getPlayer().getGameMode() == GameMode.CREATIVE) {
                return;
            }
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onBlockBreak(final BlockBreakEvent event) {
        if (event.getPlayer().getWorld().getEnvironment() == World.Environment.THE_END) {
            if (event.getPlayer().isOp() && event.getPlayer().getGameMode() == GameMode.CREATIVE) {
                return;
            }
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onPlayerChangedWorld(final PlayerChangedWorldEvent event) {
        if (event.getPlayer().getWorld().getEnvironment() == World.Environment.THE_END && event.getPlayer().getLocation().distanceSquared(new Location(event.getPlayer().getWorld(), 100.0, 49.0, 0.0)) < 4.0) {
            event.getPlayer().teleport(event.getPlayer().getWorld().getSpawnLocation());
        }
    }
    
    @EventHandler
    public void onCreatePortal(final EntityCreatePortalEvent event) {
        if (event.getEntity().getType() == EntityType.ENDER_DRAGON) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onPortal(final PlayerPortalEvent event) {
        if (event.getCause() != PlayerTeleportEvent.TeleportCause.END_PORTAL) {
            return;
        }
        final Player player = event.getPlayer();
        if (event.getTo().getWorld().getEnvironment() == World.Environment.NORMAL) {
            if (SpawnTagHandler.isTagged(event.getPlayer())) {
                event.setCancelled(true);
                if (!this.msgCooldown.containsKey(player.getName()) || this.msgCooldown.get(player.getName()) < System.currentTimeMillis()) {
                    event.getPlayer().sendMessage(ChatColor.RED + "You cannot leave the end while spawn tagged.");
                    this.msgCooldown.put(player.getName(), System.currentTimeMillis() + 3000L);
                }
            }
            if (event.getFrom().getWorld().getEntitiesByClass((Class)EnderDragon.class).size() != 0) {
                event.setCancelled(true);
                if (!this.msgCooldown.containsKey(player.getName()) || this.msgCooldown.get(player.getName()) < System.currentTimeMillis()) {
                    event.getPlayer().sendMessage(ChatColor.RED + "You cannot leave the end before the dragon is killed.");
                    this.msgCooldown.put(player.getName(), System.currentTimeMillis() + 3000L);
                }
            }
        }
        else if (event.getTo().getWorld().getEnvironment() == World.Environment.THE_END) {
            if (SpawnTagHandler.isTagged(event.getPlayer())) {
                event.setCancelled(true);
                if (!this.msgCooldown.containsKey(player.getName()) || this.msgCooldown.get(player.getName()) < System.currentTimeMillis()) {
                    event.getPlayer().sendMessage(ChatColor.RED + "You cannot enter the end while spawn tagged.");
                    this.msgCooldown.put(player.getName(), System.currentTimeMillis() + 3000L);
                }
            }
            if (buttplug.fdsjfhkdsjfdsjhk().getPvPTimerMap().hasTimer(player.getName())) {
                event.setCancelled(true);
                if (!this.msgCooldown.containsKey(player.getName()) || this.msgCooldown.get(player.getName()) < System.currentTimeMillis()) {
                    event.getPlayer().sendMessage(ChatColor.RED + "You cannot enter the end while you have pvp protection.");
                    this.msgCooldown.put(player.getName(), System.currentTimeMillis() + 3000L);
                }
            }
            if (!EndListener.endActive && event.getPlayer().getGameMode() != GameMode.CREATIVE) {
                event.setCancelled(true);
                if (!this.msgCooldown.containsKey(player.getName()) || this.msgCooldown.get(player.getName()) < System.currentTimeMillis()) {
                    event.getPlayer().sendMessage(ChatColor.RED + "The End is currently disabled.");
                    this.msgCooldown.put(player.getName(), System.currentTimeMillis() + 3000L);
                }
            }
        }
        if (event.getPlayer().hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) {
            boolean found = false;
            for (final PotionEffect potionEffect : event.getPlayer().getActivePotionEffects()) {
                if (potionEffect.getType().equals((Object)PotionEffectType.INCREASE_DAMAGE) && potionEffect.getDuration() < 200) {
                    found = true;
                }
            }
            if (found) {
                event.getPlayer().removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
            }
        }
    }
    
    @EventHandler
    public void onEntityExplode(final EntityExplodeEvent event) {
        if (event.getEntity() instanceof EnderDragon) {
            event.blockList().clear();
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onEntityPortal(final EntityPortalEvent event) {
        if (event.getEntity() instanceof EnderDragon) {
            event.setCancelled(true);
        }
    }
    
    static {
        EndListener.endActive = true;
    }
}
