package net.frozenorb.foxtrot.team.commands.pvp;

import org.bukkit.entity.*;
import net.frozenorb.foxtrot.*;
import org.bukkit.*;
import net.frozenorb.foxtrot.command.annotations.*;

public class PvPSetLivesCommand
{
    @Command(names = { "pvptimer setlives", "timer setlives", "pvp setlives", "pvptimer setlives", "timer setlives", "pvp setlives" }, permissionNode = "op")
    public static void pvpSetLives(final Player sender, @Param(name = "player") final OfflinePlayer target, @Param(name = "Amount") final int amount) {
        buttplug.fdsjfhkdsjfdsjhk().getTransferableLivesMap().setLives(target.getName(), amount);
        sender.sendMessage(ChatColor.YELLOW + "Set " + ChatColor.GREEN + target.getName() + ChatColor.YELLOW + "'s life count to " + amount + ".");
    }
}
