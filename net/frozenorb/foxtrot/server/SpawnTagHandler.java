package net.frozenorb.foxtrot.server;

import java.util.*;
import org.bukkit.entity.*;
import net.frozenorb.foxtrot.*;
import net.frozenorb.foxtrot.team.dtr.bitmask.*;
import org.bukkit.*;

public class SpawnTagHandler
{
    public static final int MAX_SPAWN_TAG = 60;
    private static HashMap<String, Long> spawnTags;
    
    public static void removeTag(final Player player) {
        SpawnTagHandler.spawnTags.remove(player.getName());
    }
    
    public static void addSeconds(final Player player, final int seconds) {
        if (!buttplug.fdsjfhkdsjfdsjhk().getServerHandler().isEOTW() && DTRBitmaskType.SAFE_ZONE.appliesAt(player.getLocation())) {
            return;
        }
        if (isTagged(player)) {
            final int secondsTaggedFor = (int)((SpawnTagHandler.spawnTags.get(player.getName()) - System.currentTimeMillis()) / 1000L);
            final int newSeconds = Math.min(secondsTaggedFor + seconds, 60);
            SpawnTagHandler.spawnTags.put(player.getName(), System.currentTimeMillis() + newSeconds * 1000L);
        }
        else {
            player.sendMessage(ChatColor.YELLOW + "You have been spawn-tagged for §c" + seconds + " §eseconds!");
            SpawnTagHandler.spawnTags.put(player.getName(), System.currentTimeMillis() + seconds * 1000L);
        }
    }
    
    public static long getTag(final Player player) {
        return SpawnTagHandler.spawnTags.get(player.getName()) - System.currentTimeMillis();
    }
    
    public static boolean isTagged(final Player player) {
        return SpawnTagHandler.spawnTags.containsKey(player.getName()) && SpawnTagHandler.spawnTags.get(player.getName()) > System.currentTimeMillis();
    }
    
    public static HashMap<String, Long> getSpawnTags() {
        return SpawnTagHandler.spawnTags;
    }
    
    static {
        SpawnTagHandler.spawnTags = new HashMap<String, Long>();
    }
}
