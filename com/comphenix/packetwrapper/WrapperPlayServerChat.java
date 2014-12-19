package com.comphenix.packetwrapper;

import com.comphenix.protocol.*;
import com.comphenix.protocol.events.*;
import com.comphenix.protocol.wrappers.*;

public class WrapperPlayServerChat extends AbstractPacket
{
    public static final PacketType TYPE;
    
    public WrapperPlayServerChat() {
        super(new PacketContainer(WrapperPlayServerChat.TYPE), WrapperPlayServerChat.TYPE);
        this.handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayServerChat(final PacketContainer packet) {
        super(packet, WrapperPlayServerChat.TYPE);
    }
    
    public WrappedChatComponent getMessage() {
        return (WrappedChatComponent)this.handle.getChatComponents().read(0);
    }
    
    public void setMessage(final WrappedChatComponent value) {
        this.handle.getChatComponents().write(0, (Object)value);
    }
    
    static {
        TYPE = PacketType.Play.Server.CHAT;
    }
}
