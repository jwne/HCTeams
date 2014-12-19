package com.comphenix.packetwrapper;

import com.comphenix.protocol.*;
import com.comphenix.protocol.events.*;

public class WrapperPlayServerRelEntityMove extends WrapperPlayServerEntity
{
    public static final PacketType TYPE;
    
    public WrapperPlayServerRelEntityMove() {
        super(new PacketContainer(WrapperPlayServerRelEntityMove.TYPE), WrapperPlayServerRelEntityMove.TYPE);
        this.handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayServerRelEntityMove(final PacketContainer packet) {
        super(packet, WrapperPlayServerRelEntityMove.TYPE);
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
    
    static {
        TYPE = PacketType.Play.Server.REL_ENTITY_MOVE;
    }
}
