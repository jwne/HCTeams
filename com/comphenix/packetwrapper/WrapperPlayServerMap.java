package com.comphenix.packetwrapper;

import com.comphenix.protocol.*;
import com.comphenix.protocol.events.*;
import javax.annotation.*;

public class WrapperPlayServerMap extends AbstractPacket
{
    public static final PacketType TYPE;
    
    public WrapperPlayServerMap() {
        super(new PacketContainer(WrapperPlayServerMap.TYPE), WrapperPlayServerMap.TYPE);
        this.handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayServerMap(final PacketContainer packet) {
        super(packet, WrapperPlayServerMap.TYPE);
    }
    
    public int getItemDamage() {
        return (int)this.handle.getIntegers().read(0);
    }
    
    public void setItemDamage(final int value) {
        this.handle.getIntegers().write(0, (Object)value);
    }
    
    public byte[] getData() {
        return (byte[])this.handle.getByteArrays().read(0);
    }
    
    public void setData(@Nonnull final byte[] value) {
        if (value == null) {
            throw new IllegalArgumentException("Array cannot be NULL.");
        }
        this.handle.getByteArrays().write(0, (Object)value);
    }
    
    static {
        TYPE = PacketType.Play.Server.MAP;
    }
}
