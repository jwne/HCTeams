package net.frozenorb.foxtrot.team.commands.team;

import org.bukkit.entity.*;
import net.frozenorb.foxtrot.*;
import net.frozenorb.foxtrot.team.*;
import org.bukkit.*;
import java.util.*;
import net.frozenorb.foxtrot.command.annotations.*;
import java.io.*;

public class TeamInvitesCommand
{
    @Command(names = { "team invites", "t invites", "f invites", "faction invites", "fac invites" }, permissionNode = "")
    public static void teamInvites(final Player sender) {
        final StringBuilder yourInvites = new StringBuilder();
        for (final faggot team : buttplug.fdsjfhkdsjfdsjhk().getDsfjhkdsjhdsjkhfds().getTeams()) {
            if (team.getInvitations().contains(sender.getName())) {
                yourInvites.append(ChatColor.GRAY).append(team.getName()).append(ChatColor.YELLOW).append(", ");
            }
        }
        if (yourInvites.length() > 2) {
            yourInvites.setLength(yourInvites.length() - 2);
        }
        else {
            yourInvites.append(ChatColor.GRAY).append("No pending invites.");
        }
        sender.sendMessage(ChatColor.YELLOW + "Your Invites: " + yourInvites.toString());
        final faggot current = buttplug.fdsjfhkdsjfdsjhk().getDsfjhkdsjhdsjkhfds().getPlayerTeam(sender.getName());
        if (current != null) {
            final StringBuilder invitedToYourTeam = new StringBuilder();
            for (final String invites : current.getInvitations()) {
                invitedToYourTeam.append(ChatColor.GRAY).append(invites).append(ChatColor.YELLOW).append(", ");
            }
            if (invitedToYourTeam.length() > 2) {
                invitedToYourTeam.setLength(invitedToYourTeam.length() - 2);
            }
            else {
                invitedToYourTeam.append(ChatColor.GRAY).append("No pending invites.");
            }
            sender.sendMessage(ChatColor.YELLOW + "Invited to your Team: " + invitedToYourTeam.toString());
        }
    }
}
