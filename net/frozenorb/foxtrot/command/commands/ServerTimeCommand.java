package net.frozenorb.foxtrot.command.commands;

import org.bukkit.entity.*;
import org.bukkit.*;
import java.util.*;
import net.frozenorb.foxtrot.command.annotations.*;

public class ServerTimeCommand
{
    @Command(names = { "ServerTime" }, permissionNode = "")
    public static void serverTime(final Player sender) {
        sender.sendMessage(ChatColor.YELLOW.toString() + "It is " + ChatColor.LIGHT_PURPLE + new Date().toString() + ChatColor.YELLOW + ".");
    }
}
