package net.frozenorb.foxtrot.command.commands;

import org.bukkit.entity.*;
import org.bukkit.*;
import net.frozenorb.foxtrot.*;
import net.frozenorb.foxtrot.team.*;
import net.frozenorb.foxtrot.command.annotations.*;
import java.util.*;
import org.bukkit.scheduler.*;
import lombok.*;
import org.bukkit.plugin.*;

public class FocusCommand
{
    public static Map<String, Focusable> currentTrackers;
    
    @Command(names = { "Focus", "Track", "Hunt" }, permissionNode = "")
    public static void focus(final Player sender, @Param(name = "<reset | x,z | player>") final String param) {
        if (sender.getItemInHand() == null || sender.getItemInHand().getType() != Material.COMPASS) {
            sender.sendMessage(ChatColor.RED + "You must be holding a compass to do this!");
            return;
        }
        if (param.equalsIgnoreCase("reset")) {
            final Focusable focusable = new Focusable(ChatColor.YELLOW + "the " + ChatColor.RED + "Warzone") {
                Location loc = null;
                
                @Override
                public Location updateLocation() {
                    if (this.loc == null) {
                        this.loc = new Location((World)Bukkit.getWorlds().get(0), 0.0, 70.0, 0.0);
                    }
                    return this.loc;
                }
                
                @Override
                public FocusType getFocusType() {
                    return FocusType.WARZONE;
                }
            };
            focus(sender, focusable);
            return;
        }
        if (param.contains(",")) {
            final String[] split = param.split(",");
            try {
                final double x = Double.parseDouble(split[0]);
                final double z = Double.parseDouble(split[1]);
                final Focusable focusable2 = new Focusable(ChatColor.LIGHT_PURPLE + "(" + ChatColor.AQUA + (int)x + ChatColor.LIGHT_PURPLE + ", " + ChatColor.AQUA + (int)z + ChatColor.LIGHT_PURPLE + ")") {
                    Location loc = null;
                    
                    @Override
                    public Location updateLocation() {
                        if (this.loc == null) {
                            this.loc = new Location((World)Bukkit.getWorlds().get(0), x, 70.0, z);
                        }
                        return this.loc;
                    }
                    
                    @Override
                    public FocusType getFocusType() {
                        return FocusType.LOCATION;
                    }
                };
                focus(sender, focusable2);
            }
            catch (NumberFormatException e) {
                sender.sendMessage(ChatColor.RED + "Invalid X and Z coordinates!");
            }
            return;
        }
        final Player target = Bukkit.getPlayer(param);
        if (target == null || target.hasMetadata("invisible")) {
            sender.sendMessage(ChatColor.RED + "Player '" + param + "' could not be found.");
            return;
        }
        if (buttplug.fdsjfhkdsjfdsjhk().getDsfjhkdsjhdsjkhfds().getPlayerTeam(sender.getName()) == null) {
            sender.sendMessage(ChatColor.RED + "You are not on a team!");
            return;
        }
        final faggot senderTeam = buttplug.fdsjfhkdsjfdsjhk().getDsfjhkdsjhdsjkhfds().getPlayerTeam(sender.getName());
        final faggot targetTeam = buttplug.fdsjfhkdsjfdsjhk().getDsfjhkdsjhdsjkhfds().getPlayerTeam(target.getName());
        if (senderTeam != targetTeam) {
            sender.sendMessage(ChatColor.RED + "You can only track players that are on the same team as you!");
            return;
        }
        if (sender.equals(target)) {
            sender.sendMessage(ChatColor.RED + "You may not focus on yourself!");
            return;
        }
        if (!target.getWorld().equals(sender.getWorld())) {
            sender.sendMessage(ChatColor.RED + "That player is not in the same world as you!");
            return;
        }
        final String cacheName = target.getName();
        final Focusable focusable3 = new Focusable(target.getDisplayName()) {
            @Override
            public FocusType getFocusType() {
                return FocusType.PLAYER;
            }
            
            @Override
            public Location updateLocation() {
                final Player pss = Bukkit.getPlayerExact(cacheName);
                if (pss == null) {
                    return null;
                }
                return pss.getLocation();
            }
        };
        focus(sender, focusable3);
    }
    
    private static void focus(final Player player, final Focusable focusable) {
        if (FocusCommand.currentTrackers.containsKey(player.getName())) {
            FocusCommand.currentTrackers.remove(player.getName()).cancel();
        }
        FocusCommand.currentTrackers.put(player.getName(), focusable);
        focusable.start(player);
        player.sendMessage(ChatColor.YELLOW + "You have begun to focus on " + ChatColor.RED + focusable.data + ChatColor.YELLOW + ".");
    }
    
    static {
        FocusCommand.currentTrackers = new HashMap<String, Focusable>();
    }
    
    public abstract static class Focusable extends BukkitRunnable
    {
        private Player p;
        @NonNull
        private String data;
        private Location lastLocation;
        
        public abstract FocusType getFocusType();
        
        public abstract Location updateLocation();
        
        public void start(final Player p) {
            this.p = p;
            this.lastLocation = this.updateLocation();
            this.runTaskTimer((Plugin)buttplug.fdsjfhkdsjfdsjhk(), 0L, 20L);
        }
        
        public void run() {
            final Location l = this.updateLocation();
            if (l == null) {
                if (this.getFocusType() == FocusType.TEAM) {
                    this.p.sendMessage(ChatColor.YELLOW + "Focus cancelled! §c" + this.data + "§e no longer has claimed territory.");
                    FocusCommand.currentTrackers.remove(this.p.getName());
                    this.cancel();
                    return;
                }
                if (this.getFocusType() == FocusType.PLAYER) {
                    if (this.lastLocation != null) {
                        this.p.sendMessage(this.data + " §elogged out and will be refocused when they log in!");
                    }
                    this.lastLocation = null;
                    return;
                }
            }
            else if (this.lastLocation == null) {
                this.p.sendMessage(this.data + " §alogged back in and is now being focused!");
            }
            this.lastLocation = l;
            this.p.setCompassTarget(l);
        }
        
        public Focusable(@NonNull final String data) {
            super();
            if (data == null) {
                throw new NullPointerException("data");
            }
            this.data = data;
        }
    }
    
    private enum FocusType
    {
        WARZONE, 
        TEAM, 
        PLAYER, 
        LOCATION;
    }
}
