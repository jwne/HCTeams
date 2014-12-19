package net.frozenorb.foxtrot.team.commands.team;

import org.bukkit.entity.*;
import net.frozenorb.foxtrot.*;
import net.frozenorb.foxtrot.team.claims.*;
import org.bukkit.*;
import net.frozenorb.foxtrot.team.*;
import net.frozenorb.foxtrot.command.annotations.*;

public class TeamSetHQCommand
{
    @Command(names = { "team sethq", "t sethq", "f sethq", "faction sethq", "fac sethq", "team sethome", "t sethome", "f sethome", "faction sethome", "fac sethome", "sethome" }, permissionNode = "")
    public static void teamSetHQ(final Player sender) {
        final faggot team = buttplug.fdsjfhkdsjfdsjhk().getDsfjhkdsjhdsjkhfds().getPlayerTeam(sender.getName());
        if (team == null) {
            sender.sendMessage(ChatColor.GRAY + "You are not on a team!");
            return;
        }
        if (team.isOwner(sender.getName()) || team.isCaptain(sender.getName())) {
            if (LandBoard.getInstance().getTeam(sender.getLocation()) != team) {
                sender.sendMessage(ChatColor.RED + "You can only set HQ in your team's territory.");
                return;
            }
            team.setHQ(sender.getLocation());
            for (final Player player : Bukkit.getOnlinePlayers()) {
                if (team.isMember(player)) {
                    player.sendMessage(ChatColor.DARK_AQUA + sender.getName() + " has updated the team's HQ point!");
                }
            }
            sender.sendMessage(ChatColor.DARK_AQUA + "Headquarters set.");
        }
        else {
            sender.sendMessage(ChatColor.DARK_AQUA + "Only team captains can do this.");
        }
    }
}
