package com.comphenix.packetwrapper;

import com.comphenix.protocol.*;
import com.comphenix.protocol.events.*;
import com.comphenix.protocol.reflect.*;

public class WrapperPlayClientBlockDig extends AbstractPacket
{
    public static final PacketType TYPE;
    
    public WrapperPlayClientBlockDig() {
        super(new PacketContainer(WrapperPlayClientBlockDig.TYPE), WrapperPlayClientBlockDig.TYPE);
        this.handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayClientBlockDig(final PacketContainer packet) {
        super(packet, WrapperPlayClientBlockDig.TYPE);
    }
    
    public Status getStatus() {
        return Status.values()[(int)this.handle.getIntegers().read(4)];
    }
    
    public void setStatus(final Status value) {
        this.handle.getIntegers().write(4, (Object)value.ordinal());
    }
    
    public int getX() {
        return (int)this.handle.getIntegers().read(0);
    }
    
    public void setX(final int value) {
        this.handle.getIntegers().write(0, (Object)value);
    }
    
    public byte getY() {
        return (byte)this.handle.getIntegers().read(1);
    }
    
    public void setY(final byte value) {
        this.handle.getIntegers().write(1, (Object)(int)value);
    }
    
    public int getZ() {
        return (int)this.handle.getIntegers().read(2);
    }
    
    public void setZ(final int value) {
        this.handle.getIntegers().write(2, (Object)value);
    }
    
    public int getFace() {
        return (byte)this.handle.getIntegers().read(3);
    }
    
    public void setFace(final int value) {
        this.handle.getIntegers().write(3, (Object)value);
    }
    
    static {
        TYPE = PacketType.Play.Client.BLOCK_DIG;
    }
    
    public enum Status
    {
        STARTED_DIGGING, 
        CANCELLED_DIGGING, 
        FINISHED_DIGGING, 
        DROP_ITEM_STACK, 
        DROP_ITEM, 
        SHOOT_ARROW;
    }
    
    public static class BlockSide extends IntEnum
    {
        public static final int BOTTOM = 0;
        public static final int TOP = 1;
        public static final int BEHIND = 2;
        public static final int FRONT = 3;
        public static final int LEFT = 4;
        public static final int RIGHT = 5;
        private static BlockSide INSTANCE;
        private static final int[] xOffset;
        private static final int[] yOffset;
        private static final int[] zOffset;
        
        public static BlockSide getInstance() {
            return BlockSide.INSTANCE;
        }
        
        public static int getXOffset(final int blockFace) {
            return BlockSide.xOffset[blockFace];
        }
        
        public static int getYOffset(final int blockFace) {
            return BlockSide.yOffset[blockFace];
        }
        
        public static int getZOffset(final int blockFace) {
            return BlockSide.zOffset[blockFace];
        }
        
        static {
            BlockSide.INSTANCE = new BlockSide();
            xOffset = new int[] { 0, 0, 0, 0, -1, 1 };
            yOffset = new int[] { -1, 1, 0, 0, 0, 0 };
            zOffset = new int[] { 0, 0, -1, 1, 0, 0 };
        }
    }
}
