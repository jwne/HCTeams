package com.comphenix.packetwrapper;

import com.comphenix.protocol.*;
import com.comphenix.protocol.events.*;

public class WrapperPlayServerTabComplete extends AbstractPacket
{
    public static final PacketType TYPE;
    
    public WrapperPlayServerTabComplete() {
        super(new PacketContainer(WrapperPlayServerTabComplete.TYPE), WrapperPlayServerTabComplete.TYPE);
        this.handle.getModifier().writeDefaults();
    }
    
    public String[] getText() {
        return (String[])this.handle.getStringArrays().read(0);
    }
    
    public void setText(final String[] value) {
        this.handle.getStringArrays().write(0, (Object)value);
    }
    
    static {
        TYPE = PacketType.Play.Server.TAB_COMPLETE;
    }
}
