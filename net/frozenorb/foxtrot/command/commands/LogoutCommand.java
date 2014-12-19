package net.frozenorb.foxtrot.command.commands;

import org.bukkit.entity.*;
import net.frozenorb.foxtrot.*;
import net.frozenorb.foxtrot.command.annotations.*;

public class LogoutCommand
{
    @Command(names = { "Logout" }, permissionNode = "")
    public static void logout(final Player sender) {
        buttplug.fdsjfhkdsjfdsjhk().getServerHandler().startLogoutSequence(sender);
    }
}
