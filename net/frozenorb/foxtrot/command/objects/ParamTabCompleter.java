package net.frozenorb.foxtrot.command.objects;

import org.bukkit.entity.*;
import java.util.*;

public abstract class ParamTabCompleter
{
    public abstract List<String> tabComplete(final Player p0, final String p1);
}
