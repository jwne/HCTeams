package net.frozenorb.foxtrot.nametag;

import net.frozenorb.foxtrot.*;
import org.bukkit.*;
import net.frozenorb.foxtrot.listener.*;
import net.frozenorb.foxtrot.server.*;
import net.frozenorb.foxtrot.team.*;
import java.util.*;
import org.bukkit.scheduler.*;
import org.bukkit.entity.*;
import org.bukkit.plugin.*;

public class NametagManager
{
    private static List<TeamInfo> registeredTeams;
    private static int teamCreateIndex;
    private static HashMap<String, HashMap<String, TeamInfo>> teamMap;
    
    public static void reloadPlayer(final Player toRefresh) {
        for (final Player refreshFor : buttplug.fdsjfhkdsjfdsjhk().getServer().getOnlinePlayers()) {
            reloadPlayer(toRefresh, refreshFor);
        }
    }
    
    public static void reloadPlayer(final Player toRefresh, final Player refreshFor) {
        final faggot team = buttplug.fdsjfhkdsjfdsjhk().getDsfjhkdsjhdsjkhfds().getPlayerTeam(toRefresh.getName());
        TeamInfo teamInfo = getOrCreate(ChatColor.YELLOW.toString(), "");
        if (team != null) {
            if (team.isMember(refreshFor.getName())) {
                teamInfo = getOrCreate(ChatColor.DARK_GREEN.toString(), "");
            }
            else if (team.isAlly(refreshFor.getName())) {
                teamInfo = getOrCreate(ChatColor.LIGHT_PURPLE.toString(), "");
            }
        }
        if (refreshFor == toRefresh) {
            teamInfo = getOrCreate(ChatColor.DARK_GREEN.toString(), "");
        }
        if (refreshFor.getGameMode() == GameMode.CREATIVE && refreshFor.getItemInHand() != null && refreshFor.getItemInHand().getType() == Material.REDSTONE_BLOCK) {
            String enderpearlString = "";
            String combatTagString = "";
            if (EnderpearlListener.getEnderpearlCooldown().containsKey(toRefresh.getName()) && EnderpearlListener.getEnderpearlCooldown().get(toRefresh.getName()) > System.currentTimeMillis()) {
                final long millisLeft = EnderpearlListener.getEnderpearlCooldown().get(toRefresh.getName()) - System.currentTimeMillis();
                final double value = millisLeft / 1000.0;
                final double sec = Math.round(10.0 * value) / 10.0;
                enderpearlString = sec + " ";
            }
            if (SpawnTagHandler.isTagged(toRefresh)) {
                final long millisLeft = SpawnTagHandler.getTag(toRefresh);
                final double value = millisLeft / 1000.0;
                final double sec = Math.round(10.0 * value) / 10.0;
                combatTagString = " " + sec;
            }
            teamInfo = getOrCreate(ChatColor.GREEN.toString() + enderpearlString + teamInfo.getPrefix(), ChatColor.DARK_RED + combatTagString);
        }
        HashMap<String, TeamInfo> teamInfoMap = new HashMap<String, TeamInfo>();
        if (NametagManager.teamMap.containsKey(refreshFor.getName())) {
            teamInfoMap = NametagManager.teamMap.get(refreshFor.getName());
            if (teamInfoMap.containsKey(toRefresh.getName())) {
                final TeamInfo tem = teamInfoMap.get(toRefresh.getName());
                if (tem != teamInfo) {
                    sendPacketsRemoveFromTeam(tem, toRefresh.getName(), refreshFor);
                    teamInfoMap.remove(toRefresh.getName());
                }
            }
        }
        sendPacketsAddToTeam(teamInfo, new String[] { toRefresh.getName() }, refreshFor);
        teamInfoMap.put(toRefresh.getName(), teamInfo);
        NametagManager.teamMap.put(refreshFor.getName(), teamInfoMap);
    }
    
    public static void initPlayer(final Player player) {
        for (final TeamInfo teamInfo : NametagManager.registeredTeams) {
            sendPacketsAddTeam(teamInfo, player);
        }
    }
    
    public static TeamInfo getOrCreate(final String prefix, final String suffix) {
        for (final TeamInfo teamInfo : NametagManager.registeredTeams) {
            if (teamInfo.getPrefix().equals(prefix) && teamInfo.getSuffix().equals(suffix)) {
                return teamInfo;
            }
        }
        final TeamInfo newTeam = new TeamInfo(String.valueOf(NametagManager.teamCreateIndex), prefix, suffix);
        ++NametagManager.teamCreateIndex;
        NametagManager.registeredTeams.add(newTeam);
        for (final Player player : buttplug.fdsjfhkdsjfdsjhk().getServer().getOnlinePlayers()) {
            sendPacketsAddTeam(newTeam, player);
        }
        return newTeam;
    }
    
    public static void sendTeamsToPlayer(final Player player) {
        for (final Player toRefresh : buttplug.fdsjfhkdsjfdsjhk().getServer().getOnlinePlayers()) {
            reloadPlayer(toRefresh, player);
        }
    }
    
    public static void sendPacketsAddTeam(final TeamInfo team, final Player p) {
        try {
            new ScoreboardTeamPacketMod(team.getName(), team.getPrefix(), team.getSuffix(), new ArrayList(), 0).sendToPlayer(p);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void sendPacketsAddToTeam(final TeamInfo team, final String[] player, final Player p) {
        try {
            new ScoreboardTeamPacketMod(team.getName(), Arrays.asList(player), 3).sendToPlayer(p);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void sendPacketsRemoveFromTeam(final TeamInfo team, final String player, final Player tp) {
        try {
            new ScoreboardTeamPacketMod(team.getName(), Arrays.asList(player), 4).sendToPlayer(tp);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static HashMap<String, HashMap<String, TeamInfo>> getTeamMap() {
        return NametagManager.teamMap;
    }
    
    static {
        NametagManager.registeredTeams = new ArrayList<TeamInfo>();
        NametagManager.teamCreateIndex = 1;
        NametagManager.teamMap = new HashMap<String, HashMap<String, TeamInfo>>();
        new BukkitRunnable() {
            public void run() {
                for (final Player player : buttplug.fdsjfhkdsjfdsjhk().getServer().getOnlinePlayers()) {
                    if (player.getGameMode() == GameMode.CREATIVE && player.getItemInHand() != null && player.getItemInHand().getType() == Material.REDSTONE_BLOCK) {
                        for (final Entity entity : player.getNearbyEntities(20.0, 40.0, 20.0)) {
                            if (entity instanceof Player) {
                                NametagManager.reloadPlayer((Player)entity, player);
                            }
                        }
                    }
                }
            }
        }.runTaskTimer((Plugin)buttplug.fdsjfhkdsjfdsjhk(), 20L, 20L);
    }
}
