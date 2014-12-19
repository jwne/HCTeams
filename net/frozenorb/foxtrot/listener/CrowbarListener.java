package net.frozenorb.foxtrot.listener;

import org.bukkit.event.player.*;
import net.frozenorb.foxtrot.util.*;
import net.frozenorb.foxtrot.*;
import net.frozenorb.foxtrot.team.claims.*;
import net.frozenorb.foxtrot.team.dtr.bitmask.*;
import org.bukkit.inventory.*;
import java.util.*;
import org.bukkit.*;
import org.apache.commons.lang.*;
import net.frozenorb.foxtrot.team.*;
import org.bukkit.block.*;
import org.bukkit.inventory.meta.*;
import org.bukkit.event.*;
import org.bukkit.event.block.*;

public class CrowbarListener implements Listener
{
    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent event) {
        if (event.isCancelled()) {
            return;
        }
        if (event.getItem() == null || !InvUtils.isSimilar(event.getItem(), InvUtils.CROWBAR_NAME) || (event.getAction() != Action.LEFT_CLICK_BLOCK && event.getAction() != Action.RIGHT_CLICK_BLOCK)) {
            return;
        }
        if (!buttplug.fdsjfhkdsjfdsjhk().getServerHandler().isUnclaimedOrRaidable(event.getClickedBlock().getLocation()) && !buttplug.fdsjfhkdsjfdsjhk().getServerHandler().isAdminOverride(event.getPlayer())) {
            final faggot team = LandBoard.getInstance().getTeam(event.getClickedBlock().getLocation());
            if (team != null && !team.isMember(event.getPlayer())) {
                event.getPlayer().sendMessage(ChatColor.YELLOW + "You cannot crowbar in " + ChatColor.RED + team.getName(event.getPlayer()) + ChatColor.YELLOW + "'s territory!");
                return;
            }
        }
        if (DTRBitmaskType.SAFE_ZONE.appliesAt(event.getClickedBlock().getLocation())) {
            event.getPlayer().sendMessage(ChatColor.YELLOW + "You cannot crowbar spawn!");
            return;
        }
        if (event.getClickedBlock().getType() == Material.ENDER_PORTAL_FRAME) {
            int portals = InvUtils.getCrowbarUsesPortal(event.getItem());
            if (portals == 0) {
                event.getPlayer().sendMessage(ChatColor.RED + "This crowbar has no more uses on end portals!");
                return;
            }
            event.getClickedBlock().getWorld().playEffect(event.getClickedBlock().getLocation(), Effect.STEP_SOUND, event.getClickedBlock().getTypeId());
            event.getClickedBlock().setType(Material.AIR);
            event.getClickedBlock().getState().update();
            event.getClickedBlock().getWorld().dropItemNaturally(event.getClickedBlock().getLocation(), new ItemStack(Material.ENDER_PORTAL_FRAME));
            event.getClickedBlock().getWorld().playSound(event.getClickedBlock().getLocation(), Sound.ANVIL_USE, 1.0f, 1.0f);
            for (int x = -3; x < 3; ++x) {
                for (int z = -3; z < 3; ++z) {
                    final Block block = event.getClickedBlock().getLocation().add((double)x, 0.0, (double)z).getBlock();
                    if (block.getType() == Material.ENDER_PORTAL) {
                        block.setType(Material.AIR);
                        block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, Material.ENDER_PORTAL.getId());
                    }
                }
            }
            if (--portals == 0) {
                event.getPlayer().setItemInHand((ItemStack)null);
                event.getClickedBlock().getLocation().getWorld().playSound(event.getClickedBlock().getLocation(), Sound.ITEM_BREAK, 1.0f, 1.0f);
                return;
            }
            final ItemMeta meta = event.getItem().getItemMeta();
            meta.setLore((List)InvUtils.getCrowbarLore(portals, 0));
            event.getItem().setItemMeta(meta);
            final double max = Material.DIAMOND_HOE.getMaxDurability();
            final double dura = max / 6.0 * portals;
            event.getItem().setDurability((short)(max - dura));
            event.getPlayer().setItemInHand(event.getItem());
        }
        else if (event.getClickedBlock().getType() == Material.MOB_SPAWNER) {
            final CreatureSpawner spawner = (CreatureSpawner)event.getClickedBlock().getState();
            int spawners = InvUtils.getCrowbarUsesSpawner(event.getItem());
            if (spawners == 0) {
                event.getPlayer().sendMessage(ChatColor.RED + "This crowbar has no more uses on mob spawners!");
                return;
            }
            if (event.getClickedBlock().getWorld().getEnvironment() == World.Environment.NETHER) {
                event.getPlayer().sendMessage(ChatColor.RED + "You cannot break spawners in the nether!");
                event.setCancelled(true);
                return;
            }
            if (event.getClickedBlock().getWorld().getEnvironment() == World.Environment.THE_END) {
                event.getPlayer().sendMessage(ChatColor.RED + "You cannot break spawners in the end!");
                event.setCancelled(true);
                return;
            }
            event.getClickedBlock().getLocation().getWorld().playEffect(event.getClickedBlock().getLocation(), Effect.STEP_SOUND, event.getClickedBlock().getTypeId());
            event.getClickedBlock().setType(Material.AIR);
            event.getClickedBlock().getState().update();
            final ItemStack drop = new ItemStack(Material.MOB_SPAWNER);
            ItemMeta meta2 = drop.getItemMeta();
            meta2.setDisplayName(ChatColor.RESET + StringUtils.capitaliseAllWords(spawner.getSpawnedType().toString().toLowerCase().replaceAll("_", " ")) + " Spawner");
            drop.setItemMeta(meta2);
            event.getClickedBlock().getLocation().getWorld().dropItemNaturally(event.getClickedBlock().getLocation(), drop);
            event.getClickedBlock().getLocation().getWorld().playSound(event.getClickedBlock().getLocation(), Sound.ANVIL_USE, 1.0f, 1.0f);
            if (--spawners == 0) {
                event.getPlayer().setItemInHand((ItemStack)null);
                event.getClickedBlock().getLocation().getWorld().playSound(event.getClickedBlock().getLocation(), Sound.ITEM_BREAK, 1.0f, 1.0f);
                return;
            }
            meta2 = event.getItem().getItemMeta();
            meta2.setLore((List)InvUtils.getCrowbarLore(0, spawners));
            event.getItem().setItemMeta(meta2);
            final double max2 = Material.DIAMOND_HOE.getMaxDurability();
            final double dura2 = max2 / 1.0 * spawners;
            event.getItem().setDurability((short)(max2 - dura2));
            event.getPlayer().setItemInHand(event.getItem());
        }
        else {
            event.getPlayer().sendMessage(ChatColor.RED + "Crowbars can only break end portals and mob spawners!");
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onBlockBreak(final BlockBreakEvent event) {
        if (event.getBlock().getType() == Material.MOB_SPAWNER && event.getBlock().getWorld().getEnvironment() == World.Environment.NETHER) {
            event.getPlayer().sendMessage(ChatColor.RED + "You cannot break mob spawners in the nether!");
            event.setCancelled(true);
        }
    }
}
