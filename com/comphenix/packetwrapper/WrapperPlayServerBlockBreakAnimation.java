package com.comphenix.packetwrapper;

import com.comphenix.protocol.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import com.comphenix.protocol.events.*;

public class WrapperPlayServerBlockBreakAnimation extends AbstractPacket
{
    public static final PacketType TYPE;
    
    public WrapperPlayServerBlockBreakAnimation() {
        super(new PacketContainer(WrapperPlayServerBlockBreakAnimation.TYPE), WrapperPlayServerBlockBreakAnimation.TYPE);
        this.handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayServerBlockBreakAnimation(final PacketContainer packet) {
        super(packet, WrapperPlayServerBlockBreakAnimation.TYPE);
    }
    
    public int getEntityID() {
        return (int)this.handle.getIntegers().read(0);
    }
    
    public void setEntityID(final int value) {
        this.handle.getIntegers().write(0, (Object)value);
    }
    
    public Entity getEntity(final World world) {
        return (Entity)this.handle.getEntityModifier(world).read(0);
    }
    
    public Entity getEntity(final PacketEvent event) {
        return this.getEntity(event.getPlayer().getWorld());
    }
    
    public int getX() {
        return (int)this.handle.getIntegers().read(1);
    }
    
    public void setX(final int value) {
        this.handle.getIntegers().write(1, (Object)value);
    }
    
    public int getY() {
        return (int)this.handle.getIntegers().read(2);
    }
    
    public void setY(final int value) {
        this.handle.getIntegers().write(2, (Object)value);
    }
    
    public int getZ() {
        return (int)this.handle.getIntegers().read(3);
    }
    
    public void setZ(final int value) {
        this.handle.getIntegers().write(3, (Object)value);
    }
    
    public byte getDestroyStage() {
        return (byte)this.handle.getIntegers().read(4);
    }
    
    public void setDestroyStage(final byte value) {
        this.handle.getIntegers().write(4, (Object)(int)value);
    }
    
    static {
        TYPE = PacketType.Play.Server.BLOCK_BREAK_ANIMATION;
    }
}
