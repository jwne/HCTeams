package net.frozenorb.foxtrot.command.commands;

import org.bukkit.entity.*;
import net.frozenorb.foxtrot.team.dtr.bitmask.*;
import org.bukkit.*;
import net.frozenorb.foxtrot.team.*;
import net.frozenorb.foxtrot.command.annotations.*;

public class BitmaskCommand
{
    @Command(names = { "bitmask list", "bitmasks list" }, permissionNode = "op")
    public static void bitmaskList(final Player sender) {
        for (final DTRBitmaskType bitmaskType : DTRBitmaskType.values()) {
            sender.sendMessage(ChatColor.GOLD + bitmaskType.getName() + " (" + bitmaskType.getBitmask() + "): " + ChatColor.YELLOW + bitmaskType.getDescription());
        }
    }
    
    @Command(names = { "bitmask info", "bitmasks info" }, permissionNode = "op")
    public static void bitmaskInfo(final Player sender, @Param(name = "target") final faggot target) {
        if (target.getOwner() != null) {
            sender.sendMessage(ChatColor.RED + "Bitmask flags cannot be applied to teams without a null leader.");
            return;
        }
        sender.sendMessage(ChatColor.YELLOW + "Bitmask flags of " + ChatColor.GOLD + target.getName() + ChatColor.YELLOW + ":");
        for (final DTRBitmaskType bitmaskType : DTRBitmaskType.values()) {
            if (target.hasDTRBitmask(bitmaskType)) {
                sender.sendMessage(ChatColor.GOLD + bitmaskType.getName() + " (" + bitmaskType.getBitmask() + "): " + ChatColor.YELLOW + bitmaskType.getDescription());
            }
        }
        sender.sendMessage(ChatColor.GOLD + "Raw DTR: " + ChatColor.YELLOW + target.getDTR());
    }
    
    @Command(names = { "bitmask add", "bitmasks add" }, permissionNode = "op")
    public static void bitmaskAdd(final Player sender, @Param(name = "target") final faggot target, @Param(name = "bitmask") final DTRBitmaskType bitmaskType) {
        if (target.getOwner() != null) {
            sender.sendMessage(ChatColor.RED + "Bitmask flags cannot be applied to teams without a null leader.");
            return;
        }
        if (target.hasDTRBitmask(bitmaskType)) {
            sender.sendMessage(ChatColor.RED + "This claim already has the bitmask value " + bitmaskType.getName() + ".");
            return;
        }
        int dtrInt = (int)target.getDTR();
        dtrInt += bitmaskType.getBitmask();
        target.setDTR(dtrInt);
        bitmaskInfo(sender, target);
    }
    
    @Command(names = { "bitmask remove", "bitmasks remove" }, permissionNode = "op")
    public static void bitmaskRemove(final Player sender, @Param(name = "target") final faggot target, @Param(name = "bitmask") final DTRBitmaskType bitmaskType) {
        if (target.getOwner() != null) {
            sender.sendMessage(ChatColor.RED + "Bitmask flags cannot be applied to teams without a null leader.");
            return;
        }
        if (!target.hasDTRBitmask(bitmaskType)) {
            sender.sendMessage(ChatColor.RED + "This claim doesn't have the bitmask value " + bitmaskType.getName() + ".");
            return;
        }
        int dtrInt = (int)target.getDTR();
        dtrInt -= bitmaskType.getBitmask();
        target.setDTR(dtrInt);
        bitmaskInfo(sender, target);
    }
}
