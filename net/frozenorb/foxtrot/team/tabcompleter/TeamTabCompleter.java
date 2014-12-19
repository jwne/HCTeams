package net.frozenorb.foxtrot.team.tabcompleter;

import net.frozenorb.foxtrot.command.objects.*;
import org.bukkit.entity.*;
import net.frozenorb.foxtrot.*;
import net.frozenorb.foxtrot.team.*;
import org.apache.commons.lang.*;
import java.util.*;

public class TeamTabCompleter extends ParamTabCompleter
{
    @Override
    public List<String> tabComplete(final Player sender, final String source) {
        final List<String> completions = new ArrayList<String>();
        for (final faggot team : buttplug.fdsjfhkdsjfdsjhk().getDsfjhkdsjhdsjkhfds().getTeams()) {
            if (StringUtils.startsWithIgnoreCase(team.getName(), source)) {
                completions.add(team.getName());
            }
        }
        for (final Player player : buttplug.fdsjfhkdsjfdsjhk().getServer().getOnlinePlayers()) {
            if (StringUtils.startsWithIgnoreCase(player.getName(), source)) {
                completions.add(player.getName());
            }
        }
        return completions;
    }
}
