package com.comphenix.packetwrapper;

import com.comphenix.protocol.*;
import com.comphenix.protocol.events.*;

public class WrapperPlayClientEnchantItem extends AbstractPacket
{
    public static final PacketType TYPE;
    
    public WrapperPlayClientEnchantItem() {
        super(new PacketContainer(WrapperPlayClientEnchantItem.TYPE), WrapperPlayClientEnchantItem.TYPE);
        this.handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayClientEnchantItem(final PacketContainer packet) {
        super(packet, WrapperPlayClientEnchantItem.TYPE);
    }
    
    public byte getWindowId() {
        return (byte)this.handle.getIntegers().read(0);
    }
    
    public void setWindowId(final byte value) {
        this.handle.getIntegers().write(0, (Object)(int)value);
    }
    
    public byte getEnchantment() {
        return (byte)this.handle.getIntegers().read(1);
    }
    
    public void setEnchantment(final byte value) {
        this.handle.getIntegers().write(1, (Object)(int)value);
    }
    
    static {
        TYPE = PacketType.Play.Client.ENCHANT_ITEM;
    }
}
