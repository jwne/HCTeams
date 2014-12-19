package net.frozenorb.foxtrot.command.commands;

import org.bukkit.entity.*;
import net.frozenorb.foxtrot.*;
import org.bukkit.*;
import net.frozenorb.foxtrot.command.annotations.*;

public class ToggleChatCommand
{
    @Command(names = { "ToggleChat", "ToggleGlobalChat", "TC", "TGC" }, permissionNode = "")
    public static void toggleChat(final Player sender) {
        final boolean val = !buttplug.fdsjfhkdsjfdsjhk().getToggleGlobalChatMap().isGlobalChatToggled(sender.getName());
        sender.sendMessage(ChatColor.YELLOW + "You are now " + (val ? (ChatColor.GREEN + "able") : (ChatColor.RED + "unable")) + ChatColor.YELLOW + " to see global chat!");
        buttplug.fdsjfhkdsjfdsjhk().getToggleGlobalChatMap().setGlobalChatToggled(sender.getName(), val);
    }
}
