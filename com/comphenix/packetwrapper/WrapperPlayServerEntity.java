package com.comphenix.packetwrapper;

import com.comphenix.protocol.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import com.comphenix.protocol.events.*;

public class WrapperPlayServerEntity extends AbstractPacket
{
    public static final PacketType TYPE;
    
    public WrapperPlayServerEntity() {
        super(new PacketContainer(WrapperPlayServerEntity.TYPE), WrapperPlayServerEntity.TYPE);
        this.handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayServerEntity(final PacketContainer packet) {
        super(packet, WrapperPlayServerEntity.TYPE);
    }
    
    protected WrapperPlayServerEntity(final PacketContainer packet, final PacketType type) {
        super(packet, type);
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
    
    static {
        TYPE = PacketType.Play.Server.ENTITY;
    }
}
