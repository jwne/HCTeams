package net.frozenorb.foxtrot.team.commands;

import org.bukkit.entity.*;
import net.frozenorb.foxtrot.team.*;
import net.frozenorb.foxtrot.*;
import org.bukkit.*;
import net.frozenorb.foxtrot.command.annotations.*;

public class ForceLeaderCommand
{
    @Command(names = { "ForceLeader" }, permissionNode = "foxtrot.forceleader")
    public static void forceLeader(final Player sender, @Param(name = "Team", defaultValue = "self") final faggot team, @Param(name = "Target", defaultValue = "self") String target) {
        if (target.equals("self")) {
            target = sender.getName();
        }
        else if (target.equals("null")) {
            target = null;
        }
        if (target != null && !buttplug.fdsjfhkdsjfdsjhk().getPlaytimeMap().contains(target)) {
            sender.sendMessage(ChatColor.RED + "That player has never played here!");
        }
        else {
            if (target != null && !team.isMember(target)) {
                sender.sendMessage(ChatColor.RED + "That player is not a member of " + team.getName() + ".");
                return;
            }
            team.setOwner(target);
            sender.sendMessage(ChatColor.GREEN + target + " is now the owner of §b" + team.getName());
        }
    }
}
