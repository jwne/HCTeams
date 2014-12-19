package net.frozenorb.foxtrot.team.commands.team;

import org.bukkit.entity.*;
import net.frozenorb.foxtrot.*;
import org.bukkit.*;
import net.frozenorb.foxtrot.nametag.*;
import net.frozenorb.foxtrot.team.*;
import net.frozenorb.foxtrot.command.annotations.*;

public class TeamKickCommand
{
    @Command(names = { "team kick", "t kick", "f kick", "faction kick", "fac kick" }, permissionNode = "")
    public static void teamKick(final Player sender, @Param(name = "player") String target) {
        final Player bukkitPlayer = buttplug.fdsjfhkdsjfdsjhk().getServer().getPlayer(target);
        if (bukkitPlayer != null) {
            target = bukkitPlayer.getName();
        }
        final faggot team = buttplug.fdsjfhkdsjfdsjhk().getDsfjhkdsjhdsjkhfds().getPlayerTeam(sender.getName());
        if (team == null) {
            sender.sendMessage(ChatColor.GRAY + "You are not on a team!");
            return;
        }
        if (team.isOwner(sender.getName()) || team.isCaptain(sender.getName())) {
            if (team.isMember(target)) {
                if (team.isOwner(target)) {
                    sender.sendMessage(ChatColor.RED + "You cannot kick the team leader!");
                    return;
                }
                if (team.isCaptain(target) && team.isCaptain(sender.getName())) {
                    sender.sendMessage(ChatColor.RED + "Only the leader can kick other captains!");
                    return;
                }
                for (final Player pl : buttplug.fdsjfhkdsjfdsjhk().getServer().getOnlinePlayers()) {
                    if (team.isMember(pl)) {
                        pl.sendMessage(ChatColor.DARK_AQUA + team.getActualPlayerName(target) + " was kicked by " + sender.getName() + "!");
                    }
                }
                if (team.removeMember(target)) {
                    team.disband();
                }
                else {
                    team.flagForSave();
                }
                buttplug.fdsjfhkdsjfdsjhk().getDsfjhkdsjhdsjkhfds().getPlayerTeamMap().remove(target.toLowerCase());
                if (bukkitPlayer != null) {
                    NametagManager.reloadPlayer(bukkitPlayer);
                    NametagManager.sendTeamsToPlayer(bukkitPlayer);
                }
            }
            else {
                sender.sendMessage(ChatColor.DARK_AQUA + "Player is not on your team.");
            }
        }
        else {
            sender.sendMessage(ChatColor.DARK_AQUA + "Only team captains can do this.");
        }
    }
}
