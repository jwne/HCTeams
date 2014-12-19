package com.comphenix.packetwrapper;

import com.comphenix.protocol.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import com.comphenix.protocol.events.*;
import com.comphenix.protocol.wrappers.*;

public class WrapperPlayClientUseEntity extends AbstractPacket
{
    public static final PacketType TYPE;
    
    public WrapperPlayClientUseEntity() {
        super(new PacketContainer(WrapperPlayClientUseEntity.TYPE), WrapperPlayClientUseEntity.TYPE);
        this.handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayClientUseEntity(final PacketContainer packet) {
        super(packet, WrapperPlayClientUseEntity.TYPE);
    }
    
    public int getTargetID() {
        return (int)this.handle.getIntegers().read(0);
    }
    
    public Entity getTarget(final World world) {
        return (Entity)this.handle.getEntityModifier(world).read(0);
    }
    
    public Entity getTarget(final PacketEvent event) {
        return this.getTarget(event.getPlayer().getWorld());
    }
    
    public void setTargetID(final int value) {
        this.handle.getIntegers().write(0, (Object)value);
    }
    
    public EnumWrappers.EntityUseAction getMouse() {
        return (EnumWrappers.EntityUseAction)this.handle.getEntityUseActions().read(0);
    }
    
    public void setMouse(final EnumWrappers.EntityUseAction value) {
        this.handle.getEntityUseActions().write(0, (Object)value);
    }
    
    static {
        TYPE = PacketType.Play.Client.USE_ENTITY;
    }
}
