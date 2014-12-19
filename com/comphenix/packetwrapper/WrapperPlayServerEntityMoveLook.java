package com.comphenix.packetwrapper;

import com.comphenix.protocol.*;
import com.comphenix.protocol.events.*;

public class WrapperPlayServerEntityMoveLook extends WrapperPlayServerEntity
{
    public static final PacketType TYPE;
    
    public WrapperPlayServerEntityMoveLook() {
        super(new PacketContainer(WrapperPlayServerEntityMoveLook.TYPE), WrapperPlayServerEntityMoveLook.TYPE);
        this.handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayServerEntityMoveLook(final PacketContainer packet) {
        super(packet, WrapperPlayServerEntityMoveLook.TYPE);
    }
    
    public double getDx() {
        return (byte)this.handle.getBytes().read(0) / 32.0;
    }
    
    public void setDx(final double value) {
        if (Math.abs(value) > 4.0) {
            throw new IllegalArgumentException("Displacement cannot exceed 4 meters.");
        }
        this.handle.getBytes().write(0, (Object)(byte)Math.min(Math.floor(value * 32.0), 127.0));
    }
    
    public double getDy() {
        return (byte)this.handle.getBytes().read(1) / 32.0;
    }
    
    public void setDy(final double value) {
        if (Math.abs(value) > 4.0) {
            throw new IllegalArgumentException("Displacement cannot exceed 4 meters.");
        }
        this.handle.getBytes().write(1, (Object)(byte)Math.min(Math.floor(value * 32.0), 127.0));
    }
    
    public double getDz() {
        return (byte)this.handle.getBytes().read(2) / 32.0;
    }
    
    public void setDz(final double value) {
        if (Math.abs(value) > 4.0) {
            throw new IllegalArgumentException("Displacement cannot exceed 4 meters.");
        }
        this.handle.getBytes().write(2, (Object)(byte)Math.min(Math.floor(value * 32.0), 127.0));
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
        TYPE = PacketType.Play.Server.ENTITY_MOVE_LOOK;
    }
}
