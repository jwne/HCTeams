package net.frozenorb.foxtrot.command.commands;

import org.bukkit.command.*;
import org.bukkit.*;
import net.frozenorb.foxtrot.command.annotations.*;
import net.frozenorb.foxtrot.jedis.*;
import net.frozenorb.foxtrot.util.*;

public class SaveCommand
{
    @Command(names = { "Save" }, permissionNode = "op")
    public static void save(final CommandSender sender) {
        sender.sendMessage(ChatColor.RED + "/saveredis - saves changes to redis");
        sender.sendMessage(ChatColor.RED + "/saveserver - [alpha] takes a backup of the server directory");
    }
    
    @Command(names = { "SaveRedis" }, permissionNode = "op")
    public static void saveRedis(final CommandSender sender) {
        RedisSaveTask.save(false);
    }
    
    @Command(names = { "SaveServer" }, permissionNode = "op")
    public static void saveServer(final CommandSender sender) {
        sender.sendMessage(ChatColor.YELLOW + "Server save and backup started.");
        BackupUtils.fullBackup(new FoxCallback() {
            @Override
            public void call(final Object object) {
                sender.sendMessage(ChatColor.YELLOW + "Server save and backup completed.");
            }
        });
    }
    
    @Command(names = { "SaveRedis ForceAll" }, permissionNode = "op")
    public static void saveRedisForceAll(final CommandSender sender) {
        RedisSaveTask.save(true);
    }
}
