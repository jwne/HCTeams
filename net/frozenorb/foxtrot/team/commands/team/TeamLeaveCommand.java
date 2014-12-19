package net.frozenorb.foxtrot.team.commands.team;

import org.bukkit.entity.*;
import net.frozenorb.foxtrot.*;
import net.frozenorb.foxtrot.team.claims.*;
import org.bukkit.*;
import net.frozenorb.foxtrot.nametag.*;
import net.frozenorb.foxtrot.team.*;
import net.frozenorb.foxtrot.command.annotations.*;

public class TeamLeaveCommand
{
    @Command(names = { "team leave", "t leave", "f leave", "faction leave", "fac leave" }, permissionNode = "")
    public static void teamLeave(final Player sender) {
        final faggot team = buttplug.fdsjfhkdsjfdsjhk().getDsfjhkdsjhdsjkhfds().getPlayerTeam(sender.getName());
        if (team == null) {
            sender.sendMessage(ChatColor.GRAY + "You are not on a team!");
            return;
        }
        if (team.isOwner(sender.getName()) && team.getSize() > 1) {
            sender.sendMessage(ChatColor.RED + "Please choose a new leader before leaving your team!");
            return;
        }
        if (LandBoard.getInstance().getTeam(sender.getLocation()) == team) {
            sender.sendMessage(ChatColor.RED + "You cannot leave your team while on team territory.");
            return;
        }
        if (team.removeMember(sender.getName())) {
            team.disband();
            sender.sendMessage(ChatColor.DARK_AQUA + "Successfully left and disbanded team!");
            buttplug.fdsjfhkdsjfdsjhk().getDsfjhkdsjhdsjkhfds().getPlayerTeamMap().remove(sender.getName().toLowerCase());
        }
        else {
            buttplug.fdsjfhkdsjfdsjhk().getDsfjhkdsjhdsjkhfds().getPlayerTeamMap().remove(sender.getName().toLowerCase());
            team.flagForSave();
            for (final Player player : Bukkit.getOnlinePlayers()) {
                if (team.isMember(player)) {
                    player.sendMessage(ChatColor.YELLOW + sender.getName() + " has left the team.");
                }
            }
            sender.sendMessage(ChatColor.DARK_AQUA + "Successfully left the team!");
        }
        NametagManager.reloadPlayer(sender);
        NametagManager.sendTeamsToPlayer(sender);
    }
}
