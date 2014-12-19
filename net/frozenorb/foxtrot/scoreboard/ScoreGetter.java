package net.frozenorb.foxtrot.scoreboard;

import org.bukkit.entity.*;
import org.bukkit.*;
import net.frozenorb.foxtrot.server.*;
import net.frozenorb.foxtrot.listener.*;
import net.frozenorb.foxtrot.*;

public interface ScoreGetter
{
    public static final int NO_SCORE = -1;
    public static final ScoreGetter SPAWN_TAG = new ScoreGetter() {
        @Override
        public String getTitle(Player player) {
            return ChatColor.RED.toString() + ChatColor.BOLD + "Spawn Tag";
        }
        
        @Override
        public int getSeconds(Player player) {
            long diff;
            if (SpawnTagHandler.isTagged(player)) {
                diff = SpawnTagHandler.getTag(player);
                if (diff >= 0L) {
                    return (int)diff / 1000;
                }
            }
            return -1;
        }
    };
    public static final ScoreGetter ENDERPEARL = new ScoreGetter() {
        @Override
        public String getTitle(Player player) {
            return ChatColor.YELLOW.toString() + ChatColor.BOLD + "Enderpearl";
        }
        
        @Override
        public int getSeconds(Player player) {
            long diff;
            if (EnderpearlListener.getEnderpearlCooldown().containsKey(player.getName()) && EnderpearlListener.getEnderpearlCooldown().get(player.getName()) >= System.currentTimeMillis()) {
                diff = EnderpearlListener.getEnderpearlCooldown().get(player.getName()) - System.currentTimeMillis();
                if (diff >= 0L) {
                    return (int)diff / 1000;
                }
            }
            return -1;
        }
    };
    public static final ScoreGetter PVP_TIMER = new ScoreGetter() {
        @Override
        public String getTitle(Player player) {
            return ChatColor.GREEN.toString() + ChatColor.BOLD + "PVP Timer";
        }
        
        @Override
        public int getSeconds(Player player) {
            long diff;
            if (buttplug.fdsjfhkdsjfdsjhk().getPvPTimerMap().hasTimer(player.getName())) {
                diff = buttplug.fdsjfhkdsjfdsjhk().getPvPTimerMap().getTimer(player.getName()) - System.currentTimeMillis();
                if (diff >= 0L) {
                    return (int)diff / 1000;
                }
            }
            return -1;
        }
    };
    public static final ScoreGetter[] SCORES = { ScoreGetter.SPAWN_TAG, ScoreGetter.ENDERPEARL, ScoreGetter.PVP_TIMER };
    
    String getTitle(Player p0);
    
    int getSeconds(Player p0);
}
