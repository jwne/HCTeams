package com.comphenix.packetwrapper;

import com.comphenix.protocol.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import com.comphenix.protocol.events.*;
import com.comphenix.protocol.reflect.*;

public class WrapperPlayClientArmAnimation extends AbstractPacket
{
    public static final PacketType TYPE;
    
    public WrapperPlayClientArmAnimation() {
        super(new PacketContainer(WrapperPlayClientArmAnimation.TYPE), WrapperPlayClientArmAnimation.TYPE);
        this.handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayClientArmAnimation(final PacketContainer packet) {
        super(packet, WrapperPlayClientArmAnimation.TYPE);
    }
    
    public int getEntityID() {
        return (int)this.handle.getIntegers().read(0);
    }
    
    public void setEntityID(final int value) {
        this.handle.getIntegers().write(0, (Object)value);
    }
    
    public Entity getEntity(final World world) {
        return (Entity)this.handle.getEntityModifier(world).read(0);
    }
    
    public Entity getEntity(final PacketEvent event) {
        return this.getEntity(event.getPlayer().getWorld());
    }
    
    public int getAnimation() {
        return (int)this.handle.getIntegers().read(1);
    }
    
    public void setAnimation(final int value) {
        this.handle.getIntegers().write(1, (Object)value);
    }
    
    static {
        TYPE = PacketType.Play.Client.ARM_ANIMATION;
    }
    
    public static class Animations extends IntEnum
    {
        public static final int NO_ANIMATION = 0;
        public static final int SWING_ARM = 1;
        public static final int DAMAGE_ANIMATION = 2;
        public static final int LEAVE_BED = 3;
        public static final int EAT_FOOD = 5;
        public static final int UNKNOWN = 102;
        public static final int CROUCH = 104;
        public static final int UNCROUCH = 105;
        private static Animations INSTANCE;
        
        public static Animations getInstance() {
            return Animations.INSTANCE;
        }
        
        static {
            Animations.INSTANCE = new Animations();
        }
    }
}
