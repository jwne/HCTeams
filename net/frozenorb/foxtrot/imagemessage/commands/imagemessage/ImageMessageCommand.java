package net.frozenorb.foxtrot.imagemessage.commands.imagemessage;

import org.bukkit.entity.*;
import org.bukkit.*;
import java.io.*;
import net.frozenorb.foxtrot.command.annotations.*;
import net.frozenorb.foxtrot.imagemessage.*;
import java.util.*;

public class ImageMessageCommand
{
    @Command(names = { "ImageMessage List", "IM List" }, permissionNode = "foxtrot.imagemessage")
    public static void imageMessageList(final Player sender) {
        sender.sendMessage(ChatColor.GREEN + "Viewing available image message types...");
        for (final File file : new File("ascii-art").listFiles()) {
            sender.sendMessage(ChatColor.YELLOW + "- " + file.getName().split("\\.")[0]);
        }
    }
    
    @Command(names = { "ImageMessage Broadcast", "IM Broadcast", "ImageMessage BC", "IM BC" }, permissionNode = "foxtrot.imagemessage")
    public static void imageMessageBroadcast(final Player sender, @Param(name = "Image") final String image, @Param(name = "Message", wildcard = true) final String message) {
        final List<String> messages = new ArrayList<String>();
        messages.add(" ");
        messages.add(" ");
        for (final String messageSplit : message.split("\\|")) {
            messages.add(messageSplit);
        }
        new ImageMessage(image).appendText((String[])messages.toArray(new String[messages.size()])).broadcast();
    }
}
