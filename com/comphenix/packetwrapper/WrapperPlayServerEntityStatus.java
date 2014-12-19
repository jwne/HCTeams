package com.comphenix.packetwrapper;

import com.comphenix.protocol.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import com.comphenix.protocol.events.*;

public class WrapperPlayServerEntityStatus extends AbstractPacket
{
    public static final PacketType TYPE;
    
    public WrapperPlayServerEntityStatus() {
        super(new PacketContainer(WrapperPlayServerEntityStatus.TYPE), WrapperPlayServerEntityStatus.TYPE);
        this.handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayServerEntityStatus(final PacketContainer packet) {
        super(packet, WrapperPlayServerEntityStatus.TYPE);
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
    
    public int getEntityStatus() {
        return (int)this.handle.getBytes().read(0);
    }
    
    public void setEntityStatus(final int value) {
        this.handle.getBytes().write(0, (Object)(byte)value);
    }
    
    static {
        TYPE = PacketType.Play.Server.ENTITY_STATUS;
    }
    
    public static class Status
    {
        public static final int ENTITY_HURT = 2;
        public static final int ENTITY_DEAD = 3;
        public static final int WOLF_TAMING = 6;
        public static final int WOLF_TAMED = 7;
        public static final int WOLF_SHAKING_OFF_WATER = 8;
        public static final int EATING_ACCEPTED = 9;
        public static final int SHEEP_EATING_GRASS = 10;
        public static final int IRON_GOLEM_GIFTING_ROSE = 11;
        public static final int VILLAGER_SPAWN_HEART_PARTICLE = 12;
        public static final int VILLAGER_SPAWN_ANGRY_PARTICLE = 13;
        public static final int VILLAGER_SPAWN_HAPPY_PARTICLE = 14;
        public static final int WITCH_SPAWN_MAGIC_PARTICLE = 15;
        public static final int ZOMBIE_VILLAGERIZING = 16;
        public static final int FIREWORK_EXPLODING = 17;
        private static Status INSTANCE;
        
        public static Status getInstance() {
            return Status.INSTANCE;
        }
        
        static {
            Status.INSTANCE = new Status();
        }
    }
}
