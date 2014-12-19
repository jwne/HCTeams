package net.frozenorb.foxtrot.team.commands.team;

import org.bukkit.entity.*;
import net.frozenorb.foxtrot.team.*;
import net.frozenorb.foxtrot.*;
import org.bukkit.*;
import net.frozenorb.foxtrot.nametag.*;
import net.frozenorb.foxtrot.command.annotations.*;

public class TeamAcceptCommand
{
    @Command(names = { "team accept", "t accept", "f accept", "faction accept", "fac accept", "team a", "t a", "f a", "faction a", "fac a", "team join", "t join", "f join", "faction join", "fac join" }, permissionNode = "")
    public static void teamAccept(final Player sender, @Param(name = "team") final faggot target) {
        if (target.getInvitations().contains(sender.getName())) {
            if (buttplug.fdsjfhkdsjfdsjhk().getDsfjhkdsjhdsjkhfds().getPlayerTeam(sender.getName()) != null) {
                sender.sendMessage(ChatColor.RED + "You are already on a team!");
                return;
            }
            target.getInvitations().remove(sender.getName());
            target.addMember(sender.getName());
            buttplug.fdsjfhkdsjfdsjhk().getDsfjhkdsjhdsjkhfds().setTeam(sender.getName(), target);
            for (final Player player : buttplug.fdsjfhkdsjfdsjhk().getServer().getOnlinePlayers()) {
                if (target.isMember(player)) {
                    player.sendMessage(ChatColor.YELLOW + sender.getName() + " has joined the team!");
                }
            }
            NametagManager.reloadPlayer(sender);
            NametagManager.sendTeamsToPlayer(sender);
        }
        else {
            sender.sendMessage(ChatColor.RED + "This team has not invited you!");
        }
    }
}
