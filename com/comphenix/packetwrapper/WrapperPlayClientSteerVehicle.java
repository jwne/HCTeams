package com.comphenix.packetwrapper;

import com.comphenix.protocol.*;
import com.comphenix.protocol.events.*;

public class WrapperPlayClientSteerVehicle extends AbstractPacket
{
    public static final PacketType TYPE;
    
    public WrapperPlayClientSteerVehicle() {
        super(new PacketContainer(WrapperPlayClientSteerVehicle.TYPE), WrapperPlayClientSteerVehicle.TYPE);
        this.handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayClientSteerVehicle(final PacketContainer packet) {
        super(packet, WrapperPlayClientSteerVehicle.TYPE);
    }
    
    public float getSideways() {
        return (float)this.handle.getFloat().read(0);
    }
    
    public void setSideways(final float value) {
        this.handle.getFloat().write(0, (Object)value);
    }
    
    public float getForward() {
        return (float)this.handle.getFloat().read(1);
    }
    
    public void setForward(final float value) {
        this.handle.getFloat().write(1, (Object)value);
    }
    
    public boolean getJump() {
        return (boolean)this.handle.getSpecificModifier((Class)Boolean.TYPE).read(0);
    }
    
    public void setJump(final boolean value) {
        this.handle.getSpecificModifier((Class)Boolean.TYPE).write(0, (Object)value);
    }
    
    public boolean getUnmount() {
        return (boolean)this.handle.getSpecificModifier((Class)Boolean.TYPE).read(1);
    }
    
    public void setUnmount(final boolean value) {
        this.handle.getSpecificModifier((Class)Boolean.TYPE).write(1, (Object)value);
    }
    
    static {
        TYPE = PacketType.Play.Client.STEER_VEHICLE;
    }
}
