package net.frozenorb.foxtrot.team.commands.team;

import org.bukkit.entity.*;
import net.frozenorb.foxtrot.*;
import org.bukkit.*;
import net.frozenorb.foxtrot.team.*;
import java.util.*;
import net.frozenorb.foxtrot.command.annotations.*;

public class TeamDisbandCommand
{
    @Command(names = { "team disband", "t disband", "f disband", "faction disband", "fac disband" }, permissionNode = "")
    public static void teamDisband(final Player player) {
        final faggot team = buttplug.fdsjfhkdsjfdsjhk().getDsfjhkdsjhdsjkhfds().getPlayerTeam(player.getName());
        if (team == null) {
            player.sendMessage(ChatColor.RED + "You are not on a team!");
            return;
        }
        if (!team.isOwner(player.getName())) {
            player.sendMessage(ChatColor.RED + "You must be the leader of the team to disband it!");
            return;
        }
        if (team.isRaidable()) {
            player.sendMessage(ChatColor.RED + "You cannot disband your team while raidable.");
            return;
        }
        for (final Player online : team.getOnlineMembers()) {
            online.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + player.getName() + " has disbanded the team.");
        }
        team.disband();
    }
}
