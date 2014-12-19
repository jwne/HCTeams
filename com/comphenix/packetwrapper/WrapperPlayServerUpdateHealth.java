package com.comphenix.packetwrapper;

import com.comphenix.protocol.*;
import com.comphenix.protocol.events.*;

public class WrapperPlayServerUpdateHealth extends AbstractPacket
{
    public static final PacketType TYPE;
    
    public WrapperPlayServerUpdateHealth() {
        super(new PacketContainer(WrapperPlayServerUpdateHealth.TYPE), WrapperPlayServerUpdateHealth.TYPE);
        this.handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayServerUpdateHealth(final PacketContainer packet) {
        super(packet, WrapperPlayServerUpdateHealth.TYPE);
    }
    
    public float getHealth() {
        return (float)this.handle.getFloat().read(0);
    }
    
    public void setHealth(final float value) {
        this.handle.getFloat().write(0, (Object)value);
    }
    
    public short getFood() {
        return (short)this.handle.getIntegers().read(0);
    }
    
    public void setFood(final short value) {
        this.handle.getIntegers().write(0, (Object)(int)value);
    }
    
    public float getFoodSaturation() {
        return (float)this.handle.getFloat().read(1);
    }
    
    public void setFoodSaturation(final float value) {
        this.handle.getFloat().write(1, (Object)value);
    }
    
    static {
        TYPE = PacketType.Play.Server.UPDATE_HEALTH;
    }
}
