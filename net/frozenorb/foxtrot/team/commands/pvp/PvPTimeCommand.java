package net.frozenorb.foxtrot.team.commands.pvp;

import org.bukkit.entity.*;
import net.frozenorb.foxtrot.*;
import org.bukkit.*;
import net.frozenorb.foxtrot.util.*;
import net.frozenorb.foxtrot.command.annotations.*;

public class PvPTimeCommand
{
    @Command(names = { "pvptimer time", "timer time", "pvp time" }, permissionNode = "")
    public static void pvpTime(final Player sender) {
        if (buttplug.fdsjfhkdsjfdsjhk().getPvPTimerMap().hasTimer(sender.getName())) {
            sender.sendMessage(ChatColor.RED + "You have " + TimeUtils.getDurationBreakdown(buttplug.fdsjfhkdsjfdsjhk().getPvPTimerMap().getTimer(sender.getName()) - System.currentTimeMillis()) + " left on your PVP Timer.");
        }
        else {
            sender.sendMessage(ChatColor.RED + "You do not have a PVP Timer on!");
        }
    }
}
