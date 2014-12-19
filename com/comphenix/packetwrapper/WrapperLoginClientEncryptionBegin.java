package com.comphenix.packetwrapper;

import com.comphenix.protocol.*;
import com.comphenix.protocol.events.*;

public class WrapperLoginClientEncryptionBegin extends AbstractPacket
{
    public static final PacketType TYPE;
    
    public WrapperLoginClientEncryptionBegin() {
        super(new PacketContainer(WrapperLoginClientEncryptionBegin.TYPE), WrapperLoginClientEncryptionBegin.TYPE);
        this.handle.getModifier().writeDefaults();
    }
    
    public WrapperLoginClientEncryptionBegin(final PacketContainer packet) {
        super(packet, WrapperLoginClientEncryptionBegin.TYPE);
    }
    
    public byte[] getSharedSecret() {
        return (byte[])this.handle.getByteArrays().read(0);
    }
    
    public void getSharedSecret(final byte[] value) {
        this.handle.getByteArrays().write(0, (Object)value);
    }
    
    public byte[] getVerifyTokenResponse() {
        return (byte[])this.handle.getByteArrays().read(1);
    }
    
    public void setVerifyTokenResponse(final byte[] value) {
        this.handle.getByteArrays().write(1, (Object)value);
    }
    
    static {
        TYPE = PacketType.Login.Client.ENCRYPTION_BEGIN;
    }
}
