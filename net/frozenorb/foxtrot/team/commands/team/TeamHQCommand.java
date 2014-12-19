package net.frozenorb.foxtrot.team.commands.team;

import org.bukkit.entity.*;
import net.frozenorb.foxtrot.*;
import org.bukkit.*;
import net.frozenorb.foxtrot.team.*;
import net.frozenorb.foxtrot.command.annotations.*;

public class TeamHQCommand
{
    @Command(names = { "team hq", "t hq", "f hq", "faction hq", "fac hq", "team home", "t home", "f home", "faction home", "fac home", "home" }, permissionNode = "")
    public static void teamHQ(final Player sender) {
        if (buttplug.fdsjfhkdsjfdsjhk().getDsfjhkdsjhdsjkhfds().getPlayerTeam(sender.getName()) == null) {
            sender.sendMessage(ChatColor.DARK_AQUA + "You are not on a team!");
            return;
        }
        final faggot team = buttplug.fdsjfhkdsjfdsjhk().getDsfjhkdsjhdsjkhfds().getPlayerTeam(sender.getName());
        if (team.getHq() == null) {
            sender.sendMessage(ChatColor.RED + "HQ not set.");
            return;
        }
        if (sender.getWorld().getEnvironment() == World.Environment.THE_END) {
            sender.sendMessage(ChatColor.RED + "You can only exit the End through the End Portal!");
            return;
        }
        if (sender.getWorld().getEnvironment() == World.Environment.NETHER) {
            sender.sendMessage(ChatColor.RED + "You may not go to faction headquarters from the Nether!");
            return;
        }
        buttplug.fdsjfhkdsjfdsjhk().getServerHandler().beginWarp(sender, team, 75);
    }
}
