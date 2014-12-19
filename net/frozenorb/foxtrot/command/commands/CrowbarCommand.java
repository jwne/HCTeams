package net.frozenorb.foxtrot.command.commands;

import org.bukkit.entity.*;
import org.bukkit.*;
import net.frozenorb.foxtrot.util.*;
import net.frozenorb.foxtrot.command.annotations.*;

public class CrowbarCommand
{
    @Command(names = { "Crowbar" }, permissionNode = "op")
    public static void crowbar(final Player sender) {
        if (sender.getGameMode() != GameMode.CREATIVE) {
            sender.sendMessage(ChatColor.RED + "This command must be ran in creative.");
            return;
        }
        sender.setItemInHand(InvUtils.CROWBAR);
        sender.sendMessage(ChatColor.YELLOW + "Gave you a crowbar.");
    }
}
