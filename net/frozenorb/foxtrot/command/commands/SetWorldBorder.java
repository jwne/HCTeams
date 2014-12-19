package net.frozenorb.foxtrot.command.commands;

import org.bukkit.entity.*;
import net.frozenorb.foxtrot.listener.*;
import org.bukkit.*;
import net.frozenorb.foxtrot.command.annotations.*;

public class SetWorldBorder
{
    @Command(names = { "SetWorldBorder" }, permissionNode = "op")
    public static void setWorldBorder(final Player sender, @Param(name = "Distance") final int value) {
        BorderListener.BORDER_SIZE = value;
        sender.sendMessage(ChatColor.GRAY + "The world border is now set to " + BorderListener.BORDER_SIZE + " blocks.");
    }
}
