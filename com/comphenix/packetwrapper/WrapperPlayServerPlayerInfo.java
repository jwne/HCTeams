package com.comphenix.packetwrapper;

import com.comphenix.protocol.*;
import com.comphenix.protocol.events.*;

public class WrapperPlayServerPlayerInfo extends AbstractPacket
{
    public static final PacketType TYPE;
    
    public WrapperPlayServerPlayerInfo() {
        super(new PacketContainer(WrapperPlayServerPlayerInfo.TYPE), WrapperPlayServerPlayerInfo.TYPE);
        this.handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayServerPlayerInfo(final PacketContainer packet) {
        super(packet, WrapperPlayServerPlayerInfo.TYPE);
    }
    
    public String getPlayerName() {
        return (String)this.handle.getStrings().read(0);
    }
    
    public void setPlayerName(final String value) {
        this.handle.getStrings().write(0, (Object)value);
    }
    
    public boolean getOnline() {
        return (boolean)this.handle.getSpecificModifier((Class)Boolean.TYPE).read(0);
    }
    
    public void setOnline(final boolean value) {
        this.handle.getSpecificModifier((Class)Boolean.TYPE).write(0, (Object)value);
    }
    
    public short getPing() {
        return (short)this.handle.getIntegers().read(0);
    }
    
    public void setPing(final short value) {
        this.handle.getIntegers().write(0, (Object)(int)value);
    }
    
    static {
        TYPE = PacketType.Play.Server.PLAYER_INFO;
    }
}
