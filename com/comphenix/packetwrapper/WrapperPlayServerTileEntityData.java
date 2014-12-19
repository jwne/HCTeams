package com.comphenix.packetwrapper;

import com.comphenix.protocol.*;
import com.comphenix.protocol.events.*;
import com.comphenix.protocol.wrappers.nbt.*;

public class WrapperPlayServerTileEntityData extends AbstractPacket
{
    public static final PacketType TYPE;
    
    public WrapperPlayServerTileEntityData() {
        super(new PacketContainer(WrapperPlayServerTileEntityData.TYPE), WrapperPlayServerTileEntityData.TYPE);
        this.handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayServerTileEntityData(final PacketContainer packet) {
        super(packet, WrapperPlayServerTileEntityData.TYPE);
    }
    
    public int getX() {
        return (int)this.handle.getIntegers().read(0);
    }
    
    public void setX(final int value) {
        this.handle.getIntegers().write(0, (Object)value);
    }
    
    public short getY() {
        return (short)this.handle.getIntegers().read(1);
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
    
    public byte getAction() {
        return (byte)this.handle.getIntegers().read(3);
    }
    
    public void setAction(final byte value) {
        this.handle.getIntegers().write(3, (Object)(int)value);
    }
    
    public NbtBase<?> getNbtData() {
        return (NbtBase<?>)this.handle.getNbtModifier().read(0);
    }
    
    public void setNbtData(final NbtBase<?> value) {
        this.handle.getNbtModifier().write(0, (Object)value);
    }
    
    static {
        TYPE = PacketType.Play.Server.TILE_ENTITY_DATA;
    }
}
