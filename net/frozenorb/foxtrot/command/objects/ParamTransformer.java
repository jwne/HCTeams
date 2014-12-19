package net.frozenorb.foxtrot.command.objects;

import org.bukkit.command.*;

public abstract class ParamTransformer<T>
{
    public abstract T transform(final CommandSender p0, final String p1);
}
