package net.frozenorb.foxtrot.team.dtr;

import org.bukkit.scheduler.*;
import net.frozenorb.foxtrot.team.*;
import net.frozenorb.foxtrot.*;
import org.bukkit.entity.*;
import org.bukkit.*;
import java.util.*;

public class DTRHandler extends BukkitRunnable
{
    private static final double[] BASE_DTR_INCREMENT;
    private static final double[] MAX_DTR;
    private static Set<String> wasOnCooldown;
    
    public static double getBaseDTRIncrement(final int teamsize) {
        return (teamsize == 0) ? 0.0 : (DTRHandler.BASE_DTR_INCREMENT[teamsize - 1] * 3.0);
    }
    
    public static double getMaxDTR(final int teamsize) {
        return (teamsize == 0) ? 100.0 : DTRHandler.MAX_DTR[teamsize - 1];
    }
    
    public static boolean isOnCooldown(final faggot team) {
        return team.getDeathCooldown() > System.currentTimeMillis() || team.getRaidableCooldown() > System.currentTimeMillis();
    }
    
    public static boolean isRegenerating(final faggot team) {
        return !isOnCooldown(team) && team.getDTR() != team.getMaxDTR();
    }
    
    public static void setCooldown(final faggot team) {
        DTRHandler.wasOnCooldown.add(team.getName().toLowerCase());
    }
    
    public void run() {
        final Map<faggot, Integer> playerOnlineMap = new HashMap<faggot, Integer>();
        for (final Player player : buttplug.fdsjfhkdsjfdsjhk().getServer().getOnlinePlayers()) {
            if (!player.hasMetadata("invisible")) {
                final faggot playerTeam = buttplug.fdsjfhkdsjfdsjhk().getDsfjhkdsjhdsjkhfds().getPlayerTeam(player.getName());
                if (playerTeam != null) {
                    if (playerOnlineMap.containsKey(playerTeam)) {
                        playerOnlineMap.put(playerTeam, playerOnlineMap.get(playerTeam) + 1);
                    }
                    else {
                        playerOnlineMap.put(playerTeam, 1);
                    }
                }
            }
        }
        for (final Map.Entry<faggot, Integer> teamEntry : playerOnlineMap.entrySet()) {
            if (teamEntry.getKey().getOwner() != null) {
                try {
                    if (isOnCooldown(teamEntry.getKey())) {
                        DTRHandler.wasOnCooldown.add(teamEntry.getKey().getName().toLowerCase());
                    }
                    else {
                        if (DTRHandler.wasOnCooldown.contains(teamEntry.getKey().getName().toLowerCase())) {
                            DTRHandler.wasOnCooldown.remove(teamEntry.getKey().getName().toLowerCase());
                            for (final Player player : teamEntry.getKey().getOnlineMembers()) {
                                player.sendMessage(ChatColor.GREEN.toString() + ChatColor.BOLD + "Your team is now regenerating DTR!");
                            }
                        }
                        teamEntry.getKey().setDTR(Math.min(teamEntry.getKey().getDTR() + teamEntry.getKey().getDTRIncrement(teamEntry.getValue()).doubleValue(), teamEntry.getKey().getMaxDTR()));
                    }
                }
                catch (Exception e) {
                    System.out.println("Error regenerating DTR for faction " + teamEntry.getKey().getName() + ".");
                    e.printStackTrace();
                }
            }
        }
    }
    
    static {
        BASE_DTR_INCREMENT = new double[] { 1.5, 0.5, 0.45, 0.4, 0.36, 0.33, 0.3, 0.27, 0.24, 0.22, 0.21, 0.2, 0.19, 0.18, 0.175, 0.17, 0.168, 0.166, 0.164, 0.162, 0.16, 0.158, 0.156, 0.154, 0.152, 0.15, 0.148, 0.146, 0.144, 0.142, 0.142, 0.142, 0.142, 0.142, 0.142 };
        MAX_DTR = new double[] { 1.01, 1.8, 2.2, 2.7, 3.2, 3.4, 3.6, 3.8, 3.9, 4.18, 4.23, 4.36, 4.42, 4.59, 4.67, 4.72, 4.89, 4.92, 5.04, 5.15, 5.29, 5.37, 5.48, 5.52, 5.6, 5.73, 5.81, 5.96, 6.08, 6.16, 6.16, 6.16, 6.16, 6.16, 6.16 };
        DTRHandler.wasOnCooldown = new HashSet<String>();
    }
}
