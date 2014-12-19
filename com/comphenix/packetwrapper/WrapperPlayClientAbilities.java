package com.comphenix.packetwrapper;

import com.comphenix.protocol.*;
import com.comphenix.protocol.events.*;

public class WrapperPlayClientAbilities extends AbstractPacket
{
    public static final PacketType TYPE;
    
    public WrapperPlayClientAbilities() {
        super(new PacketContainer(WrapperPlayClientAbilities.TYPE), WrapperPlayClientAbilities.TYPE);
        this.handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayClientAbilities(final PacketContainer packet) {
        super(packet, WrapperPlayClientAbilities.TYPE);
    }
    
    public boolean isCreativeMode() {
        return (boolean)this.handle.getSpecificModifier((Class)Boolean.TYPE).read(0);
    }
    
    public void setCreativeMode(final boolean value) {
        this.handle.getSpecificModifier((Class)Boolean.TYPE).write(0, (Object)value);
    }
    
    public boolean isFlying() {
        return (boolean)this.handle.getSpecificModifier((Class)Boolean.TYPE).read(1);
    }
    
    public void setFlying(final boolean value) {
        this.handle.getSpecificModifier((Class)Boolean.TYPE).write(1, (Object)value);
    }
    
    public boolean isFlyingAllowed() {
        return (boolean)this.handle.getSpecificModifier((Class)Boolean.TYPE).read(2);
    }
    
    public void setFlyingAllowed(final boolean value) {
        this.handle.getSpecificModifier((Class)Boolean.TYPE).write(2, (Object)value);
    }
    
    public boolean isGodMode() {
        return (boolean)this.handle.getSpecificModifier((Class)Boolean.TYPE).read(3);
    }
    
    public void setGodMode(final boolean value) {
        this.handle.getSpecificModifier((Class)Boolean.TYPE).write(3, (Object)value);
    }
    
    public float getFlyingSpeed() {
        return (float)this.handle.getFloat().read(0);
    }
    
    public void setFlyingSpeed(final float value) {
        this.handle.getFloat().write(0, (Object)value);
    }
    
    public float getWalkingSpeed() {
        return (float)this.handle.getFloat().read(1);
    }
    
    public void setWalkingSpeed(final float value) {
        this.handle.getFloat().write(1, (Object)value);
    }
    
    static {
        TYPE = PacketType.Play.Client.ABILITIES;
    }
}
