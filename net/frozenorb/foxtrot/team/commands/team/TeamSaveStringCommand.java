package net.frozenorb.foxtrot.team.commands.team;

import org.bukkit.entity.*;
import net.frozenorb.foxtrot.team.*;
import org.bukkit.*;
import net.frozenorb.foxtrot.command.annotations.*;

public class TeamSaveStringCommand
{
    @Command(names = { "team savestring", "t savestring", "f savestring", "faction savestring", "fac savestring" }, permissionNode = "op")
    public static void teamSaveString(final Player sender, @Param(name = "team", defaultValue = "self") final faggot target) {
        final String saveString = target.saveString(false);
        sender.sendMessage(ChatColor.BLUE.toString() + ChatColor.UNDERLINE + "Save String (" + target.getName() + ")");
        sender.sendMessage("");
        for (final String line : saveString.split("\n")) {
            sender.sendMessage(ChatColor.BLUE + line.substring(0, line.indexOf(":")) + ": " + ChatColor.YELLOW + line.substring(line.indexOf(":") + 1).replace(",", ChatColor.BLUE + "," + ChatColor.YELLOW).replace(":", ChatColor.BLUE + ":" + ChatColor.YELLOW));
        }
    }
}
