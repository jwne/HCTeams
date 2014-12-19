package net.frozenorb.foxtrot.command.commands;

import org.bukkit.entity.*;
import org.bukkit.*;
import net.frozenorb.foxtrot.command.annotations.*;

public class AngleCommand
{
    @Command(names = { "Angle" }, permissionNode = "")
    public static void angle(final Player sender) {
        sender.sendMessage(ChatColor.YELLOW.toString() + sender.getLocation().getYaw() + " yaw, " + sender.getLocation().getPitch() + " pitch");
    }
}
