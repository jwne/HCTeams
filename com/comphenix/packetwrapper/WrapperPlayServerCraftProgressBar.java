package com.comphenix.packetwrapper;

import com.comphenix.protocol.*;
import com.comphenix.protocol.events.*;

public class WrapperPlayServerCraftProgressBar extends AbstractPacket
{
    public static final PacketType TYPE;
    
    public WrapperPlayServerCraftProgressBar() {
        super(new PacketContainer(WrapperPlayServerCraftProgressBar.TYPE), WrapperPlayServerCraftProgressBar.TYPE);
        this.handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayServerCraftProgressBar(final PacketContainer packet) {
        super(packet, WrapperPlayServerCraftProgressBar.TYPE);
    }
    
    public byte getWindowId() {
        return (byte)this.handle.getIntegers().read(0);
    }
    
    public void setWindowId(final byte value) {
        this.handle.getIntegers().write(0, (Object)(int)value);
    }
    
    public short getProperty() {
        return (short)this.handle.getIntegers().read(1);
    }
    
    public void setProperty(final short value) {
        this.handle.getIntegers().write(1, (Object)(int)value);
    }
    
    public short getValue() {
        return (short)this.handle.getIntegers().read(2);
    }
    
    public void setValue(final short value) {
        this.handle.getIntegers().write(2, (Object)(int)value);
    }
    
    static {
        TYPE = PacketType.Play.Server.CRAFT_PROGRESS_BAR;
    }
    
    public static class FurnaceProperties
    {
        public static final int PROGRESS_ARROW = 0;
        public static final int PROGRESS_FIRE_ICON = 1;
        private static FurnaceProperties INSTANCE;
        
        public static FurnaceProperties getInstace() {
            return FurnaceProperties.INSTANCE;
        }
        
        static {
            FurnaceProperties.INSTANCE = new FurnaceProperties();
        }
    }
}
