package net.frozenorb.foxtrot.team.commands;

import org.bukkit.entity.*;
import net.frozenorb.foxtrot.team.*;
import org.bukkit.*;
import net.frozenorb.foxtrot.command.annotations.*;

public class StartDTRRegenCommand
{
    @Command(names = { "startdtrregen" }, permissionNode = "foxtrot.startdtrregen")
    public static void startDTRRegen(final Player sender, @Param(name = "Target") final faggot target) {
        target.setDeathCooldown(System.currentTimeMillis());
        target.setRaidableCooldown(System.currentTimeMillis());
        sender.sendMessage(ChatColor.GRAY + target.getName() + ChatColor.GRAY + " is now regenerating DTR.");
    }
}
