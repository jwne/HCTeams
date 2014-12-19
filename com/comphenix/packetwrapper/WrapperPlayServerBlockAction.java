package com.comphenix.packetwrapper;

import com.comphenix.protocol.*;
import com.comphenix.protocol.events.*;
import com.comphenix.protocol.reflect.*;
import org.bukkit.*;

public class WrapperPlayServerBlockAction extends AbstractPacket
{
    public static final PacketType TYPE;
    
    public WrapperPlayServerBlockAction() {
        super(new PacketContainer(WrapperPlayServerBlockAction.TYPE), WrapperPlayServerBlockAction.TYPE);
        this.handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayServerBlockAction(final PacketContainer packet) {
        super(packet, WrapperPlayServerBlockAction.TYPE);
    }
    
    public int getX() {
        return (int)this.handle.getIntegers().read(0);
    }
    
    public void setX(final int value) {
        this.handle.getIntegers().write(0, (Object)value);
    }
    
    public int getY() {
        return (int)this.handle.getIntegers().read(1);
    }
    
    public void setY(final short value) {
        this.handle.getIntegers().write(1, (Object)(int)value);
    }
    
    public int getZ() {
        return (int)this.handle.getIntegers().read(2);
    }
    
    public void setZ(final int value) {
        this.handle.getIntegers().write(2, (Object)value);
    }
    
    public short getBlockId() {
        return (short)this.getBlockType().getId();
    }
    
    public void setBlockId(final short value) {
        this.setBlockType(Material.getMaterial((int)value));
    }
    
    public Material getBlockType() {
        return (Material)this.handle.getBlocks().read(0);
    }
    
    public void setBlockType(final Material value) {
        this.handle.getBlocks().write(0, (Object)value);
    }
    
    public byte getByte1() {
        return (byte)this.handle.getIntegers().read(3);
    }
    
    public void setByte1(final byte value) {
        this.handle.getIntegers().write(3, (Object)(int)value);
    }
    
    public byte getByte2() {
        return (byte)this.handle.getIntegers().read(4);
    }
    
    public void setByte2(final byte value) {
        this.handle.getIntegers().write(4, (Object)(int)value);
    }
    
    public NoteBlockData getNoteBlockData() {
        return new NoteBlockData();
    }
    
    public PistionData getPistonData() {
        return new PistionData();
    }
    
    public ChestData getChestData() {
        return new ChestData();
    }
    
    static {
        TYPE = PacketType.Play.Server.BLOCK_ACTION;
    }
    
    public static class BlockFaceDirection extends IntEnum
    {
        public static final int DOWN = 0;
        public static final int UP = 1;
        public static final int SOUTH = 2;
        public static final int WEST = 3;
        public static final int NORTH = 4;
        public static final int EAST = 5;
        private static BlockFaceDirection INSTANCE;
        
        public static BlockFaceDirection getInstance() {
            return BlockFaceDirection.INSTANCE;
        }
        
        static {
            BlockFaceDirection.INSTANCE = new BlockFaceDirection();
        }
    }
    
    public class NoteBlockData
    {
        public Instrument getInstrument() {
            return Instrument.getByType(WrapperPlayServerBlockAction.this.getByte1());
        }
        
        public void setInstrument(final Instrument value) {
            WrapperPlayServerBlockAction.this.setByte1(value.getType());
        }
        
        public byte getPitch() {
            return WrapperPlayServerBlockAction.this.getByte2();
        }
        
        public void setPitch(final byte value) {
            WrapperPlayServerBlockAction.this.setByte2(value);
        }
    }
    
    public class PistionData
    {
        public byte getState() {
            return WrapperPlayServerBlockAction.this.getByte1();
        }
        
        public void setState(final byte value) {
            WrapperPlayServerBlockAction.this.setByte1(value);
        }
        
        public int getDirection() {
            return WrapperPlayServerBlockAction.this.getByte2();
        }
        
        public void setDirection(final int value) {
            WrapperPlayServerBlockAction.this.setByte2((byte)value);
        }
    }
    
    public class ChestData
    {
        public boolean isOpen() {
            return WrapperPlayServerBlockAction.this.getByte2() != 0;
        }
        
        public void setOpen(final boolean open) {
            WrapperPlayServerBlockAction.this.setByte2((byte)(open ? 1 : 0));
        }
    }
}
