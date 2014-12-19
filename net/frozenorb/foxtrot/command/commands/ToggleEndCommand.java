package net.frozenorb.foxtrot.command.commands;

import org.bukkit.entity.*;
import net.frozenorb.foxtrot.listener.*;
import org.bukkit.*;
import net.frozenorb.foxtrot.command.annotations.*;

public class ToggleEndCommand
{
    @Command(names = { "ToggleEnd" }, permissionNode = "foxtrot.toggleend")
    public static void toggleEnd(final Player sender) {
        EndListener.endActive = !EndListener.endActive;
        sender.sendMessage(ChatColor.GRAY + "End enabled? " + ChatColor.DARK_AQUA + (EndListener.endActive ? "Yes" : "No"));
    }
}
