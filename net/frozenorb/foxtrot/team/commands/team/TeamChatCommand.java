package net.frozenorb.foxtrot.team.commands.team;

import org.bukkit.entity.*;
import net.frozenorb.foxtrot.*;
import org.bukkit.*;
import net.frozenorb.foxtrot.team.chat.*;
import net.frozenorb.foxtrot.command.annotations.*;

public class TeamChatCommand
{
    @Command(names = { "team chat", "t chat", "f chat", "faction chat", "fac chat", "team c", "t c", "f c", "faction c", "fac c", "mc" }, permissionNode = "")
    public static void teamChat(final Player sender, @Param(name = "chat mode", defaultValue = "toggle") final String params) {
        if (buttplug.fdsjfhkdsjfdsjhk().getDsfjhkdsjhdsjkhfds().getPlayerTeam(sender.getName()) == null) {
            sender.sendMessage(ChatColor.GRAY + "You're not in a team!");
            return;
        }
        ChatMode chatMode = null;
        if (params.equalsIgnoreCase("t") || params.equalsIgnoreCase("team") || params.equalsIgnoreCase("f") || params.equalsIgnoreCase("fac") || params.equalsIgnoreCase("faction") || params.equalsIgnoreCase("fc")) {
            chatMode = ChatMode.TEAM;
        }
        else if (params.equalsIgnoreCase("g") || params.equalsIgnoreCase("p") || params.equalsIgnoreCase("global") || params.equalsIgnoreCase("public") || params.equalsIgnoreCase("gc")) {
            chatMode = ChatMode.PUBLIC;
        }
        setChat(sender, chatMode);
    }
    
    private static void setChat(final Player player, ChatMode chatMode) {
        if (chatMode != null) {
            switch (chatMode) {
                case PUBLIC: {
                    player.sendMessage(ChatColor.DARK_AQUA + "You are now in public chat.");
                    break;
                }
                case TEAM: {
                    player.sendMessage(ChatColor.DARK_AQUA + "You are now in team chat.");
                    break;
                }
            }
            buttplug.fdsjfhkdsjfdsjhk().getChatModeMap().setChatMode(player.getName(), chatMode);
        }
        else {
            chatMode = buttplug.fdsjfhkdsjfdsjhk().getChatModeMap().getChatMode(player.getName());
            switch (chatMode) {
                case PUBLIC: {
                    setChat(player, ChatMode.TEAM);
                    break;
                }
                case TEAM: {
                    setChat(player, ChatMode.PUBLIC);
                    break;
                }
            }
        }
    }
}
