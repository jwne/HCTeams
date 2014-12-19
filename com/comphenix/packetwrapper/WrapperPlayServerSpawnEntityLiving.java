package com.comphenix.packetwrapper;

import com.comphenix.protocol.injector.*;
import com.comphenix.protocol.*;
import org.bukkit.*;
import com.comphenix.protocol.events.*;
import org.bukkit.entity.*;
import com.comphenix.protocol.wrappers.*;

public class WrapperPlayServerSpawnEntityLiving extends AbstractPacket
{
    public static final PacketType TYPE;
    private static PacketConstructor entityConstructor;
    
    public WrapperPlayServerSpawnEntityLiving() {
        super(new PacketContainer(WrapperPlayServerSpawnEntityLiving.TYPE), WrapperPlayServerSpawnEntityLiving.TYPE);
        this.handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayServerSpawnEntityLiving(final PacketContainer packet) {
        super(packet, WrapperPlayServerSpawnEntityLiving.TYPE);
    }
    
    public WrapperPlayServerSpawnEntityLiving(final Entity entity) {
        super(fromEntity(entity), WrapperPlayServerSpawnEntityLiving.TYPE);
    }
    
    private static PacketContainer fromEntity(final Entity entity) {
        if (WrapperPlayServerSpawnEntityLiving.entityConstructor == null) {
            WrapperPlayServerSpawnEntityLiving.entityConstructor = ProtocolLibrary.getProtocolManager().createPacketConstructor(WrapperPlayServerSpawnEntityLiving.TYPE, new Object[] { entity });
        }
        return WrapperPlayServerSpawnEntityLiving.entityConstructor.createPacket(new Object[] { entity });
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
    
    public EntityType getType() {
        return EntityType.fromId((int)this.handle.getIntegers().read(1));
    }
    
    public void setType(final EntityType value) {
        this.handle.getIntegers().write(1, (Object)(int)value.getTypeId());
    }
    
    public double getX() {
        return (int)this.handle.getIntegers().read(2) / 32.0;
    }
    
    public void setX(final double value) {
        this.handle.getIntegers().write(2, (Object)(int)Math.floor(value * 32.0));
    }
    
    public double getY() {
        return (int)this.handle.getIntegers().read(3) / 32.0;
    }
    
    public void setY(final double value) {
        this.handle.getIntegers().write(3, (Object)(int)Math.floor(value * 32.0));
    }
    
    public double getZ() {
        return (int)this.handle.getIntegers().read(4) / 32.0;
    }
    
    public void setZ(final double value) {
        this.handle.getIntegers().write(4, (Object)(int)Math.floor(value * 32.0));
    }
    
    public float getYaw() {
        return (byte)this.handle.getBytes().read(0) * 360.0f / 256.0f;
    }
    
    public void setYaw(final float value) {
        this.handle.getBytes().write(0, (Object)(byte)(value * 256.0f / 360.0f));
    }
    
    public float getHeadPitch() {
        return (byte)this.handle.getBytes().read(1) * 360.0f / 256.0f;
    }
    
    public void setHeadPitch(final float value) {
        this.handle.getBytes().write(1, (Object)(byte)(value * 256.0f / 360.0f));
    }
    
    public float getHeadYaw() {
        return (byte)this.handle.getBytes().read(2) * 360.0f / 256.0f;
    }
    
    public void setHeadYaw(final float value) {
        this.handle.getBytes().write(2, (Object)(byte)(value * 256.0f / 360.0f));
    }
    
    public double getVelocityX() {
        return (int)this.handle.getIntegers().read(5) / 8000.0;
    }
    
    public void setVelocityX(final double value) {
        this.handle.getIntegers().write(5, (Object)(int)(value * 8000.0));
    }
    
    public double getVelocityY() {
        return (int)this.handle.getIntegers().read(6) / 8000.0;
    }
    
    public void setVelocityY(final double value) {
        this.handle.getIntegers().write(6, (Object)(int)(value * 8000.0));
    }
    
    public double getVelocityZ() {
        return (int)this.handle.getIntegers().read(7) / 8000.0;
    }
    
    public void setVelocityZ(final double value) {
        this.handle.getIntegers().write(7, (Object)(int)(value * 8000.0));
    }
    
    public WrappedDataWatcher getMetadata() {
        return (WrappedDataWatcher)this.handle.getDataWatcherModifier().read(0);
    }
    
    public void setMetadata(final WrappedDataWatcher value) {
        this.handle.getDataWatcherModifier().write(0, (Object)value);
    }
    
    static {
        TYPE = PacketType.Play.Server.SPAWN_ENTITY_LIVING;
    }
}
