package net.frozenorb.foxtrot.team.commands.pvp;

import org.bukkit.entity.*;
import net.frozenorb.foxtrot.*;
import org.bukkit.*;
import net.frozenorb.foxtrot.command.annotations.*;

public class PvPGiveLivesCommand
{
    @Command(names = { "pvptimer givelives", "timer givelives", "pvp givelives", "pvptimer givelife", "timer givelife", "pvp givelife" }, permissionNode = "")
    public static void pvpGiveLives(final Player sender, @Param(name = "Player") final Player target, @Param(name = "Amount") final int amount) {
        final int transferableLives = buttplug.fdsjfhkdsjfdsjhk().getTransferableLivesMap().getLives(sender.getName());
        if (transferableLives < amount) {
            sender.sendMessage(ChatColor.RED + "You do not have that many lives which can be given to other players!");
            return;
        }
        if (amount <= 0) {
            sender.sendMessage(ChatColor.RED + "You can only give a positive number of lives!");
            return;
        }
        buttplug.fdsjfhkdsjfdsjhk().getTransferableLivesMap().setLives(sender.getName(), transferableLives - amount);
        buttplug.fdsjfhkdsjfdsjhk().getTransferableLivesMap().setLives(target.getName(), buttplug.fdsjfhkdsjfdsjhk().getTransferableLivesMap().getLives(target.getName()) + amount);
        sender.sendMessage(ChatColor.YELLOW + "Gave " + amount + " " + ((amount == 1) ? "life" : "lives") + " to " + ChatColor.BLUE + target.getName() + ChatColor.YELLOW + ".");
        target.sendMessage(ChatColor.YELLOW + "Received " + amount + " " + ((amount == 1) ? "life" : "lives") + " from " + ChatColor.BLUE + sender.getName() + ChatColor.YELLOW + ".");
    }
}
