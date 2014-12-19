package com.comphenix.packetwrapper;

import java.nio.*;
import org.bukkit.*;

public class BlockChangeArray
{
    private static final int RECORD_SIZE = 4;
    private int[] data;
    
    public BlockChangeArray(final int blockChanges) {
        super();
        this.data = new int[blockChanges];
    }
    
    public BlockChangeArray(final byte[] input) {
        super();
        if (input.length % 4 != 0) {
            throw new IllegalArgumentException("The lenght of the input data array should be a multiple of 4.");
        }
        final IntBuffer source = ByteBuffer.wrap(input).asIntBuffer();
        final IntBuffer destination = IntBuffer.allocate(input.length / 4);
        destination.put(source);
        this.data = destination.array();
    }
    
    public BlockChange getBlockChange(final int index) {
        if (index < 0 || index >= this.getSize()) {
            throw new IllegalArgumentException("Index is out of bounds.");
        }
        return new BlockChange(index);
    }
    
    public void setBlockChange(final int index, final BlockChange change) {
        if (change == null) {
            throw new IllegalArgumentException("Block change cannot be NULL.");
        }
        this.data[index] = change.asInteger();
    }
    
    public int getSize() {
        return this.data.length;
    }
    
    public byte[] toByteArray() {
        final ByteBuffer copy = ByteBuffer.allocate(this.data.length * 4);
        copy.asIntBuffer().put(this.data);
        return copy.array();
    }
    
    public class BlockChange
    {
        private final int index;
        
        private BlockChange(final int index) {
            super();
            this.index = index;
        }
        
        public BlockChange setLocation(final Location loc) {
            this.setRelativeX(loc.getBlockX() & 0xF);
            this.setRelativeZ(loc.getBlockZ() & 0xF);
            this.setAbsoluteY(loc.getBlockY());
            return this;
        }
        
        public Location getLocation(final World world, final int chunkX, final int chunkZ) {
            if (world == null) {
                throw new IllegalArgumentException("World cannot be NULL.");
            }
            return new Location(world, (double)((chunkX << 4) + this.getRelativeX()), (double)this.getAbsoluteY(), (double)((chunkZ << 4) + this.getRelativeZ()));
        }
        
        public BlockChange setRelativeX(final int relativeX) {
            this.setValue(relativeX, 28, -268435456);
            return this;
        }
        
        public int getRelativeX() {
            return this.getValue(28, -268435456);
        }
        
        public BlockChange setRelativeZ(final int relativeX) {
            this.setValue(relativeX, 24, 251658240);
            return this;
        }
        
        public byte getRelativeZ() {
            return (byte)this.getValue(24, 251658240);
        }
        
        public BlockChange setAbsoluteY(final int absoluteY) {
            this.setValue(absoluteY, 16, 16711680);
            return this;
        }
        
        public int getAbsoluteY() {
            return this.getValue(16, 16711680);
        }
        
        public BlockChange setBlockID(final int blockID) {
            this.setValue(blockID, 4, 65520);
            return this;
        }
        
        public int getBlockID() {
            return this.getValue(4, 65520);
        }
        
        public BlockChange setMetadata(final int metadata) {
            this.setValue(metadata, 0, 15);
            return this;
        }
        
        public int getMetadata() {
            return this.getValue(0, 15);
        }
        
        public int getIndex() {
            return this.index;
        }
        
        private int asInteger() {
            return BlockChangeArray.this.data[this.index];
        }
        
        private void setValue(final int value, final int leftShift, final int updateMask) {
            BlockChangeArray.this.data[this.index] = ((value << leftShift & updateMask) | (BlockChangeArray.this.data[this.index] & ~updateMask));
        }
        
        private int getValue(final int rightShift, final int updateMask) {
            return (BlockChangeArray.this.data[this.index] & updateMask) >> rightShift;
        }
    }
}
