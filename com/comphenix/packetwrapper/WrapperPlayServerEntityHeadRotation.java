package com.comphenix.packetwrapper;

import com.comphenix.protocol.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import com.comphenix.protocol.events.*;

public class WrapperPlayServerEntityHeadRotation extends AbstractPacket
{
    public static final PacketType TYPE;
    
    public WrapperPlayServerEntityHeadRotation() {
        super(new PacketContainer(WrapperPlayServerEntityHeadRotation.TYPE), WrapperPlayServerEntityHeadRotation.TYPE);
        this.handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayServerEntityHeadRotation(final PacketContainer packet) {
        super(packet, WrapperPlayServerEntityHeadRotation.TYPE);
    }
    
    public int getEntityId() {
        return (int)this.handle.getIntegers().read(0);
    }
    
    public void setEntityId(final int value) {
        this.handle.getIntegers().write(0, (Object)value);
    }
    
    public Entity getEntity(final World world) {
        return (Entity)this.handle.getEntityModifier(world).read(0);
    }
    
    public Entity getEntity(final PacketEvent event) {
        return this.getEntity(event.getPlayer().getWorld());
    }
    
    public float getHeadYaw() {
        return (byte)this.handle.getBytes().read(0) * 360.0f / 256.0f;
    }
    
    public void setHeadYaw(final float value) {
        this.handle.getBytes().write(0, (Object)(byte)(value * 256.0f / 360.0f));
    }
    
    static {
        TYPE = PacketType.Play.Server.ENTITY_HEAD_ROTATION;
    }
}
