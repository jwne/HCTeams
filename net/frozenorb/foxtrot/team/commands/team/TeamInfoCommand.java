package net.frozenorb.foxtrot.team.commands.team;

import org.bukkit.entity.*;
import net.frozenorb.foxtrot.team.*;
import net.frozenorb.foxtrot.command.annotations.*;

public class TeamInfoCommand
{
    @Command(names = { "team info", "t info", "f info", "faction info", "fac info", "team who", "t who", "f who", "faction who", "fac who", "team show", "t show", "f show", "faction show", "fac show", "team i", "t i", "f i", "faction i", "fac i" }, permissionNode = "")
    public static void teamInfo(final Player sender, @Param(name = "team", defaultValue = "self") final faggot target) {
        target.sendTeamInfo(sender);
    }
}
