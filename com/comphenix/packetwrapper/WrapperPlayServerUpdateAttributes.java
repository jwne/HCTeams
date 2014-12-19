package com.comphenix.packetwrapper;

import com.comphenix.protocol.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import com.comphenix.protocol.events.*;
import java.util.*;
import com.comphenix.protocol.wrappers.*;

public class WrapperPlayServerUpdateAttributes extends AbstractPacket
{
    public static final PacketType TYPE;
    
    public WrapperPlayServerUpdateAttributes() {
        super(new PacketContainer(WrapperPlayServerUpdateAttributes.TYPE), WrapperPlayServerUpdateAttributes.TYPE);
        this.handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayServerUpdateAttributes(final PacketContainer packet) {
        super(packet, WrapperPlayServerUpdateAttributes.TYPE);
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
    
    public List<WrappedAttribute> getAttributes() {
        return (List<WrappedAttribute>)this.handle.getAttributeCollectionModifier().read(0);
    }
    
    public void setAttributes(final List<WrappedAttribute> value) {
        this.handle.getAttributeCollectionModifier().write(0, (Object)value);
    }
    
    static {
        TYPE = PacketType.Play.Server.UPDATE_ATTRIBUTES;
    }
}
