package net.frozenorb.foxtrot.team.commands.team;

import org.bukkit.entity.*;
import net.frozenorb.foxtrot.*;
import org.bukkit.*;
import net.frozenorb.foxtrot.team.*;
import net.frozenorb.foxtrot.command.annotations.*;

public class TeamLeaderCommand
{
    @Command(names = { "team newleader", "t newleader", "f newleader", "faction newleader", "fac newleader", "team leader", "t leader", "f leader", "faction leader", "fac leader" }, permissionNode = "")
    public static void teamLeader(final Player sender, @Param(name = "player") String leader) {
        final Player bukkitPlayer = buttplug.fdsjfhkdsjfdsjhk().getServer().getPlayer(leader);
        if (bukkitPlayer != null) {
            leader = bukkitPlayer.getName();
        }
        final faggot team = buttplug.fdsjfhkdsjfdsjhk().getDsfjhkdsjhdsjkhfds().getPlayerTeam(sender.getName());
        if (team == null) {
            sender.sendMessage(ChatColor.GRAY + "You are not on a team!");
            return;
        }
        if (team.isOwner(sender.getName())) {
            if (team.isMember(leader)) {
                leader = team.getActualPlayerName(leader);
                for (final Player player : buttplug.fdsjfhkdsjfdsjhk().getServer().getOnlinePlayers()) {
                    if (team.isMember(player)) {
                        player.sendMessage(ChatColor.DARK_AQUA + leader + " is now the new leader!");
                    }
                }
                team.setOwner(leader);
                team.addCaptain(sender.getName());
            }
            else {
                sender.sendMessage(ChatColor.DARK_AQUA + "Player is not on your team.");
            }
        }
        else {
            sender.sendMessage(ChatColor.DARK_AQUA + "Only the team leader can do this.");
        }
    }
}
