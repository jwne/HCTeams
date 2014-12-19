package net.frozenorb.foxtrot.team.commands.team;

import org.bukkit.entity.*;
import net.frozenorb.foxtrot.command.annotations.*;

public class TeamHelpCommand
{
    @Command(names = { "team help", "t help", "f help", "faction help", "fac help" }, permissionNode = "")
    public static void teamHelp(final Player player) {
        TeamCommand.team(player);
    }
}
