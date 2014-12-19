package net.frozenorb.foxtrot.server;

import org.bukkit.event.player.*;
import net.frozenorb.foxtrot.*;
import org.bukkit.*;

public enum RegionType
{
    SPAWN(event -> {
        if (SpawnTagHandler.isTagged(event.getPlayer()) && event.getPlayer().getGameMode() != GameMode.CREATIVE) {
            event.getPlayer().sendMessage(ChatColor.RED + "You cannot enter spawn while spawn-tagged.");
            event.setTo(event.getFrom());
            return false;
        }
        else {
            event.getPlayer().setHealth(event.getPlayer().getMaxHealth());
            event.getPlayer().setFoodLevel(20);
            return true;
        }
    }), 
    WARZONE(event -> true), 
    WILDNERNESS(event -> true), 
    ROAD(event -> true), 
    CLAIMED_LAND(event -> {
        if (buttplug.fdsjfhkdsjfdsjhk().getPvPTimerMap().hasTimer(event.getPlayer().getName())) {
            event.setTo(event.getFrom());
            event.getPlayer().sendMessage(ChatColor.RED + "You cannot do this while your PVP Timer is active!");
            event.getPlayer().sendMessage(ChatColor.RED + "Type '" + ChatColor.YELLOW + "/pvp enable" + ChatColor.RED + "' to remove your timer.");
            return false;
        }
        else {
            return true;
        }
    });
    
    private RegionMoveHandler moveHandler;
    
    private RegionType(final RegionMoveHandler moveHandler) {
        this.moveHandler = moveHandler;
    }
    
    public RegionMoveHandler getMoveHandler() {
        return this.moveHandler;
    }
}
