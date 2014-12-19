package net.frozenorb.foxtrot.listener;

import net.frozenorb.foxtrot.*;
import org.bukkit.plugin.*;
import org.bukkit.metadata.*;
import org.bukkit.event.*;
import org.bukkit.event.block.*;
import org.bukkit.*;
import org.bukkit.block.*;

public class FoundDiamondsListener implements Listener
{
    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockPlace(final BlockPlaceEvent event) {
        if (!event.isCancelled() && event.getBlock().getType() == Material.DIAMOND_ORE) {
            event.getBlock().setMetadata("DiamondPlaced", (MetadataValue)new FixedMetadataValue((Plugin)buttplug.fdsjfhkdsjfdsjhk(), (Object)true));
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockBreak(final BlockBreakEvent event) {
        if (!event.isCancelled() && event.getBlock().getType() == Material.DIAMOND_ORE && !event.getBlock().hasMetadata("DiamondPlaced")) {
            int diamonds = 0;
            for (int x = -5; x < 5; ++x) {
                for (int y = -5; y < 5; ++y) {
                    for (int z = -5; z < 5; ++z) {
                        final Block block = event.getBlock().getLocation().add((double)x, (double)y, (double)z).getBlock();
                        if (block.getType() == Material.DIAMOND_ORE && !block.hasMetadata("DiamondPlaced")) {
                            ++diamonds;
                            block.setMetadata("DiamondPlaced", (MetadataValue)new FixedMetadataValue((Plugin)buttplug.fdsjfhkdsjfdsjhk(), (Object)true));
                        }
                    }
                }
            }
            buttplug.fdsjfhkdsjfdsjhk().getServer().broadcastMessage("[FD] " + ChatColor.AQUA + event.getPlayer().getName() + " found " + diamonds + " diamond" + ((diamonds == 1) ? "" : "s") + ".");
        }
    }
}
