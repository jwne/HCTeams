package net.frozenorb.foxtrot.command.objects;

import org.spigotmc.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.*;
import net.frozenorb.foxtrot.command.*;
import java.util.*;

public class TwixCommandMap extends SimpleCommandMap
{
    private CustomTimingsHandler foxTabComplete;
    
    public TwixCommandMap(final Server server) {
        super(server);
        this.foxTabComplete = new CustomTimingsHandler("Foxtrot - CH Command Tab Complete");
    }
    
    public List<String> tabComplete(final CommandSender sender, final String cmdLine) {
        this.foxTabComplete.startTiming();
        try {
            if (cmdLine.equals("/") || cmdLine.equals("/ ")) {
                return null;
            }
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "Tab completion only works from in-game. Sorry!");
                return null;
            }
            final Player player = (Player)sender;
            final int spaceIndex = cmdLine.indexOf(32);
            final Set<String> completions = new HashSet<String>();
        Label_0525:
            for (final CommandData command : CommandHandler.getCommands()) {
                if (!command.canAccess((CommandSender)player)) {
                    continue;
                }
                for (final String alias : command.getNames()) {
                    String split = alias.split(" ")[0];
                    if (spaceIndex != -1) {
                        split = alias;
                    }
                    if (split.toLowerCase().startsWith(cmdLine) || (split + " ").toLowerCase().startsWith(cmdLine)) {
                        if (spaceIndex == -1 && cmdLine.length() < alias.length()) {
                            completions.add("/" + split.toLowerCase());
                        }
                        else {
                            if (cmdLine.toLowerCase().startsWith(split.toLowerCase()) && cmdLine.endsWith(" ") && command.getParameters().size() != 0) {
                                int paramIndex = cmdLine.split(" ").length - alias.split(" ").length;
                                if (paramIndex == command.getParameters().size() || !cmdLine.endsWith(" ")) {
                                    --paramIndex;
                                }
                                if (paramIndex < 0) {
                                    paramIndex = 0;
                                }
                                final ParamData paramData = command.getParameters().get(paramIndex);
                                final String[] params = cmdLine.split(" ");
                                for (final String completion : CommandHandler.tabCompleteParameter(player, cmdLine.endsWith(" ") ? "" : params[params.length - 1], paramData.getParameterClass())) {
                                    completions.add(completion);
                                }
                                break Label_0525;
                            }
                            final String[] splitString = split.toLowerCase().split(" ");
                            completions.add(splitString[splitString.length - 1].trim());
                        }
                    }
                }
            }
            final List<String> completionList = new ArrayList<String>(completions);
            final List<String> vanillaCompletionList = (List<String>)super.tabComplete(sender, cmdLine);
            if (vanillaCompletionList != null) {
                for (final String vanillaCompletion : vanillaCompletionList) {
                    completionList.add(vanillaCompletion);
                }
            }
            return completionList;
        }
        catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<String>();
        }
        finally {
            this.foxTabComplete.stopTiming();
        }
    }
}
