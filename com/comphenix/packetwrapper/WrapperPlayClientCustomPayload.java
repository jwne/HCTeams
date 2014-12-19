package com.comphenix.packetwrapper;

import com.comphenix.protocol.*;
import com.comphenix.protocol.events.*;

public class WrapperPlayClientCustomPayload extends AbstractPacket
{
    public static final PacketType TYPE;
    
    public WrapperPlayClientCustomPayload() {
        super(new PacketContainer(WrapperPlayClientCustomPayload.TYPE), WrapperPlayClientCustomPayload.TYPE);
        this.handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayClientCustomPayload(final PacketContainer packet) {
        super(packet, WrapperPlayClientCustomPayload.TYPE);
    }
    
    public String getChannel() {
        return (String)this.handle.getStrings().read(0);
    }
    
    public void setChannel(final String value) {
        this.handle.getStrings().write(0, (Object)value);
    }
    
    public short getLength() {
        return (short)this.handle.getIntegers().read(0);
    }
    
    public void setLength(final short value) {
        this.handle.getIntegers().write(0, (Object)(int)value);
    }
    
    public byte[] getData() {
        return (byte[])this.handle.getByteArrays().read(0);
    }
    
    public void setData(final byte[] value) {
        this.setLength((short)value.length);
        this.handle.getByteArrays().write(0, (Object)value);
    }
    
    static {
        TYPE = PacketType.Play.Client.CUSTOM_PAYLOAD;
    }
}
