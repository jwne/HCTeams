package net.frozenorb.foxtrot.team.commands.team;

import org.bukkit.entity.*;
import net.frozenorb.foxtrot.*;
import org.bukkit.*;
import net.frozenorb.foxtrot.team.*;
import java.util.*;
import net.frozenorb.foxtrot.command.annotations.*;

public class TeamUninviteCommand
{
    @Command(names = { "team uninvite", "t uninvite", "f uninvite", "faction uninvite", "fac uninvite", "team revoke", "t revoke", "f revoke", "faction revoke", "fac revoke" }, permissionNode = "")
    public static void teamInvite(final Player sender, @Param(name = "all | player") String name) {
        final faggot team = buttplug.fdsjfhkdsjfdsjhk().getDsfjhkdsjhdsjkhfds().getPlayerTeam(sender.getName());
        if (team == null) {
            sender.sendMessage(ChatColor.GRAY + "You are not on a team!");
            return;
        }
        if (team.isOwner(sender.getName()) || team.isCaptain(sender.getName())) {
            if (name.equalsIgnoreCase("all")) {
                team.getInvitations().clear();
                sender.sendMessage(ChatColor.GRAY + "You have cleared all pending invitations.");
            }
            else {
                String remove = null;
                for (final String possibleName : team.getInvitations()) {
                    if (possibleName.equalsIgnoreCase(name)) {
                        remove = possibleName;
                        break;
                    }
                }
                if (remove != null) {
                    team.getInvitations().remove(remove);
                    team.flagForSave();
                    sender.sendMessage(ChatColor.GREEN + "Cancelled pending invitation for " + remove + "!");
                }
                else {
                    sender.sendMessage(ChatColor.RED + "No pending invitation for '" + name + "'!");
                }
            }
        }
        else {
            sender.sendMessage(ChatColor.DARK_AQUA + "Only team captains can do this.");
        }
    }
}
