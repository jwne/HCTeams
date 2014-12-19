package net.frozenorb.foxtrot.scoreboard;

import org.bukkit.entity.*;
import java.util.*;
import net.frozenorb.foxtrot.*;
import org.bukkit.*;
import org.bukkit.scoreboard.*;
import net.frozenorb.foxtrot.util.*;

public class FoxtrotBoard
{
    private Player player;
    private Objective obj;
    private Set<String> displayedScores;
    
    public FoxtrotBoard(final Player player) {
        super();
        this.displayedScores = new HashSet<String>();
        this.player = player;
        final Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
        (this.obj = board.registerNewObjective("HCTeams", "dummy")).setDisplayName(buttplug.fdsjfhkdsjfdsjhk().getMapHandler().getScoreboardTitle());
        this.obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        this.update();
        player.setScoreboard(board);
    }
    
    public void update() {
        int nextVal = 14;
        for (final ScoreGetter getter : ScoreGetter.SCORES) {
            final int seconds = getter.getSeconds(this.player);
            final String title = getter.getTitle(this.player);
            if (seconds == -1) {
                if (this.displayedScores.contains(title)) {
                    this.obj.getScoreboard().resetScores(title);
                    this.displayedScores.remove(title);
                }
            }
            else {
                this.displayedScores.add(title);
                this.obj.getScore(title).setScore(nextVal);
                this.getTeam(title, seconds).addEntry(title);
                --nextVal;
            }
        }
        if (nextVal < 14) {
            this.obj.getScore(ChatColor.RESET + " ").setScore(15);
        }
        else {
            this.obj.getScoreboard().resetScores(ChatColor.RESET + " ");
        }
    }
    
    private Team getTeam(final String title, final int seconds) {
        final String name = ChatColor.stripColor(title);
        Team team = this.obj.getScoreboard().getTeam(name);
        if (team == null) {
            team = this.obj.getScoreboard().registerNewTeam(name);
        }
        final String time = TimeUtils.getMMSS(seconds);
        team.setSuffix(ChatColor.GRAY + ": " + ChatColor.RED + time);
        return team;
    }
    
    public Player getPlayer() {
        return this.player;
    }
    
    public Objective getObj() {
        return this.obj;
    }
    
    public Set<String> getDisplayedScores() {
        return this.displayedScores;
    }
}
