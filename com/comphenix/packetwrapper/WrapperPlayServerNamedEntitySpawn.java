package com.comphenix.packetwrapper;

import com.comphenix.protocol.injector.*;
import com.comphenix.protocol.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import com.comphenix.protocol.events.*;
import org.bukkit.util.*;
import com.comphenix.protocol.wrappers.*;
import com.google.common.base.*;

public class WrapperPlayServerNamedEntitySpawn extends AbstractPacket
{
    public static final PacketType TYPE;
    private static PacketConstructor entityConstructor;
    
    public WrapperPlayServerNamedEntitySpawn() {
        super(new PacketContainer(WrapperPlayServerNamedEntitySpawn.TYPE), WrapperPlayServerNamedEntitySpawn.TYPE);
        this.handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayServerNamedEntitySpawn(final PacketContainer packet) {
        super(packet, WrapperPlayServerNamedEntitySpawn.TYPE);
    }
    
    public WrapperPlayServerNamedEntitySpawn(final Player player) {
        super(fromPlayer(player), WrapperPlayServerNamedEntitySpawn.TYPE);
    }
    
    private static PacketContainer fromPlayer(final Player player) {
        if (WrapperPlayServerNamedEntitySpawn.entityConstructor == null) {
            WrapperPlayServerNamedEntitySpawn.entityConstructor = ProtocolLibrary.getProtocolManager().createPacketConstructor(WrapperPlayServerNamedEntitySpawn.TYPE, new Object[] { player });
        }
        return WrapperPlayServerNamedEntitySpawn.entityConstructor.createPacket(new Object[] { player });
    }
    
    public int getEntityID() {
        return (int)this.handle.getIntegers().read(0);
    }
    
    public void setEntityID(final int value) {
        this.handle.getIntegers().write(0, (Object)value);
    }
    
    public Entity getEntity(final World world) {
        return (Entity)this.handle.getEntityModifier(world).read(0);
    }
    
    public Entity getEntity(final PacketEvent event) {
        return this.getEntity(event.getPlayer().getWorld());
    }
    
    public String getPlayerName() {
        final WrappedGameProfile profile = this.getProfile();
        return (profile != null) ? profile.getName() : null;
    }
    
    public void setPlayerName(final String value) {
        if (value != null && value.length() > 16) {
            throw new IllegalArgumentException("Maximum player name lenght is 16 characters.");
        }
        this.setProfile(new WrappedGameProfile(this.getPlayerUUID(), value));
    }
    
    public String getPlayerUUID() {
        final WrappedGameProfile profile = this.getProfile();
        return (profile != null) ? profile.getId() : null;
    }
    
    public void setPlayerUUID(final String uuid) {
        this.setProfile(new WrappedGameProfile(uuid, this.getPlayerName()));
    }
    
    public WrappedGameProfile getProfile() {
        return (WrappedGameProfile)this.handle.getGameProfiles().read(0);
    }
    
    public void setProfile(final WrappedGameProfile value) {
        this.handle.getGameProfiles().write(0, (Object)value);
    }
    
    public Vector getPosition() {
        return new Vector(this.getX(), this.getY(), this.getZ());
    }
    
    public void setPosition(final Vector position) {
        this.setX(position.getX());
        this.setY(position.getY());
        this.setZ(position.getZ());
    }
    
    public double getX() {
        return (int)this.handle.getIntegers().read(1) / 32.0;
    }
    
    public void setX(final double value) {
        this.handle.getIntegers().write(1, (Object)(int)Math.floor(value * 32.0));
    }
    
    public double getY() {
        return (int)this.handle.getIntegers().read(2) / 32.0;
    }
    
    public void setY(final double value) {
        this.handle.getIntegers().write(2, (Object)(int)Math.floor(value * 32.0));
    }
    
    public double getZ() {
        return (int)this.handle.getIntegers().read(3) / 32.0;
    }
    
    public void setZ(final double value) {
        this.handle.getIntegers().write(3, (Object)(int)Math.floor(value * 32.0));
    }
    
    public float getYaw() {
        return (byte)this.handle.getBytes().read(0) * 360.0f / 256.0f;
    }
    
    public void setYaw(final float value) {
        this.handle.getBytes().write(0, (Object)(byte)(value * 256.0f / 360.0f));
    }
    
    public float getPitch() {
        return (byte)this.handle.getBytes().read(1) * 360.0f / 256.0f;
    }
    
    public void setPitch(final float value) {
        this.handle.getBytes().write(1, (Object)(byte)(value * 256.0f / 360.0f));
    }
    
    public short getCurrentItem() {
        return (short)this.handle.getIntegers().read(4);
    }
    
    public void setCurrentItem(final short value) {
        this.handle.getIntegers().write(4, (Object)(int)value);
    }
    
    public WrappedDataWatcher getMetadata() {
        return (WrappedDataWatcher)this.handle.getDataWatcherModifier().read(0);
    }
    
    public void setMetadata(final WrappedDataWatcher value) {
        this.handle.getDataWatcherModifier().write(0, (Object)value);
    }
    
    @Override
    public PacketContainer getHandle() {
        Preconditions.checkNotNull((Object)this.getPlayerName(), (Object)"Must specify a player name.");
        Preconditions.checkNotNull((Object)this.getPlayerUUID(), (Object)"Must specify a player UUID.");
        return super.getHandle();
    }
    
    static {
        TYPE = PacketType.Play.Server.NAMED_ENTITY_SPAWN;
    }
}
