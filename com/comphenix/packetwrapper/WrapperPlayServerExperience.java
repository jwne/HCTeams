package com.comphenix.packetwrapper;

import com.comphenix.protocol.*;
import com.comphenix.protocol.events.*;

public class WrapperPlayServerExperience extends AbstractPacket
{
    public static final PacketType TYPE;
    
    public WrapperPlayServerExperience() {
        super(new PacketContainer(WrapperPlayServerExperience.TYPE), WrapperPlayServerExperience.TYPE);
        this.handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayServerExperience(final PacketContainer packet) {
        super(packet, WrapperPlayServerExperience.TYPE);
    }
    
    public float getExperienceBar() {
        return (float)this.handle.getFloat().read(0);
    }
    
    public void setExperienceBar(final float value) {
        this.handle.getFloat().write(0, (Object)value);
    }
    
    public short getLevel() {
        return (short)this.handle.getIntegers().read(1);
    }
    
    public void setLevel(final short value) {
        this.handle.getIntegers().write(1, (Object)(int)value);
    }
    
    public short getTotalExperience() {
        return (short)this.handle.getIntegers().read(0);
    }
    
    public void setTotalExperience(final short value) {
        this.handle.getIntegers().write(0, (Object)(int)value);
    }
    
    static {
        TYPE = PacketType.Play.Server.EXPERIENCE;
    }
}
