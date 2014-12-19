package com.comphenix.packetwrapper;

import com.comphenix.protocol.*;
import com.comphenix.protocol.events.*;

public class WrapperPlayServerPosition extends AbstractPacket
{
    public static final PacketType TYPE;
    
    public WrapperPlayServerPosition() {
        super(new PacketContainer(WrapperPlayServerPosition.TYPE), WrapperPlayServerPosition.TYPE);
        this.handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayServerPosition(final PacketContainer packet) {
        super(packet, WrapperPlayServerPosition.TYPE);
    }
    
    public double getX() {
        return (double)this.handle.getDoubles().read(0);
    }
    
    public void setX(final double value) {
        this.handle.getDoubles().write(0, (Object)value);
    }
    
    public double getY() {
        return (double)this.handle.getDoubles().read(1);
    }
    
    public void setY(final double value) {
        this.handle.getDoubles().write(1, (Object)value);
    }
    
    public double getZ() {
        return (double)this.handle.getDoubles().read(2);
    }
    
    public void setZ(final double value) {
        this.handle.getDoubles().write(2, (Object)value);
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
    
    public boolean getOnGround() {
        return (boolean)this.handle.getSpecificModifier((Class)Boolean.TYPE).read(0);
    }
    
    public void setOnGround(final boolean value) {
        this.handle.getSpecificModifier((Class)Boolean.TYPE).write(0, (Object)value);
    }
    
    static {
        TYPE = PacketType.Play.Server.POSITION;
    }
}
