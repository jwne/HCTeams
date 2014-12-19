package com.comphenix.packetwrapper;

import com.comphenix.protocol.*;
import com.comphenix.protocol.events.*;

public class WrapperPlayClientLook extends WrapperPlayClientFlying
{
    public static final PacketType TYPE;
    
    public WrapperPlayClientLook() {
        super(new PacketContainer(WrapperPlayClientLook.TYPE), WrapperPlayClientLook.TYPE);
        this.handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayClientLook(final PacketContainer packet) {
        super(packet, WrapperPlayClientLook.TYPE);
    }
    
    public float getYaw() {
        return (float)this.handle.getFloat().read(0);
    }
    
    public void setYaw(final float value) {
        this.handle.getFloat().write(0, (Object)value);
    }
    
    public float getPitch() {
        return (float)this.handle.getFloat().read(1);
    }
    
    public void setPitch(final float value) {
        this.handle.getFloat().write(1, (Object)value);
    }
    
    static {
        TYPE = PacketType.Play.Client.LOOK;
    }
}
