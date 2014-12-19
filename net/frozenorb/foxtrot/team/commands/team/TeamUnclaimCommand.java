package net.frozenorb.foxtrot.team.commands.team;

import org.bukkit.entity.*;
import net.frozenorb.foxtrot.*;
import net.frozenorb.foxtrot.team.claims.*;
import org.bukkit.*;
import net.frozenorb.foxtrot.team.*;
import java.util.*;
import net.frozenorb.foxtrot.command.annotations.*;

public class TeamUnclaimCommand
{
    @Command(names = { "team unclaim", "t unclaim", "f unclaim", "faction unclaim", "fac unclaim" }, permissionNode = "")
    public static void teamUnclaim(final Player sender, @Param(name = "all?", defaultValue = "f") final String all) {
        final faggot team = buttplug.fdsjfhkdsjfdsjhk().getDsfjhkdsjhdsjkhfds().getPlayerTeam(sender.getName());
        if (team == null) {
            sender.sendMessage(ChatColor.GRAY + "You are not on a team!");
            return;
        }
        if (!team.isOwner(sender.getName())) {
            sender.sendMessage(ChatColor.DARK_AQUA + "Only the team leader can do this.");
            return;
        }
        if (team.isRaidable()) {
            sender.sendMessage(ChatColor.RED + "You may not unclaim land while your faction is raidable!");
            return;
        }
        if (all.equalsIgnoreCase("all")) {
            final int claims = team.getClaims().size();
            int refund = 0;
            for (final Claim claim : team.getClaims()) {
                refund += Claim.getPrice(claim, team, false);
            }
            team.setBalance(team.getBalance() + refund);
            LandBoard.getInstance().clear(team);
            team.getClaims().clear();
            team.setHQ(null);
            team.flagForSave();
            for (final Player player : buttplug.fdsjfhkdsjfdsjhk().getServer().getOnlinePlayers()) {
                if (team.isMember(player)) {
                    player.sendMessage(ChatColor.YELLOW + sender.getName() + " has unclaimed all of your team's claims. (" + ChatColor.LIGHT_PURPLE + claims + " total" + ChatColor.YELLOW + ")");
                }
            }
            return;
        }
        if (LandBoard.getInstance().getClaim(sender.getLocation()) != null && team.ownsLocation(sender.getLocation())) {
            final Claim claim2 = LandBoard.getInstance().getClaim(sender.getLocation());
            final int refund = Claim.getPrice(claim2, team, false);
            team.setBalance(team.getBalance() + refund);
            team.getClaims().remove(claim2);
            team.flagForSave();
            LandBoard.getInstance().setTeamAt(claim2, null);
            for (final Player player : buttplug.fdsjfhkdsjfdsjhk().getServer().getOnlinePlayers()) {
                if (team.isMember(player)) {
                    player.sendMessage(ChatColor.YELLOW + sender.getName() + " has unclaimed " + ChatColor.LIGHT_PURPLE + claim2.getFriendlyName() + ChatColor.YELLOW + ".");
                }
            }
            if (team.getHq() != null && claim2.contains(team.getHq())) {
                team.setHQ(null);
                sender.sendMessage(ChatColor.RED + "Your HQ was in this claim, so it has been unset.");
            }
            return;
        }
        sender.sendMessage(ChatColor.RED + "You do not own this claim.");
        sender.sendMessage(ChatColor.RED + "To unclaim all claims, type " + ChatColor.YELLOW + "/team unclaim all" + ChatColor.RED + ".");
    }
}
