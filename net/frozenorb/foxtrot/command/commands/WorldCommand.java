package net.frozenorb.foxtrot.command.commands;

import org.bukkit.entity.*;
import org.bukkit.*;
import net.frozenorb.foxtrot.command.annotations.*;

public class WorldCommand
{
    @Command(names = { "world" }, permissionNode = "op")
    public static void world(final Player sender, @Param(name = "World") final World world) {
        sender.teleport(world.getSpawnLocation());
    }
}
