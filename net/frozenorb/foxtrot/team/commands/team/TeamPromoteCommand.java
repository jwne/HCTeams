package net.frozenorb.foxtrot.team.commands.team;

import org.bukkit.entity.*;
import net.frozenorb.foxtrot.*;
import org.bukkit.*;
import net.frozenorb.foxtrot.team.*;
import java.util.*;
import net.frozenorb.foxtrot.command.annotations.*;

public class TeamPromoteCommand
{
    @Command(names = { "team promote", "t promote", "f promote", "faction promote", "fac promote", "team captain", "t captain", "f captain", "faction captain", "fac captain" }, permissionNode = "")
    public static void teamPromote(final Player sender, @Param(name = "Player") final OfflinePlayer target) {
        final faggot team = buttplug.fdsjfhkdsjfdsjhk().getDsfjhkdsjhdsjkhfds().getPlayerTeam(sender.getName());
        if (team == null) {
            sender.sendMessage(ChatColor.GRAY + "You are not on a team!");
            return;
        }
        if (team.isOwner(sender.getName()) || sender.isOp()) {
            if (team.isMember(target.getName())) {
                if (team.isCaptain(target.getName())) {
                    sender.sendMessage(ChatColor.RED + "That player is already a Captain!");
                    return;
                }
                if (team.isOwner(target.getName())) {
                    sender.sendMessage(ChatColor.RED + "You can only promote team members!");
                    return;
                }
                for (final Player player : team.getOnlineMembers()) {
                    player.sendMessage(ChatColor.DARK_AQUA + target.getName() + " has been made a Captain!");
                }
                team.addCaptain(target.getName());
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
