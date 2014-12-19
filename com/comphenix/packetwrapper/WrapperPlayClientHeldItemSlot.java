package com.comphenix.packetwrapper;

import com.comphenix.protocol.*;
import com.comphenix.protocol.events.*;

public class WrapperPlayClientHeldItemSlot extends AbstractPacket
{
    public static final PacketType TYPE;
    
    public WrapperPlayClientHeldItemSlot() {
        super(new PacketContainer(WrapperPlayClientHeldItemSlot.TYPE), WrapperPlayClientHeldItemSlot.TYPE);
        this.handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayClientHeldItemSlot(final PacketContainer packet) {
        super(packet, WrapperPlayClientHeldItemSlot.TYPE);
    }
    
    public short getSlot() {
        return (short)this.handle.getIntegers().read(0);
    }
    
    public void setSlot(final short value) {
        this.handle.getIntegers().write(0, (Object)(int)value);
    }
    
    static {
        TYPE = PacketType.Play.Client.HELD_ITEM_SLOT;
    }
}
