package com.comphenix.packetwrapper;

import com.comphenix.protocol.*;
import com.comphenix.protocol.events.*;

public class WrapperPlayClientTabComplete extends AbstractPacket
{
    public static final PacketType TYPE;
    
    public WrapperPlayClientTabComplete() {
        super(new PacketContainer(WrapperPlayClientTabComplete.TYPE), WrapperPlayClientTabComplete.TYPE);
        this.handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayClientTabComplete(final PacketContainer packet) {
        super(packet, WrapperPlayClientTabComplete.TYPE);
    }
    
    public String getText() {
        return (String)this.handle.getStrings().read(0);
    }
    
    public void setText(final String value) {
        this.handle.getStrings().write(0, (Object)value);
    }
    
    static {
        TYPE = PacketType.Play.Client.TAB_COMPLETE;
    }
}
