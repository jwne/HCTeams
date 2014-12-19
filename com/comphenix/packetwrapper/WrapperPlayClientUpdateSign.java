package com.comphenix.packetwrapper;

import com.comphenix.protocol.*;
import com.comphenix.protocol.events.*;
import org.bukkit.*;
import javax.annotation.*;

public class WrapperPlayClientUpdateSign extends AbstractPacket
{
    public static final PacketType TYPE;
    
    public WrapperPlayClientUpdateSign() {
        super(new PacketContainer(WrapperPlayClientUpdateSign.TYPE), WrapperPlayClientUpdateSign.TYPE);
        this.handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayClientUpdateSign(final PacketContainer packet) {
        super(packet, WrapperPlayClientUpdateSign.TYPE);
    }
    
    public int getX() {
        return (int)this.handle.getIntegers().read(0);
    }
    
    public void setX(final int value) {
        this.handle.getIntegers().write(0, (Object)value);
    }
    
    public short getY() {
        return (short)this.handle.getIntegers().read(1);
    }
    
    public void setY(final short value) {
        this.handle.getIntegers().write(1, (Object)(int)value);
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
        if (loc == null) {
            throw new IllegalArgumentException("Location cannot be NULL.");
        }
        this.setX(loc.getBlockX());
        this.setY((short)loc.getBlockY());
        this.setZ(loc.getBlockZ());
    }
    
    public String[] getLines() {
        return (String[])this.handle.getStringArrays().read(0);
    }
    
    public void setLines(@Nonnull final String[] lines) {
        if (lines == null) {
            throw new IllegalArgumentException("Array cannot be NULL.");
        }
        if (lines.length != 4) {
            throw new IllegalArgumentException("The lines array must be four elements long.");
        }
        this.handle.getStringArrays().write(0, (Object)lines);
    }
    
    static {
        TYPE = PacketType.Play.Client.UPDATE_SIGN;
    }
}
