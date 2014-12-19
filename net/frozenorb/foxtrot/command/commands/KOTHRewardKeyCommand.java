package net.frozenorb.foxtrot.command.commands;

import org.bukkit.entity.*;
import org.bukkit.*;
import net.frozenorb.foxtrot.util.*;
import net.frozenorb.foxtrot.command.annotations.*;

public class KOTHRewardKeyCommand
{
    @Command(names = { "kothrewardkey" }, permissionNode = "op")
    public static void kothRewardKey(final Player sender, @Param(name = "KOTH") final String koth, @Param(name = "Tier") final int tier) {
        if (sender.getGameMode() != GameMode.CREATIVE) {
            sender.sendMessage(ChatColor.RED + "This command must be ran in creative.");
            return;
        }
        sender.setItemInHand(InvUtils.generateKOTHRewardKey(koth, tier));
        sender.sendMessage(ChatColor.YELLOW + "Gave you a KOTH reward key.");
    }
}
