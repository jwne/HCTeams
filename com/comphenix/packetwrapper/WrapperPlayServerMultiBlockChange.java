package com.comphenix.packetwrapper;

import com.comphenix.protocol.*;
import com.comphenix.protocol.events.*;
import com.comphenix.protocol.wrappers.*;

public class WrapperPlayServerMultiBlockChange extends AbstractPacket
{
    public static final PacketType TYPE;
    
    public WrapperPlayServerMultiBlockChange() {
        super(new PacketContainer(WrapperPlayServerMultiBlockChange.TYPE), WrapperPlayServerMultiBlockChange.TYPE);
        this.handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayServerMultiBlockChange(final PacketContainer packet) {
        super(packet, WrapperPlayServerMultiBlockChange.TYPE);
    }
    
    public int getChunkX() {
        return this.getChunk().getChunkX();
    }
    
    public void setChunkX(final int index) {
        this.setChunk(new ChunkCoordIntPair(index, this.getChunkZ()));
    }
    
    public int getChunkZ() {
        return this.getChunk().getChunkZ();
    }
    
    public void setChunkZ(final int index) {
        this.setChunk(new ChunkCoordIntPair(this.getChunkX(), index));
    }
    
    public ChunkCoordIntPair getChunk() {
        return (ChunkCoordIntPair)this.handle.getChunkCoordIntPairs().read(0);
    }
    
    public void setChunk(final ChunkCoordIntPair value) {
        this.handle.getChunkCoordIntPairs().write(0, (Object)value);
    }
    
    public short getRecordCount() {
        return (short)this.handle.getIntegers().read(0);
    }
    
    public void setRecordCount(final short value) {
        this.handle.getIntegers().write(0, (Object)(int)value);
    }
    
    public byte[] getRecordData() {
        return (byte[])this.handle.getByteArrays().read(0);
    }
    
    public void setRecordData(final byte[] value) {
        this.setRecordCount((short)value.length);
        this.handle.getByteArrays().write(0, (Object)value);
    }
    
    public void setRecordData(final BlockChangeArray array) {
        this.setRecordData(array.toByteArray());
    }
    
    public BlockChangeArray getRecordDataArray() {
        return new BlockChangeArray(this.getRecordData());
    }
    
    static {
        TYPE = PacketType.Play.Server.MULTI_BLOCK_CHANGE;
    }
}
