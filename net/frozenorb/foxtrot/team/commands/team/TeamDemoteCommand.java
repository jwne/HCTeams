package net.frozenorb.foxtrot.team.commands.team;

import org.bukkit.entity.*;
import net.frozenorb.foxtrot.*;
import org.bukkit.*;
import net.frozenorb.foxtrot.team.*;
import java.util.*;
import net.frozenorb.foxtrot.command.annotations.*;

public class TeamDemoteCommand
{
    @Command(names = { "team demote", "t demote", "f demote", "faction demote", "fac demote" }, permissionNode = "")
    public static void teamDemote(final Player sender, @Param(name = "player") OfflinePlayer name) {
        final faggot team = buttplug.fdsjfhkdsjfdsjhk().getDsfjhkdsjhdsjkhfds().getPlayerTeam(sender.getName());
        if (team == null) {
            sender.sendMessage(ChatColor.GRAY + "You are not on a team!");
            return;
        }
        if (team.isOwner(sender.getName()) || sender.isOp()) {
            if (team.isMember(name.getName())) {
                if (!team.isCaptain(name.getName())) {
                    sender.sendMessage(ChatColor.RED + "You can only demote team Captains!");
                    return;
                }
                for (final Player player : team.getOnlineMembers()) {
                    player.sendMessage(ChatColor.DARK_AQUA + team.getActualPlayerName(name.getName()) + " has been removed as an officer!");
                }
                team.removeCaptain(name.getName());
            }
            else {
                sender.sendMessage(ChatColor.DARK_AQUA + "Player is not on your team.");
            }
        }
        else {
            sender.sendMessage(ChatColor.DARK_AQUA + "Only team leaders can do this.");
        }
    }
}
