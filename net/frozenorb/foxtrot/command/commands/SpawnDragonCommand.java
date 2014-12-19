package net.frozenorb.foxtrot.command.commands;

import org.bukkit.*;
import org.bukkit.entity.*;
import net.frozenorb.foxtrot.command.annotations.*;

public class SpawnDragonCommand
{
    @Command(names = { "SpawnDragon" }, permissionNode = "")
    public static void spawnDragon(final Player sender) {
        if (sender.getWorld().getEnvironment() != World.Environment.THE_END) {
            sender.sendMessage(ChatColor.RED + "You must be in the end.");
            return;
        }
        sender.getWorld().spawnCreature(sender.getLocation(), EntityType.ENDER_DRAGON);
        sender.sendMessage("Spawned enderdragon.");
    }
}
