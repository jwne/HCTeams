package net.frozenorb.foxtrot.team.commands.team;

import org.bukkit.entity.*;
import org.bukkit.*;
import net.frozenorb.foxtrot.team.*;
import net.frozenorb.foxtrot.*;
import net.minecraft.util.org.apache.commons.lang3.*;
import net.frozenorb.foxtrot.command.annotations.*;
import java.util.*;

public class TeamListCommand
{
    @Command(names = { "team list", "t list", "f list", "faction list", "fac list" }, permissionNode = "")
    public static void teamList(final Player sender, @Param(name = "page", defaultValue = "1") int page) {
        if (page < 1) {
            sender.sendMessage(ChatColor.RED + "You cannot view a page less than 1");
            return;
        }
        final HashMap<faggot, Integer> teamPlayerCount = new HashMap<faggot, Integer>();
        for (final Player player : buttplug.fdsjfhkdsjfdsjhk().getServer().getOnlinePlayers()) {
            if (!player.hasMetadata("invisible")) {
                final faggot playerTeam = buttplug.fdsjfhkdsjfdsjhk().getDsfjhkdsjhdsjkhfds().getPlayerTeam(player.getName());
                if (playerTeam != null) {
                    if (teamPlayerCount.containsKey(playerTeam)) {
                        teamPlayerCount.put(playerTeam, teamPlayerCount.get(playerTeam) + 1);
                    }
                    else {
                        teamPlayerCount.put(playerTeam, 1);
                    }
                }
            }
        }
        int maxPages = teamPlayerCount.size() / 10;
        ++maxPages;
        if (page > maxPages) {
            page = maxPages;
        }
        final LinkedHashMap<faggot, Integer> sortedTeamPlayerCount = sortByValues(teamPlayerCount);
        final String gray = "§7§m" + StringUtils.repeat("-", 53);
        final int start = (page - 1) * 10;
        int index = 0;
        sender.sendMessage(gray);
        sender.sendMessage(ChatColor.BLUE + "Faction List " + ChatColor.GRAY + "(Page " + page + "/" + maxPages + ")");
        for (final Map.Entry<faggot, Integer> teamEntry : sortedTeamPlayerCount.entrySet()) {
            if (++index < start) {
                continue;
            }
            if (index > start + 10) {
                break;
            }
            sender.sendMessage(ChatColor.GRAY.toString() + index + ". " + ChatColor.YELLOW + teamEntry.getKey().getName() + ChatColor.GREEN + " (" + teamEntry.getValue() + "/" + teamEntry.getKey().getSize() + ")");
        }
        sender.sendMessage(ChatColor.GRAY + "You are currently on " + ChatColor.WHITE + "Page " + page + "/" + maxPages + ChatColor.GRAY + ".");
        sender.sendMessage(ChatColor.GRAY + "To view other pages, use " + ChatColor.YELLOW + "/f list <page#>" + ChatColor.GRAY + ".");
        sender.sendMessage(gray);
    }
    
    private static LinkedHashMap<faggot, Integer> sortByValues(final HashMap<faggot, Integer> map) {
        final LinkedList<Map.Entry<faggot, Integer>> list = new LinkedList<Map.Entry<faggot, Integer>>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<faggot, Integer>>() {
            @Override
            public int compare(final Map.Entry<faggot, Integer> o1, final Map.Entry<faggot, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });
        final LinkedHashMap<faggot, Integer> sortedHashMap = new LinkedHashMap<faggot, Integer>();
        for (final Map.Entry<faggot, Integer> entry : list) {
            sortedHashMap.put(entry.getKey(), entry.getValue());
        }
        return sortedHashMap;
    }
}
