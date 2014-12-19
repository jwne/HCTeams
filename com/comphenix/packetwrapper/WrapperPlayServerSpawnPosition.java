package com.comphenix.packetwrapper;

import com.comphenix.protocol.*;
import com.comphenix.protocol.events.*;
import org.bukkit.util.*;

public class WrapperPlayServerSpawnPosition extends AbstractPacket
{
    public static final PacketType TYPE;
    
    public WrapperPlayServerSpawnPosition() {
        super(new PacketContainer(WrapperPlayServerSpawnPosition.TYPE), WrapperPlayServerSpawnPosition.TYPE);
        this.handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayServerSpawnPosition(final PacketContainer packet) {
        super(packet, WrapperPlayServerSpawnPosition.TYPE);
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
    
    public void setLocation(final Vector point) {
        this.setX(point.getBlockX());
        this.setY(point.getBlockY());
        this.setZ(point.getBlockZ());
    }
    
    public Vector getLocation() {
        return new Vector(this.getX(), this.getY(), this.getZ());
    }
    
    static {
        TYPE = PacketType.Play.Server.SPAWN_POSITION;
    }
}
