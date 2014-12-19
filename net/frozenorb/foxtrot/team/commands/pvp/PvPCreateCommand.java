package net.frozenorb.foxtrot.team.commands.pvp;

import org.bukkit.entity.*;
import net.frozenorb.foxtrot.*;
import java.util.concurrent.*;
import org.bukkit.*;
import net.frozenorb.foxtrot.command.annotations.*;

public class PvPCreateCommand
{
    @Command(names = { "pvptimer create", "timer create", "pvp create" }, permissionNode = "op")
    public static void pvpCreate(final Player sender, @Param(name = "target", defaultValue = "self") final Player target) {
        buttplug.fdsjfhkdsjfdsjhk().getPvPTimerMap().createTimer(target.getName(), (int)TimeUnit.MINUTES.toSeconds(30L));
        target.sendMessage(ChatColor.YELLOW + "You have 30 minutes of PVP Timer!");
        if (sender != target) {
            sender.sendMessage(ChatColor.YELLOW + "Gave 30 minutes of PVP Timer to " + target.getName() + ".");
        }
    }
}
