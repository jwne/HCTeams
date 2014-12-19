package com.comphenix.packetwrapper;

import com.comphenix.protocol.*;
import com.comphenix.protocol.events.*;
import org.bukkit.inventory.*;

public class WrapperPlayClientWindowClick extends AbstractPacket
{
    public static final PacketType TYPE;
    
    public WrapperPlayClientWindowClick() {
        super(new PacketContainer(WrapperPlayClientWindowClick.TYPE), WrapperPlayClientWindowClick.TYPE);
        this.handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayClientWindowClick(final PacketContainer packet) {
        super(packet, WrapperPlayClientWindowClick.TYPE);
    }
    
    public byte getWindowId() {
        return (byte)this.handle.getIntegers().read(0);
    }
    
    public void setWindowId(final byte value) {
        this.handle.getIntegers().write(0, (Object)(int)value);
    }
    
    public short getSlot() {
        return (short)this.handle.getIntegers().read(1);
    }
    
    public void setSlot(final short value) {
        this.handle.getIntegers().write(1, (Object)(int)value);
    }
    
    public byte getMouseButton() {
        return (byte)this.handle.getIntegers().read(2);
    }
    
    public void setMouseButton(final byte value) {
        this.handle.getIntegers().write(2, (Object)(int)value);
    }
    
    public short getActionNumber() {
        return (short)this.handle.getShorts().read(0);
    }
    
    public void setActionNumber(final short value) {
        this.handle.getShorts().write(0, (Object)value);
    }
    
    public int getMode() {
        return (int)this.handle.getIntegers().read(3);
    }
    
    public void setMode(final int mode) {
        this.handle.getIntegers().write(3, (Object)mode);
    }
    
    public ItemStack getClickedItem() {
        return (ItemStack)this.handle.getItemModifier().read(0);
    }
    
    public void setClickedItem(final ItemStack value) {
        this.handle.getItemModifier().write(0, (Object)value);
    }
    
    static {
        TYPE = PacketType.Play.Client.WINDOW_CLICK;
    }
}
