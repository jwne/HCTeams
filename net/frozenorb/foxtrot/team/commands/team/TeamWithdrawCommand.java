package net.frozenorb.foxtrot.team.commands.team;

import org.bukkit.entity.*;
import net.frozenorb.foxtrot.*;
import org.bukkit.*;
import net.frozenorb.foxtrot.team.*;
import net.frozenorb.foxtrot.command.annotations.*;

public class TeamWithdrawCommand
{
    @Command(names = { "team withdraw", "t withdraw", "f withdraw", "faction withdraw", "fac withdraw", "team w", "t w", "f w", "faction w", "fac w" }, permissionNode = "")
    public static void teamInvite(final Player sender, @Param(name = "amount") final int amount) {
        final faggot team = buttplug.fdsjfhkdsjfdsjhk().getDsfjhkdsjhdsjkhfds().getPlayerTeam(sender.getName());
        if (team == null) {
            sender.sendMessage(ChatColor.GRAY + "You are not on a team!");
            return;
        }
        if (team.isCaptain(sender.getName()) || team.isOwner(sender.getName())) {
            if (team.getBalance() < amount) {
                sender.sendMessage(ChatColor.RED + "The team doesn't have enough money to do this!");
                return;
            }
            if (amount <= 0) {
                sender.sendMessage(ChatColor.RED + "You can't withdraw $0.0 (or less)!");
                return;
            }
            buttplug.fdsjfhkdsjfdsjhk().getBalanceMap().setBalance(sender.getName(), buttplug.fdsjfhkdsjfdsjhk().getBalanceMap().getBalance(sender.getName()) + amount);
            sender.sendMessage(ChatColor.YELLOW + "You have withdrawn §d" + amount + "§e from the team balance!");
            team.setBalance(team.getBalance() - amount);
            team.getOnlineMembers().forEach(pe -> pe.sendMessage("§e" + sender.getName() + " withdrew §d" + amount + " §efrom the team balance."));
        }
        else {
            sender.sendMessage(ChatColor.DARK_AQUA + "Only team captains can do this.");
        }
    }
}
