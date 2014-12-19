package net.frozenorb.foxtrot.deathmessage.util;

import org.bukkit.inventory.*;
import org.bukkit.*;
import org.apache.commons.lang.*;

public class MobUtil
{
    public static String getItemName(final ItemStack i) {
        if (i.getItemMeta().hasDisplayName()) {
            return ChatColor.stripColor(i.getItemMeta().getDisplayName());
        }
        return WordUtils.capitalizeFully(i.getType().name().replace('_', ' '));
    }
}
