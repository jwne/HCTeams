package com.comphenix.packetwrapper;

import com.comphenix.protocol.*;
import com.comphenix.protocol.events.*;

public class WrapperPlayServerMapChunk extends AbstractPacket
{
    public static final PacketType TYPE;
    
    public WrapperPlayServerMapChunk() {
        super(new PacketContainer(WrapperPlayServerMapChunk.TYPE), WrapperPlayServerMapChunk.TYPE);
        this.handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayServerMapChunk(final PacketContainer packet) {
        super(packet, WrapperPlayServerMapChunk.TYPE);
    }
    
    public int getChunkX() {
        return (int)this.handle.getIntegers().read(0);
    }
    
    public void setChunkX(final int value) {
        this.handle.getIntegers().write(0, (Object)value);
    }
    
    public int getChunkZ() {
        return (int)this.handle.getIntegers().read(1);
    }
    
    public void setChunkZ(final int value) {
        this.handle.getIntegers().write(1, (Object)value);
    }
    
    public boolean getGroundUpContinuous() {
        return (boolean)this.handle.getSpecificModifier((Class)Boolean.TYPE).read(0);
    }
    
    public void setGroundUpContinuous(final boolean value) {
        this.handle.getSpecificModifier((Class)Boolean.TYPE).write(0, (Object)value);
    }
    
    public short getPrimaryBitMap() {
        return (short)this.handle.getIntegers().read(2);
    }
    
    public void setPrimaryBitMap(final short value) {
        this.handle.getIntegers().write(2, (Object)(int)value);
    }
    
    public short getAddBitMap() {
        return (short)this.handle.getIntegers().read(3);
    }
    
    public void setAddBitMap(final short value) {
        this.handle.getIntegers().write(3, (Object)(int)value);
    }
    
    public int getCompressedSize() {
        return (int)this.handle.getIntegers().read(4);
    }
    
    public void setCompressedSize(final int value) {
        this.handle.getIntegers().write(4, (Object)value);
    }
    
    public byte[] getCompressedData() {
        return (byte[])this.handle.getByteArrays().read(0);
    }
    
    public void setCompressedData(final byte[] value) {
        this.handle.getByteArrays().write(0, (Object)value);
    }
    
    public byte[] getUncompressedData() {
        return (byte[])this.handle.getByteArrays().read(1);
    }
    
    public void setUncompressedData(final byte[] value) {
        this.handle.getByteArrays().write(1, (Object)value);
    }
    
    static {
        TYPE = PacketType.Play.Server.MAP_CHUNK;
    }
}
