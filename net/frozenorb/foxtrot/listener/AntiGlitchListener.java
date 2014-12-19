package net.frozenorb.foxtrot.listener;

import org.bukkit.event.block.*;
import net.frozenorb.foxtrot.team.claims.*;
import org.bukkit.event.*;

public class AntiGlitchListener implements Listener
{
    @EventHandler(priority = EventPriority.MONITOR)
    public void onVerticalBlockPlaceGlitch(final BlockPlaceEvent event) {
        if (LandBoard.getInstance().getTeam(event.getBlock().getLocation()) != null && event.isCancelled()) {
            event.getPlayer().teleport(event.getPlayer().getLocation());
        }
    }
}
