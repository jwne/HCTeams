package net.frozenorb.foxtrot.command.commands;

import org.bukkit.entity.*;
import net.frozenorb.foxtrot.*;
import org.bukkit.*;
import net.frozenorb.foxtrot.util.*;
import net.frozenorb.foxtrot.jedis.persist.*;
import net.frozenorb.foxtrot.command.annotations.*;

public class PlaytimeCommand
{
    @Command(names = { "Playtime", "PTime" }, permissionNode = "")
    public static void playtime(final Player sender, @Param(name = "Target", defaultValue = "self") final OfflinePlayer target) {
        final PlaytimeMap playtime = buttplug.fdsjfhkdsjfdsjhk().getPlaytimeMap();
        long playtimeTime = playtime.getPlaytime(target.getName());
        if (target.isOnline() && sender.canSee(target.getPlayer())) {
            playtimeTime += playtime.getCurrentSession(target.getName()) / 1000L;
        }
        sender.sendMessage(ChatColor.GREEN + target.getName() + ChatColor.YELLOW + "'s total playtime is " + ChatColor.GREEN + TimeUtils.getDurationBreakdown(playtimeTime * 1000L) + ChatColor.YELLOW + ".");
    }
}
