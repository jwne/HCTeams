package com.comphenix.packetwrapper;

import com.comphenix.protocol.*;
import com.comphenix.protocol.events.*;

public class WrapperPlayClientChat extends AbstractPacket
{
    public static final PacketType TYPE;
    
    public WrapperPlayClientChat() {
        super(new PacketContainer(WrapperPlayClientChat.TYPE), WrapperPlayClientChat.TYPE);
        this.handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayClientChat(final PacketContainer packet) {
        super(packet, WrapperPlayClientChat.TYPE);
    }
    
    public String getMessage() {
        return (String)this.handle.getStrings().read(0);
    }
    
    public void setMessage(final String value) {
        this.handle.getStrings().write(0, (Object)value);
    }
    
    static {
        TYPE = PacketType.Play.Client.CHAT;
    }
}
