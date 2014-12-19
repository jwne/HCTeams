package net.frozenorb.foxtrot.team.commands;

import org.bukkit.entity.*;
import net.frozenorb.foxtrot.team.*;
import org.bukkit.*;
import net.frozenorb.foxtrot.command.annotations.*;

public class SetDTRCommand
{
    @Command(names = { "SetDTR" }, permissionNode = "foxtrot.setdtr")
    public static void setDTR(final Player sender, @Param(name = "Target") final faggot target, @Param(name = "DTR") final float value) {
        target.setDTR(value);
        sender.sendMessage(ChatColor.YELLOW + target.getName() + " has a new DTR of: " + value);
    }
}
