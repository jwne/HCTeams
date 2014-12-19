package com.comphenix.packetwrapper;

import com.comphenix.protocol.*;
import com.comphenix.protocol.events.*;

public class WrapperPlayServerCustomPayload extends AbstractPacket
{
    public static final PacketType TYPE;
    
    public WrapperPlayServerCustomPayload() {
        super(new PacketContainer(WrapperPlayServerCustomPayload.TYPE), WrapperPlayServerCustomPayload.TYPE);
        this.handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayServerCustomPayload(final PacketContainer packet) {
        super(packet, WrapperPlayServerCustomPayload.TYPE);
    }
    
    public String getChannel() {
        return (String)this.handle.getStrings().read(0);
    }
    
    public void setChannel(final String value) {
        this.handle.getStrings().write(0, (Object)value);
    }
    
    public byte[] getData() {
        return (byte[])this.handle.getByteArrays().read(0);
    }
    
    public void setData(final byte[] value) {
        this.handle.getByteArrays().write(0, (Object)value);
    }
    
    static {
        TYPE = PacketType.Play.Server.CUSTOM_PAYLOAD;
    }
}
