package net.frozenorb.foxtrot.team.commands.pvp;

import org.bukkit.entity.*;
import net.frozenorb.foxtrot.*;
import org.bukkit.*;
import net.frozenorb.foxtrot.command.annotations.*;

public class PvPReviveCommand
{
    @Command(names = { "pvptimer revive", "timer revive", "pvp revive", "pvptimer revive", "timer revive", "pvp revive", "f revive" }, permissionNode = "")
    public static void pvpRevive(final Player sender, @Param(name = "player") final OfflinePlayer target) {
        final int transferableLives = buttplug.fdsjfhkdsjfdsjhk().getTransferableLivesMap().getLives(sender.getName());
        if (buttplug.fdsjfhkdsjfdsjhk().getServerHandler().isPreEOTW()) {
            sender.sendMessage(ChatColor.RED + "The server is in EOTW Mode: Lives cannot be used.");
            return;
        }
        if (transferableLives == 0) {
            sender.sendMessage(ChatColor.RED + "You have no lives which can be used to revive other players!");
            return;
        }
        if (!buttplug.fdsjfhkdsjfdsjhk().getDeathbanMap().isDeathbanned(target.getName())) {
            sender.sendMessage(ChatColor.RED + "That player is not deathbanned!");
            return;
        }
        buttplug.fdsjfhkdsjfdsjhk().getTransferableLivesMap().setLives(sender.getName(), transferableLives - 1);
        sender.sendMessage(ChatColor.YELLOW + "You have revived " + ChatColor.GREEN + target.getName() + ChatColor.YELLOW + " with a life!");
        buttplug.fdsjfhkdsjfdsjhk().getDeathbanMap().revive(target.getName());
    }
}
