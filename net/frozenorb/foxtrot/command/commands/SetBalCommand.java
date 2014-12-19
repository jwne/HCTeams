package net.frozenorb.foxtrot.command.commands;

import org.bukkit.entity.*;
import net.frozenorb.foxtrot.*;
import net.frozenorb.foxtrot.command.annotations.*;

public class SetBalCommand
{
    @Command(names = { "SetBal" }, permissionNode = "foxtrot.setbal")
    public static void setBal(final Player sender, @Param(name = "Target") final String target, @Param(name = "Amount") final int value) {
        buttplug.fdsjfhkdsjfdsjhk().getBalanceMap().setBalance(target, value);
    }
}
