package com.comphenix.packetwrapper;

import com.comphenix.protocol.*;
import com.comphenix.protocol.events.*;

public class WrapperPlayServerHeldItemSlot extends AbstractPacket
{
    public static final PacketType TYPE;
    
    public WrapperPlayServerHeldItemSlot() {
        super(new PacketContainer(WrapperPlayServerHeldItemSlot.TYPE), WrapperPlayServerHeldItemSlot.TYPE);
        this.handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayServerHeldItemSlot(final PacketContainer packet) {
        super(packet, WrapperPlayServerHeldItemSlot.TYPE);
    }
    
    public short getSlotId() {
        return (short)this.handle.getIntegers().read(0);
    }
    
    public void setSlotId(final short value) {
        this.handle.getIntegers().write(0, (Object)(int)value);
    }
    
    static {
        TYPE = PacketType.Play.Server.HELD_ITEM_SLOT;
    }
}
