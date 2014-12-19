package net.frozenorb.foxtrot.command.commands;

import org.bukkit.entity.*;
import net.frozenorb.foxtrot.scoreboard.*;
import org.bukkit.*;
import net.frozenorb.foxtrot.command.annotations.*;

public class ToggleScoreboardTimers
{
    @Command(names = { "ToggleScoreboardTimers" }, permissionNode = "op")
    public static void toggleScoreboardTimers(final Player sender) {
        ScoreboardHandler.scoreboardTimerEnabled = !ScoreboardHandler.scoreboardTimerEnabled;
        sender.sendMessage(ChatColor.YELLOW + "Scoreboard timers enabled? " + ChatColor.GREEN + ScoreboardHandler.scoreboardTimerEnabled);
    }
}
