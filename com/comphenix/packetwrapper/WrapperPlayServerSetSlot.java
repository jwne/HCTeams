package com.comphenix.packetwrapper;

import com.comphenix.protocol.*;
import com.comphenix.protocol.events.*;
import org.bukkit.inventory.*;

public class WrapperPlayServerSetSlot extends AbstractPacket
{
    public static final PacketType TYPE;
    
    public WrapperPlayServerSetSlot() {
        super(new PacketContainer(WrapperPlayServerSetSlot.TYPE), WrapperPlayServerSetSlot.TYPE);
        this.handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayServerSetSlot(final PacketContainer packet) {
        super(packet, WrapperPlayServerSetSlot.TYPE);
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
    
    public ItemStack getSlotData() {
        return (ItemStack)this.handle.getItemModifier().read(0);
    }
    
    public void setSlotData(final ItemStack value) {
        this.handle.getItemModifier().write(0, (Object)value);
    }
    
    static {
        TYPE = PacketType.Play.Server.SET_SLOT;
    }
}
