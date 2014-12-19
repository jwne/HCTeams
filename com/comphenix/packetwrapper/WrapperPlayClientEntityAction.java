package com.comphenix.packetwrapper;

import com.comphenix.protocol.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import com.comphenix.protocol.events.*;
import com.comphenix.protocol.reflect.*;

public class WrapperPlayClientEntityAction extends AbstractPacket
{
    public static final PacketType TYPE;
    
    public WrapperPlayClientEntityAction() {
        super(new PacketContainer(WrapperPlayClientEntityAction.TYPE), WrapperPlayClientEntityAction.TYPE);
        this.handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayClientEntityAction(final PacketContainer packet) {
        super(packet, WrapperPlayClientEntityAction.TYPE);
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
    
    public byte getActionId() {
        return (byte)this.handle.getIntegers().read(1);
    }
    
    public void setActionId(final byte value) {
        this.handle.getIntegers().write(1, (Object)(int)value);
    }
    
    public int getJumpBoost() {
        return (int)this.handle.getIntegers().read(2);
    }
    
    public void setJumpBoost(final int value) {
        this.handle.getIntegers().write(2, (Object)value);
    }
    
    static {
        TYPE = PacketType.Play.Client.ENTITY_ACTION;
    }
    
    public static class Action extends IntEnum
    {
        public static final int CROUCH = 1;
        public static final int UNCROUCH = 2;
        public static final int LEAVE_BED = 3;
        public static final int START_SPRINTING = 4;
        public static final int STOP_SPRINTING = 5;
        private static final Action INSTANCE;
        
        public static Action getInstance() {
            return Action.INSTANCE;
        }
        
        static {
            INSTANCE = new Action();
        }
    }
}
