package com.comphenix.packetwrapper;

import com.comphenix.protocol.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import com.comphenix.protocol.events.*;

public class WrapperPlayServerCollect extends AbstractPacket
{
    public static final PacketType TYPE;
    
    public WrapperPlayServerCollect() {
        super(new PacketContainer(WrapperPlayServerCollect.TYPE), WrapperPlayServerCollect.TYPE);
        this.handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayServerCollect(final PacketContainer packet) {
        super(packet, WrapperPlayServerCollect.TYPE);
    }
    
    public int getCollectedEntityID() {
        return (int)this.handle.getIntegers().read(0);
    }
    
    public Entity getCollectedEntity(final World world) {
        return (Entity)this.handle.getEntityModifier(world).read(0);
    }
    
    public Entity getCollectedEntity(final PacketEvent event) {
        return this.getCollectedEntity(event.getPlayer().getWorld());
    }
    
    public void setCollectedEntityID(final int value) {
        this.handle.getIntegers().write(0, (Object)value);
    }
    
    public int getCollectorEntityID() {
        return (int)this.handle.getIntegers().read(1);
    }
    
    public void setCollectorEntityID(final int value) {
        this.handle.getIntegers().write(1, (Object)value);
    }
    
    public Entity getCollectorEntity(final World world) {
        return (Entity)this.handle.getEntityModifier(world).read(1);
    }
    
    public Entity getCollectorEntity(final PacketEvent event) {
        return this.getCollectorEntity(event.getPlayer().getWorld());
    }
    
    static {
        TYPE = PacketType.Play.Server.COLLECT;
    }
}
