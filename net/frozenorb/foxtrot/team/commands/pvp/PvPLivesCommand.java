package net.frozenorb.foxtrot.team.commands.pvp;

import org.bukkit.command.*;
import org.bukkit.*;
import net.frozenorb.foxtrot.*;
import net.frozenorb.foxtrot.command.annotations.*;

public class PvPLivesCommand
{
    @Command(names = { "pvptimer lives", "timer lives", "pvp lives" }, permissionNode = "")
    public static void pvpLives(final CommandSender sender, @Param(name = "Player", defaultValue = "self") final OfflinePlayer target) {
        sender.sendMessage(ChatColor.GOLD + target.getName() + "'s Lives: " + ChatColor.WHITE + buttplug.fdsjfhkdsjfdsjhk().getTransferableLivesMap().getLives(target.getName()));
    }
}
