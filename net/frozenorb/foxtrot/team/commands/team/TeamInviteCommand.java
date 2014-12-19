package net.frozenorb.foxtrot.team.commands.team;

import org.bukkit.entity.*;
import net.frozenorb.foxtrot.*;
import org.bukkit.*;
import net.frozenorb.foxtrot.team.*;
import net.frozenorb.foxtrot.command.annotations.*;

public class TeamInviteCommand
{
    @Command(names = { "team invite", "t invite", "f invite", "faction invite", "fac invite" }, permissionNode = "")
    public static void teamInvite(final Player sender, @Param(name = "player") final OfflinePlayer target) {
        final faggot team = buttplug.fdsjfhkdsjfdsjhk().getDsfjhkdsjhdsjkhfds().getPlayerTeam(sender.getName());
        if (team == null) {
            sender.sendMessage(ChatColor.GRAY + "You are not on a team!");
            return;
        }
        if (team.getMembers().size() >= 25) {
            sender.sendMessage(ChatColor.RED + "The max team size is " + 25 + "!");
            return;
        }
        if (team.isOwner(sender.getName()) || team.isCaptain(sender.getName())) {
            if (!team.isMember(target.getName())) {
                if (team.getInvitations().contains(target.getName())) {
                    sender.sendMessage(ChatColor.RED + "That player has already been invited.");
                    return;
                }
                if (team.isRaidable()) {
                    sender.sendMessage(ChatColor.RED + "You may not invite players if your team is raidable! You must boost your DTR!");
                    return;
                }
                team.getInvitations().add(target.getName());
                team.flagForSave();
                if (target.isOnline()) {
                    final Player targetPlayer = target.getPlayer();
                    targetPlayer.sendMessage(ChatColor.DARK_AQUA + sender.getName() + " invited you to join '" + ChatColor.YELLOW + team.getName() + ChatColor.DARK_AQUA + "'.");
                    targetPlayer.sendMessage(ChatColor.DARK_AQUA + "Type '" + ChatColor.YELLOW + "/f join " + team.getName() + ChatColor.DARK_AQUA + "' to join.");
                }
                for (final Player player : buttplug.fdsjfhkdsjfdsjhk().getServer().getOnlinePlayers()) {
                    if (team.isMember(player)) {
                        player.sendMessage(ChatColor.YELLOW + target.getName() + " has been invited to the team!");
                    }
                }
            }
            else {
                sender.sendMessage(ChatColor.DARK_AQUA + "Player is already on your team.");
            }
        }
        else {
            sender.sendMessage(ChatColor.DARK_AQUA + "Only team captains can do this.");
        }
    }
}
