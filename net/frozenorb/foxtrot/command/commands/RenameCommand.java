package net.frozenorb.foxtrot.command.commands;

import org.bukkit.entity.*;
import org.bukkit.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import net.frozenorb.foxtrot.command.annotations.*;

public class RenameCommand
{
    @Command(names = { "Rename" }, permissionNode = "foxtrot.rename")
    public static void rename(final Player sender, @Param(name = "Name", wildcard = true) String name) {
        final ItemStack inHand = sender.getItemInHand();
        if (inHand == null || inHand.getType() == Material.AIR) {
            sender.sendMessage(ChatColor.RED + "You're not holding anything!");
            return;
        }
        final ItemMeta meta = inHand.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        inHand.setItemMeta(meta);
        sender.sendMessage(ChatColor.GRAY + "Renamed your item in hand.");
    }
}
