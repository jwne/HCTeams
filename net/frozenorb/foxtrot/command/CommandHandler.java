package net.frozenorb.foxtrot.command;

import org.bukkit.plugin.*;
import org.bukkit.scheduler.*;
import net.frozenorb.foxtrot.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.apache.commons.lang.*;
import org.bukkit.*;
import java.lang.reflect.*;
import net.frozenorb.foxtrot.command.objects.*;
import net.frozenorb.foxtrot.command.annotations.*;
import java.lang.annotation.*;
import org.bukkit.event.player.*;
import org.bukkit.event.*;
import org.bukkit.event.server.*;
import java.util.*;

public class CommandHandler implements Listener
{
    private static List<CommandData> commands;
    private static Map<Class<?>, ParamTransformer> parameterTransformers;
    private static Map<Class<?>, ParamTabCompleter> parameterTabCompleters;
    
    public static void init() {
        buttplug.fdsjfhkdsjfdsjhk().getServer().getPluginManager().registerEvents((Listener)new CommandHandler(), (Plugin)buttplug.fdsjfhkdsjfdsjhk());
        new BukkitRunnable() {
            public void run() {
                try {
                    final Field currentCommandMap = buttplug.fdsjfhkdsjfdsjhk().getServer().getClass().getDeclaredField("commandMap");
                    currentCommandMap.setAccessible(true);
                    final Object currentCommandMapObject = currentCommandMap.get(buttplug.fdsjfhkdsjfdsjhk().getServer());
                    final TwixCommandMap commandMap = new TwixCommandMap(buttplug.fdsjfhkdsjfdsjhk().getServer());
                    final Field knownCommands = SimpleCommandMap.class.getDeclaredField("knownCommands");
                    knownCommands.setAccessible(true);
                    final Field modifiersField = Field.class.getDeclaredField("modifiers");
                    modifiersField.setAccessible(true);
                    modifiersField.setInt(knownCommands, knownCommands.getModifiers() & 0xFFFFFFEF);
                    knownCommands.set(commandMap, knownCommands.get(currentCommandMapObject));
                    currentCommandMap.set(buttplug.fdsjfhkdsjfdsjhk().getServer(), commandMap);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.runTaskLater((Plugin)buttplug.fdsjfhkdsjfdsjhk(), 5L);
        new CommandRegistrar().register();
        registerTransformer(Integer.TYPE, new ParamTransformer() {
            @Override
            public Object transform(final CommandSender sender, final String source) {
                try {
                    return Integer.valueOf(source);
                }
                catch (NumberFormatException exception) {
                    sender.sendMessage(ChatColor.RED + source + " is not a valid number.");
                    return null;
                }
            }
        });
        registerTransformer(Float.TYPE, new ParamTransformer() {
            @Override
            public Object transform(final CommandSender sender, final String source) {
                try {
                    return Float.valueOf(source);
                }
                catch (NumberFormatException exception) {
                    sender.sendMessage(ChatColor.RED + source + " is not a valid number.");
                    return null;
                }
            }
        });
        registerTransformer(Boolean.TYPE, new ParamTransformer() {
            @Override
            public Object transform(final CommandSender sender, final String source) {
                try {
                    return Boolean.valueOf(source);
                }
                catch (NumberFormatException exception) {
                    sender.sendMessage(ChatColor.RED + source + " is not a valid boolean.");
                    return null;
                }
            }
        });
        registerTabCompleter(Boolean.TYPE, new ParamTabCompleter() {
            @Override
            public List<String> tabComplete(final Player sender, final String source) {
                final List<String> completions = new ArrayList<String>();
                for (final String string : new String[] { "true", "false" }) {
                    if (StringUtils.startsWithIgnoreCase(string, source)) {
                        completions.add(string);
                    }
                }
                return completions;
            }
        });
        registerTransformer(Player.class, new ParamTransformer<Player>() {
            @Override
            public Player transform(final CommandSender sender, final String source) {
                if (sender instanceof Player && (source.equalsIgnoreCase("self") || source.equals(""))) {
                    return (Player)sender;
                }
                final Player player = buttplug.fdsjfhkdsjfdsjhk().getServer().getPlayer(source);
                if (player == null) {
                    sender.sendMessage(ChatColor.RED + "No player with the name " + source + " found.");
                    return null;
                }
                return player;
            }
        });
        registerTabCompleter(Player.class, new ParamTabCompleter() {
            @Override
            public List<String> tabComplete(final Player sender, final String source) {
                final List<String> completions = new ArrayList<String>();
                for (final Player player : buttplug.fdsjfhkdsjfdsjhk().getServer().getOnlinePlayers()) {
                    if (StringUtils.startsWithIgnoreCase(player.getName(), source)) {
                        completions.add(player.getName());
                    }
                }
                return completions;
            }
        });
        registerTransformer(OfflinePlayer.class, new ParamTransformer() {
            @Override
            public Object transform(final CommandSender sender, final String source) {
                if (sender instanceof Player && (source.equalsIgnoreCase("self") || source.equals(""))) {
                    return sender;
                }
                return buttplug.fdsjfhkdsjfdsjhk().getServer().getOfflinePlayer(source);
            }
        });
        registerTabCompleter(OfflinePlayer.class, new ParamTabCompleter() {
            @Override
            public List<String> tabComplete(final Player sender, final String source) {
                final List<String> completions = new ArrayList<String>();
                for (final Player player : buttplug.fdsjfhkdsjfdsjhk().getServer().getOnlinePlayers()) {
                    if (StringUtils.startsWithIgnoreCase(player.getName(), source)) {
                        completions.add(player.getName());
                    }
                }
                return completions;
            }
        });
        registerTransformer(World.class, new ParamTransformer() {
            @Override
            public Object transform(final CommandSender sender, final String source) {
                final World world = buttplug.fdsjfhkdsjfdsjhk().getServer().getWorld(source);
                if (world == null) {
                    sender.sendMessage(ChatColor.RED + "No world with the name " + source + " found.");
                    return null;
                }
                return world;
            }
        });
        registerTabCompleter(World.class, new ParamTabCompleter() {
            @Override
            public List<String> tabComplete(final Player sender, final String source) {
                final List<String> completions = new ArrayList<String>();
                for (final World world : buttplug.fdsjfhkdsjfdsjhk().getServer().getWorlds()) {
                    if (StringUtils.startsWithIgnoreCase(world.getName(), source)) {
                        completions.add(world.getName());
                    }
                }
                return completions;
            }
        });
        registerClass(CommandHandler.class);
    }
    
    @Command(names = { "ListCommands" }, permissionNode = "foxtrot.listcommands")
    public static void listCommands(final CommandSender sender) {
        for (final CommandData command : CommandHandler.commands) {
            sender.sendMessage(command.getPermissionNode() + ChatColor.YELLOW + " " + command.getUsageString());
        }
    }
    
    public static void registerTransformer(final Class<?> transforms, final ParamTransformer transformer) {
        CommandHandler.parameterTransformers.put(transforms, transformer);
    }
    
    public static void registerTabCompleter(final Class<?> tabCompletes, final ParamTabCompleter tabCompleter) {
        CommandHandler.parameterTabCompleters.put(tabCompletes, tabCompleter);
    }
    
    public static void registerClass(final Class<?> registeredClass) {
        for (final Method method : registeredClass.getMethods()) {
            if (method.getAnnotation(Command.class) != null) {
                if (!Modifier.isStatic(method.getModifiers())) {
                    buttplug.fdsjfhkdsjfdsjhk().getLogger().warning("Method " + method.getName() + " has an @Command annotation. but isn't static.");
                }
                else {
                    registerMethod(method);
                }
            }
        }
    }
    
    public static void registerMethod(final Method method) {
        final Command command = method.getAnnotation(Command.class);
        final List<ParamData> paramData = new ArrayList<ParamData>();
        for (int i = 1; i < method.getParameterTypes().length; ++i) {
            Param param = null;
            for (final Annotation annotation : method.getParameterAnnotations()[i]) {
                if (annotation instanceof Param) {
                    param = (Param)annotation;
                    break;
                }
            }
            if (param == null) {
                buttplug.fdsjfhkdsjfdsjhk().getLogger().warning(method.getDeclaringClass().getSimpleName() + " -> " + method.getName() + " is missing a @Param annotation.");
                return;
            }
            paramData.add(new ParamData(method.getParameterTypes()[i], param));
        }
        CommandHandler.commands.add(new CommandData(method, command, paramData, !method.getParameterTypes()[0].getClass().equals(Player.class)));
        Collections.sort(CommandHandler.commands, new Comparator<CommandData>() {
            @Override
            public int compare(final CommandData o1, final CommandData o2) {
                return Integer.valueOf(o2.getName().length()).compareTo(Integer.valueOf(o1.getName().length()));
            }
        });
    }
    
    @EventHandler
    public void onCommandPreProcess(final PlayerCommandPreprocessEvent event) {
        String[] args = new String[0];
        CommandData found = null;
    Label_0186:
        for (final CommandData commandData : CommandHandler.commands) {
            final String[] names = commandData.getNames();
            final int length = names.length;
            int i = 0;
            while (i < length) {
                final String alias = names[i];
                final String messageString = event.getMessage().substring(1).toLowerCase() + " ";
                final String aliasString = alias.toLowerCase() + " ";
                if (messageString.startsWith(aliasString)) {
                    found = commandData;
                    if (event.getMessage().length() > alias.length() + 2) {
                        args = event.getMessage().substring(alias.length() + 2).split(" ");
                        break Label_0186;
                    }
                    break Label_0186;
                }
                else {
                    ++i;
                }
            }
        }
        if (found == null) {
            return;
        }
        event.setCancelled(true);
        if (!found.canAccess((CommandSender)event.getPlayer())) {
            event.getPlayer().sendMessage(ChatColor.RED + "No permission.");
            return;
        }
        found.execute((CommandSender)event.getPlayer(), args);
    }
    
    @EventHandler
    public void onConsoleCommand(final ServerCommandEvent event) {
        String[] args = new String[0];
        CommandData found = null;
    Label_0182:
        for (final CommandData commandData : CommandHandler.commands) {
            final String[] names = commandData.getNames();
            final int length = names.length;
            int i = 0;
            while (i < length) {
                final String alias = names[i];
                final String messageString = event.getCommand().toLowerCase() + " ";
                final String aliasString = alias.toLowerCase() + " ";
                if (messageString.startsWith(aliasString)) {
                    found = commandData;
                    if (event.getCommand().length() > alias.length() + 1) {
                        args = event.getCommand().substring(alias.length() + 1).split(" ");
                        break Label_0182;
                    }
                    break Label_0182;
                }
                else {
                    ++i;
                }
            }
        }
        if (found == null) {
            return;
        }
        event.setCommand("");
        if (!found.isConsole()) {
            event.getSender().sendMessage(ChatColor.RED + "This command does not support execution from the console.");
            return;
        }
        found.execute(event.getSender(), args);
    }
    
    public static Object transformParameter(final CommandSender sender, final String parameter, final Class<?> transformTo) {
        if (transformTo.equals(String.class)) {
            return parameter;
        }
        return CommandHandler.parameterTransformers.get(transformTo).transform(sender, parameter);
    }
    
    public static List<String> tabCompleteParameter(final Player sender, final String parameter, final Class<?> transformTo) {
        if (!CommandHandler.parameterTabCompleters.containsKey(transformTo)) {
            return new ArrayList<String>();
        }
        return CommandHandler.parameterTabCompleters.get(transformTo).tabComplete(sender, parameter);
    }
    
    public static List<CommandData> getCommands() {
        return CommandHandler.commands;
    }
    
    static {
        CommandHandler.commands = new ArrayList<CommandData>();
        CommandHandler.parameterTransformers = new HashMap<Class<?>, ParamTransformer>();
        CommandHandler.parameterTabCompleters = new HashMap<Class<?>, ParamTabCompleter>();
    }
}
