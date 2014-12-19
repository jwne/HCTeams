package net.frozenorb.foxtrot.team.dtr.bitmask.transformer;

import net.frozenorb.foxtrot.command.objects.*;
import org.bukkit.command.*;
import net.frozenorb.foxtrot.team.dtr.bitmask.*;
import org.bukkit.*;

public class DTRBitmaskTypeTransformer extends ParamTransformer
{
    @Override
    public Object transform(final CommandSender sender, final String source) {
        for (final DTRBitmaskType bitmaskType : DTRBitmaskType.values()) {
            if (source.equalsIgnoreCase(bitmaskType.getName())) {
                return bitmaskType;
            }
        }
        sender.sendMessage(ChatColor.RED + "No bitmask type with the name " + source + " found.");
        return null;
    }
}
