package com.comphenix.packetwrapper;

import com.comphenix.protocol.*;
import com.comphenix.protocol.events.*;
import com.comphenix.protocol.wrappers.*;

public class WrapperLoginServerDisconnect extends AbstractPacket
{
    public static final PacketType TYPE;
    
    public WrapperLoginServerDisconnect() {
        super(new PacketContainer(WrapperLoginServerDisconnect.TYPE), WrapperLoginServerDisconnect.TYPE);
        this.handle.getModifier().writeDefaults();
    }
    
    public WrapperLoginServerDisconnect(final PacketContainer packet) {
        super(packet, WrapperLoginServerDisconnect.TYPE);
    }
    
    public WrappedChatComponent getJsonData() {
        return (WrappedChatComponent)this.handle.getChatComponents().read(0);
    }
    
    public void setJsonData(final WrappedChatComponent value) {
        this.handle.getChatComponents().write(0, (Object)value);
    }
    
    static {
        TYPE = PacketType.Login.Server.DISCONNECT;
    }
}
