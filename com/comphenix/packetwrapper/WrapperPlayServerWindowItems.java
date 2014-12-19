package com.comphenix.packetwrapper;

import com.comphenix.protocol.*;
import com.comphenix.protocol.events.*;
import org.bukkit.inventory.*;

public class WrapperPlayServerWindowItems extends AbstractPacket
{
    public static final PacketType TYPE;
    
    public WrapperPlayServerWindowItems() {
        super(new PacketContainer(WrapperPlayServerWindowItems.TYPE), WrapperPlayServerWindowItems.TYPE);
        this.handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayServerWindowItems(final PacketContainer packet) {
        super(packet, WrapperPlayServerWindowItems.TYPE);
    }
    
    public byte getWindowId() {
        return (byte)this.handle.getIntegers().read(0);
    }
    
    public void setWindowId(final byte value) {
        this.handle.getIntegers().write(0, (Object)(int)value);
    }
    
    public ItemStack[] getItems() {
        return (ItemStack[])this.handle.getItemArrayModifier().read(0);
    }
    
    public void setItems(final ItemStack[] value) {
        this.handle.getItemArrayModifier().write(0, (Object)value);
    }
    
    static {
        TYPE = PacketType.Play.Server.WINDOW_ITEMS;
    }
}
