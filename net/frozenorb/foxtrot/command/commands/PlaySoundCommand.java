package net.frozenorb.foxtrot.command.commands;

import org.bukkit.entity.*;
import org.bukkit.*;
import net.frozenorb.foxtrot.command.annotations.*;

public class PlaySoundCommand
{
    @Command(names = { "PlaySound" }, permissionNode = "op")
    public static void playSound(final Player sender, @Param(name = "Sound") final String sound, @Param(name = "Pitch") final float pitch) {
        try {
            final Sound soundObj = Sound.valueOf(sound);
            sender.playSound(sender.getLocation(), sound, 20.0f, pitch);
        }
        catch (Exception ex) {
            sender.sendMessage(ex.getMessage());
        }
    }
}
