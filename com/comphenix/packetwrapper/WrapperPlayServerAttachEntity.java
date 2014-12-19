package com.comphenix.packetwrapper;

import com.comphenix.protocol.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import com.comphenix.protocol.events.*;

public class WrapperPlayServerAttachEntity extends AbstractPacket
{
    public static final PacketType TYPE;
    
    public WrapperPlayServerAttachEntity() {
        super(new PacketContainer(WrapperPlayServerAttachEntity.TYPE), WrapperPlayServerAttachEntity.TYPE);
        this.handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayServerAttachEntity(final PacketContainer packet) {
        super(packet, WrapperPlayServerAttachEntity.TYPE);
    }
    
    public boolean getLeached() {
        return (int)this.handle.getIntegers().read(0) != 0;
    }
    
    public void setLeached(final boolean value) {
        this.handle.getIntegers().write(0, (Object)(int)(value ? 1 : 0));
    }
    
    public int getEntityId() {
        return (int)this.handle.getIntegers().read(1);
    }
    
    public void setEntityId(final int value) {
        this.handle.getIntegers().write(1, (Object)value);
    }
    
    public Entity getEntity(final World world) {
        return (Entity)this.handle.getEntityModifier(world).read(1);
    }
    
    public Entity getEntity(final PacketEvent event) {
        return this.getEntity(event.getPlayer().getWorld());
    }
    
    public int getVehicleId() {
        return (int)this.handle.getIntegers().read(2);
    }
    
    public void setVehicleId(final int value) {
        this.handle.getIntegers().write(2, (Object)value);
    }
    
    public Entity getVehicle(final World world) {
        return (Entity)this.handle.getEntityModifier(world).read(2);
    }
    
    public Entity getVehicle(final PacketEvent event) {
        return this.getVehicle(event.getPlayer().getWorld());
    }
    
    static {
        TYPE = PacketType.Play.Server.ATTACH_ENTITY;
    }
}
