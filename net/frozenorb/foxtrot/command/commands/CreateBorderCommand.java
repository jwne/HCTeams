package net.frozenorb.foxtrot.command.commands;

import org.bukkit.entity.*;
import net.frozenorb.foxtrot.server.*;
import org.bukkit.*;
import org.bukkit.scheduler.*;
import java.util.*;
import net.frozenorb.foxtrot.*;
import org.bukkit.plugin.*;
import org.bukkit.block.*;
import net.frozenorb.foxtrot.command.annotations.*;

public class CreateBorderCommand
{
    @Command(names = { "CreateBorder" }, permissionNode = "op")
    public static void createBorder(final Player sender) {
        final ArrayList<Location> toChange = new ArrayList<Location>();
        final int radius = ServerHandler.WARZONE_RADIUS;
        int lastz = 0;
        for (int x = -radius; x <= radius; ++x) {
            final int z = (int)(Math.sqrt(radius * radius - x * x) + 0.5);
            int bOld;
            int a;
            if ((bOld = lastz) < (a = z)) {
                final int swap = bOld;
                bOld = a;
                a = swap;
            }
            for (int zp = a; zp <= bOld; ++zp) {
                for (int y = 5; y < 256; ++y) {
                    final Location l = new Location(Bukkit.getWorld("world"), (double)x, (double)y, (double)zp);
                    final Location l2 = new Location(Bukkit.getWorld("world"), (double)x, (double)y, (double)(-zp));
                    final Block b = l.getBlock();
                    final Block b2 = l2.getBlock();
                    if (!b.getType().name().startsWith("LOG") && b.getType() != Material.AIR && !b.isEmpty() && b.getType().isSolid() && !b.getType().isTransparent() && b.getType() != Material.LEAVES && b.getType() != Material.LEAVES_2) {
                        toChange.add(l);
                    }
                    if (!b2.getType().name().startsWith("LOG") && b2.getType() != Material.AIR && !b2.isEmpty() && b2.getType().isSolid() && !b2.getType().isTransparent() && b2.getType() != Material.LEAVES && b2.getType() != Material.LEAVES_2) {
                        toChange.add(l2);
                    }
                }
            }
            lastz = z;
        }
        sender.sendMessage(ChatColor.YELLOW + "Total of " + toChange.size() + " blocks to be changed.");
        final Iterator<Location> iter = toChange.iterator();
        new BukkitRunnable() {
            public void run() {
                int done = 0;
                while (iter.hasNext() && done <= 200) {
                    iter.next().getBlock().setTypeIdAndData(Material.WOOL.getId(), (byte)14, false);
                    ++done;
                    iter.remove();
                }
                if (!iter.hasNext()) {
                    Bukkit.broadcastMessage("finished drawing line");
                    this.cancel();
                }
            }
        }.runTaskTimer((Plugin)buttplug.fdsjfhkdsjfdsjhk(), 0L, 1L);
    }
}
