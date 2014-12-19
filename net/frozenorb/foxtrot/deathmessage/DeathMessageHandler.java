package net.frozenorb.foxtrot.deathmessage;

import net.frozenorb.foxtrot.deathmessage.objects.*;
import net.frozenorb.foxtrot.*;
import net.frozenorb.foxtrot.deathmessage.listeners.*;
import org.bukkit.event.*;
import org.bukkit.plugin.*;
import net.frozenorb.foxtrot.deathmessage.trackers.*;
import org.bukkit.entity.*;
import java.util.*;

public class DeathMessageHandler
{
    private static Map<String, List<Damage>> damage;
    
    public static void init() {
        buttplug.fdsjfhkdsjfdsjhk().getServer().getPluginManager().registerEvents((Listener)new DamageListener(), (Plugin)buttplug.fdsjfhkdsjfdsjhk());
        buttplug.fdsjfhkdsjfdsjhk().getServer().getPluginManager().registerEvents((Listener)new GeneralTracker(), (Plugin)buttplug.fdsjfhkdsjfdsjhk());
        buttplug.fdsjfhkdsjfdsjhk().getServer().getPluginManager().registerEvents((Listener)new PVPTracker(), (Plugin)buttplug.fdsjfhkdsjfdsjhk());
        buttplug.fdsjfhkdsjfdsjhk().getServer().getPluginManager().registerEvents((Listener)new EntityTracker(), (Plugin)buttplug.fdsjfhkdsjfdsjhk());
        buttplug.fdsjfhkdsjfdsjhk().getServer().getPluginManager().registerEvents((Listener)new FallTracker(), (Plugin)buttplug.fdsjfhkdsjfdsjhk());
        buttplug.fdsjfhkdsjfdsjhk().getServer().getPluginManager().registerEvents((Listener)new ArrowTracker(), (Plugin)buttplug.fdsjfhkdsjfdsjhk());
        buttplug.fdsjfhkdsjfdsjhk().getServer().getPluginManager().registerEvents((Listener)new VoidTracker(), (Plugin)buttplug.fdsjfhkdsjfdsjhk());
    }
    
    public static List<Damage> getDamage(final Player player) {
        return DeathMessageHandler.damage.get(player.getName());
    }
    
    public static void addDamage(final Player player, final Damage addedDamage) {
        if (!DeathMessageHandler.damage.containsKey(player.getName())) {
            DeathMessageHandler.damage.put(player.getName(), new ArrayList<Damage>());
        }
        DeathMessageHandler.damage.get(player.getName()).add(addedDamage);
    }
    
    public static void clearDamage(final Player player) {
        DeathMessageHandler.damage.remove(player.getName());
    }
    
    static {
        DeathMessageHandler.damage = new HashMap<String, List<Damage>>();
    }
}
