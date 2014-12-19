package com.comphenix.packetwrapper;

import com.comphenix.protocol.*;
import com.comphenix.protocol.events.*;

public class WrapperPlayServerSpawnEntityWeather extends AbstractPacket
{
    public static final PacketType TYPE;
    
    public WrapperPlayServerSpawnEntityWeather() {
        super(new PacketContainer(WrapperPlayServerSpawnEntityWeather.TYPE), WrapperPlayServerSpawnEntityWeather.TYPE);
        this.handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayServerSpawnEntityWeather(final PacketContainer packet) {
        super(packet, WrapperPlayServerSpawnEntityWeather.TYPE);
    }
    
    public int getEntityId() {
        return (int)this.handle.getIntegers().read(0);
    }
    
    public void setEntityId(final int value) {
        this.handle.getIntegers().write(0, (Object)value);
    }
    
    public byte getType() {
        return (byte)this.handle.getIntegers().read(4);
    }
    
    public void setType(final byte value) {
        this.handle.getIntegers().write(4, (Object)(int)value);
    }
    
    public double getX() {
        return (int)this.handle.getIntegers().read(1) / 32.0;
    }
    
    public void setX(final double value) {
        this.handle.getIntegers().write(1, (Object)(int)(value * 32.0));
    }
    
    public double getY() {
        return (int)this.handle.getIntegers().read(2) / 32.0;
    }
    
    public void setY(final double value) {
        this.handle.getIntegers().write(2, (Object)(int)(value * 32.0));
    }
    
    public double getZ() {
        return (int)this.handle.getIntegers().read(3) / 32.0;
    }
    
    public void setZ(final double value) {
        this.handle.getIntegers().write(3, (Object)(int)(value * 32.0));
    }
    
    static {
        TYPE = PacketType.Play.Server.SPAWN_ENTITY_WEATHER;
    }
}
