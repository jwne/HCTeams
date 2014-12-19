package com.comphenix.packetwrapper;

import com.comphenix.protocol.*;
import com.comphenix.protocol.events.*;

public class WrapperHandshakeClientSetProtocol extends AbstractPacket
{
    public static final PacketType TYPE;
    
    public WrapperHandshakeClientSetProtocol() {
        super(new PacketContainer(WrapperHandshakeClientSetProtocol.TYPE), WrapperHandshakeClientSetProtocol.TYPE);
        this.handle.getModifier().writeDefaults();
    }
    
    public WrapperHandshakeClientSetProtocol(final PacketContainer packet) {
        super(packet, WrapperHandshakeClientSetProtocol.TYPE);
    }
    
    public int getProtocolVersion() {
        return (int)this.handle.getIntegers().read(0);
    }
    
    public void setProtocolVersion(final int value) {
        this.handle.getIntegers().write(0, (Object)value);
    }
    
    public String getServerHostname() {
        return (String)this.handle.getStrings().read(0);
    }
    
    public void setServerHostname(final String value) {
        this.handle.getStrings().write(0, (Object)value);
    }
    
    public short getServerPort() {
        return (short)this.handle.getIntegers().read(1);
    }
    
    public void setServerPort(final short value) {
        this.handle.getIntegers().write(1, (Object)(int)value);
    }
    
    public PacketType.Protocol getNextProtocol() {
        return (PacketType.Protocol)this.handle.getProtocols().read(0);
    }
    
    public void setNextProtocol(final PacketType.Protocol value) {
        this.handle.getProtocols().write(0, (Object)value);
    }
    
    static {
        TYPE = PacketType.Handshake.Client.SET_PROTOCOL;
    }
}
