package com.comphenix.packetwrapper;

import com.comphenix.protocol.*;
import com.comphenix.protocol.events.*;
import org.bukkit.*;
import com.comphenix.protocol.reflect.*;

public class WrapperPlayServerWorldEvent extends AbstractPacket
{
    public static final PacketType TYPE;
    
    public WrapperPlayServerWorldEvent() {
        super(new PacketContainer(WrapperPlayServerWorldEvent.TYPE), WrapperPlayServerWorldEvent.TYPE);
        this.handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayServerWorldEvent(final PacketContainer packet) {
        super(packet, WrapperPlayServerWorldEvent.TYPE);
    }
    
    public int getEffectId() {
        return (int)this.handle.getIntegers().read(0);
    }
    
    public void setEffectId(final int value) {
        this.handle.getIntegers().write(0, (Object)value);
    }
    
    public int getX() {
        return (int)this.handle.getIntegers().read(2);
    }
    
    public void setX(final int value) {
        this.handle.getIntegers().write(2, (Object)value);
    }
    
    public int getY() {
        return (int)this.handle.getIntegers().read(3);
    }
    
    public void setY(final int value) {
        this.handle.getIntegers().write(3, (Object)value);
    }
    
    public int getZ() {
        return (int)this.handle.getIntegers().read(4);
    }
    
    public void setZ(final int value) {
        this.handle.getIntegers().write(4, (Object)value);
    }
    
    public Location getLocation(final World world) {
        return new Location(world, (double)this.getX(), (double)this.getY(), (double)this.getZ());
    }
    
    public void setLocation(final Location loc) {
        this.setX(loc.getBlockX());
        this.setY(loc.getBlockY());
        this.setZ(loc.getBlockZ());
    }
    
    public int getData() {
        return (int)this.handle.getIntegers().read(1);
    }
    
    public void setData(final int value) {
        this.handle.getIntegers().write(1, (Object)value);
    }
    
    public boolean getDisableRelativeVolume() {
        return (boolean)this.handle.getSpecificModifier((Class)Boolean.TYPE).read(0);
    }
    
    public void setDisableRelativeVolume(final boolean value) {
        this.handle.getSpecificModifier((Class)Boolean.TYPE).write(0, (Object)value);
    }
    
    static {
        TYPE = PacketType.Play.Server.WORLD_EVENT;
    }
    
    public static class SoundEffects extends IntEnum
    {
        public static final int RANDOM_CLICK_1 = 1000;
        public static final int RANDOM_CLICK_2 = 1001;
        public static final int RANDOM_BOW = 1002;
        public static final int RANDOM_DOOR = 1003;
        public static final int RANDOM_FIZZ = 1004;
        public static final int PLAY_MUSIC_DISK = 1005;
        public static final int MOB_GHAST_CHARGE = 1007;
        public static final int MOB_GHAST_FIREBALL_QUIET = 1009;
        public static final int MOB_GHAST_FIREBALL = 1008;
        public static final int MOB_ZOMBIE_WOOD = 1010;
        public static final int MOB_ZOMBIE_METAL = 1011;
        public static final int MOB_ZOMBIE_WOODBREAK = 1012;
        public static final int MOB_WITHER_SPAWN = 1013;
        public static final int MOB_WITHER_SHOOT = 1014;
        public static final int MOB_BAT_TAKEOFF = 1015;
        public static final int MOB_ZOMBIE_INFECT = 1016;
        public static final int MOB_ZOMBIE_UNFECT = 1017;
        public static final int MOB_ENDER_DRAGON_END = 1018;
        public static final int RANDOM_ANVIL_BREAK = 1020;
        public static final int RANDOM_ANVIL_USE = 1021;
        public static final int RANDOM_ANVIL_LAND = 1022;
        private static final SoundEffects INSTANCE;
        
        public static SoundEffects getInstance() {
            return SoundEffects.INSTANCE;
        }
        
        static {
            INSTANCE = new SoundEffects();
        }
    }
    
    public static class ParticleEffects extends IntEnum
    {
        public static final int SPAWN_SMOKE_PARTICLES = 2000;
        public static final int BLOCK_BREAK = 2001;
        public static final int SPLASH_POTION = 2002;
        public static final int EYE_OF_ENDER = 2003;
        public static final int MOB_SPAWN_EFFECT = 2004;
        public static final int HAPPY_VILLAGER = 2005;
        public static final int FALL_PARTICLES = 2006;
        private static final ParticleEffects INSTANCE;
        
        public static ParticleEffects getInstance() {
            return ParticleEffects.INSTANCE;
        }
        
        static {
            INSTANCE = new ParticleEffects();
        }
    }
    
    public static class SmokeDirections extends IntEnum
    {
        public static final int SOUTH_EAST = 0;
        public static final int SOUTH = 1;
        public static final int SOUTH_WEST = 2;
        public static final int EAST = 3;
        public static final int UP = 4;
        public static final int WEST = 5;
        public static final int NORTH_EAST = 6;
        public static final int NORTH = 7;
        public static final int NORTH_WEST = 8;
        private static final SmokeDirections INSTANCE;
        
        public static SmokeDirections getInstance() {
            return SmokeDirections.INSTANCE;
        }
        
        static {
            INSTANCE = new SmokeDirections();
        }
    }
}
