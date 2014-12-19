package net.frozenorb.foxtrot.command.commands;

import org.bukkit.entity.*;
import net.frozenorb.foxtrot.*;
import net.frozenorb.foxtrot.listener.*;
import org.bukkit.*;
import net.frozenorb.foxtrot.command.annotations.*;

public class EOTWCommand
{
    @Command(names = { "EOTW" }, permissionNode = "op")
    public static void eotw(final Player sender) {
        if (sender.getGameMode() != GameMode.CREATIVE) {
            sender.sendMessage(ChatColor.RED + "This command must be ran in creative.");
            return;
        }
        buttplug.fdsjfhkdsjfdsjhk().getServerHandler().setEOTW(!buttplug.fdsjfhkdsjfdsjhk().getServerHandler().isEOTW());
        EndListener.endActive = !buttplug.fdsjfhkdsjfdsjhk().getServerHandler().isEOTW();
        if (buttplug.fdsjfhkdsjfdsjhk().getServerHandler().isEOTW()) {
            for (final Player player : buttplug.fdsjfhkdsjfdsjhk().getServer().getOnlinePlayers()) {
                player.playSound(player.getLocation(), Sound.WITHER_SPAWN, 1.0f, 1.0f);
            }
            Bukkit.broadcastMessage(ChatColor.RED + "\u2588\u2588\u2588\u2588\u2588\u2588\u2588");
            Bukkit.broadcastMessage(ChatColor.RED + "\u2588" + ChatColor.DARK_RED + "\u2588\u2588\u2588\u2588\u2588" + ChatColor.RED + "\u2588" + " " + ChatColor.DARK_RED + "[EOTW]");
            Bukkit.broadcastMessage(ChatColor.RED + "\u2588" + ChatColor.DARK_RED + "\u2588" + ChatColor.RED + "\u2588\u2588\u2588\u2588\u2588" + " " + ChatColor.RED.toString() + ChatColor.BOLD + "EOTW has commenced.");
            Bukkit.broadcastMessage(ChatColor.RED + "\u2588" + ChatColor.DARK_RED + "\u2588\u2588\u2588\u2588" + ChatColor.RED + "\u2588\u2588" + " " + ChatColor.RED + "All SafeZones are now Deathban.");
            Bukkit.broadcastMessage(ChatColor.RED + "\u2588" + ChatColor.DARK_RED + "\u2588" + ChatColor.RED + "\u2588\u2588\u2588\u2588\u2588" + " " + ChatColor.RED + "The world border has moved");
            Bukkit.broadcastMessage(ChatColor.RED + "\u2588" + ChatColor.DARK_RED + "\u2588\u2588\u2588\u2588\u2588" + ChatColor.RED + "\u2588" + " " + ChatColor.RED + "to 1000.");
            Bukkit.broadcastMessage(ChatColor.RED + "\u2588\u2588\u2588\u2588\u2588\u2588\u2588");
        }
        else {
            sender.sendMessage(ChatColor.RED + "The server is no longer in EOTW mode.");
        }
    }
    
    @Command(names = { "PreEOTW" }, permissionNode = "op")
    public static void preeotw(final Player sender) {
        if (sender.getGameMode() != GameMode.CREATIVE) {
            sender.sendMessage(ChatColor.RED + "This command must be ran in creative.");
            return;
        }
        buttplug.fdsjfhkdsjfdsjhk().getServerHandler().setPreEOTW(!buttplug.fdsjfhkdsjfdsjhk().getServerHandler().isPreEOTW());
        if (buttplug.fdsjfhkdsjfdsjhk().getServerHandler().isPreEOTW()) {
            for (final Player player : buttplug.fdsjfhkdsjfdsjhk().getServer().getOnlinePlayers()) {
                player.playSound(player.getLocation(), Sound.WITHER_SPAWN, 1.0f, 1.0f);
            }
            Bukkit.broadcastMessage(ChatColor.RED + "\u2588\u2588\u2588\u2588\u2588\u2588\u2588");
            Bukkit.broadcastMessage(ChatColor.RED + "\u2588" + ChatColor.DARK_RED + "\u2588\u2588\u2588\u2588\u2588" + ChatColor.RED + "\u2588" + " " + ChatColor.DARK_RED + "[Pre-EOTW]");
            Bukkit.broadcastMessage(ChatColor.RED + "\u2588" + ChatColor.DARK_RED + "\u2588" + ChatColor.RED + "\u2588\u2588\u2588\u2588\u2588" + " " + ChatColor.RED.toString() + ChatColor.BOLD + "EOTW is about to commence.");
            Bukkit.broadcastMessage(ChatColor.RED + "\u2588" + ChatColor.DARK_RED + "\u2588\u2588\u2588\u2588" + ChatColor.RED + "\u2588\u2588" + " " + ChatColor.RED + "PvP Protection is disabled.");
            Bukkit.broadcastMessage(ChatColor.RED + "\u2588" + ChatColor.DARK_RED + "\u2588" + ChatColor.RED + "\u2588\u2588\u2588\u2588\u2588" + " " + ChatColor.RED + "All players have been un-deathbanned.");
            Bukkit.broadcastMessage(ChatColor.RED + "\u2588" + ChatColor.DARK_RED + "\u2588\u2588\u2588\u2588\u2588" + ChatColor.RED + "\u2588" + " " + ChatColor.RED + "All deathbans are now permanent.");
            Bukkit.broadcastMessage(ChatColor.RED + "\u2588\u2588\u2588\u2588\u2588\u2588\u2588");
        }
        else {
            sender.sendMessage(ChatColor.RED + "The server is no longer in Pre-EOTW mode.");
        }
    }
}
