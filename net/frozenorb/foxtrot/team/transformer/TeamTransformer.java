package net.frozenorb.foxtrot.team.transformer;

import net.frozenorb.foxtrot.command.objects.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import net.frozenorb.foxtrot.*;
import org.bukkit.*;
import net.frozenorb.foxtrot.team.*;

public class TeamTransformer extends ParamTransformer
{
    @Override
    public Object transform(final CommandSender sender, String source) {
        if (sender instanceof Player && (source.equalsIgnoreCase("self") || source.equals(""))) {
            final faggot team = buttplug.fdsjfhkdsjfdsjhk().getDsfjhkdsjhdsjkhfds().getPlayerTeam(sender.getName());
            if (team == null) {
                sender.sendMessage(ChatColor.GRAY + "You're not on a team!");
            }
            return team;
        }
        faggot team = buttplug.fdsjfhkdsjfdsjhk().getDsfjhkdsjhdsjkhfds().getTeam(source);
        if (team == null) {
            final Player bukkitPlayer = buttplug.fdsjfhkdsjfdsjhk().getServer().getPlayer(source);
            if (bukkitPlayer != null) {
                source = bukkitPlayer.getName();
            }
            team = buttplug.fdsjfhkdsjfdsjhk().getDsfjhkdsjhdsjkhfds().getPlayerTeam(source);
            if (team == null) {
                sender.sendMessage(ChatColor.RED + "No team with the name or member " + source + " found.");
                return null;
            }
        }
        return team;
    }
}
