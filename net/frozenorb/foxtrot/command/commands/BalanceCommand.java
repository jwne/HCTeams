package net.frozenorb.foxtrot.command.commands;

import org.bukkit.entity.*;
import org.bukkit.*;
import java.util.*;
import java.text.*;
import net.frozenorb.foxtrot.*;
import net.frozenorb.foxtrot.command.annotations.*;

public class BalanceCommand
{
    @Command(names = { "Balance", "Econ", "Bal", "$" }, permissionNode = "")
    public static void balace(final Player sender, @Param(name = "Target", defaultValue = "self") String target) {
        if (target.equals("self")) {
            target = sender.getName();
        }
        sender.sendMessage(ChatColor.GOLD + "Balance: " + ChatColor.WHITE + NumberFormat.getNumberInstance(Locale.US).format(buttplug.fdsjfhkdsjfdsjhk().getBalanceMap().getBalance(target)));
    }
}
