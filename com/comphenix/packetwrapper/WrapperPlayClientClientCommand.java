package com.comphenix.packetwrapper;

import com.comphenix.protocol.*;
import com.comphenix.protocol.events.*;
import com.comphenix.protocol.wrappers.*;

public class WrapperPlayClientClientCommand extends AbstractPacket
{
    public static final PacketType TYPE;
    
    public WrapperPlayClientClientCommand() {
        super(new PacketContainer(WrapperPlayClientClientCommand.TYPE), WrapperPlayClientClientCommand.TYPE);
        this.handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayClientClientCommand(final PacketContainer packet) {
        super(packet, WrapperPlayClientClientCommand.TYPE);
    }
    
    public EnumWrappers.ClientCommand getCommand() {
        return (EnumWrappers.ClientCommand)this.handle.getClientCommands().read(0);
    }
    
    public void setCommand(final EnumWrappers.ClientCommand value) {
        this.handle.getClientCommands().write(0, (Object)value);
    }
    
    static {
        TYPE = PacketType.Play.Client.CLIENT_COMMAND;
    }
}
