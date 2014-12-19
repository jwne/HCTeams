package net.frozenorb.foxtrot.command.commands;

import org.bukkit.command.*;
import net.frozenorb.foxtrot.*;
import java.io.*;
import java.util.*;
import org.bukkit.*;
import net.frozenorb.foxtrot.command.annotations.*;

public class ReviveCommand
{
    @Command(names = { "Revive" }, permissionNode = "op")
    public static void playSound(final CommandSender sender, @Param(name = "Target") final String target, @Param(name = "Reason", wildcard = true) final String reason) {
        if (buttplug.fdsjfhkdsjfdsjhk().getDeathbanMap().isDeathbanned(target)) {
            final File logTo = new File("adminrevives.log");
            try {
                logTo.createNewFile();
                final BufferedWriter output = new BufferedWriter(new FileWriter(logTo, true));
                output.append((CharSequence)"[").append((CharSequence)new Date().toString()).append((CharSequence)"] ").append((CharSequence)(sender.getName() + " revived " + target + " for " + reason)).append((CharSequence)"\n");
                output.close();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            buttplug.fdsjfhkdsjfdsjhk().getDeathbanMap().revive(target);
            sender.sendMessage(ChatColor.GREEN + "Revived " + target + "!");
        }
        else {
            sender.sendMessage(ChatColor.RED + "That player is not deathbanned!");
        }
    }
}
