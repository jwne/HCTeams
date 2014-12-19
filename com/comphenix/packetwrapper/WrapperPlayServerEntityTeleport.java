package com.comphenix.packetwrapper;

import com.comphenix.protocol.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import com.comphenix.protocol.events.*;

public class WrapperPlayServerEntityTeleport extends AbstractPacket
{
    public static final PacketType TYPE;
    
    public WrapperPlayServerEntityTeleport() {
        super(new PacketContainer(WrapperPlayServerEntityTeleport.TYPE), WrapperPlayServerEntityTeleport.TYPE);
        this.handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayServerEntityTeleport(final PacketContainer packet) {
        super(packet, WrapperPlayServerEntityTeleport.TYPE);
    }
    
    public int getEntityID() {
        return (int)this.handle.getIntegers().read(0);
    }
    
    public void setEntityID(final int value) {
        this.handle.getIntegers().write(0, (Object)value);
    }
    
    public Entity getEntity(final World world) {
        return (Entity)this.handle.getEntityModifier(world).read(0);
    }
    
    public Entity getEntity(final PacketEvent event) {
        return this.getEntity(event.getPlayer().getWorld());
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
    
    public float getYaw() {
        return (byte)this.handle.getBytes().read(0) * 360.0f / 256.0f;
    }
    
    public void setYaw(final float value) {
        this.handle.getBytes().write(0, (Object)(byte)(value * 256.0f / 360.0f));
    }
    
    public float getPitch() {
        return (byte)this.handle.getBytes().read(1) * 360.0f / 256.0f;
    }
    
    public void setPitch(final float value) {
        this.handle.getBytes().write(1, (Object)(byte)(value * 256.0f / 360.0f));
    }
    
    static {
        TYPE = PacketType.Play.Server.ENTITY_TELEPORT;
    }
}
