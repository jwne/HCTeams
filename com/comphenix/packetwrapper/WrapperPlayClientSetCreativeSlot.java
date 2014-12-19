package com.comphenix.packetwrapper;

import com.comphenix.protocol.*;
import com.comphenix.protocol.events.*;
import org.bukkit.inventory.*;

public class WrapperPlayClientSetCreativeSlot extends AbstractPacket
{
    public static final PacketType TYPE;
    
    public WrapperPlayClientSetCreativeSlot() {
        super(new PacketContainer(WrapperPlayClientSetCreativeSlot.TYPE), WrapperPlayClientSetCreativeSlot.TYPE);
        this.handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayClientSetCreativeSlot(final PacketContainer packet) {
        super(packet, WrapperPlayClientSetCreativeSlot.TYPE);
    }
    
    public short getSlot() {
        return (short)this.handle.getIntegers().read(0);
    }
    
    public void setSlot(final short value) {
        this.handle.getIntegers().write(0, (Object)(int)value);
    }
    
    public ItemStack getClickedItem() {
        return (ItemStack)this.handle.getItemModifier().read(0);
    }
    
    public void setClickedItem(final ItemStack value) {
        this.handle.getItemModifier().write(0, (Object)value);
    }
    
    static {
        TYPE = PacketType.Play.Client.SET_CREATIVE_SLOT;
    }
}
