package com.comphenix.packetwrapper;

import com.comphenix.protocol.*;
import com.comphenix.protocol.events.*;
import java.util.*;
import com.comphenix.protocol.wrappers.*;
import org.bukkit.util.*;

public class WrapperPlayServerExplosion extends AbstractPacket
{
    public static final PacketType TYPE;
    
    public WrapperPlayServerExplosion() {
        super(new PacketContainer(WrapperPlayServerExplosion.TYPE), WrapperPlayServerExplosion.TYPE);
        this.handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayServerExplosion(final PacketContainer packet) {
        super(packet, WrapperPlayServerExplosion.TYPE);
    }
    
    public double getX() {
        return (double)this.handle.getDoubles().read(0);
    }
    
    public void setX(final double value) {
        this.handle.getDoubles().write(0, (Object)value);
    }
    
    public double getY() {
        return (double)this.handle.getDoubles().read(1);
    }
    
    public void setY(final double value) {
        this.handle.getDoubles().write(1, (Object)value);
    }
    
    public double getZ() {
        return (double)this.handle.getDoubles().read(2);
    }
    
    public void setZ(final double value) {
        this.handle.getDoubles().write(2, (Object)value);
    }
    
    public float getRadius() {
        return (float)this.handle.getFloat().read(0);
    }
    
    public void setRadius(final float value) {
        this.handle.getFloat().write(0, (Object)value);
    }
    
    public List<ChunkPosition> getRecords() {
        return (List<ChunkPosition>)this.handle.getPositionCollectionModifier().read(0);
    }
    
    public void setRecords(final List<ChunkPosition> value) {
        this.handle.getPositionCollectionModifier().write(0, (Object)value);
    }
    
    public float getPlayerMotionX() {
        return (float)this.handle.getFloat().read(0);
    }
    
    public void setPlayerMotionX(final float value) {
        this.handle.getFloat().write(0, (Object)value);
    }
    
    public float getPlayerMotionY() {
        return (float)this.handle.getFloat().read(1);
    }
    
    public void setPlayerMotionY(final float value) {
        this.handle.getFloat().write(1, (Object)value);
    }
    
    public float getPlayerMotionZ() {
        return (float)this.handle.getFloat().read(2);
    }
    
    public void setPlayerMotionZ(final float value) {
        this.handle.getFloat().write(2, (Object)value);
    }
    
    public Vector getPlayerMotion() {
        return new Vector(this.getPlayerMotionX(), this.getPlayerMotionY(), this.getPlayerMotionZ());
    }
    
    public void setPlayerMotion(final Vector motion) {
        this.setPlayerMotionX((float)motion.getX());
        this.setPlayerMotionY((float)motion.getY());
        this.setPlayerMotionZ((float)motion.getZ());
    }
    
    static {
        TYPE = PacketType.Play.Server.EXPLOSION;
    }
}
