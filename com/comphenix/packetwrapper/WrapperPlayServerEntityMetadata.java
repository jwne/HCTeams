package com.comphenix.packetwrapper;

import com.comphenix.protocol.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import com.comphenix.protocol.events.*;
import java.util.*;
import com.comphenix.protocol.wrappers.*;

public class WrapperPlayServerEntityMetadata extends AbstractPacket
{
    public static final PacketType TYPE;
    
    public WrapperPlayServerEntityMetadata() {
        super(new PacketContainer(WrapperPlayServerEntityMetadata.TYPE), WrapperPlayServerEntityMetadata.TYPE);
        this.handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayServerEntityMetadata(final PacketContainer packet) {
        super(packet, WrapperPlayServerEntityMetadata.TYPE);
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
    
    public List<WrappedWatchableObject> getEntityMetadata() {
        return (List<WrappedWatchableObject>)this.handle.getWatchableCollectionModifier().read(0);
    }
    
    public void setEntityMetadata(final List<WrappedWatchableObject> value) {
        this.handle.getWatchableCollectionModifier().write(0, (Object)value);
    }
    
    static {
        TYPE = PacketType.Play.Server.ENTITY_METADATA;
    }
}
