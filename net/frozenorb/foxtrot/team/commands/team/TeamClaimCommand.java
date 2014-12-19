package net.frozenorb.foxtrot.team.commands.team;

import org.bukkit.inventory.*;
import org.bukkit.entity.*;
import net.frozenorb.foxtrot.*;
import org.bukkit.scheduler.*;
import org.bukkit.plugin.*;
import net.frozenorb.foxtrot.team.claims.*;
import net.frozenorb.foxtrot.team.*;
import net.frozenorb.foxtrot.command.annotations.*;
import org.bukkit.event.player.*;
import org.bukkit.event.*;
import org.bukkit.event.inventory.*;
import org.bukkit.*;
import net.frozenorb.foxtrot.util.*;
import java.util.*;
import org.bukkit.inventory.meta.*;

public class TeamClaimCommand implements Listener
{
    public static final ItemStack SELECTION_WAND;
    
    @Command(names = { "team claim", "t claim", "f claim", "faction claim", "fac claim" }, permissionNode = "")
    public static void teamClaim(final Player sender) {
        final faggot team = buttplug.fdsjfhkdsjfdsjhk().getDsfjhkdsjhdsjkhfds().getPlayerTeam(sender.getName());
        if (team == null) {
            sender.sendMessage(ChatColor.GRAY + "You are not on a team!");
            return;
        }
        if (team.isOwner(sender.getName()) || team.isCaptain(sender.getName())) {
            sender.getInventory().remove(TeamClaimCommand.SELECTION_WAND);
            if (team.isRaidable()) {
                sender.sendMessage(ChatColor.RED + "You may not claim land while your faction is raidable!");
                return;
            }
            new BukkitRunnable() {
                public void run() {
                    sender.getInventory().addItem(new ItemStack[] { TeamClaimCommand.SELECTION_WAND.clone() });
                }
            }.runTaskLater((Plugin)buttplug.fdsjfhkdsjfdsjhk(), 1L);
            new VisualClaim(sender, VisualClaimType.CREATE, false).draw(false);
            if (!VisualClaim.getCurrentMaps().containsKey(sender.getName())) {
                new VisualClaim(sender, VisualClaimType.MAP, false).draw(true);
            }
        }
        else {
            sender.sendMessage(ChatColor.DARK_AQUA + "Only team captains can do this.");
        }
    }
    
    @Command(names = { "team opclaim", "t opclaim", "f opclaim", "faction opclaim", "fac opclaim" }, permissionNode = "op")
    public static void teamOpClaim(final Player sender) {
        final faggot team = buttplug.fdsjfhkdsjfdsjhk().getDsfjhkdsjhdsjkhfds().getPlayerTeam(sender.getName());
        if (team == null) {
            sender.sendMessage(ChatColor.GRAY + "You are not on a team!");
            return;
        }
        sender.getInventory().remove(TeamClaimCommand.SELECTION_WAND);
        new BukkitRunnable() {
            public void run() {
                sender.getInventory().addItem(new ItemStack[] { TeamClaimCommand.SELECTION_WAND.clone() });
            }
        }.runTaskLater((Plugin)buttplug.fdsjfhkdsjfdsjhk(), 1L);
        new VisualClaim(sender, VisualClaimType.CREATE, true).draw(false);
        if (!VisualClaim.getCurrentMaps().containsKey(sender.getName())) {
            new VisualClaim(sender, VisualClaimType.MAP, true).draw(true);
        }
    }
    
    @EventHandler
    public void onPlayerDropItem(final PlayerDropItemEvent e) {
        if (e.getItemDrop().getItemStack().equals((Object)TeamClaimCommand.SELECTION_WAND)) {
            final VisualClaim vc = VisualClaim.getVisualClaim(e.getPlayer().getName());
            if (vc != null) {
                e.setCancelled(true);
                vc.cancel(false);
            }
            buttplug.fdsjfhkdsjfdsjhk().getServer().getScheduler().runTaskLater((Plugin)buttplug.fdsjfhkdsjfdsjhk(), () -> e.getItemDrop().remove(), 1L);
        }
    }
    
    @EventHandler
    public void onInventoryOpen(final InventoryOpenEvent e) {
        e.getPlayer().getInventory().remove(TeamClaimCommand.SELECTION_WAND);
    }
    
    static {
        SELECTION_WAND = new ItemStack(Material.WOOD_HOE);
        final ItemMeta meta = TeamClaimCommand.SELECTION_WAND.getItemMeta();
        meta.setDisplayName("§a§oClaiming Wand");
        meta.setLore((List)ListUtils.wrap(" | §eRight/Left Click§6 Block   §b- §fSelect claim's corners | §eRight Click §6Air  |  §b- §fCancel current claim | §9Shift §eLeft Click §6Block/Air   §b- §fPurchase current claim", ""));
        TeamClaimCommand.SELECTION_WAND.setItemMeta(meta);
    }
}
