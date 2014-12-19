package com.comphenix.packetwrapper;

import com.comphenix.protocol.*;
import com.comphenix.protocol.events.*;

public class WrapperPlayClientPosition extends WrapperPlayClientFlying
{
    public static final PacketType TYPE;
    
    public WrapperPlayClientPosition() {
        super(new PacketContainer(WrapperPlayClientPosition.TYPE), WrapperPlayClientPosition.TYPE);
        this.handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayClientPosition(final PacketContainer packet) {
        super(packet, WrapperPlayClientPosition.TYPE);
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
    
    public double getStance() {
        return (double)this.handle.getDoubles().read(3);
    }
    
    public void setStance(final double value) {
        this.handle.getDoubles().write(3, (Object)value);
    }
    
    public double getZ() {
        return (double)this.handle.getDoubles().read(2);
    }
    
    public void setZ(final double value) {
        this.handle.getDoubles().write(2, (Object)value);
    }
    
    static {
        TYPE = PacketType.Play.Client.POSITION;
    }
}
