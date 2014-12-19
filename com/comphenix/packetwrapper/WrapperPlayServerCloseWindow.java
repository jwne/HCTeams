package com.comphenix.packetwrapper;

import com.comphenix.protocol.*;
import com.comphenix.protocol.events.*;

public class WrapperPlayServerCloseWindow extends AbstractPacket
{
    public static final PacketType TYPE;
    
    public WrapperPlayServerCloseWindow() {
        super(new PacketContainer(WrapperPlayServerCloseWindow.TYPE), WrapperPlayServerCloseWindow.TYPE);
        this.handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayServerCloseWindow(final PacketContainer packet) {
        super(packet, WrapperPlayServerCloseWindow.TYPE);
    }
    
    public byte getWindowId() {
        return (byte)this.handle.getIntegers().read(0);
    }
    
    public void setWindowId(final byte value) {
        this.handle.getIntegers().write(0, (Object)(int)value);
    }
    
    static {
        TYPE = PacketType.Play.Server.CLOSE_WINDOW;
    }
}
