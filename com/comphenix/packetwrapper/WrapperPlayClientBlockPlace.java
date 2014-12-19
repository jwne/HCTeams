package com.comphenix.packetwrapper;

import com.comphenix.protocol.*;
import com.comphenix.protocol.events.*;
import org.bukkit.inventory.*;

public class WrapperPlayClientBlockPlace extends AbstractPacket
{
    public static final PacketType TYPE;
    
    public WrapperPlayClientBlockPlace() {
        super(new PacketContainer(WrapperPlayClientBlockPlace.TYPE), WrapperPlayClientBlockPlace.TYPE);
        this.handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayClientBlockPlace(final PacketContainer packet) {
        super(packet, WrapperPlayClientBlockPlace.TYPE);
    }
    
    public int getX() {
        return (int)this.handle.getIntegers().read(0);
    }
    
    public void setX(final int value) {
        this.handle.getIntegers().write(0, (Object)value);
    }
    
    public byte getY() {
        return (byte)this.handle.getIntegers().read(1);
    }
    
    public void setY(final byte value) {
        this.handle.getIntegers().write(1, (Object)(int)value);
    }
    
    public int getZ() {
        return (int)this.handle.getIntegers().read(2);
    }
    
    public void setZ(final int value) {
        this.handle.getIntegers().write(2, (Object)value);
    }
    
    public byte getDirection() {
        return (byte)this.handle.getIntegers().read(3);
    }
    
    public void setDirection(final byte value) {
        this.handle.getIntegers().write(3, (Object)(int)value);
    }
    
    public ItemStack getHeldItem() {
        return (ItemStack)this.handle.getItemModifier().read(0);
    }
    
    public void setHeldItem(final ItemStack value) {
        this.handle.getItemModifier().write(0, (Object)value);
    }
    
    public float getCursorPositionX() {
        return (float)this.handle.getFloat().read(0);
    }
    
    public void setCursorPositionX(final float value) {
        this.handle.getFloat().write(0, (Object)value);
    }
    
    public float getCursorPositionY() {
        return (float)this.handle.getFloat().read(1);
    }
    
    public void setCursorPositionY(final float value) {
        this.handle.getFloat().write(1, (Object)value);
    }
    
    public byte getCursorPositionZ() {
        return (byte)this.handle.getFloat().read(2);
    }
    
    public void setCursorPositionZ(final byte value) {
        this.handle.getFloat().write(2, (Object)(float)value);
    }
    
    static {
        TYPE = PacketType.Play.Client.BLOCK_PLACE;
    }
}
