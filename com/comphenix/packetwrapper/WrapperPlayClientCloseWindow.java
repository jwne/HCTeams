package com.comphenix.packetwrapper;

import com.comphenix.protocol.*;
import com.comphenix.protocol.events.*;

public class WrapperPlayClientCloseWindow extends AbstractPacket
{
    public static final PacketType TYPE;
    
    public WrapperPlayClientCloseWindow() {
        super(new PacketContainer(WrapperPlayClientCloseWindow.TYPE), WrapperPlayClientCloseWindow.TYPE);
        this.handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayClientCloseWindow(final PacketContainer packet) {
        super(packet, WrapperPlayClientCloseWindow.TYPE);
    }
    
    public byte getWindowId() {
        return (byte)this.handle.getIntegers().read(0);
    }
    
    public void setWindowId(final byte value) {
        this.handle.getIntegers().write(0, (Object)(int)value);
    }
    
    static {
        TYPE = PacketType.Play.Client.CLOSE_WINDOW;
    }
}
