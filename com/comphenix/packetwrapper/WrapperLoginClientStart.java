package com.comphenix.packetwrapper;

import com.comphenix.protocol.*;
import com.comphenix.protocol.events.*;
import com.comphenix.protocol.wrappers.*;

public class WrapperLoginClientStart extends AbstractPacket
{
    public static final PacketType TYPE;
    
    public WrapperLoginClientStart() {
        super(new PacketContainer(WrapperLoginClientStart.TYPE), WrapperLoginClientStart.TYPE);
        this.handle.getModifier().writeDefaults();
    }
    
    public WrapperLoginClientStart(final PacketContainer packet) {
        super(packet, WrapperLoginClientStart.TYPE);
    }
    
    public WrappedGameProfile getProfile() {
        return (WrappedGameProfile)this.handle.getGameProfiles().read(0);
    }
    
    public void setProfile(final WrappedGameProfile value) {
        this.handle.getGameProfiles().write(0, (Object)value);
    }
    
    static {
        TYPE = PacketType.Login.Client.START;
    }
}
