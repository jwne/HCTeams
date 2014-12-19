package net.frozenorb.foxtrot.team.commands.pvp;

import org.bukkit.command.*;
import net.frozenorb.foxtrot.*;
import org.bukkit.*;
import net.frozenorb.foxtrot.command.annotations.*;

public class PvPAddLivesCommand
{
    @Command(names = { "pvptimer addlives", "timer addlives", "pvp addlives", "pvptimer addlives", "timer addlives", "pvp addlives" }, permissionNode = "op")
    public static void pvpSetLives(final CommandSender sender, @Param(name = "player") final OfflinePlayer target, @Param(name = "Amount") final int amount) {
        buttplug.fdsjfhkdsjfdsjhk().getTransferableLivesMap().setLives(target.getName(), buttplug.fdsjfhkdsjfdsjhk().getTransferableLivesMap().getLives(target.getName()) + amount);
        sender.sendMessage(ChatColor.YELLOW + "Gave " + ChatColor.GREEN + target.getName() + ChatColor.YELLOW + " " + amount + " lives.");
    }
}
