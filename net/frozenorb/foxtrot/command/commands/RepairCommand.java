package net.frozenorb.foxtrot.command.commands;

import org.bukkit.entity.*;
import org.bukkit.*;
import org.bukkit.inventory.*;
import net.frozenorb.foxtrot.command.annotations.*;

public class RepairCommand
{
    @Command(names = { "Repair" }, permissionNode = "foxtrot.repair")
    public static void repair(final Player sender) {
        final ItemStack inHand = sender.getItemInHand();
        if (inHand == null || inHand.getType() == Material.AIR) {
            sender.sendMessage(ChatColor.RED + "You're not holding anything!");
            return;
        }
        inHand.setDurability((short)0);
        sender.sendMessage(ChatColor.GRAY + "Repaired your item in hand.");
    }
}
