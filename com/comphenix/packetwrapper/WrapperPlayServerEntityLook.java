package com.comphenix.packetwrapper;

import com.comphenix.protocol.*;
import com.comphenix.protocol.events.*;

public class WrapperPlayServerEntityLook extends WrapperPlayServerEntity
{
    public static final PacketType TYPE;
    
    public WrapperPlayServerEntityLook() {
        super(new PacketContainer(WrapperPlayServerEntityLook.TYPE), WrapperPlayServerEntityLook.TYPE);
        this.handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayServerEntityLook(final PacketContainer packet) {
        super(packet, WrapperPlayServerEntityLook.TYPE);
    }
    
    public float getYaw() {
        return (byte)this.handle.getBytes().read(3) * 360.0f / 256.0f;
    }
    
    public void setYaw(final float value) {
        this.handle.getBytes().write(3, (Object)(byte)(value * 256.0f / 360.0f));
    }
    
    public float getPitch() {
        return (byte)this.handle.getBytes().read(4) * 360.0f / 256.0f;
    }
    
    public void setPitch(final float value) {
        this.handle.getBytes().write(4, (Object)(byte)(value * 256.0f / 360.0f));
    }
    
    static {
        TYPE = PacketType.Play.Server.ENTITY_LOOK;
    }
}
