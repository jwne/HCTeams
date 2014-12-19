package com.comphenix.packetwrapper;

import com.comphenix.protocol.*;
import com.comphenix.protocol.events.*;
import java.security.*;

public class WrapperLoginServerEncryptionBegin extends AbstractPacket
{
    public static final PacketType TYPE;
    
    public WrapperLoginServerEncryptionBegin() {
        super(new PacketContainer(WrapperLoginServerEncryptionBegin.TYPE), WrapperLoginServerEncryptionBegin.TYPE);
        this.handle.getModifier().writeDefaults();
    }
    
    public WrapperLoginServerEncryptionBegin(final PacketContainer packet) {
        super(packet, WrapperLoginServerEncryptionBegin.TYPE);
    }
    
    public String getServerId() {
        return (String)this.handle.getStrings().read(0);
    }
    
    public void setServerId(final String value) {
        this.handle.getStrings().write(0, (Object)value);
    }
    
    public PublicKey getPublicKey() {
        return (PublicKey)this.handle.getSpecificModifier((Class)PublicKey.class).read(0);
    }
    
    public void setPublicKey(final PublicKey value) {
        this.handle.getSpecificModifier((Class)PublicKey.class).write(0, (Object)value);
    }
    
    public byte[] getVerifyToken() {
        return (byte[])this.handle.getByteArrays().read(0);
    }
    
    public void getVerifyToken(final byte[] value) {
        this.handle.getByteArrays().write(0, (Object)value);
    }
    
    static {
        TYPE = PacketType.Login.Server.ENCRYPTION_BEGIN;
    }
}
