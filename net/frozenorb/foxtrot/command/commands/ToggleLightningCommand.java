package net.frozenorb.foxtrot.command.commands;

import org.bukkit.entity.*;
import net.frozenorb.foxtrot.*;
import org.bukkit.*;
import net.frozenorb.foxtrot.command.annotations.*;

public class ToggleLightningCommand
{
    @Command(names = { "ToggleLightning" }, permissionNode = "")
    public static void toggleLightning(final Player sender) {
        final boolean val = !buttplug.fdsjfhkdsjfdsjhk().getToggleLightningMap().isLightningToggled(sender.getName());
        sender.sendMessage(ChatColor.YELLOW + "You are now " + (val ? (ChatColor.GREEN + "able") : (ChatColor.RED + "unable")) + ChatColor.YELLOW + " to see lightning strikes on deaths!");
        buttplug.fdsjfhkdsjfdsjhk().getToggleLightningMap().setLightningToggled(sender.getName(), val);
    }
}
