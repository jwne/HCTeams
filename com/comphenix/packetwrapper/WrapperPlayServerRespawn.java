package com.comphenix.packetwrapper;

import com.comphenix.protocol.*;
import com.comphenix.protocol.events.*;
import com.comphenix.protocol.wrappers.*;
import org.bukkit.*;

public class WrapperPlayServerRespawn extends AbstractPacket
{
    public static final PacketType TYPE;
    
    public WrapperPlayServerRespawn() {
        super(new PacketContainer(WrapperPlayServerRespawn.TYPE), WrapperPlayServerRespawn.TYPE);
        this.handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayServerRespawn(final PacketContainer packet) {
        super(packet, WrapperPlayServerRespawn.TYPE);
    }
    
    public int getDimension() {
        return (int)this.handle.getIntegers().read(0);
    }
    
    public void setDimension(final int value) {
        this.handle.getIntegers().write(0, (Object)value);
    }
    
    public EnumWrappers.Difficulty getDifficulty() {
        return (EnumWrappers.Difficulty)this.handle.getDifficulties().read(0);
    }
    
    public void setDifficulty(final EnumWrappers.Difficulty value) {
        this.handle.getDifficulties().write(0, (Object)value);
    }
    
    public EnumWrappers.NativeGameMode getGameMode() {
        return (EnumWrappers.NativeGameMode)this.handle.getGameModes().read(0);
    }
    
    public void setGameMode(final EnumWrappers.NativeGameMode mode) {
        this.handle.getGameModes().write(0, (Object)mode);
    }
    
    public WorldType getLevelType() {
        return (WorldType)this.handle.getWorldTypeModifier().read(0);
    }
    
    public void setLevelType(final WorldType value) {
        this.handle.getWorldTypeModifier().write(0, (Object)value);
    }
    
    static {
        TYPE = PacketType.Play.Server.RESPAWN;
    }
}
