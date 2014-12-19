package net.frozenorb.foxtrot.listener;

import net.frozenorb.foxtrot.*;
import net.frozenorb.foxtrot.team.*;
import net.frozenorb.foxtrot.team.claims.*;
import org.bukkit.event.*;
import java.util.*;
import org.bukkit.block.*;
import org.bukkit.event.block.*;
import org.bukkit.event.hanging.*;
import org.bukkit.entity.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.*;
import org.bukkit.inventory.*;

public class TeamListener implements Listener
{
    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event) {
        final faggot team = buttplug.fdsjfhkdsjfdsjhk().getDsfjhkdsjhdsjkhfds().getPlayerTeam(event.getPlayer().getName());
        if (team != null) {
            for (final Player online : team.getOnlineMembers()) {
                if (online != event.getPlayer()) {
                    online.sendMessage(ChatColor.GREEN + "Member Online: " + ChatColor.WHITE + event.getPlayer().getName());
                }
            }
            team.sendTeamInfo(event.getPlayer());
        }
        else {
            event.getPlayer().sendMessage(ChatColor.GRAY + "You are not on a team!");
        }
    }
    
    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent event) {
        final faggot team = buttplug.fdsjfhkdsjfdsjhk().getDsfjhkdsjhdsjkhfds().getPlayerTeam(event.getPlayer().getName());
        if (team != null) {
            for (final Player online : team.getOnlineMembers()) {
                online.sendMessage(ChatColor.RED + "Member Offline: " + ChatColor.WHITE + event.getPlayer().getName());
            }
        }
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockIgnite(final BlockIgniteEvent event) {
        if (event.getPlayer() != null && buttplug.fdsjfhkdsjfdsjhk().getServerHandler().isAdminOverride(event.getPlayer())) {
            return;
        }
        if (buttplug.fdsjfhkdsjfdsjhk().getServerHandler().isUnclaimedOrRaidable(event.getBlock().getLocation())) {
            return;
        }
        if (LandBoard.getInstance().getTeam(event.getBlock().getLocation()) != null) {
            final faggot owner = LandBoard.getInstance().getTeam(event.getBlock().getLocation());
            if (event.getCause() == BlockIgniteEvent.IgniteCause.FLINT_AND_STEEL && owner.isMember(event.getPlayer())) {
                return;
            }
            event.setCancelled(true);
        }
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockPlace(final BlockPlaceEvent event) {
        if (event.isCancelled() || buttplug.fdsjfhkdsjfdsjhk().getServerHandler().isAdminOverride(event.getPlayer())) {
            return;
        }
        if (buttplug.fdsjfhkdsjfdsjhk().getServerHandler().isUnclaimedOrRaidable(event.getBlock().getLocation())) {
            return;
        }
        final faggot team = LandBoard.getInstance().getTeam(event.getBlock().getLocation());
        if (team != null && !team.isMember(event.getPlayer())) {
            event.getPlayer().sendMessage(ChatColor.YELLOW + "You cannot build in " + team.getName(event.getPlayer()) + ChatColor.YELLOW + "'s territory!");
            event.setCancelled(true);
        }
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockBreak(final BlockBreakEvent event) {
        if (event.isCancelled() || buttplug.fdsjfhkdsjfdsjhk().getServerHandler().isAdminOverride(event.getPlayer())) {
            return;
        }
        if (buttplug.fdsjfhkdsjfdsjhk().getServerHandler().isUnclaimedOrRaidable(event.getBlock().getLocation())) {
            return;
        }
        final faggot team = LandBoard.getInstance().getTeam(event.getBlock().getLocation());
        if (team == null || team.isMember(event.getPlayer())) {
            return;
        }
        event.getPlayer().sendMessage(ChatColor.YELLOW + "You cannot build in " + team.getName(event.getPlayer()) + ChatColor.YELLOW + "'s territory!");
        event.setCancelled(true);
        if (!Arrays.asList(FoxListener.NON_TRANSPARENT_ATTACK_DISABLING_BLOCKS).contains(event.getBlock().getType()) && (event.getBlock().isEmpty() || event.getBlock().getType().isTransparent() || !event.getBlock().getType().isSolid())) {
            return;
        }
        buttplug.fdsjfhkdsjfdsjhk().getServerHandler().disablePlayerAttacking(event.getPlayer(), 1);
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockPistonRetract(final BlockPistonRetractEvent event) {
        if (event.isCancelled() || !event.isSticky() || buttplug.fdsjfhkdsjfdsjhk().getServerHandler().isUnclaimedOrRaidable(event.getBlock().getLocation())) {
            return;
        }
        final Block retractBlock = event.getRetractLocation().getBlock();
        if (retractBlock.isEmpty() || retractBlock.isLiquid()) {
            return;
        }
        final faggot pistonTeam = LandBoard.getInstance().getTeam(event.getBlock().getLocation());
        final faggot targetTeam = LandBoard.getInstance().getTeam(retractBlock.getLocation());
        if (pistonTeam == targetTeam) {
            return;
        }
        event.setCancelled(true);
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockPistonExtend(final BlockPistonExtendEvent event) {
        if (event.isCancelled()) {
            return;
        }
        final faggot pistonTeam = LandBoard.getInstance().getTeam(event.getBlock().getLocation());
        int i = 0;
        for (final Block block : event.getBlocks()) {
            ++i;
            final Block targetBlock = event.getBlock().getRelative(event.getDirection(), i + 1);
            final faggot targetTeam = LandBoard.getInstance().getTeam(targetBlock.getLocation());
            if (targetTeam != pistonTeam && targetTeam != null) {
                if (targetTeam.isRaidable()) {
                    continue;
                }
                if (!targetBlock.isEmpty() && !targetBlock.isLiquid()) {
                    continue;
                }
                event.setCancelled(true);
            }
        }
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onHangingPlace(final HangingPlaceEvent event) {
        if (buttplug.fdsjfhkdsjfdsjhk().getServerHandler().isAdminOverride(event.getPlayer()) || buttplug.fdsjfhkdsjfdsjhk().getServerHandler().isUnclaimedOrRaidable(event.getEntity().getLocation())) {
            return;
        }
        final faggot team = LandBoard.getInstance().getTeam(event.getEntity().getLocation());
        if (team != null && !team.isMember(event.getPlayer())) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onHangingBreakByEntity(final HangingBreakByEntityEvent event) {
        if (!(event.getRemover() instanceof Player) || buttplug.fdsjfhkdsjfdsjhk().getServerHandler().isAdminOverride((Player)event.getRemover())) {
            return;
        }
        if (buttplug.fdsjfhkdsjfdsjhk().getServerHandler().isUnclaimedOrRaidable(event.getEntity().getLocation())) {
            return;
        }
        final faggot team = LandBoard.getInstance().getTeam(event.getEntity().getLocation());
        if (team != null && !team.isMember((Player)event.getRemover())) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerInteractEntityEvent(final PlayerInteractEntityEvent event) {
        if (event.isCancelled() || event.getRightClicked().getType() != EntityType.ITEM_FRAME || buttplug.fdsjfhkdsjfdsjhk().getServerHandler().isAdminOverride(event.getPlayer())) {
            return;
        }
        if (buttplug.fdsjfhkdsjfdsjhk().getServerHandler().isUnclaimedOrRaidable(event.getRightClicked().getLocation())) {
            return;
        }
        final faggot team = LandBoard.getInstance().getTeam(event.getRightClicked().getLocation());
        if (team != null && !team.isMember(event.getPlayer())) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityDamageByEntity(final EntityDamageByEntityEvent event) {
        if (event.isCancelled() || !(event.getEntity() instanceof Player) || event.getEntity().getType() != EntityType.ITEM_FRAME || buttplug.fdsjfhkdsjfdsjhk().getServerHandler().isAdminOverride((Player)event.getDamager())) {
            return;
        }
        if (buttplug.fdsjfhkdsjfdsjhk().getServerHandler().isUnclaimedOrRaidable(event.getEntity().getLocation())) {
            return;
        }
        final faggot team = LandBoard.getInstance().getTeam(event.getEntity().getLocation());
        if (team != null && !team.isMember((Player)event.getDamager())) {
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
            final faggot team = buttplug.fdsjfhkdsjfdsjhk().getDsfjhkdsjhdsjkhfds().getPlayerTeam(damager.getName());
            final Player victim = (Player)event.getEntity();
            if (team != null && team.isMember(victim.getName()) && event.getCause() != EntityDamageEvent.DamageCause.FALL) {
                damager.sendMessage(ChatColor.GREEN + "You cannot hurt " + ChatColor.YELLOW + victim.getName() + ChatColor.GREEN + ".");
                event.setCancelled(true);
            }
        }
    }
    
    @EventHandler
    public void onBucketEmpty(final PlayerBucketEmptyEvent event) {
        if (buttplug.fdsjfhkdsjfdsjhk().getServerHandler().isAdminOverride(event.getPlayer())) {
            return;
        }
        final faggot owner = LandBoard.getInstance().getTeam(event.getBlockClicked().getRelative(event.getBlockFace()).getLocation());
        if (owner != null && !owner.isMember(event.getPlayer())) {
            event.setCancelled(true);
            event.getBlockClicked().getRelative(event.getBlockFace()).setType(Material.AIR);
            event.setItemStack(new ItemStack(event.getBucket()));
            event.getPlayer().sendMessage(ChatColor.YELLOW + "You cannot build in " + owner.getName(event.getPlayer()) + ChatColor.YELLOW + "'s territory!");
        }
    }
}
