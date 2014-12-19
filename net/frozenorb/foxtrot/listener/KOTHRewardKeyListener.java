package net.frozenorb.foxtrot.listener;

import org.bukkit.event.player.*;
import net.frozenorb.foxtrot.team.dtr.bitmask.*;
import net.frozenorb.foxtrot.util.*;
import org.bukkit.inventory.*;
import org.bukkit.*;
import net.frozenorb.foxtrot.*;
import net.minecraft.util.org.apache.commons.lang3.text.*;
import org.bukkit.scheduler.*;
import java.util.*;
import org.bukkit.plugin.*;
import org.bukkit.block.*;
import org.bukkit.event.*;
import java.io.*;

public class KOTHRewardKeyListener implements Listener
{
    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent event) {
        if (event.getClickedBlock() == null || event.getItem() == null || event.getClickedBlock().getType() != Material.ENDER_CHEST || !DTRBitmaskType.SAFE_ZONE.appliesAt(event.getClickedBlock().getLocation()) || !InvUtils.isSimilar(event.getItem(), ChatColor.RED + "KOTH Reward Key")) {
            return;
        }
        event.setCancelled(true);
        int open = 0;
        for (final ItemStack itemStack : event.getPlayer().getInventory().getContents()) {
            if (itemStack == null || itemStack.getType() == Material.AIR) {
                ++open;
            }
        }
        if (open < 5) {
            event.getPlayer().sendMessage(ChatColor.RED + "You must have at least 5 open inventory slots to use a KOTH reward key!");
            return;
        }
        final int tier = InvUtils.getKOTHRewardKeyTier(event.getItem());
        event.getPlayer().setItemInHand((ItemStack)null);
        event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.FIREWORK_BLAST, 1.0f, 1.0f);
        final Block block = event.getClickedBlock().getRelative(BlockFace.DOWN, tier + 3);
        if (block.getType() != Material.CHEST) {
            return;
        }
        final Chest chest = (Chest)block.getState();
        final ItemStack[] lootTables = chest.getBlockInventory().getContents();
        final List<ItemStack> loot = new ArrayList<ItemStack>();
        int given = 0;
        int tries = 0;
        while (given < 5 && tries < 500) {
            ++tries;
            final ItemStack chosenItem = lootTables[buttplug.RANDOM.nextInt(lootTables.length)];
            if (chosenItem != null && chosenItem.getType() != Material.AIR) {
                if (chosenItem.getAmount() == 0) {
                    continue;
                }
                ++given;
                if (chosenItem.getAmount() > 1) {
                    final ItemStack targetClone = chosenItem.clone();
                    targetClone.setAmount(buttplug.RANDOM.nextInt(chosenItem.getAmount()));
                    loot.add(targetClone);
                }
                else {
                    loot.add(chosenItem);
                }
            }
        }
        final StringBuilder builder = new StringBuilder();
        for (final ItemStack itemStack2 : loot) {
            final String displayName = (itemStack2.hasItemMeta() && itemStack2.getItemMeta().hasDisplayName()) ? (ChatColor.RED.toString() + ChatColor.ITALIC + ChatColor.stripColor(itemStack2.getItemMeta().getDisplayName())) : (ChatColor.BLUE.toString() + itemStack2.getAmount() + "x " + ChatColor.YELLOW + WordUtils.capitalize(itemStack2.getType().name().replace("_", " ").toLowerCase()));
            builder.append(ChatColor.YELLOW).append(displayName).append(ChatColor.GOLD).append(", ");
        }
        if (builder.length() > 2) {
            builder.setLength(builder.length() - 2);
        }
        buttplug.fdsjfhkdsjfdsjhk().getServer().broadcastMessage(ChatColor.GOLD + "[KingOfTheHill] " + ChatColor.GOLD + event.getPlayer().getName() + ChatColor.YELLOW + " is obtaining loot for a " + ChatColor.BLUE.toString() + ChatColor.ITALIC + "Level " + tier + " Key" + ChatColor.YELLOW + " obtained from " + ChatColor.GOLD + InvUtils.getLoreData(event.getItem(), 1) + ChatColor.YELLOW + " at " + ChatColor.GOLD + InvUtils.getLoreData(event.getItem(), 3) + ChatColor.YELLOW + ".");
        new BukkitRunnable() {
            public void run() {
                buttplug.fdsjfhkdsjfdsjhk().getServer().broadcastMessage(ChatColor.GOLD + "[KingOfTheHill] " + ChatColor.GOLD + event.getPlayer().getName() + ChatColor.YELLOW + " obtained " + builder.toString() + ChatColor.GOLD + "," + ChatColor.YELLOW + " from a " + ChatColor.BLUE.toString() + ChatColor.ITALIC + "Level " + tier + " Key" + ChatColor.YELLOW + ".");
                for (final ItemStack lootItem : loot) {
                    event.getPlayer().getInventory().addItem(new ItemStack[] { lootItem });
                }
                event.getPlayer().updateInventory();
            }
        }.runTaskLater((Plugin)buttplug.fdsjfhkdsjfdsjhk(), 100L);
    }
}
