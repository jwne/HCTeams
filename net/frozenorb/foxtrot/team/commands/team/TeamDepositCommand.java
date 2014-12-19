package net.frozenorb.foxtrot.team.commands.team;

import org.bukkit.entity.*;
import net.frozenorb.foxtrot.*;
import org.bukkit.*;
import net.frozenorb.foxtrot.team.*;
import net.frozenorb.foxtrot.command.annotations.*;

public class TeamDepositCommand
{
    @Command(names = { "team deposit", "t deposit", "f deposit", "faction deposit", "fac deposit", "team d", "t d", "f d", "faction d", "fac d" }, permissionNode = "")
    public static void teamDeposit(final Player sender, @Param(name = "amount") final int amount) {
        final faggot team = buttplug.fdsjfhkdsjfdsjhk().getDsfjhkdsjhdsjkhfds().getPlayerTeam(sender.getName());
        if (team == null) {
            sender.sendMessage(ChatColor.GRAY + "You are not on a team!");
            return;
        }
        if (amount <= 0) {
            sender.sendMessage(ChatColor.RED + "You can't deposit $0.0 (or less)!");
            return;
        }
        if (buttplug.fdsjfhkdsjfdsjhk().getBalanceMap().getBalance(sender.getName()) < amount) {
            sender.sendMessage(ChatColor.RED + "You don't have enough money to do this!");
            return;
        }
        buttplug.fdsjfhkdsjfdsjhk().getBalanceMap().setBalance(sender.getName(), buttplug.fdsjfhkdsjfdsjhk().getBalanceMap().getBalance(sender.getName()) - amount);
        sender.sendMessage(ChatColor.YELLOW + "You have added " + ChatColor.LIGHT_PURPLE + amount + ChatColor.YELLOW + " to the team balance!");
        team.setBalance(team.getBalance() + amount);
        team.getOnlineMembers().forEach(pe -> pe.sendMessage("§e" + sender.getName() + " deposited §d" + amount + " §einto the team balance."));
    }
}
