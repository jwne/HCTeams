package com.comphenix.packetwrapper;

import com.comphenix.protocol.*;
import com.comphenix.protocol.events.*;
import com.comphenix.protocol.wrappers.*;

public class WrapperPlayServerKickDisconnect extends AbstractPacket
{
    public static final PacketType TYPE;
    
    public WrapperPlayServerKickDisconnect() {
        super(new PacketContainer(WrapperPlayServerKickDisconnect.TYPE), WrapperPlayServerKickDisconnect.TYPE);
        this.handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayServerKickDisconnect(final PacketContainer packet) {
        super(packet, WrapperPlayServerKickDisconnect.TYPE);
    }
    
    public WrappedChatComponent getReason() {
        return (WrappedChatComponent)this.handle.getChatComponents().read(0);
    }
    
    public void setReason(final WrappedChatComponent value) {
        this.handle.getChatComponents().write(0, (Object)value);
    }
    
    static {
        TYPE = PacketType.Play.Server.KICK_DISCONNECT;
    }
}
