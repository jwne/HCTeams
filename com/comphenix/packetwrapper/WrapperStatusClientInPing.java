package com.comphenix.packetwrapper;

import com.comphenix.protocol.*;
import com.comphenix.protocol.events.*;

public class WrapperStatusClientInPing extends AbstractPacket
{
    public static final PacketType TYPE;
    
    public WrapperStatusClientInPing() {
        super(new PacketContainer(WrapperStatusClientInPing.TYPE), WrapperStatusClientInPing.TYPE);
        this.handle.getModifier().writeDefaults();
    }
    
    public WrapperStatusClientInPing(final PacketContainer packet) {
        super(packet, WrapperStatusClientInPing.TYPE);
    }
    
    public long getToken() {
        return (long)this.handle.getLongs().read(0);
    }
    
    public void setToken(final long value) {
        this.handle.getLongs().write(0, (Object)value);
    }
    
    static {
        TYPE = PacketType.Status.Client.IN_PING;
    }
}
