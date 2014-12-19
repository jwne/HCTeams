package net.frozenorb.foxtrot.command.objects;

import java.lang.reflect.*;
import org.spigotmc.*;
import net.frozenorb.foxtrot.command.annotations.*;
import org.bukkit.command.*;
import java.util.*;
import org.bukkit.*;
import net.frozenorb.foxtrot.command.*;

public class CommandData
{
    private boolean console;
    private String[] names;
    private String[] flags;
    private String permissionNode;
    private List<ParamData> parameters;
    private Method method;
    private CustomTimingsHandler timingsHandler;
    
    public CommandData(final Method method, final Command commandAnnotation, final List<ParamData> parameters, final boolean console) {
        super();
        this.parameters = new ArrayList<ParamData>();
        this.names = commandAnnotation.names();
        this.flags = commandAnnotation.flags();
        this.permissionNode = commandAnnotation.permissionNode();
        this.parameters = parameters;
        this.method = method;
        this.console = console;
        this.timingsHandler = new CustomTimingsHandler("Foxtrot - CH '/" + this.getName() + "' Process");
    }
    
    public String getName() {
        return this.names[0];
    }
    
    public boolean canAccess(final CommandSender sender) {
        boolean permission = true;
        if (this.permissionNode.equals("op")) {
            if (!sender.isOp()) {
                permission = false;
            }
        }
        else if (!this.permissionNode.equals("") && !sender.hasPermission(this.permissionNode)) {
            permission = false;
        }
        return permission;
    }
    
    public String getUsageString() {
        return this.getUsageString(this.getName());
    }
    
    public String getUsageString(final String aliasUsed) {
        final StringBuilder stringBuilder = new StringBuilder();
        for (final ParamData paramHelp : this.getParameters()) {
            final boolean needed = paramHelp.getDefaultValue().equals("");
            stringBuilder.append(needed ? "<" : "[").append(paramHelp.getName());
            stringBuilder.append(needed ? ">" : "]").append(" ");
        }
        return "/" + aliasUsed.toLowerCase() + " " + stringBuilder.toString().trim().toLowerCase();
    }
    
    public void execute(final CommandSender sender, final String[] params) {
        final ArrayList<Object> transformedParams = new ArrayList<Object>();
        transformedParams.add(sender);
        for (int paramIndex = 0; paramIndex < this.getParameters().size(); ++paramIndex) {
            final ParamData param = this.getParameters().get(paramIndex);
            String passedParam = ((paramIndex < params.length) ? params[paramIndex] : param.getDefaultValue()).trim();
            if (paramIndex >= params.length && (param.getDefaultValue() == null || param.getDefaultValue().equals(""))) {
                sender.sendMessage(ChatColor.RED + "Usage: " + this.getUsageString());
                return;
            }
            if (param.isWildcard() && !passedParam.trim().equals(param.getDefaultValue().trim())) {
                passedParam = toString(params, paramIndex);
            }
            final Object result = CommandHandler.transformParameter(sender, passedParam, param.getParameterClass());
            if (result == null) {
                return;
            }
            transformedParams.add(result);
            if (param.isWildcard()) {
                break;
            }
        }
        this.timingsHandler.startTiming();
        try {
            this.method.invoke(null, transformedParams.toArray(new Object[transformedParams.size()]));
        }
        catch (Exception e) {
            sender.sendMessage(ChatColor.RED + "It appears there was some issues processing your command...");
            e.printStackTrace();
        }
        this.timingsHandler.stopTiming();
    }
    
    public static String toString(final String[] args, final int start) {
        final StringBuilder stringBuilder = new StringBuilder();
        for (int arg = start; arg < args.length; ++arg) {
            stringBuilder.append(args[arg]).append(" ");
        }
        return stringBuilder.toString().trim();
    }
    
    public boolean isConsole() {
        return this.console;
    }
    
    public String[] getNames() {
        return this.names;
    }
    
    public String[] getFlags() {
        return this.flags;
    }
    
    public String getPermissionNode() {
        return this.permissionNode;
    }
    
    public List<ParamData> getParameters() {
        return this.parameters;
    }
    
    public Method getMethod() {
        return this.method;
    }
    
    public CustomTimingsHandler getTimingsHandler() {
        return this.timingsHandler;
    }
}
