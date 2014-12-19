package net.frozenorb.foxtrot.team.commands;

import org.bukkit.entity.*;
import net.frozenorb.foxtrot.*;
import org.bukkit.*;
import net.frozenorb.foxtrot.team.*;
import net.frozenorb.foxtrot.command.annotations.*;

public class ForceLeaveCommand
{
    @Command(names = { "forceleave" }, permissionNode = "op")
    public static void forceLeave(final Player player) {
        final faggot team = buttplug.fdsjfhkdsjfdsjhk().getDsfjhkdsjhdsjkhfds().getPlayerTeam(player.getName());
        if (team == null) {
            player.sendMessage(ChatColor.RED + "You are not on a team!");
            return;
        }
        team.removeMember(player.getName());
        buttplug.fdsjfhkdsjfdsjhk().getDsfjhkdsjhdsjkhfds().getPlayerTeamMap().remove(player.getName().toLowerCase());
        player.sendMessage(ChatColor.GRAY + "Force-left your team.");
    }
}
