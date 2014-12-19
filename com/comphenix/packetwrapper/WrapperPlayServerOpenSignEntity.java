package com.comphenix.packetwrapper;

import com.comphenix.protocol.*;
import com.comphenix.protocol.events.*;
import org.bukkit.*;

public class WrapperPlayServerOpenSignEntity extends AbstractPacket
{
    public static final PacketType TYPE;
    
    public WrapperPlayServerOpenSignEntity() {
        super(new PacketContainer(WrapperPlayServerOpenSignEntity.TYPE), WrapperPlayServerOpenSignEntity.TYPE);
        this.handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayServerOpenSignEntity(final PacketContainer packet) {
        super(packet, WrapperPlayServerOpenSignEntity.TYPE);
    }
    
    public int getX() {
        return (int)this.handle.getIntegers().read(0);
    }
    
    public void setX(final int value) {
        this.handle.getIntegers().write(0, (Object)value);
    }
    
    public int getY() {
        return (int)this.handle.getIntegers().read(1);
    }
    
    public void setY(final int value) {
        this.handle.getIntegers().write(1, (Object)value);
    }
    
    public int getZ() {
        return (int)this.handle.getIntegers().read(2);
    }
    
    public void setZ(final int value) {
        this.handle.getIntegers().write(2, (Object)value);
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
        TYPE = PacketType.Play.Server.OPEN_SIGN_ENTITY;
    }
}
