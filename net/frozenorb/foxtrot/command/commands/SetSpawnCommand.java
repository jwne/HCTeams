package net.frozenorb.foxtrot.command.commands;

import org.bukkit.entity.*;
import net.frozenorb.foxtrot.*;
import org.bukkit.plugin.*;
import org.bukkit.*;
import org.bukkit.conversations.*;
import net.frozenorb.foxtrot.command.annotations.*;

public class SetSpawnCommand
{
    @Command(names = { "SetSpawn" }, permissionNode = "op")
    public static void setSpawn(final Player sender) {
        final ConversationFactory factory = new ConversationFactory((Plugin)buttplug.fdsjfhkdsjfdsjhk()).withModality(true).withPrefix((ConversationPrefix)new NullConversationPrefix()).withFirstPrompt((Prompt)new StringPrompt() {
            public String getPromptText(final ConversationContext context) {
                return "§aAre you sure you want to set spawn here? Type §byes§a to confirm or §cno§a to quit.";
            }
            
            public Prompt acceptInput(final ConversationContext cc, final String s) {
                if (s.equalsIgnoreCase("yes")) {
                    final Location l = ((Player)cc.getForWhom()).getLocation();
                    cc.getForWhom().sendRawMessage(ChatColor.GREEN + "Spawn set!");
                    ((Player)cc.getForWhom()).getWorld().setSpawnLocation(l.getBlockX(), l.getBlockY(), l.getBlockZ());
                    return Prompt.END_OF_CONVERSATION;
                }
                if (s.equalsIgnoreCase("no")) {
                    cc.getForWhom().sendRawMessage(ChatColor.GREEN + "Spawn setting cancelled.");
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
