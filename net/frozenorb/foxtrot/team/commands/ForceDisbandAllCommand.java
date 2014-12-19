package net.frozenorb.foxtrot.team.commands;

import org.bukkit.entity.*;
import net.frozenorb.foxtrot.*;
import org.bukkit.plugin.*;
import net.frozenorb.foxtrot.team.*;
import org.bukkit.*;
import java.util.*;
import org.bukkit.conversations.*;
import net.frozenorb.foxtrot.command.annotations.*;

public class ForceDisbandAllCommand
{
    @Command(names = { "forcedisbandall" }, permissionNode = "op")
    public static void forceDisbandAll(final Player sender) {
        final ConversationFactory factory = new ConversationFactory((Plugin)buttplug.fdsjfhkdsjfdsjhk()).withModality(true).withPrefix((ConversationPrefix)new NullConversationPrefix()).withFirstPrompt((Prompt)new StringPrompt() {
            public String getPromptText(final ConversationContext context) {
                return "§aAre you sure you want to disband all factions? Type §byes§a to confirm or §cno§a to quit.";
            }
            
            public Prompt acceptInput(final ConversationContext cc, final String s) {
                if (s.equalsIgnoreCase("yes")) {
                    final List<faggot> teams = new ArrayList<faggot>();
                    for (final faggot team : buttplug.fdsjfhkdsjfdsjhk().getDsfjhkdsjhdsjkhfds().getTeams()) {
                        teams.add(team);
                    }
                    for (final faggot team : teams) {
                        team.disband();
                    }
                    buttplug.fdsjfhkdsjfdsjhk().getDsfjhkdsjhdsjkhfds().getTeamNameMap().clear();
                    buttplug.fdsjfhkdsjfdsjhk().getDsfjhkdsjhdsjkhfds().getTeamUniqueIdMap().clear();
                    buttplug.fdsjfhkdsjfdsjhk().getDsfjhkdsjhdsjkhfds().getPlayerTeamMap().clear();
                    buttplug.fdsjfhkdsjfdsjhk().getServer().broadcastMessage(ChatColor.RED.toString() + ChatColor.BOLD + "All factions have been forcibly disbanded!");
                    return Prompt.END_OF_CONVERSATION;
                }
                if (s.equalsIgnoreCase("no")) {
                    cc.getForWhom().sendRawMessage(ChatColor.GREEN + "Disbanding cancelled.");
                    return Prompt.END_OF_CONVERSATION;
                }
                cc.getForWhom().sendRawMessage(ChatColor.GREEN + "Unrecognized response. Type §b/yes§a to confirm or §c/no§a to quit.");
                return Prompt.END_OF_CONVERSATION;
            }
        }).withEscapeSequence("/no").withTimeout(10).thatExcludesNonPlayersWithMessage("Go away evil console!");
        final Conversation con = factory.buildConversation((Conversable)sender);
        sender.beginConversation(con);
    }
}
