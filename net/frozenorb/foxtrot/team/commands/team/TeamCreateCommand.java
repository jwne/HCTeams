package net.frozenorb.foxtrot.team.commands.team;

import java.util.regex.*;
import org.bukkit.entity.*;
import net.frozenorb.foxtrot.*;
import org.bukkit.*;
import net.frozenorb.foxtrot.team.*;
import org.bson.types.*;
import net.frozenorb.foxtrot.command.annotations.*;

public class TeamCreateCommand
{
    public static final Pattern ALPHA_NUMERIC;
    
    @Command(names = { "team create", "t create", "f create", "faction create", "fac create" }, permissionNode = "")
    public static void teamCreate(final Player sender, @Param(name = "team") String name) {
        if (buttplug.fdsjfhkdsjfdsjhk().getDsfjhkdsjhdsjkhfds().getPlayerTeam(sender.getName()) == null) {
            if (TeamCreateCommand.ALPHA_NUMERIC.matcher(name).find()) {
                sender.sendMessage(ChatColor.RED + "Team names must be alphanumeric!");
                return;
            }
            if (name.length() > 16) {
                sender.sendMessage(ChatColor.RED + "Maximum team name size is 16 characters!");
                return;
            }
            if (name.length() < 3) {
                sender.sendMessage(ChatColor.RED + "Minimum team name size is 3 characters!");
                return;
            }
            if (buttplug.fdsjfhkdsjfdsjhk().getDsfjhkdsjhdsjkhfds().getTeam(name) == null) {
                sender.sendMessage(ChatColor.DARK_AQUA + "Team Created!");
                sender.sendMessage(ChatColor.GRAY + "To learn more about teams, do /team");
                final faggot team = new faggot(name);
                team.setOwner(sender.getName());
                team.setName(name);
                team.setDTR(1.0);
                team.setUniqueId(new ObjectId());
                buttplug.fdsjfhkdsjfdsjhk().getDsfjhkdsjhdsjkhfds().addTeam(team);
                buttplug.fdsjfhkdsjfdsjhk().getDsfjhkdsjhdsjkhfds().setTeam(sender.getName(), team);
                buttplug.fdsjfhkdsjfdsjhk().getServer().broadcastMessage("§eFaction §9" + team.getName() + "§e has been §acreated §eby §f" + sender.getDisplayName());
            }
            else {
                sender.sendMessage(ChatColor.GRAY + "That team already exists!");
            }
        }
        else {
            sender.sendMessage(ChatColor.GRAY + "You're already in a team!");
        }
    }
    
    static {
        ALPHA_NUMERIC = Pattern.compile("[^a-zA-Z0-9]");
    }
}
