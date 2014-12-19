package net.frozenorb.foxtrot.team.commands;

import org.bukkit.entity.*;
import net.frozenorb.foxtrot.team.*;
import org.bukkit.*;
import java.util.*;
import net.frozenorb.foxtrot.command.annotations.*;

public class ForceDisbandCommand
{
    @Command(names = { "forcedisband" }, permissionNode = "op")
    public static void forceDisband(final Player sender, @Param(name = "team") final faggot target) {
        for (final Player online : target.getOnlineMembers()) {
            online.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + sender.getName() + " has force-disbanded the team.");
        }
        target.disband();
        sender.sendMessage(ChatColor.GRAY + "Force-disbanded the team " + target.getName() + ".");
    }
}
