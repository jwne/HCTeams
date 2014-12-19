package net.frozenorb.foxtrot.command.commands;

import org.bukkit.entity.*;
import org.bukkit.*;
import net.frozenorb.foxtrot.command.annotations.*;

public class ToggleDonorOnlyCommand
{
    public static boolean donorOnly;
    
    @Command(names = { "ToggleDonorOnly" }, permissionNode = "")
    public static void toggleDonorOnly(final Player sender) {
        ToggleDonorOnlyCommand.donorOnly = !ToggleDonorOnlyCommand.donorOnly;
        sender.sendMessage(ChatColor.GREEN + "Donor only mode? " + ChatColor.YELLOW + ToggleDonorOnlyCommand.donorOnly);
    }
    
    static {
        ToggleDonorOnlyCommand.donorOnly = false;
    }
}
