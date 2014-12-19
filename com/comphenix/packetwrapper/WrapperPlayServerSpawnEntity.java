package com.comphenix.packetwrapper;

import com.comphenix.protocol.injector.*;
import org.bukkit.entity.*;
import com.comphenix.protocol.*;
import org.bukkit.*;
import com.comphenix.protocol.events.*;
import com.comphenix.protocol.reflect.*;

public class WrapperPlayServerSpawnEntity extends AbstractPacket
{
    public static final PacketType TYPE;
    private static PacketConstructor entityConstructor;
    
    public WrapperPlayServerSpawnEntity() {
        super(new PacketContainer(WrapperPlayServerSpawnEntity.TYPE), WrapperPlayServerSpawnEntity.TYPE);
        this.handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayServerSpawnEntity(final PacketContainer packet) {
        super(packet, WrapperPlayServerSpawnEntity.TYPE);
    }
    
    public WrapperPlayServerSpawnEntity(final Entity entity, final int type, final int objectData) {
        super(fromEntity(entity, type, objectData), WrapperPlayServerSpawnEntity.TYPE);
    }
    
    private static PacketContainer fromEntity(final Entity entity, final int type, final int objectData) {
        if (WrapperPlayServerSpawnEntity.entityConstructor == null) {
            WrapperPlayServerSpawnEntity.entityConstructor = ProtocolLibrary.getProtocolManager().createPacketConstructor(WrapperPlayServerSpawnEntity.TYPE, new Object[] { entity, type, objectData });
        }
        return WrapperPlayServerSpawnEntity.entityConstructor.createPacket(new Object[] { entity, type, objectData });
    }
    
    public int getEntityID() {
        return (int)this.handle.getIntegers().read(0);
    }
    
    public Entity getEntity(final World world) {
        return (Entity)this.handle.getEntityModifier(world).read(0);
    }
    
    public Entity getEntity(final PacketEvent event) {
        return this.getEntity(event.getPlayer().getWorld());
    }
    
    public void setEntityID(final int value) {
        this.handle.getIntegers().write(0, (Object)value);
    }
    
    public int getType() {
        return (int)this.handle.getIntegers().read(9);
    }
    
    public void setType(final int value) {
        this.handle.getIntegers().write(9, (Object)value);
    }
    
    public double getX() {
        return (int)this.handle.getIntegers().read(1) / 32.0;
    }
    
    public void setX(final double value) {
        this.handle.getIntegers().write(1, (Object)(int)Math.floor(value * 32.0));
    }
    
    public double getY() {
        return (int)this.handle.getIntegers().read(2) / 32.0;
    }
    
    public void setY(final double value) {
        this.handle.getIntegers().write(2, (Object)(int)Math.floor(value * 32.0));
    }
    
    public double getZ() {
        return (int)this.handle.getIntegers().read(3) / 32.0;
    }
    
    public void setZ(final double value) {
        this.handle.getIntegers().write(3, (Object)(int)Math.floor(value * 32.0));
    }
    
    public double getOptionalSpeedX() {
        return (int)this.handle.getIntegers().read(4) / 8000.0;
    }
    
    public void setOptionalSpeedX(final double value) {
        this.handle.getIntegers().write(4, (Object)(int)(value * 8000.0));
    }
    
    public double getOptionalSpeedY() {
        return (int)this.handle.getIntegers().read(5) / 8000.0;
    }
    
    public void setOptionalSpeedY(final double value) {
        this.handle.getIntegers().write(5, (Object)(int)(value * 8000.0));
    }
    
    public double getOptionalSpeedZ() {
        return (int)this.handle.getIntegers().read(6) / 8000.0;
    }
    
    public void setOptionalSpeedZ(final double value) {
        this.handle.getIntegers().write(6, (Object)(int)(value * 8000.0));
    }
    
    public float getYaw() {
        return (int)this.handle.getIntegers().read(7) * 360.0f / 256.0f;
    }
    
    public void setYaw(final float value) {
        this.handle.getIntegers().write(7, (Object)(int)(value * 256.0f / 360.0f));
    }
    
    public float getPitch() {
        return (int)this.handle.getIntegers().read(8) * 360.0f / 256.0f;
    }
    
    public void setPitch(final float value) {
        this.handle.getIntegers().write(8, (Object)(int)(value * 256.0f / 360.0f));
    }
    
    public int getObjectData() {
        return (int)this.handle.getIntegers().read(10);
    }
    
    public void setObjectData(final int value) {
        this.handle.getIntegers().write(10, (Object)value);
    }
    
    static {
        TYPE = PacketType.Play.Server.SPAWN_ENTITY;
    }
    
    public static class ObjectTypes extends IntEnum
    {
        public static final int BOAT = 1;
        public static final int ITEM_STACK = 2;
        public static final int MINECART = 10;
        public static final int MINECART_STORAGE = 11;
        public static final int MINECART_POWERED = 12;
        public static final int ACTIVATED_TNT = 50;
        public static final int ENDER_CRYSTAL = 51;
        public static final int ARROW_PROJECTILE = 60;
        public static final int SNOWBALL_PROJECTILE = 61;
        public static final int EGG_PROJECTILE = 62;
        public static final int FIRE_BALL_GHAST = 63;
        public static final int FIRE_BALL_BLAZE = 64;
        public static final int THROWN_ENDERPEARL = 65;
        public static final int WITHER_SKULL = 66;
        public static final int FALLING_BLOCK = 70;
        public static final int ITEM_FRAME = 71;
        public static final int EYE_OF_ENDER = 72;
        public static final int THROWN_POTION = 73;
        public static final int FALLING_DRAGON_EGG = 74;
        public static final int THROWN_EXP_BOTTLE = 75;
        public static final int FISHING_FLOAT = 90;
        private static ObjectTypes INSTANCE;
        
        public static ObjectTypes getInstance() {
            return ObjectTypes.INSTANCE;
        }
        
        static {
            ObjectTypes.INSTANCE = new ObjectTypes();
        }
    }
}
