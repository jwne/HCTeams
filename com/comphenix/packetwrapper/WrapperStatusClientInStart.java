package com.comphenix.packetwrapper;

import com.comphenix.protocol.*;
import com.comphenix.protocol.events.*;

public class WrapperStatusClientInStart extends AbstractPacket
{
    public static final PacketType TYPE;
    
    public WrapperStatusClientInStart() {
        super(new PacketContainer(WrapperStatusClientInStart.TYPE), WrapperStatusClientInStart.TYPE);
        this.handle.getModifier().writeDefaults();
    }
    
    public WrapperStatusClientInStart(final PacketContainer packet) {
        super(packet, WrapperStatusClientInStart.TYPE);
    }
    
    static {
        TYPE = PacketType.Status.Client.IN_START;
    }
}
