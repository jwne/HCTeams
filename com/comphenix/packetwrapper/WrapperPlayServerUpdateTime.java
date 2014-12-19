package com.comphenix.packetwrapper;

import com.comphenix.protocol.*;
import com.comphenix.protocol.events.*;

public class WrapperPlayServerUpdateTime extends AbstractPacket
{
    public static final PacketType TYPE;
    
    public WrapperPlayServerUpdateTime() {
        super(new PacketContainer(WrapperPlayServerUpdateTime.TYPE), WrapperPlayServerUpdateTime.TYPE);
        this.handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayServerUpdateTime(final PacketContainer packet) {
        super(packet, WrapperPlayServerUpdateTime.TYPE);
    }
    
    public long getAgeOfTheWorld() {
        return (long)this.handle.getLongs().read(0);
    }
    
    public void setAgeOfTheWorld(final long value) {
        this.handle.getLongs().write(0, (Object)value);
    }
    
    public long getTimeOfDay() {
        return (long)this.handle.getLongs().read(1);
    }
    
    public void setTimeOfDay(final long value) {
        this.handle.getLongs().write(1, (Object)value);
    }
    
    static {
        TYPE = PacketType.Play.Server.UPDATE_TIME;
    }
}
