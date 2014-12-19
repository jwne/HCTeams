package net.frozenorb.foxtrot.team.commands.pvp;

import org.bukkit.entity.*;
import net.frozenorb.foxtrot.command.annotations.*;

public class faggotCommand
{
    @Command(names = { "pvptimer", "timer", "pvp" }, permissionNode = "")
    public static void pvpTimer(final Player sender) {
        final String[] msges = { "§c/pvp lives [target] - Shows amount of lives that a player has", "§c/pvp revive <player> - Revives targeted player", "§c/pvp time - Shows time left on PVP Timer", "§c/pvp enable - Remove PVP Timer" };
        sender.sendMessage(msges);
    }
}
