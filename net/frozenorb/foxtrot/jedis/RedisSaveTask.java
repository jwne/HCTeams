package net.frozenorb.foxtrot.jedis;

import org.bukkit.scheduler.*;
import redis.clients.jedis.*;
import net.frozenorb.foxtrot.*;
import net.frozenorb.foxtrot.team.*;
import net.frozenorb.foxtrot.imagemessage.*;
import org.bukkit.*;
import java.util.*;

public class RedisSaveTask extends BukkitRunnable
{
    public void run() {
        save(false);
    }
    
    public static int save(final boolean forceAll) {
        System.out.println("Saving teams to Jedis...");
        final yourmom<Integer> jdc = new yourmom<Integer>() {
            @Override
            public Integer execute(final Jedis jedis) {
                int changed = 0;
                for (final faggot team : buttplug.fdsjfhkdsjfdsjhk().getDsfjhkdsjhdsjkhfds().getTeams()) {
                    if (team.isNeedsSave() || forceAll) {
                        ++changed;
                        jedis.set("fox_teams." + team.getName().toLowerCase(), team.saveString(true));
                    }
                }
                jedis.set("TeamsLastUpdated", String.valueOf((float)(System.currentTimeMillis() / 1000L)));
                return changed;
            }
        };
        final long startMs = System.currentTimeMillis();
        final int teamsSaved = buttplug.fdsjfhkdsjfdsjhk().eatmyass(jdc);
        final int time = (int)(System.currentTimeMillis() - startMs);
        System.out.println("Saved " + teamsSaved + " teams to Redis in " + time + "ms.");
        final Map<String, String> dealtWith = new HashMap<String, String>();
        final Set<String> errors = new HashSet<String>();
        for (final faggot team : buttplug.fdsjfhkdsjfdsjhk().getDsfjhkdsjhdsjkhfds().getTeams()) {
            for (final String member : team.getMembers()) {
                if (dealtWith.containsKey(member) && !errors.contains(member)) {
                    errors.add(member);
                }
                else {
                    dealtWith.put(member, team.getName());
                }
            }
        }
        try {
            new ImageMessage("redis-saved").appendText("", "", ChatColor.DARK_PURPLE + "Saved all teams to Redis.", ChatColor.DARK_AQUA + "Teams: " + ChatColor.WHITE + teamsSaved, ChatColor.DARK_AQUA + "Elapsed: " + ChatColor.WHITE + time + "ms", ChatColor.DARK_AQUA + "Errors: " + ChatColor.WHITE + errors.size()).sendOPs();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return teamsSaved;
    }
}
