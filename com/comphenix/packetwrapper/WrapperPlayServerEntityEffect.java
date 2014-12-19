package com.comphenix.packetwrapper;

import com.comphenix.protocol.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import com.comphenix.protocol.events.*;
import org.bukkit.potion.*;

public class WrapperPlayServerEntityEffect extends AbstractPacket
{
    public static final PacketType TYPE;
    
    public WrapperPlayServerEntityEffect() {
        super(new PacketContainer(WrapperPlayServerEntityEffect.TYPE), WrapperPlayServerEntityEffect.TYPE);
        this.handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayServerEntityEffect(final PacketContainer packet) {
        super(packet, WrapperPlayServerEntityEffect.TYPE);
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
        return (byte)this.handle.getBytes().read(0);
    }
    
    public void setEffectId(final byte value) {
        this.handle.getBytes().write(0, (Object)value);
    }
    
    public PotionEffectType getEffect() {
        return PotionEffectType.getById((int)this.getEffectId());
    }
    
    public void setEffect(final PotionEffectType value) {
        this.setEffectId((byte)value.getId());
    }
    
    public byte getAmplifier() {
        return (byte)this.handle.getBytes().read(1);
    }
    
    public void setAmplifier(final byte value) {
        this.handle.getBytes().write(1, (Object)value);
    }
    
    public short getDuration() {
        return (short)this.handle.getShorts().read(0);
    }
    
    public void setDuration(final short value) {
        this.handle.getShorts().write(0, (Object)value);
    }
    
    static {
        TYPE = PacketType.Play.Server.ENTITY_EFFECT;
    }
}
