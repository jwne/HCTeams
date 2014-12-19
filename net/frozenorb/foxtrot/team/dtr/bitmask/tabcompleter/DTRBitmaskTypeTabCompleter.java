package net.frozenorb.foxtrot.team.dtr.bitmask.tabcompleter;

import net.frozenorb.foxtrot.command.objects.*;
import org.bukkit.entity.*;
import java.util.*;
import net.frozenorb.foxtrot.team.dtr.bitmask.*;
import org.apache.commons.lang.*;

public class DTRBitmaskTypeTabCompleter extends ParamTabCompleter
{
    @Override
    public List<String> tabComplete(final Player sender, final String source) {
        final List<String> completions = new ArrayList<String>();
        for (final DTRBitmaskType bitmaskType : DTRBitmaskType.values()) {
            if (StringUtils.startsWithIgnoreCase(bitmaskType.getName(), source)) {
                completions.add(bitmaskType.getName());
            }
        }
        return completions;
    }
}
