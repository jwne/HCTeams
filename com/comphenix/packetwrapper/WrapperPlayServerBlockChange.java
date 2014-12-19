package com.comphenix.packetwrapper;

import com.comphenix.protocol.*;
import com.comphenix.protocol.events.*;
import org.bukkit.*;

public class WrapperPlayServerBlockChange extends AbstractPacket
{
    public static final PacketType TYPE;
    
    public WrapperPlayServerBlockChange() {
        super(new PacketContainer(WrapperPlayServerBlockChange.TYPE), WrapperPlayServerBlockChange.TYPE);
        this.handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayServerBlockChange(final PacketContainer packet) {
        super(packet, WrapperPlayServerBlockChange.TYPE);
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
    
    public Material getBlockType() {
        return (Material)this.handle.getBlocks().read(0);
    }
    
    public void setBlockType(final Material value) {
        this.handle.getBlocks().write(0, (Object)value);
    }
    
    public byte getBlockMetadata() {
        return (byte)this.handle.getIntegers().read(3);
    }
    
    public void setBlockMetadata(final byte value) {
        this.handle.getIntegers().write(3, (Object)(int)value);
    }
    
    static {
        TYPE = PacketType.Play.Server.BLOCK_CHANGE;
    }
}
