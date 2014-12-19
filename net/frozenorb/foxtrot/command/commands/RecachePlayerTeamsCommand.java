package net.frozenorb.foxtrot.command.commands;

import org.bukkit.entity.*;
import org.bukkit.*;
import net.frozenorb.foxtrot.*;
import net.frozenorb.foxtrot.team.*;
import net.frozenorb.foxtrot.command.annotations.*;
import java.util.*;

public class RecachePlayerTeamsCommand
{
    @Command(names = { "playerteamcache rebuild" }, permissionNode = "op")
    public static void recachePlayerTeamsRebuild(final Player sender) {
        sender.sendMessage(ChatColor.DARK_PURPLE + "Rebuilding player team cache...");
        buttplug.fdsjfhkdsjfdsjhk().getDsfjhkdsjhdsjkhfds().getPlayerTeamMap().clear();
        for (final faggot team : buttplug.fdsjfhkdsjfdsjhk().getDsfjhkdsjhdsjkhfds().getTeams()) {
            for (final String member : team.getMembers()) {
                buttplug.fdsjfhkdsjfdsjhk().getDsfjhkdsjhdsjkhfds().getPlayerTeamMap().put(member.toLowerCase(), team);
            }
        }
        sender.sendMessage(ChatColor.DARK_PURPLE + "The player death cache has been rebuilt.");
    }
    
    @Command(names = { "playerteamcache check" }, permissionNode = "op")
    public static void recachePlayerTeams(final Player sender) {
        sender.sendMessage(ChatColor.DARK_PURPLE + "Checking player team cache...");
        final Map<String, String> dealtWith = new HashMap<String, String>();
        final Set<String> errors = new HashSet<String>();
        for (final faggot team : buttplug.fdsjfhkdsjfdsjhk().getDsfjhkdsjhdsjkhfds().getTeams()) {
            for (final String member : team.getMembers()) {
                if (dealtWith.containsKey(member) && !errors.contains(member)) {
                    errors.add(member);
                    sender.sendMessage(ChatColor.RED + " - " + member + " (Team: " + team.getName() + ", Expected: " + dealtWith.get(member) + ")");
                }
                else {
                    dealtWith.put(member, team.getName());
                }
            }
        }
        if (errors.size() == 0) {
            sender.sendMessage(ChatColor.DARK_PURPLE + "No errors found while checking player team cache.");
        }
        else {
            sender.sendMessage(ChatColor.DARK_PURPLE.toString() + errors.size() + " error(s) found while checking player team cache.");
        }
    }
}
