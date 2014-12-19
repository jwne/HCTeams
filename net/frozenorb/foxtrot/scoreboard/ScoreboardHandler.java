package net.frozenorb.foxtrot.scoreboard;

import java.util.*;
import org.bukkit.scheduler.*;
import net.frozenorb.foxtrot.*;
import org.bukkit.entity.*;
import org.bukkit.plugin.*;

public class ScoreboardHandler
{
    private Map<String, FoxtrotBoard> boards;
    public static boolean scoreboardTimerEnabled;
    
    public ScoreboardHandler() {
        super();
        this.boards = new HashMap<String, FoxtrotBoard>();
        new BukkitRunnable() {
            public void run() {
                if (ScoreboardHandler.scoreboardTimerEnabled) {
                    for (final Player online : buttplug.fdsjfhkdsjfdsjhk().getServer().getOnlinePlayers()) {
                        ScoreboardHandler.this.update(online);
                    }
                }
            }
        }.runTaskTimer((Plugin)buttplug.fdsjfhkdsjfdsjhk(), 20L, 20L);
    }
    
    public void update(final Player player) {
        if (this.boards.containsKey(player.getName())) {
            this.boards.get(player.getName()).update();
        }
        else {
            this.boards.put(player.getName(), new FoxtrotBoard(player));
        }
    }
    
    public void remove(final Player player) {
        this.boards.remove(player.getName());
    }
    
    static {
        ScoreboardHandler.scoreboardTimerEnabled = true;
    }
}
