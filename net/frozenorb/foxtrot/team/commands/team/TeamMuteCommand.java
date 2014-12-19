package net.frozenorb.foxtrot.team.commands.team;

import org.bukkit.entity.*;
import net.frozenorb.foxtrot.team.*;
import net.frozenorb.foxtrot.*;
import org.bukkit.*;
import net.frozenorb.foxtrot.util.*;
import org.bukkit.scheduler.*;
import java.util.*;
import org.bukkit.plugin.*;
import net.frozenorb.foxtrot.command.annotations.*;

public class TeamMuteCommand
{
    public static HashMap<String, String> factionMutes;
    
    @Command(names = { "team mute", "t mute", "f mute", "faction mute", "fac mute" }, permissionNode = "foxtrot.mutefaction")
    public static void teamMuteFaction(final Player sender, @Param(name = "team") final faggot target, @Param(name = "minutes") final String time, @Param(name = "reason", wildcard = true) final String reason) {
        final int timeSeconds = Integer.valueOf(time) * 60;
        for (final String player : target.getMembers()) {
            TeamMuteCommand.factionMutes.put(player, target.getName());
            final Player bukkitPlayer = buttplug.fdsjfhkdsjfdsjhk().getServer().getPlayerExact(player);
            if (bukkitPlayer != null) {
                bukkitPlayer.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "Your faction has been muted for " + TimeUtils.getDurationBreakdown(timeSeconds * 1000L) + " for " + reason + ".");
            }
        }
        new BukkitRunnable() {
            public void run() {
                final Iterator<Map.Entry<String, String>> mutesIterator = TeamMuteCommand.factionMutes.entrySet().iterator();
                while (mutesIterator.hasNext()) {
                    final Map.Entry<String, String> mute = mutesIterator.next();
                    if (mute.getValue().equalsIgnoreCase(target.getName())) {
                        final Player bukkitPlayer = buttplug.fdsjfhkdsjfdsjhk().getServer().getPlayerExact((String)mute.getKey());
                        if (bukkitPlayer != null) {
                            bukkitPlayer.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "Your faction's mute has expired!");
                        }
                        mutesIterator.remove();
                    }
                }
            }
        }.runTaskLater((Plugin)buttplug.fdsjfhkdsjfdsjhk(), timeSeconds * 20L);
        sender.sendMessage(ChatColor.GRAY + "Muted the faction " + target.getName() + ChatColor.GRAY + " for " + TimeUtils.getDurationBreakdown(timeSeconds * 1000L) + " for " + reason + ".");
    }
    
    @Command(names = { "team unmute", "t unmute", "f unmute", "faction unmute", "fac unmute" }, permissionNode = "foxtrot.mutefaction")
    public static void teamUnmuteFaction(final Player sender, @Param(name = "team") final faggot target) {
        final Iterator<Map.Entry<String, String>> mutesIterator = TeamMuteCommand.factionMutes.entrySet().iterator();
        while (mutesIterator.hasNext()) {
            final Map.Entry<String, String> mute = mutesIterator.next();
            if (mute.getValue().equalsIgnoreCase(target.getName())) {
                final Player bukkitPlayer = buttplug.fdsjfhkdsjfdsjhk().getServer().getPlayerExact((String)mute.getKey());
                if (bukkitPlayer != null) {
                    bukkitPlayer.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "Your faction's mute has been removed!");
                }
                mutesIterator.remove();
            }
        }
        sender.sendMessage(ChatColor.GRAY + "Unmuted the faction " + target.getName() + ChatColor.GRAY + ".");
    }
    
    static {
        TeamMuteCommand.factionMutes = new HashMap<String, String>();
    }
}
