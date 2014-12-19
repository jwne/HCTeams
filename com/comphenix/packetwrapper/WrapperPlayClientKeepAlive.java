package com.comphenix.packetwrapper;

import com.comphenix.protocol.*;
import com.comphenix.protocol.events.*;

public class WrapperPlayClientKeepAlive extends AbstractPacket
{
    public static final PacketType TYPE;
    
    public WrapperPlayClientKeepAlive() {
        super(new PacketContainer(WrapperPlayClientKeepAlive.TYPE), WrapperPlayClientKeepAlive.TYPE);
        this.handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayClientKeepAlive(final PacketContainer packet) {
        super(packet, WrapperPlayClientKeepAlive.TYPE);
    }
    
    public int getKeepAliveId() {
        return (int)this.handle.getIntegers().read(0);
    }
    
    public void setKeepAliveId(final int value) {
        this.handle.getIntegers().write(0, (Object)value);
    }
    
    static {
        TYPE = PacketType.Play.Client.KEEP_ALIVE;
    }
}
