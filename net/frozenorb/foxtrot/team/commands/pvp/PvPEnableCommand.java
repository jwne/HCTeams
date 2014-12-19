package net.frozenorb.foxtrot.team.commands.pvp;

import org.bukkit.entity.*;
import net.frozenorb.foxtrot.*;
import org.bukkit.*;
import net.frozenorb.foxtrot.command.annotations.*;

public class PvPEnableCommand
{
    @Command(names = { "pvptimer enable", "timer enable", "pvp enable", "pvptimer remove", "timer remove", "pvp remove" }, permissionNode = "")
    public static void pvpEnable(final Player sender) {
        if (buttplug.fdsjfhkdsjfdsjhk().getPvPTimerMap().hasTimer(sender.getName())) {
            buttplug.fdsjfhkdsjfdsjhk().getPvPTimerMap().removeTimer(sender.getName());
            sender.sendMessage(ChatColor.RED + "Your PVP Timer has been removed!");
        }
        else {
            sender.sendMessage(ChatColor.RED + "You do not have a PVP Timer on!");
        }
    }
}
