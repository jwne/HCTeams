package net.frozenorb.foxtrot.command.commands;

import org.bukkit.entity.*;
import org.bukkit.*;
import net.frozenorb.foxtrot.command.annotations.*;

public class HelpCommand
{
    @Command(names = { "Help" }, permissionNode = "")
    public static void help(final Player sender) {
        sender.sendMessage(ChatColor.YELLOW + "Welcome to HCTeams! Please contact an admin for help.");
    }
}
