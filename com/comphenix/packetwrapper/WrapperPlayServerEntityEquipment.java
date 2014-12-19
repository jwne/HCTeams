package com.comphenix.packetwrapper;

import com.comphenix.protocol.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import com.comphenix.protocol.events.*;
import org.bukkit.inventory.*;

public class WrapperPlayServerEntityEquipment extends AbstractPacket
{
    public static final PacketType TYPE;
    
    public WrapperPlayServerEntityEquipment() {
        super(new PacketContainer(WrapperPlayServerEntityEquipment.TYPE), WrapperPlayServerEntityEquipment.TYPE);
        this.handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayServerEntityEquipment(final PacketContainer packet) {
        super(packet, WrapperPlayServerEntityEquipment.TYPE);
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
    
    public short getSlot() {
        return (short)this.handle.getIntegers().read(1);
    }
    
    public void setSlot(final short value) {
        this.handle.getIntegers().write(1, (Object)(int)value);
    }
    
    public ItemStack getItem() {
        return (ItemStack)this.handle.getItemModifier().read(0);
    }
    
    public void setItem(final ItemStack value) {
        this.handle.getItemModifier().write(0, (Object)value);
    }
    
    static {
        TYPE = PacketType.Play.Server.ENTITY_EQUIPMENT;
    }
}
