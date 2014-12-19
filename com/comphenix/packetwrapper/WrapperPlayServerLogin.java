package com.comphenix.packetwrapper;

import com.comphenix.protocol.*;
import org.bukkit.entity.*;
import com.comphenix.protocol.events.*;
import com.comphenix.protocol.wrappers.*;
import org.bukkit.*;

public class WrapperPlayServerLogin extends AbstractPacket
{
    public static final PacketType TYPE;
    
    public WrapperPlayServerLogin() {
        super(new PacketContainer(WrapperPlayServerLogin.TYPE), WrapperPlayServerLogin.TYPE);
        this.handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayServerLogin(final PacketContainer packet) {
        super(packet, WrapperPlayServerLogin.TYPE);
    }
    
    public int getEntityId() {
        return (int)this.handle.getIntegers().read(0);
    }
    
    public void setEntityId(final int value) {
        this.handle.getIntegers().write(0, (Object)value);
    }
    
    public Entity getEntity(final World world) {
        return (Entity)this.handle.getEntityModifier(world).read(0);
    }
    
    public Entity getEntity(final PacketEvent event) {
        return this.getEntity(event.getPlayer().getWorld());
    }
    
    public EnumWrappers.NativeGameMode getGamemode() {
        return (EnumWrappers.NativeGameMode)this.handle.getGameModes().read(0);
    }
    
    public void setGamemode(final EnumWrappers.NativeGameMode value) {
        this.handle.getGameModes().write(0, (Object)value);
    }
    
    public boolean isHardcore() {
        return (boolean)this.handle.getBooleans().read(0);
    }
    
    public void setHardcore(final boolean value) {
        this.handle.getBooleans().write(0, (Object)value);
    }
    
    public int getDimension() {
        return (int)this.handle.getIntegers().read(1);
    }
    
    public void setDimension(final int value) {
        this.handle.getIntegers().write(1, (Object)value);
    }
    
    public EnumWrappers.Difficulty getDifficulty() {
        return (EnumWrappers.Difficulty)this.handle.getDifficulties().read(0);
    }
    
    public void setDifficulty(final EnumWrappers.Difficulty difficulty) {
        this.handle.getDifficulties().write(0, (Object)difficulty);
    }
    
    public byte getMaxPlayers() {
        return (byte)this.handle.getIntegers().read(2);
    }
    
    public void setMaxPlayers(final byte value) {
        this.handle.getIntegers().write(2, (Object)(int)value);
    }
    
    public WorldType getLevelType() {
        return (WorldType)this.handle.getWorldTypeModifier().read(0);
    }
    
    public void setLevelType(final WorldType type) {
        this.handle.getWorldTypeModifier().write(0, (Object)type);
    }
    
    static {
        TYPE = PacketType.Play.Server.LOGIN;
    }
}
