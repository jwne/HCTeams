package com.comphenix.packetwrapper;

import com.comphenix.protocol.injector.*;
import com.comphenix.protocol.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import com.comphenix.protocol.events.*;

public class WrapperPlayServerSpawnEntityPainting extends AbstractPacket
{
    public static final PacketType TYPE;
    private static PacketConstructor entityConstructor;
    
    public WrapperPlayServerSpawnEntityPainting() {
        super(new PacketContainer(WrapperPlayServerSpawnEntityPainting.TYPE), WrapperPlayServerSpawnEntityPainting.TYPE);
        this.handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayServerSpawnEntityPainting(final PacketContainer packet) {
        super(packet, WrapperPlayServerSpawnEntityPainting.TYPE);
    }
    
    public WrapperPlayServerSpawnEntityPainting(final Painting painting) {
        super(fromPainting(painting), WrapperPlayServerSpawnEntityPainting.TYPE);
    }
    
    private static PacketContainer fromPainting(final Painting painting) {
        if (WrapperPlayServerSpawnEntityPainting.entityConstructor == null) {
            WrapperPlayServerSpawnEntityPainting.entityConstructor = ProtocolLibrary.getProtocolManager().createPacketConstructor(WrapperPlayServerSpawnEntityPainting.TYPE, new Object[] { painting });
        }
        return WrapperPlayServerSpawnEntityPainting.entityConstructor.createPacket(new Object[] { painting });
    }
    
    public int getEntityId() {
        return (int)this.handle.getIntegers().read(0);
    }
    
    public Entity getEntity(final World world) {
        return (Entity)this.handle.getEntityModifier(world).read(0);
    }
    
    public Entity getEntity(final PacketEvent event) {
        return this.getEntity(event.getPlayer().getWorld());
    }
    
    public void setEntityId(final int value) {
        this.handle.getIntegers().write(0, (Object)value);
    }
    
    public String getTitle() {
        return (String)this.handle.getStrings().read(0);
    }
    
    public void setTitle(final String value) {
        this.handle.getStrings().write(0, (Object)value);
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
    
    public int getDirection() {
        return (int)this.handle.getIntegers().read(4);
    }
    
    public void setDirection(final int value) {
        this.handle.getIntegers().write(4, (Object)value);
    }
    
    static {
        TYPE = PacketType.Play.Server.SPAWN_ENTITY_PAINTING;
    }
}
