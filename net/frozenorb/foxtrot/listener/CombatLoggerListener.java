package net.frozenorb.foxtrot.listener;

import net.frozenorb.foxtrot.*;
import org.bukkit.inventory.*;
import org.bukkit.craftbukkit.v1_7_R3.entity.*;
import net.frozenorb.foxtrot.team.*;
import org.bukkit.event.*;
import net.minecraft.util.com.mojang.authlib.*;
import org.bukkit.event.world.*;
import org.bukkit.entity.*;
import java.util.*;
import org.bukkit.event.entity.*;
import net.frozenorb.foxtrot.team.dtr.bitmask.*;
import org.bukkit.event.player.*;
import org.bukkit.plugin.*;
import org.bukkit.craftbukkit.v1_7_R3.*;
import net.frozenorb.foxtrot.nms.*;
import net.minecraft.server.v1_7_R3.*;
import org.bukkit.potion.*;
import org.bukkit.metadata.*;
import org.bukkit.*;
import java.lang.reflect.*;

public class CombatLoggerListener implements Listener
{
    public static final String COMBAT_LOGGER_METADATA = "CombatLogger";
    
    @EventHandler
    public void onEntityDeath(final EntityDeathEvent event) {
        if (event.getEntity().hasMetadata("CombatLogger")) {
            final String playerName = event.getEntity().getCustomName().substring(2);
            buttplug.fdsjfhkdsjfdsjhk().getLogger().info(playerName + "'s combat logger at (" + event.getEntity().getLocation().getBlockX() + ", " + event.getEntity().getLocation().getBlockY() + ", " + event.getEntity().getLocation().getBlockZ() + ") died.");
            buttplug.fdsjfhkdsjfdsjhk().getDeathbanMap().deathban(playerName, buttplug.fdsjfhkdsjfdsjhk().getServerHandler().getDeathBanAt(playerName, event.getEntity().getLocation()));
            final faggot team = buttplug.fdsjfhkdsjfdsjhk().getDsfjhkdsjhdsjkhfds().getPlayerTeam(playerName);
            if (team != null) {
                team.playerDeath(playerName, buttplug.fdsjfhkdsjfdsjhk().getServerHandler().getDTRLossAt(event.getEntity().getLocation()));
            }
            if (event.getEntity().getKiller() != null) {
                buttplug.fdsjfhkdsjfdsjhk().getServer().broadcastMessage(ChatColor.RED + playerName + ChatColor.GRAY + " (Combat-Logger)" + ChatColor.YELLOW + " was slain by " + ChatColor.RED + event.getEntity().getKiller().getName() + ChatColor.YELLOW + ".");
                for (final ItemStack item : (ItemStack[])event.getEntity().getMetadata("CombatLogger").get(0).value()) {
                    event.getDrops().add(item);
                }
                event.getDrops().add(buttplug.fdsjfhkdsjfdsjhk().getServerHandler().generateDeathSign(playerName, event.getEntity().getKiller().getName()));
            }
            Player target = buttplug.fdsjfhkdsjfdsjhk().getServer().getPlayer(playerName);
            if (target == null) {
                final MinecraftServer server = ((CraftServer)buttplug.fdsjfhkdsjfdsjhk().getServer()).getServer();
                final EntityPlayer entity = new EntityPlayer(server, server.getWorldServer(0), getGameProfile(playerName, buttplug.fdsjfhkdsjfdsjhk().getServer().getOfflinePlayer(playerName).getUniqueId()), new PlayerInteractManager((World)server.getWorldServer(0)));
                target = (Player)entity.getBukkitEntity();
                if (target != null) {
                    target.loadData();
                }
            }
            final EntityHuman humanTarget = ((CraftHumanEntity)target).getHandle();
            target.getInventory().clear();
            target.getInventory().setArmorContents((ItemStack[])null);
            humanTarget.setHealth(0.0f);
            target.saveData();
        }
    }
    
    public static GameProfile getGameProfile(final String name, final UUID id) {
        return new GameProfile(id, name);
    }
    
    @EventHandler
    public void onEntityInteract(final PlayerInteractEntityEvent event) {
        if (event.getRightClicked().hasMetadata("CombatLogger")) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onEntityDespawn(final ChunkUnloadEvent event) {
        for (final Entity entity : event.getChunk().getEntities()) {
            if (entity.hasMetadata("CombatLogger") && !entity.isDead()) {
                event.setCancelled(true);
            }
        }
    }
    
    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event) {
        for (final Entity entity : event.getPlayer().getWorld().getEntitiesByClass((Class)Villager.class)) {
            final Villager villager = (Villager)entity;
            if (villager.isCustomNameVisible() && ChatColor.stripColor(villager.getCustomName()).equals(event.getPlayer().getName())) {
                villager.remove();
            }
        }
    }
    
    @EventHandler
    public void onEntityDamageByEntity(final EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity().hasMetadata("CombatLogger")) {
            final Player player = (Player)event.getDamager();
            final Villager villager = (Villager)event.getEntity();
            final String playerName = villager.getCustomName().substring(2);
            if (!buttplug.fdsjfhkdsjfdsjhk().getServerHandler().isEOTW() && (DTRBitmaskType.SAFE_ZONE.appliesAt(player.getLocation()) || DTRBitmaskType.SAFE_ZONE.appliesAt(villager.getLocation()))) {
                event.setCancelled(true);
                return;
            }
            if (buttplug.fdsjfhkdsjfdsjhk().getPvPTimerMap().hasTimer(player.getName())) {
                event.setCancelled(true);
                return;
            }
            final faggot team = buttplug.fdsjfhkdsjfdsjhk().getDsfjhkdsjhdsjkhfds().getPlayerTeam(playerName);
            if (team != null && team.isMember(player.getName())) {
                event.setCancelled(true);
            }
        }
    }
    
    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent event) {
        if (event.getPlayer().hasMetadata("loggedout")) {
            event.getPlayer().removeMetadata("loggedout", (Plugin)buttplug.fdsjfhkdsjfdsjhk());
            return;
        }
        if (!buttplug.fdsjfhkdsjfdsjhk().getServerHandler().isEOTW() && DTRBitmaskType.SAFE_ZONE.appliesAt(event.getPlayer().getLocation())) {
            return;
        }
        if (!buttplug.fdsjfhkdsjfdsjhk().getServerHandler().isPreEOTW() && buttplug.fdsjfhkdsjfdsjhk().getPvPTimerMap().hasTimer(event.getPlayer().getName())) {
            return;
        }
        if (event.getPlayer().isDead()) {
            return;
        }
        if (event.getPlayer().getLocation().getBlockY() <= 0) {
            return;
        }
        boolean enemyWithinRange = false;
        for (final Entity entity : event.getPlayer().getNearbyEntities(40.0, 256.0, 40.0)) {
            if (entity instanceof Player) {
                final Player other = (Player)entity;
                if (other.hasMetadata("invisible")) {
                    continue;
                }
                if (buttplug.fdsjfhkdsjfdsjhk().getDsfjhkdsjhdsjkhfds().getPlayerTeam(other.getName()) == buttplug.fdsjfhkdsjfdsjhk().getDsfjhkdsjhdsjkhfds().getPlayerTeam(event.getPlayer().getName())) {
                    continue;
                }
                enemyWithinRange = true;
            }
        }
        if (event.getPlayer().getGameMode() != GameMode.CREATIVE && !event.getPlayer().hasMetadata("invisible") && enemyWithinRange && !event.getPlayer().isDead()) {
            final String playerName = ChatColor.RED.toString() + event.getPlayer().getName();
            buttplug.fdsjfhkdsjfdsjhk().getLogger().info(event.getPlayer().getName() + " combat logged at (" + event.getPlayer().getLocation().getBlockX() + ", " + event.getPlayer().getLocation().getBlockY() + ", " + event.getPlayer().getLocation().getBlockZ() + ")");
            final ItemStack[] armor = event.getPlayer().getInventory().getArmorContents();
            final ItemStack[] inv = event.getPlayer().getInventory().getContents();
            final ItemStack[] drops = new ItemStack[armor.length + inv.length];
            System.arraycopy(armor, 0, drops, 0, armor.length);
            System.arraycopy(inv, 0, drops, armor.length, inv.length);
            final FixedVillager fixedVillager = new FixedVillager((World)((CraftWorld)event.getPlayer().getWorld()).getHandle());
            final Location location = event.getPlayer().getLocation();
            fixedVillager.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
            final int i = MathHelper.floor(fixedVillager.locX / 16.0);
            final int j = MathHelper.floor(fixedVillager.locZ / 16.0);
            final World world = (World)((CraftWorld)event.getPlayer().getWorld()).getHandle();
            world.getChunkAt(i, j).a((net.minecraft.server.v1_7_R3.Entity)fixedVillager);
            world.entityList.add(fixedVillager);
            try {
                final Method m = world.getClass().getDeclaredMethod("a", net.minecraft.server.v1_7_R3.Entity.class);
                m.setAccessible(true);
                m.invoke(world, fixedVillager);
            }
            catch (Exception ex) {}
            final Villager villager = (Villager)fixedVillager.getBukkitEntity();
            villager.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 100));
            villager.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 100));
            villager.setMetadata("CombatLogger", (MetadataValue)new FixedMetadataValue((Plugin)buttplug.fdsjfhkdsjfdsjhk(), (Object)drops));
            villager.setAgeLock(true);
            int potions = 0;
            boolean gapple = false;
            for (final ItemStack itemStack : event.getPlayer().getInventory().getContents()) {
                if (itemStack != null) {
                    if (itemStack.getType() == Material.POTION && itemStack.getDurability() == 16421) {
                        ++potions;
                    }
                    else if (!gapple && itemStack.getType() == Material.GOLDEN_APPLE && itemStack.getDurability() == 1) {
                        potions += 15;
                        gapple = true;
                    }
                }
            }
            villager.setMaxHealth(potions * 3.5 + event.getPlayer().getHealth());
            villager.setHealth(villager.getMaxHealth());
            villager.setCustomName(playerName);
            villager.setCustomNameVisible(true);
            buttplug.fdsjfhkdsjfdsjhk().getServer().getScheduler().runTaskLater((Plugin)buttplug.fdsjfhkdsjfdsjhk(), (Runnable)new Runnable() {
                @Override
                public void run() {
                    if (!villager.isDead() && villager.isValid()) {
                        villager.remove();
                    }
                }
            }, 600L);
        }
    }
}
