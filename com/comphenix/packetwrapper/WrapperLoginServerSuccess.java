package com.comphenix.packetwrapper;

import com.comphenix.protocol.*;
import com.comphenix.protocol.events.*;
import com.comphenix.protocol.wrappers.*;

public class WrapperLoginServerSuccess extends AbstractPacket
{
    public static final PacketType TYPE;
    
    public WrapperLoginServerSuccess() {
        super(new PacketContainer(WrapperLoginServerSuccess.TYPE), WrapperLoginServerSuccess.TYPE);
        this.handle.getModifier().writeDefaults();
    }
    
    public WrapperLoginServerSuccess(final PacketContainer packet) {
        super(packet, WrapperLoginServerSuccess.TYPE);
    }
    
    public WrappedGameProfile getProfile() {
        return (WrappedGameProfile)this.handle.getGameProfiles().read(0);
    }
    
    public void setProfile(final WrappedGameProfile value) {
        this.handle.getGameProfiles().write(0, (Object)value);
    }
    
    static {
        TYPE = PacketType.Login.Server.SUCCESS;
    }
}
