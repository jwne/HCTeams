package com.comphenix.packetwrapper;

import com.comphenix.protocol.events.*;
import com.google.common.base.*;
import org.bukkit.entity.*;
import com.comphenix.protocol.*;
import java.lang.reflect.*;

public abstract class AbstractPacket
{
    protected PacketContainer handle;
    
    protected AbstractPacket(final PacketContainer handle, final PacketType type) {
        super();
        if (handle == null) {
            throw new IllegalArgumentException("Packet handle cannot be NULL.");
        }
        if (!Objects.equal((Object)handle.getType(), (Object)type)) {
            throw new IllegalArgumentException(handle.getHandle() + " is not a packet of type " + type);
        }
        this.handle = handle;
    }
    
    public PacketContainer getHandle() {
        return this.handle;
    }
    
    public void sendPacket(final Player receiver) {
        try {
            ProtocolLibrary.getProtocolManager().sendServerPacket(receiver, this.getHandle());
        }
        catch (InvocationTargetException e) {
            throw new RuntimeException("Cannot send packet.", e);
        }
    }
    
    public void recievePacket(final Player sender) {
        try {
            ProtocolLibrary.getProtocolManager().recieveClientPacket(sender, this.getHandle());
        }
        catch (Exception e) {
            throw new RuntimeException("Cannot recieve packet.", e);
        }
    }
}
