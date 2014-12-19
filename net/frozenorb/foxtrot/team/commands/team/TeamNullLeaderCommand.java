package net.frozenorb.foxtrot.team.commands.team;

import org.bukkit.entity.*;
import net.frozenorb.foxtrot.*;
import net.frozenorb.foxtrot.team.*;
import org.bukkit.*;
import java.util.*;
import net.frozenorb.foxtrot.command.annotations.*;

public class TeamNullLeaderCommand
{
    @Command(names = { "team nullleader", "t nullleader", "f nullleader", "faction nullleader", "fac nullleader" }, permissionNode = "op")
    public static void teamSaveString(final Player sender) {
        int nullLeaders = 0;
        for (final faggot team : buttplug.fdsjfhkdsjfdsjhk().getDsfjhkdsjhdsjkhfds().getTeams()) {
            if (team.getOwner() == null || team.getOwner().equals("null")) {
                ++nullLeaders;
                sender.sendMessage(ChatColor.RED + "- " + team.getName());
            }
        }
        if (nullLeaders == 0) {
            sender.sendMessage(ChatColor.DARK_PURPLE + "No null teams found.");
        }
        else {
            sender.sendMessage(ChatColor.DARK_PURPLE.toString() + nullLeaders + " null teams found.");
        }
    }
}
