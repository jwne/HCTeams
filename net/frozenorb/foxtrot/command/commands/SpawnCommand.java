package net.frozenorb.foxtrot.command.commands;

import org.bukkit.entity.*;
import net.frozenorb.foxtrot.*;
import org.bukkit.*;
import net.frozenorb.foxtrot.command.annotations.*;

public class SpawnCommand
{
    @Command(names = { "spawn" }, permissionNode = "")
    public static void spawn(final Player sender) {
        if (sender.isOp()) {
            sender.teleport(buttplug.fdsjfhkdsjfdsjhk().getServerHandler().getSpawnLocation());
        }
        else {
            sender.sendMessage(ChatColor.RED + "HCTeams does not have a spawn command! You must walk there!");
        }
    }
}
