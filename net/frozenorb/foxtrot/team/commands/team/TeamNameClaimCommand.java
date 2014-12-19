package net.frozenorb.foxtrot.team.commands.team;

import org.bukkit.entity.*;
import net.frozenorb.foxtrot.*;
import org.bukkit.*;
import net.frozenorb.foxtrot.team.*;
import net.frozenorb.foxtrot.team.claims.*;
import net.frozenorb.foxtrot.command.annotations.*;

public class TeamNameClaimCommand
{
    @Command(names = { "team nameclaim", "t nameclaim", "f nameclaim", "faction nameclaim", "fac nameclaim", "team renameclaim", "t renameclaim", "f renameclaim", "faction renameclaim", "fac renameclaim" }, permissionNode = "")
    public static void teamNameClaim(final Player sender, @Param(name = "name", wildcard = true) String name) {
        final faggot team = buttplug.fdsjfhkdsjfdsjhk().getDsfjhkdsjhdsjkhfds().getPlayerTeam(sender.getName());
        if (team == null) {
            sender.sendMessage(ChatColor.GRAY + "You are not on a team!");
            return;
        }
        if (!sender.isOp()) {
            name = name.split(" ")[0];
        }
        if (!name.matches("^[a-zA-Z0-9]*$")) {
            sender.sendMessage(ChatColor.RED + "Land names must be alphanumeric!");
            return;
        }
        if (team.isOwner(sender.getName()) || team.isCaptain(sender.getName())) {
            if (LandBoard.getInstance().getTeam(sender.getLocation()) != null && team.ownsLocation(sender.getLocation())) {
                final Claim cc = LandBoard.getInstance().getClaim(sender.getLocation());
                cc.setName(name);
                sender.sendMessage(ChatColor.YELLOW + "You have renamed this claim to: " + ChatColor.WHITE + name);
                return;
            }
            sender.sendMessage(ChatColor.RED + "You do not own this land.");
        }
        else {
            sender.sendMessage(ChatColor.DARK_AQUA + "Only the team captains can do this.");
        }
    }
}
