package net.frozenorb.foxtrot.team.commands;

import org.bukkit.entity.*;
import net.frozenorb.foxtrot.*;
import org.bukkit.*;
import net.frozenorb.foxtrot.team.*;
import net.frozenorb.foxtrot.command.annotations.*;

public class ForceKickCommand
{
    @Command(names = { "forcekick" }, permissionNode = "op")
    public static void forceKick(final Player sender, @Param(name = "player") final OfflinePlayer player) {
        final faggot team = buttplug.fdsjfhkdsjfdsjhk().getDsfjhkdsjhdsjkhfds().getPlayerTeam(player.getName());
        if (team == null) {
            sender.sendMessage(ChatColor.RED + player.getName() + " is not on a team!");
            return;
        }
        if (team.getMembers().size() == 1) {
            sender.sendMessage(ChatColor.RED + player.getName() + "'s team has one member. Please use /forcedisband to perform this action.");
            return;
        }
        team.removeMember(player.getName());
        buttplug.fdsjfhkdsjfdsjhk().getDsfjhkdsjhdsjkhfds().getPlayerTeamMap().remove(player.getName().toLowerCase());
        sender.sendMessage(ChatColor.GRAY + "Force-kicked " + player.getName() + " from their team, " + team.getName() + ".");
    }
}
