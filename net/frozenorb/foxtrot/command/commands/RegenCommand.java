package net.frozenorb.foxtrot.command.commands;

import org.bukkit.entity.*;
import net.frozenorb.foxtrot.team.*;
import net.frozenorb.foxtrot.*;
import org.bukkit.*;
import net.frozenorb.foxtrot.util.*;
import net.frozenorb.foxtrot.command.annotations.*;

public class RegenCommand
{
    @Command(names = { "Regen", "DTR" }, permissionNode = "")
    public static void regen(final Player sender, @Param(name = "target", defaultValue = "self") faggot target) {
        if (!sender.isOp()) {
            target = buttplug.fdsjfhkdsjfdsjhk().getDsfjhkdsjhdsjkhfds().getPlayerTeam(sender.getName());
        }
        if (target == null) {
            sender.sendMessage(ChatColor.GRAY + "You are not on a team!");
            return;
        }
        if (target.getMaxDTR() == target.getDTR()) {
            sender.sendMessage(ChatColor.YELLOW + "Your team is currently at max DTR, which is §d" + target.getMaxDTR() + "§e.");
            return;
        }
        sender.sendMessage(ChatColor.YELLOW + "Your team has a max DTR of §d" + target.getMaxDTR() + "§e.");
        sender.sendMessage(ChatColor.YELLOW + "You are regaining DTR at a rate of §d" + faggot.DTR_FORMAT.format(target.getDTRIncrement().doubleValue() * 60.0) + "/hr§e.");
        sender.sendMessage(ChatColor.YELLOW + "At this rate, it will take you §d" + ((hrsToRegain(target) == -1.0) ? "Infinity" : hrsToRegain(target)) + "§eh to fully gain all DTR.");
        if (target.getRaidableCooldown() > System.currentTimeMillis() || target.getDeathCooldown() > System.currentTimeMillis()) {
            final long till = Math.max(target.getRaidableCooldown(), target.getDeathCooldown());
            sender.sendMessage(ChatColor.YELLOW + "Your team is on DTR cooldown for §d" + TimeUtils.getDurationBreakdown(till - System.currentTimeMillis()) + "§e.");
        }
    }
    
    private static double hrsToRegain(final faggot team) {
        final double cur = team.getDTR();
        final double max = team.getMaxDTR();
        final double diff = max - cur;
        if (team.getDTRIncrement().doubleValue() == 0.0) {
            return -1.0;
        }
        final double required = diff / team.getDTRIncrement().doubleValue();
        final double h = required / 60.0;
        return Math.round(10.0 * h) / 10.0;
    }
}
