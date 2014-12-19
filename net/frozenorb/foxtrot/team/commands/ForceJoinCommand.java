package net.frozenorb.foxtrot.team.commands;

import org.bukkit.entity.*;
import net.frozenorb.foxtrot.team.*;
import net.frozenorb.foxtrot.*;
import org.bukkit.*;
import net.frozenorb.foxtrot.command.annotations.*;

public class ForceJoinCommand
{
    @Command(names = { "ForceJoin" }, permissionNode = "foxtrot.forcejoin")
    public static void forceJoin(final Player sender, @Param(name = "Team") final faggot team, @Param(name = "Target", defaultValue = "self") final Player target) {
        if (buttplug.fdsjfhkdsjfdsjhk().getDsfjhkdsjhdsjkhfds().getPlayerTeam(target.getName()) != null) {
            if (target == sender) {
                sender.sendMessage(ChatColor.RED + "Leave your current team before attempting to forcejoin.");
            }
            else {
                sender.sendMessage(ChatColor.RED + "That player needs to leave their current team first!");
            }
            return;
        }
        team.addMember(target.getName());
        buttplug.fdsjfhkdsjfdsjhk().getDsfjhkdsjhdsjkhfds().setTeam(target.getName(), team);
        target.sendMessage(ChatColor.GREEN + "You are now a member of §b" + team.getName() + "§a!");
        if (target != sender) {
            sender.sendMessage("§aPlayer added to team!");
        }
    }
}
