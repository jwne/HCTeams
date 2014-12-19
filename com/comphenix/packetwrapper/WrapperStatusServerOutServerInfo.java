package com.comphenix.packetwrapper;

import com.comphenix.protocol.*;
import com.comphenix.protocol.events.*;
import com.comphenix.protocol.wrappers.*;

public class WrapperStatusServerOutServerInfo extends AbstractPacket
{
    public static final PacketType TYPE;
    
    public WrapperStatusServerOutServerInfo() {
        super(new PacketContainer(WrapperStatusServerOutServerInfo.TYPE), WrapperStatusServerOutServerInfo.TYPE);
        this.handle.getModifier().writeDefaults();
    }
    
    public WrapperStatusServerOutServerInfo(final PacketContainer packet) {
        super(packet, WrapperStatusServerOutServerInfo.TYPE);
    }
    
    public WrappedServerPing getServerPing() {
        return (WrappedServerPing)this.handle.getServerPings().read(0);
    }
    
    public void setServerPing(final WrappedServerPing value) {
        this.handle.getServerPings().write(0, (Object)value);
    }
    
    static {
        TYPE = PacketType.Status.Server.OUT_SERVER_INFO;
    }
}
