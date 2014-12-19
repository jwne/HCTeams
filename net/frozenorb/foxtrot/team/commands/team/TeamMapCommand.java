package net.frozenorb.foxtrot.team.commands.team;

import org.bukkit.entity.*;
import net.frozenorb.foxtrot.team.claims.*;
import net.frozenorb.foxtrot.command.annotations.*;

public class TeamMapCommand
{
    @Command(names = { "team map", "t map", "f map", "faction map", "fac map", "map" }, permissionNode = "")
    public static void teamMap(final Player sender) {
        new VisualClaim(sender, VisualClaimType.MAP, false).draw(false);
    }
}
