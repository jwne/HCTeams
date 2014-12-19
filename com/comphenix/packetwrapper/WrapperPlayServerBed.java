package com.comphenix.packetwrapper;

import com.comphenix.protocol.*;
import org.bukkit.entity.*;
import com.comphenix.protocol.events.*;
import org.bukkit.*;

public class WrapperPlayServerBed extends AbstractPacket
{
    public static final PacketType TYPE;
    
    public WrapperPlayServerBed() {
        super(new PacketContainer(WrapperPlayServerBed.TYPE), WrapperPlayServerBed.TYPE);
        this.handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayServerBed(final PacketContainer packet) {
        super(packet, WrapperPlayServerBed.TYPE);
    }
    
    public int getEntityId() {
        return (int)this.handle.getIntegers().read(0);
    }
    
    public void setEntityId(final int value) {
        this.handle.getIntegers().write(0, (Object)value);
    }
    
    public Entity getEntity(final World world) {
        return (Entity)this.handle.getEntityModifier(world).read(0);
    }
    
    public Entity getEntity(final PacketEvent event) {
        return this.getEntity(event.getPlayer().getWorld());
    }
    
    public int getX() {
        return (int)this.handle.getIntegers().read(1);
    }
    
    public void setX(final int value) {
        this.handle.getIntegers().write(1, (Object)value);
    }
    
    public byte getY() {
        return (byte)this.handle.getIntegers().read(2);
    }
    
    public void setY(final byte value) {
        this.handle.getIntegers().write(2, (Object)(int)value);
    }
    
    public int getZ() {
        return (int)this.handle.getIntegers().read(3);
    }
    
    public void setZ(final int value) {
        this.handle.getIntegers().write(3, (Object)value);
    }
    
    public Location getLocation(final PacketEvent event) {
        return new Location(event.getPlayer().getWorld(), (double)this.getX(), (double)this.getY(), (double)this.getZ());
    }
    
    public void setLocation(final Location loc) {
        this.setX(loc.getBlockX());
        this.setY((byte)loc.getBlockY());
        this.setZ(loc.getBlockZ());
    }
    
    static {
        TYPE = PacketType.Play.Server.BED;
    }
}
