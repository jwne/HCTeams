package net.frozenorb.foxtrot.command.commands;

import org.bukkit.entity.*;
import net.frozenorb.foxtrot.*;
import org.bukkit.*;
import java.text.*;
import net.frozenorb.foxtrot.command.annotations.*;

public class PayCommand
{
    @Command(names = { "Pay", "P2P" }, permissionNode = "")
    public static void mc(final Player sender, @Param(name = "Target") String target, @Param(name = "Amount") final int value) {
        final int balance = buttplug.fdsjfhkdsjfdsjhk().getBalanceMap().getBalance(sender.getName());
        if (!buttplug.fdsjfhkdsjfdsjhk().getPlaytimeMap().contains(target)) {
            sender.sendMessage(ChatColor.RED + target + " has never played before!");
            return;
        }
        final Player pTarget = Bukkit.getPlayer(target);
        if (pTarget != null) {
            target = pTarget.getName();
        }
        if (target.equalsIgnoreCase(sender.getName())) {
            sender.sendMessage(ChatColor.RED + "You cannot send money to yourself!");
            return;
        }
        if (value < 50) {
            sender.sendMessage(ChatColor.RED + "You must send at least $50!");
            return;
        }
        if (balance < value) {
            sender.sendMessage(ChatColor.RED + "You do not have $" + value + "!");
            return;
        }
        buttplug.fdsjfhkdsjfdsjhk().getBalanceMap().setBalance(target, buttplug.fdsjfhkdsjfdsjhk().getBalanceMap().getBalance(target) + value);
        buttplug.fdsjfhkdsjfdsjhk().getBalanceMap().setBalance(sender.getName(), buttplug.fdsjfhkdsjfdsjhk().getBalanceMap().getBalance(sender.getName()) - value);
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eYou sent &d" + NumberFormat.getCurrencyInstance().format(value) + "&e to &d" + target + "&e!"));
    }
}
