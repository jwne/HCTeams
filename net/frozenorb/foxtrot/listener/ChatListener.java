package net.frozenorb.foxtrot.listener;

import org.bukkit.event.player.*;
import net.frozenorb.foxtrot.*;
import org.bukkit.*;
import net.frozenorb.foxtrot.team.chat.*;
import net.frozenorb.foxtrot.team.commands.team.*;
import net.frozenorb.foxtrot.team.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;

public class ChatListener implements Listener
{
    @EventHandler(priority = EventPriority.MONITOR)
    public void onAsyncPlayerChat(final AsyncPlayerChatEvent event) {
        final faggot team = buttplug.fdsjfhkdsjfdsjhk().getDsfjhkdsjhdsjkhfds().getPlayerTeam(event.getPlayer().getName());
        final String highRollerString = buttplug.fdsjfhkdsjfdsjhk().getServerHandler().getHighRollers().contains(event.getPlayer().getName()) ? (ChatColor.DARK_PURPLE + "[HighRoller]") : "";
        ChatMode chatMode = buttplug.fdsjfhkdsjfdsjhk().getChatModeMap().getChatMode(event.getPlayer().getName());
        final boolean doTeamChat = event.getMessage().startsWith("@");
        final boolean doGlobalChat = event.getMessage().startsWith("!");
        if (doTeamChat || doGlobalChat) {
            event.setMessage(event.getMessage().substring(1));
        }
        if (doGlobalChat || team == null) {
            chatMode = ChatMode.PUBLIC;
        }
        else if (doTeamChat) {
            chatMode = ChatMode.TEAM;
        }
        switch (chatMode) {
            case PUBLIC: {
                if (event.isCancelled()) {
                    return;
                }
                if (TeamMuteCommand.factionMutes.containsKey(event.getPlayer().getName())) {
                    event.getPlayer().sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "Your faction is muted!");
                    event.setCancelled(true);
                    return;
                }
                if (team == null) {
                    event.setFormat(ChatColor.GOLD + "[" + ChatColor.YELLOW + "-" + ChatColor.GOLD + "]" + highRollerString + ChatColor.WHITE + "%s" + ChatColor.WHITE + ": %s");
                    final String finalMessage = String.format(event.getFormat(), event.getPlayer().getDisplayName(), event.getMessage());
                    for (final Player player : buttplug.fdsjfhkdsjfdsjhk().getServer().getOnlinePlayers()) {
                        if (event.getPlayer().isOp() || buttplug.fdsjfhkdsjfdsjhk().getToggleGlobalChatMap().isGlobalChatToggled(player.getName())) {
                            player.sendMessage(finalMessage);
                        }
                    }
                    event.setCancelled(true);
                    buttplug.fdsjfhkdsjfdsjhk().getServer().getConsoleSender().sendMessage(finalMessage);
                    break;
                }
                event.setFormat(ChatColor.GOLD + "[" + ChatColor.YELLOW + team.getName() + ChatColor.GOLD + "]" + highRollerString + ChatColor.WHITE + "%s" + ChatColor.WHITE + ": %s");
                final String finalMessage = String.format(event.getFormat(), event.getPlayer().getDisplayName(), event.getMessage());
                for (final Player player : buttplug.fdsjfhkdsjfdsjhk().getServer().getOnlinePlayers()) {
                    if (team.isMember(player)) {
                        player.sendMessage(finalMessage.replace(ChatColor.GOLD + "[" + ChatColor.YELLOW, ChatColor.GOLD + "[" + ChatColor.DARK_GREEN));
                    }
                    else if (team.isAlly(player)) {
                        player.sendMessage(finalMessage.replace(ChatColor.GOLD + "[" + ChatColor.YELLOW, ChatColor.GOLD + "[" + ChatColor.LIGHT_PURPLE));
                    }
                    else if (event.getPlayer().isOp() || buttplug.fdsjfhkdsjfdsjhk().getToggleGlobalChatMap().isGlobalChatToggled(player.getName())) {
                        player.sendMessage(finalMessage);
                    }
                }
                event.setCancelled(true);
                buttplug.fdsjfhkdsjfdsjhk().getServer().getConsoleSender().sendMessage(finalMessage);
                break;
            }
            case TEAM: {
                for (final Player player2 : buttplug.fdsjfhkdsjfdsjhk().getServer().getOnlinePlayers()) {
                    if (team.isMember(player2)) {
                        player2.sendMessage(ChatColor.DARK_AQUA + "(Team) " + event.getPlayer().getName() + ": " + ChatColor.YELLOW + event.getMessage());
                    }
                }
                buttplug.fdsjfhkdsjfdsjhk().getServer().getLogger().info("[Team Chat] [" + team.getName() + "] " + event.getPlayer().getName() + ": " + event.getMessage());
                event.setCancelled(true);
                break;
            }
        }
    }
}
