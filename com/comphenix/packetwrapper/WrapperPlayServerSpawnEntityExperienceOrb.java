package com.comphenix.packetwrapper;

import com.comphenix.protocol.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import com.comphenix.protocol.events.*;

public class WrapperPlayServerSpawnEntityExperienceOrb extends AbstractPacket
{
    public static final PacketType TYPE;
    
    public WrapperPlayServerSpawnEntityExperienceOrb() {
        super(new PacketContainer(WrapperPlayServerSpawnEntityExperienceOrb.TYPE), WrapperPlayServerSpawnEntityExperienceOrb.TYPE);
        this.handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayServerSpawnEntityExperienceOrb(final PacketContainer packet) {
        super(packet, WrapperPlayServerSpawnEntityExperienceOrb.TYPE);
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
    
    public double getX() {
        return (int)this.handle.getIntegers().read(1) / 32.0;
    }
    
    public void setX(final double value) {
        this.handle.getIntegers().write(1, (Object)(int)Math.floor(value * 32.0));
    }
    
    public double getY() {
        return (int)this.handle.getIntegers().read(2) / 32.0;
    }
    
    public void setY(final double value) {
        this.handle.getIntegers().write(2, (Object)(int)Math.floor(value * 32.0));
    }
    
    public double getZ() {
        return (int)this.handle.getIntegers().read(3) / 32.0;
    }
    
    public void setZ(final double value) {
        this.handle.getIntegers().write(3, (Object)(int)Math.floor(value * 32.0));
    }
    
    public short getCount() {
        return (short)this.handle.getIntegers().read(4);
    }
    
    public void setCount(final short value) {
        this.handle.getIntegers().write(4, (Object)(int)value);
    }
    
    static {
        TYPE = PacketType.Play.Server.SPAWN_ENTITY_EXPERIENCE_ORB;
    }
}
