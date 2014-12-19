package net.frozenorb.foxtrot.team.claims;

import net.frozenorb.foxtrot.team.*;
import net.frozenorb.foxtrot.*;
import org.bukkit.block.*;
import org.bukkit.entity.*;
import org.bukkit.*;
import java.util.*;

public class Claim implements Iterable<Coordinate>
{
    private String world;
    private int x1;
    private int y1;
    private int z1;
    private int x2;
    private int y2;
    private int z2;
    private String name;
    
    public Claim(final Location corner1, final Location corner2) {
        this(corner1.getWorld().getName(), corner1.getBlockX(), corner1.getBlockY(), corner1.getBlockZ(), corner2.getBlockX(), corner2.getBlockY(), corner2.getBlockZ());
    }
    
    public Claim(final String world, final int x1, final int y1, final int z1, final int x2, final int y2, final int z2) {
        super();
        this.world = world;
        this.x1 = Math.min(x1, x2);
        this.x2 = Math.max(x1, x2);
        this.y1 = Math.min(y1, y2);
        this.y2 = Math.max(y1, y2);
        this.z1 = Math.min(z1, z2);
        this.z2 = Math.max(z1, z2);
    }
    
    public static int getPrice(final Claim claim, final faggot team, final boolean buying) {
        final int x = Math.abs(claim.x1 - claim.x2);
        final int z = Math.abs(claim.z1 - claim.z2);
        int blocks = x * z;
        int done = 0;
        double mod = 0.4;
        double curPrice = 0.0;
        while (blocks > 0) {
            --blocks;
            ++done;
            curPrice += mod;
            if (done == 250) {
                done = 0;
                mod += 0.4;
            }
        }
        curPrice /= 2.0;
        if (buying && team != null) {
            curPrice += 500 * team.getClaims().size();
        }
        return (int)curPrice;
    }
    
    @Override
    public boolean equals(final Object object) {
        if (!(object instanceof Claim)) {
            return false;
        }
        final Claim claim = (Claim)object;
        return claim.getMaximumPoint().equals((Object)this.getMaximumPoint()) && claim.getMinimumPoint().equals((Object)this.getMinimumPoint());
    }
    
    public Location getMinimumPoint() {
        return new Location(buttplug.fdsjfhkdsjfdsjhk().getServer().getWorld(this.world), (double)Math.min(this.x1, this.x2), (double)Math.min(this.y1, this.y2), (double)Math.min(this.z1, this.z2));
    }
    
    public Location getMaximumPoint() {
        return new Location(buttplug.fdsjfhkdsjfdsjhk().getServer().getWorld(this.world), (double)Math.max(this.x1, this.x2), (double)Math.max(this.y1, this.y2), (double)Math.max(this.z1, this.z2));
    }
    
    public boolean contains(final int x, final int y, final int z, final String world) {
        return y >= this.y1 && y <= this.y2 && this.contains(x, z, world);
    }
    
    public boolean contains(final int x, final int z, final String world) {
        return (world == null || world.equalsIgnoreCase(this.world)) && x >= this.x1 && x <= this.x2 && z >= this.z1 && z <= this.z2;
    }
    
    public boolean contains(final Location location) {
        return this.contains(location.getBlockX(), location.getBlockY(), location.getBlockZ(), location.getWorld().getName());
    }
    
    public boolean contains(final Block block) {
        return this.contains(block.getLocation());
    }
    
    public boolean contains(final Player player) {
        return this.contains(player.getLocation());
    }
    
    public Set<Player> getPlayers() {
        final Set<Player> players = new HashSet<Player>();
        for (final Player player : buttplug.fdsjfhkdsjfdsjhk().getServer().getOnlinePlayers()) {
            if (this.contains(player)) {
                players.add(player);
            }
        }
        return players;
    }
    
    @Override
    public int hashCode() {
        return this.getMaximumPoint().hashCode() + this.getMinimumPoint().hashCode();
    }
    
    @Override
    public String toString() {
        final Location corner1 = this.getMinimumPoint();
        final Location corner2 = this.getMaximumPoint();
        return corner1.getBlockX() + ":" + corner1.getBlockY() + ":" + corner1.getBlockZ() + ":" + corner2.getBlockX() + ":" + corner2.getBlockY() + ":" + corner2.getBlockZ() + ":" + this.name + ":" + this.world;
    }
    
    public String getFriendlyName() {
        return "(" + this.world + ", " + this.x1 + ", " + this.y1 + ", " + this.z1 + ") - (" + this.world + ", " + this.x2 + ", " + this.y2 + ", " + this.z2 + ")";
    }
    
    public Claim expand(final CuboidDirection dir, final int amount) {
        switch (dir) {
            case North: {
                return new Claim(this.world, this.x1 - amount, this.y1, this.z1, this.x2, this.y2, this.z2);
            }
            case South: {
                return new Claim(this.world, this.x1, this.y1, this.z1, this.x2 + amount, this.y2, this.z2);
            }
            case East: {
                return new Claim(this.world, this.x1, this.y1, this.z1 - amount, this.x2, this.y2, this.z2);
            }
            case West: {
                return new Claim(this.world, this.x1, this.y1, this.z1, this.x2, this.y2, this.z2 + amount);
            }
            case Down: {
                return new Claim(this.world, this.x1, this.y1 - amount, this.z1, this.x2, this.y2, this.z2);
            }
            case Up: {
                return new Claim(this.world, this.x1, this.y1, this.z1, this.x2, this.y2 + amount, this.z2);
            }
            default: {
                throw new IllegalArgumentException("Invalid direction " + dir);
            }
        }
    }
    
    public Claim outset(final CuboidDirection dir, final int amount) {
        Claim claim = null;
        switch (dir) {
            case Horizontal: {
                claim = this.expand(CuboidDirection.North, amount).expand(CuboidDirection.South, amount).expand(CuboidDirection.East, amount).expand(CuboidDirection.West, amount);
                break;
            }
            case Vertical: {
                claim = this.expand(CuboidDirection.Down, amount).expand(CuboidDirection.Up, amount);
                break;
            }
            case Both: {
                claim = this.outset(CuboidDirection.Horizontal, amount).outset(CuboidDirection.Vertical, amount);
                break;
            }
            default: {
                throw new IllegalArgumentException("Invalid direction " + dir);
            }
        }
        return claim;
    }
    
    public boolean isWithin(final int x, final int z, final int radius, final String world) {
        return this.outset(CuboidDirection.Both, radius).contains(x, z, world);
    }
    
    public Location[] getCornerLocations() {
        final World world = buttplug.fdsjfhkdsjfdsjhk().getServer().getWorld(this.world);
        return new Location[] { new Location(world, (double)this.x1, (double)this.y1, (double)this.z1), new Location(world, (double)this.x2, (double)this.y1, (double)this.z2), new Location(world, (double)this.x1, (double)this.y1, (double)this.z2), new Location(world, (double)this.x2, (double)this.y1, (double)this.z1) };
    }
    
    public Claim clone() {
        return new Claim(this.world, this.x1, this.y1, this.z1, this.x2, this.y2, this.z2);
    }
    
    @Override
    public Iterator<Coordinate> iterator() {
        return new BorderIterator(this.x1, this.z1, this.x2, this.z2);
    }
    
    public void setWorld(final String world) {
        this.world = world;
    }
    
    public void setX1(final int x1) {
        this.x1 = x1;
    }
    
    public void setY1(final int y1) {
        this.y1 = y1;
    }
    
    public void setZ1(final int z1) {
        this.z1 = z1;
    }
    
    public void setX2(final int x2) {
        this.x2 = x2;
    }
    
    public void setY2(final int y2) {
        this.y2 = y2;
    }
    
    public void setZ2(final int z2) {
        this.z2 = z2;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public Claim() {
        super();
    }
    
    public String getWorld() {
        return this.world;
    }
    
    public int getX1() {
        return this.x1;
    }
    
    public int getY1() {
        return this.y1;
    }
    
    public int getZ1() {
        return this.z1;
    }
    
    public int getX2() {
        return this.x2;
    }
    
    public int getY2() {
        return this.y2;
    }
    
    public int getZ2() {
        return this.z2;
    }
    
    public String getName() {
        return this.name;
    }
    
    public enum BorderDirection
    {
        POS_X, 
        POS_Z, 
        NEG_X, 
        NEG_Z;
    }
    
    public class BorderIterator implements Iterator<Coordinate>
    {
        private int x;
        private int z;
        private boolean next;
        private BorderDirection dir;
        int maxX;
        int maxZ;
        int minX;
        int minZ;
        
        public BorderIterator(final int x1, final int z1, final int x2, final int z2) {
            super();
            this.next = true;
            this.dir = BorderDirection.POS_Z;
            this.maxX = Claim.this.getMaximumPoint().getBlockX();
            this.maxZ = Claim.this.getMaximumPoint().getBlockZ();
            this.minX = Claim.this.getMinimumPoint().getBlockX();
            this.minZ = Claim.this.getMinimumPoint().getBlockZ();
            this.x = Math.min(x1, x2);
            this.z = Math.min(z1, z2);
        }
        
        @Override
        public boolean hasNext() {
            return this.next;
        }
        
        @Override
        public Coordinate next() {
            if (this.dir == BorderDirection.POS_Z) {
                if (++this.z == this.maxZ) {
                    this.dir = BorderDirection.POS_X;
                }
            }
            else if (this.dir == BorderDirection.POS_X) {
                if (++this.x == this.maxX) {
                    this.dir = BorderDirection.NEG_Z;
                }
            }
            else if (this.dir == BorderDirection.NEG_Z) {
                if (--this.z == this.minZ) {
                    this.dir = BorderDirection.NEG_X;
                }
            }
            else if (this.dir == BorderDirection.NEG_X && --this.x == this.minX) {
                this.next = false;
            }
            return new Coordinate(this.x, this.z);
        }
        
        @Override
        public void remove() {
        }
    }
    
    public enum CuboidDirection
    {
        North, 
        East, 
        South, 
        West, 
        Up, 
        Down, 
        Horizontal, 
        Vertical, 
        Both, 
        Unknown;
    }
}
