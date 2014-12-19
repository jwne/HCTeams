package com.comphenix.packetwrapper;

import com.comphenix.protocol.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import com.comphenix.protocol.events.*;
import org.bukkit.potion.*;

public class WrapperPlayServerRemoveEntityEffect extends AbstractPacket
{
    public static final PacketType TYPE;
    
    public WrapperPlayServerRemoveEntityEffect() {
        super(new PacketContainer(WrapperPlayServerRemoveEntityEffect.TYPE), WrapperPlayServerRemoveEntityEffect.TYPE);
        this.handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayServerRemoveEntityEffect(final PacketContainer packet) {
        super(packet, WrapperPlayServerRemoveEntityEffect.TYPE);
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
    
    public byte getEffectId() {
        return (byte)this.handle.getBytes().read(1);
    }
    
    public void setEffectId(final byte value) {
        this.handle.getBytes().write(1, (Object)value);
    }
    
    public PotionEffectType getEffect() {
        return PotionEffectType.getById((int)this.getEffectId());
    }
    
    public void setEffect(final PotionEffectType value) {
        this.setEffectId((byte)value.getId());
    }
    
    static {
        TYPE = PacketType.Play.Server.REMOVE_ENTITY_EFFECT;
    }
}
