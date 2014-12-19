package net.frozenorb.foxtrot.listener;

import java.util.*;
import net.frozenorb.foxtrot.*;
import org.bukkit.event.*;
import org.bukkit.event.player.*;
import org.bukkit.*;
import net.frozenorb.foxtrot.util.*;
import org.bukkit.event.entity.*;
import org.bukkit.plugin.*;

public class DeathbanListener implements Listener
{
    private Map<String, Long> lastJoinedRevive;
    
    public DeathbanListener() {
        super();
        this.lastJoinedRevive = new HashMap<String, Long>();
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onAsyncPlayerPreLogin(final AsyncPlayerPreLoginEvent event) {
        if (event.getLoginResult() != AsyncPlayerPreLoginEvent.Result.ALLOWED) {
            return;
        }
        buttplug.fdsjfhkdsjfdsjhk().getDeathbanMap().reloadValue(event.getName());
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerLogin(final PlayerLoginEvent event) {
        if (buttplug.fdsjfhkdsjfdsjhk().getDeathbanMap().isDeathbanned(event.getPlayer().getName())) {
            final long unbannedOn = buttplug.fdsjfhkdsjfdsjhk().getDeathbanMap().getDeathban(event.getPlayer().getName());
            final long left = unbannedOn - System.currentTimeMillis();
            if (event.getPlayer().isOp()) {
                event.getPlayer().sendMessage(ChatColor.RED + "You would be deathbanned for another " + TimeUtils.getDurationBreakdown(left) + ".");
                return;
            }
            if (buttplug.fdsjfhkdsjfdsjhk().getServerHandler().isPreEOTW()) {
                event.disallow(PlayerLoginEvent.Result.KICK_BANNED, ChatColor.RED + "You have died, and are deathbanned for the remainder of the map.");
                return;
            }
            int transferableLives = buttplug.fdsjfhkdsjfdsjhk().getTransferableLivesMap().getLives(event.getPlayer().getName());
            if (this.lastJoinedRevive.containsKey(event.getPlayer().getName()) && System.currentTimeMillis() - this.lastJoinedRevive.get(event.getPlayer().getName()) < 20000L) {
                if (transferableLives > 0) {
                    buttplug.fdsjfhkdsjfdsjhk().getDeathbanMap().revive(event.getPlayer().getName());
                    buttplug.fdsjfhkdsjfdsjhk().getTransferableLivesMap().setLives(event.getPlayer().getName(), transferableLives - 1);
                    --transferableLives;
                    event.disallow(PlayerLoginEvent.Result.KICK_BANNED, ChatColor.RED + "You now have " + transferableLives + " " + ((transferableLives == 1) ? "life" : "lives") + " left. You have been revived.");
                }
                else {
                    event.disallow(PlayerLoginEvent.Result.KICK_BANNED, ChatColor.RED + "You do not have any lives. Buy a life on the website!");
                }
            }
            else if (transferableLives > 0) {
                event.disallow(PlayerLoginEvent.Result.KICK_BANNED, ChatColor.RED + "You have died, and are deathbanned. Your deathban will expire in " + TimeUtils.getDurationBreakdown(left) + ". You have " + transferableLives + " total " + ((transferableLives == 1) ? "life" : "lives") + ". To use a life, reconnect within 20 seconds.");
                this.lastJoinedRevive.put(event.getPlayer().getName(), System.currentTimeMillis());
            }
            else {
                event.disallow(PlayerLoginEvent.Result.KICK_BANNED, ChatColor.RED + "You have died, and are deathbanned. Your deathban will expire in " + TimeUtils.getDurationBreakdown(left) + ". You have no lives. To buy a life, go to MineHQ.com/store.");
            }
        }
    }
    
    @EventHandler
    public void onPlayerDeath(final PlayerDeathEvent event) {
        final int seconds = buttplug.fdsjfhkdsjfdsjhk().getServerHandler().getDeathBanAt(event.getEntity().getName(), event.getEntity().getLocation());
        buttplug.fdsjfhkdsjfdsjhk().getDeathbanMap().deathban(event.getEntity().getName(), seconds);
        final String time = TimeUtils.getDurationBreakdown(seconds * 1000);
        buttplug.fdsjfhkdsjfdsjhk().getServer().getScheduler().runTaskLater((Plugin)buttplug.fdsjfhkdsjfdsjhk(), (Runnable)new Runnable() {
            @Override
            public void run() {
                event.getEntity().teleport(event.getEntity().getLocation().add(0.0, 100.0, 0.0));
                if (buttplug.fdsjfhkdsjfdsjhk().getServerHandler().isPreEOTW()) {
                    event.getEntity().kickPlayer(ChatColor.RED + "§cCome back tomorrow for SOTW!");
                }
                else {
                    event.getEntity().kickPlayer(ChatColor.RED + "Come back in " + time + "!");
                }
            }
        }, 5L);
    }
}
