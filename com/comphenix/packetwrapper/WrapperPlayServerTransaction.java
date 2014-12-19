package com.comphenix.packetwrapper;

import com.comphenix.protocol.*;
import com.comphenix.protocol.events.*;

public class WrapperPlayServerTransaction extends AbstractPacket
{
    public static final PacketType TYPE;
    
    public WrapperPlayServerTransaction() {
        super(new PacketContainer(WrapperPlayServerTransaction.TYPE), WrapperPlayServerTransaction.TYPE);
        this.handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayServerTransaction(final PacketContainer packet) {
        super(packet, WrapperPlayServerTransaction.TYPE);
    }
    
    public byte getWindowId() {
        return (byte)this.handle.getIntegers().read(0);
    }
    
    public void setWindowId(final byte value) {
        this.handle.getIntegers().write(0, (Object)(int)value);
    }
    
    public short getActionNumber() {
        return (short)this.handle.getShorts().read(0);
    }
    
    public void setActionNumber(final short value) {
        this.handle.getShorts().write(0, (Object)value);
    }
    
    public boolean getAccepted() {
        return (boolean)this.handle.getSpecificModifier((Class)Boolean.TYPE).read(0);
    }
    
    public void setAccepted(final boolean value) {
        this.handle.getSpecificModifier((Class)Boolean.TYPE).write(0, (Object)value);
    }
    
    static {
        TYPE = PacketType.Play.Server.TRANSACTION;
    }
}
