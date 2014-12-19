package net.frozenorb.foxtrot.command.commands;

import org.bukkit.entity.*;
import net.frozenorb.foxtrot.*;
import net.frozenorb.foxtrot.team.dtr.bitmask.*;
import net.frozenorb.foxtrot.command.annotations.*;
import org.bukkit.util.*;
import org.bukkit.scheduler.*;
import org.bukkit.*;
import org.bukkit.potion.*;
import org.bukkit.block.*;
import org.bukkit.plugin.*;

public class CannonCommand
{
    public static final int SPAWN_CANNON_MAX_DISTANCE = 600;
    public static final int SPAWN_CANNON_MIN_DISTANCE = 100;
    
    @Command(names = { "cannon launch", "spawncannon launch" }, permissionNode = "")
    public static void cannonLaunch(final Player sender) {
        if (buttplug.fdsjfhkdsjfdsjhk().getServerHandler().isEOTW()) {
            sender.sendMessage(ChatColor.RED + "Spawn cannon disabled: Server is in EOTW mode.");
            return;
        }
        if (!DTRBitmaskType.SAFE_ZONE.appliesAt(sender.getLocation()) || sender.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() != Material.BEACON) {
            sender.sendMessage(ChatColor.RED + "You must be standing on the spawn cannon.");
            return;
        }
        int x = buttplug.RANDOM.nextInt(500) + 100;
        int z = buttplug.RANDOM.nextInt(500) + 100;
        if (buttplug.RANDOM.nextBoolean()) {
            x = -x;
        }
        if (buttplug.RANDOM.nextBoolean()) {
            z = -z;
        }
        spawnCannon(sender, x, z);
    }
    
    @Command(names = { "cannon aim", "spawncannon aim" }, permissionNode = "foxtrot.spawncannon.aim")
    public static void cannonAim(final Player sender, @Param(name = "x") final int x, @Param(name = "z") final int z) {
        if (buttplug.fdsjfhkdsjfdsjhk().getServerHandler().isEOTW()) {
            sender.sendMessage(ChatColor.RED + "Spawn cannon disabled: Server is in EOTW mode.");
            return;
        }
        if (!DTRBitmaskType.SAFE_ZONE.appliesAt(sender.getLocation()) || sender.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() != Material.BEACON) {
            sender.sendMessage(ChatColor.RED + "You must be standing on the spawn cannon.");
            return;
        }
        final int maxDistance = getMaxCannonDistance(sender);
        if (Math.abs(x) > maxDistance || Math.abs(z) > maxDistance) {
            sender.sendMessage(ChatColor.RED + "You cannot cannon that far. Your spawn cannon limit is " + maxDistance + ".");
            return;
        }
        if (Math.abs(x) < 100 || Math.abs(z) < 100) {
            sender.sendMessage(ChatColor.RED + "You cannot cannon to a location that close to spawn! Cannon to a distance at least " + 100 + " blocks from spawn");
        }
        spawnCannon(sender, x, z);
    }
    
    public static void spawnCannon(final Player player, final int x, final int z) {
        player.sendMessage(ChatColor.YELLOW + "Cannoning to " + ChatColor.GREEN + x + ", " + z + ChatColor.YELLOW + ".");
        player.setVelocity(new Vector(0.0f, 1.0f, 0.0f));
        new BukkitRunnable() {
            int timer = 0;
            
            public void run() {
                ++this.timer;
                if (this.timer < 40) {
                    if (this.timer % 4 == 0) {
                        player.playSound(player.getLocation(), Sound.EXPLODE, 1.0f, 1.0f);
                    }
                }
                else if (this.timer == 40) {
                    Block block;
                    for (block = player.getWorld().getBlockAt(x, 200, z); block.getType() == Material.AIR && block.getY() > 1; block = block.getRelative(BlockFace.DOWN)) {}
                    player.teleport(new Location(player.getWorld(), (double)x, (double)(block.getY() + 100), (double)z));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 80, 1));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 80, 1));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 200, 1));
                }
                else if (this.timer > 40 && (player.isOnGround() || this.timer > 240)) {
                    player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
                    this.cancel();
                }
            }
        }.runTaskTimer((Plugin)buttplug.fdsjfhkdsjfdsjhk(), 1L, 1L);
    }
    
    public static int getMaxCannonDistance(final Player player) {
        if (player.hasPermission("foxtrot.spawncannon.1250")) {
            return 1250;
        }
        if (player.hasPermission("foxtrot.spawncannon.1000")) {
            return 1000;
        }
        if (player.hasPermission("foxtrot.spawncannon.750")) {
            return 750;
        }
        if (player.hasPermission("foxtrot.spawncannon.500")) {
            return 500;
        }
        return 100;
    }
}
