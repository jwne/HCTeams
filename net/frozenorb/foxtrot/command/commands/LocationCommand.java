package net.frozenorb.foxtrot.command.commands;

import org.bukkit.entity.*;
import net.frozenorb.foxtrot.team.claims.*;
import net.frozenorb.foxtrot.*;
import org.bukkit.*;
import net.frozenorb.foxtrot.team.*;
import net.frozenorb.foxtrot.command.annotations.*;

public class LocationCommand
{
    @Command(names = { "Location", "Here", "WhereAmI", "Loc" }, permissionNode = "")
    public static void location(final Player sender) {
        final Location loc = sender.getLocation();
        final faggot owner = LandBoard.getInstance().getTeam(loc);
        if (owner != null) {
            sender.sendMessage(ChatColor.YELLOW + "You are in " + owner.getName(sender.getPlayer()) + ChatColor.YELLOW + "'s territory.");
            return;
        }
        if (!buttplug.fdsjfhkdsjfdsjhk().getServerHandler().isWarzone(loc)) {
            sender.sendMessage(ChatColor.YELLOW + "You are in " + ChatColor.GRAY + "The Wilderness" + ChatColor.YELLOW + "!");
        }
        else {
            sender.sendMessage(ChatColor.YELLOW + "You are in the " + ChatColor.RED + "Warzone" + ChatColor.YELLOW + "!");
        }
    }
}
