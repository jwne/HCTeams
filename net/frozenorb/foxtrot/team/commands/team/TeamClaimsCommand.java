package net.frozenorb.foxtrot.team.commands.team;

import org.bukkit.entity.*;
import net.frozenorb.foxtrot.team.*;
import org.bukkit.*;
import net.frozenorb.foxtrot.team.claims.*;
import java.util.*;
import net.frozenorb.foxtrot.command.annotations.*;

public class TeamClaimsCommand
{
    @Command(names = { "team claims", "t claims", "f claims", "faction claims", "fac claims" }, permissionNode = "")
    public static void teamClaims(final Player sender, @Param(name = "team", defaultValue = "self") final faggot target) {
        if (target.getClaims().size() == 0) {
            sender.sendMessage(ChatColor.RED + "That team has no claimed land!");
        }
        else {
            sender.sendMessage(ChatColor.GRAY + "-- " + ChatColor.DARK_AQUA + target.getName() + "'s Claims" + ChatColor.GRAY + " --");
            for (final Claim claim : target.getClaims()) {
                sender.sendMessage(ChatColor.GRAY + " " + claim.getFriendlyName());
            }
        }
    }
}
