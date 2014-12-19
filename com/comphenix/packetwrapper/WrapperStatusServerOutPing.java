package com.comphenix.packetwrapper;

import com.comphenix.protocol.*;
import com.comphenix.protocol.events.*;

public class WrapperStatusServerOutPing extends AbstractPacket
{
    public static final PacketType TYPE;
    
    public WrapperStatusServerOutPing() {
        super(new PacketContainer(WrapperStatusServerOutPing.TYPE), WrapperStatusServerOutPing.TYPE);
        this.handle.getModifier().writeDefaults();
    }
    
    public WrapperStatusServerOutPing(final PacketContainer packet) {
        super(packet, WrapperStatusServerOutPing.TYPE);
    }
    
    public long getTime() {
        return (long)this.handle.getLongs().read(0);
    }
    
    public void setToken(final long value) {
        this.handle.getLongs().write(0, (Object)value);
    }
    
    static {
        TYPE = PacketType.Status.Server.OUT_PING;
    }
}
