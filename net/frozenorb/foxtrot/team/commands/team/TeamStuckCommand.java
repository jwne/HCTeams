package net.frozenorb.foxtrot.team.commands.team;

import org.bukkit.entity.*;
import org.bukkit.scheduler.*;
import net.frozenorb.foxtrot.*;
import org.bukkit.plugin.*;
import net.frozenorb.foxtrot.util.*;
import net.frozenorb.foxtrot.command.annotations.*;
import net.frozenorb.foxtrot.team.claims.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.metadata.*;
import java.util.*;
import com.google.common.collect.*;

public class TeamStuckCommand implements Listener
{
    private static final double MAX_DISTANCE = 5.0;
    private static final Set<Integer> warn;
    private static List<String> warping;
    private static List<String> damaged;
    
    @Command(names = { "team stuck", "t stuck", "f stuck", "faction stuck", "fac stuck", "stuck" }, permissionNode = "")
    public static void teamStuck(final Player sender) {
        if (TeamStuckCommand.warping.contains(sender.getName())) {
            sender.sendMessage(ChatColor.RED + "You are already being warped!");
            return;
        }
        if (sender.getWorld().getEnvironment() != World.Environment.NORMAL) {
            sender.sendMessage(ChatColor.RED + "You can only use this command from the overworld.");
            return;
        }
        TeamStuckCommand.warping.add(sender.getName());
        new BukkitRunnable() {
            private int seconds = (sender.isOp() && sender.getGameMode() == GameMode.CREATIVE) ? 5 : 300;
            private Location loc = sender.getLocation();
            private Location prevLoc;
            private int xStart = (int)this.loc.getX();
            private int yStart = (int)this.loc.getY();
            private int zStart = (int)this.loc.getZ();
            private Location nearest;
            
            public void run() {
                if (TeamStuckCommand.damaged.contains(sender.getName())) {
                    sender.sendMessage(ChatColor.RED + "You took damage, teleportation cancelled!");
                    TeamStuckCommand.damaged.remove(sender.getName());
                    TeamStuckCommand.warping.remove(sender.getName());
                    this.cancel();
                    return;
                }
                if (!sender.isOnline()) {
                    TeamStuckCommand.warping.remove(sender.getName());
                    this.cancel();
                    return;
                }
                if (this.seconds == 5) {
                    new BukkitRunnable() {
                        public void run() {
                            BukkitRunnable.this.nearest = nearestSafeLocation(sender.getLocation());
                        }
                    }.runTaskAsynchronously((Plugin)buttplug.fdsjfhkdsjfdsjhk());
                }
                if (this.seconds <= 0) {
                    if (this.nearest == null) {
                        kick(sender);
                    }
                    else {
                        sender.teleport(this.nearest);
                        sender.sendMessage(ChatColor.YELLOW + "Teleported you to the nearest safe area!");
                    }
                    TeamStuckCommand.warping.remove(sender.getName());
                    this.cancel();
                    return;
                }
                final Location loc = sender.getLocation();
                if (loc.getX() >= this.xStart + 5.0 || loc.getX() <= this.xStart - 5.0 || loc.getY() >= this.yStart + 5.0 || loc.getY() <= this.yStart - 5.0 || loc.getZ() >= this.zStart + 5.0 || loc.getZ() <= this.zStart - 5.0) {
                    sender.sendMessage(ChatColor.RED + "You moved more than " + 5.0 + " blocks, teleport cancelled!");
                    TeamStuckCommand.warping.remove(sender.getName());
                    this.cancel();
                    return;
                }
                if (TeamStuckCommand.warn.contains(this.seconds)) {
                    sender.sendMessage(ChatColor.YELLOW + "You will be teleported in " + ChatColor.RED.toString() + ChatColor.BOLD + TimeUtils.getMMSS(this.seconds) + ChatColor.YELLOW + "!");
                }
                --this.seconds;
            }
        }.runTaskTimer((Plugin)buttplug.fdsjfhkdsjfdsjhk(), 0L, 20L);
    }
    
    private static Location nearestSafeLocation(final Location origin) {
        final LandBoard landBoard = LandBoard.getInstance();
        if (landBoard.getClaim(origin) == null) {
            return getActualHighestBlock(origin.getBlock()).getLocation().add(0.0, 1.0, 0.0);
        }
        for (int xPos = 0, xNeg = 0; xPos < 250; ++xPos, --xNeg) {
            for (int zPos = 0, zNeg = 0; zPos < 250; ++zPos, --zNeg) {
                final Location atPos = origin.clone().add((double)xPos, 0.0, (double)zPos);
                final Location atNeg = origin.clone().add((double)xNeg, 0.0, (double)zNeg);
                if (landBoard.getClaim(atPos) == null) {
                    return getActualHighestBlock(atPos.getBlock()).getLocation().add(0.0, 1.0, 0.0);
                }
                if (landBoard.getClaim(atNeg) == null) {
                    return getActualHighestBlock(atNeg.getBlock()).getLocation().add(0.0, 1.0, 0.0);
                }
            }
        }
        return null;
    }
    
    @EventHandler
    public void onPlayerDamage(final EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            final Player player = (Player)event.getEntity();
            if (TeamStuckCommand.warping.contains(player.getName())) {
                TeamStuckCommand.damaged.add(player.getName());
            }
        }
    }
    
    private static Block getActualHighestBlock(Block block) {
        for (block = block.getWorld().getHighestBlockAt(block.getLocation()); block.getType() == Material.AIR && block.getY() > 0; block = block.getRelative(BlockFace.DOWN)) {}
        return block;
    }
    
    private static void kick(final Player player) {
        player.setMetadata("loggedout", (MetadataValue)new FixedMetadataValue((Plugin)buttplug.fdsjfhkdsjfdsjhk(), (Object)true));
        player.kickPlayer(ChatColor.RED + "We couldn't find a safe location, so we safely logged you out for now. Contact a staff member before logging back on! " + ChatColor.BLUE + "TeamSpeak: TS.MineHQ.com");
    }
    
    static {
        (warn = new HashSet<Integer>()).add(300);
        TeamStuckCommand.warn.add(270);
        TeamStuckCommand.warn.add(240);
        TeamStuckCommand.warn.add(210);
        TeamStuckCommand.warn.add(180);
        TeamStuckCommand.warn.add(150);
        TeamStuckCommand.warn.add(120);
        TeamStuckCommand.warn.add(90);
        TeamStuckCommand.warn.add(60);
        TeamStuckCommand.warn.add(30);
        TeamStuckCommand.warn.add(10);
        TeamStuckCommand.warn.add(5);
        TeamStuckCommand.warn.add(4);
        TeamStuckCommand.warn.add(3);
        TeamStuckCommand.warn.add(2);
        TeamStuckCommand.warn.add(1);
        buttplug.fdsjfhkdsjfdsjhk().getServer().getPluginManager().registerEvents((Listener)new TeamStuckCommand(), (Plugin)buttplug.fdsjfhkdsjfdsjhk());
        TeamStuckCommand.warping = (List<String>)Lists.newArrayList();
        TeamStuckCommand.damaged = (List<String>)Lists.newArrayList();
    }
}
