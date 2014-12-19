package com.comphenix.packetwrapper;

import com.comphenix.protocol.*;
import org.bukkit.util.*;
import com.comphenix.protocol.events.*;
import org.bukkit.*;
import java.util.*;

public class WrapperPlayServerWorldParticles extends AbstractPacket
{
    public static final PacketType TYPE;
    
    public WrapperPlayServerWorldParticles() {
        super(new PacketContainer(WrapperPlayServerWorldParticles.TYPE), WrapperPlayServerWorldParticles.TYPE);
        this.handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayServerWorldParticles(final PacketContainer packet) {
        super(packet, WrapperPlayServerWorldParticles.TYPE);
    }
    
    public WrapperPlayServerWorldParticles(final ParticleEffect effect, final int count, final Location location, final Vector offset) {
        this();
        this.setParticleEffect(effect);
        this.setNumberOfParticles(count);
        this.setLocation(location);
        this.setOffset(offset);
    }
    
    public String getParticleName() {
        return (String)this.handle.getStrings().read(0);
    }
    
    public void setParticleName(final String value) {
        this.handle.getStrings().write(0, (Object)value);
    }
    
    public ParticleEffect getParticleEffect() {
        return ParticleEffect.fromName(this.getParticleName());
    }
    
    public void setParticleEffect(final ParticleEffect effect) {
        if (effect == null) {
            throw new IllegalArgumentException("effect cannot be NULL.");
        }
        this.setParticleName(effect.getParticleName());
    }
    
    public Location getLocation(final PacketEvent event) {
        return this.getLocation(event.getPlayer().getWorld());
    }
    
    public Location getLocation(final World world) {
        return new Location(world, (double)this.getX(), (double)this.getY(), (double)this.getZ());
    }
    
    public void setLocation(final Location loc) {
        if (loc == null) {
            throw new IllegalArgumentException("Location cannot be NULL.");
        }
        this.setX((float)loc.getX());
        this.setY((float)loc.getY());
        this.setZ((float)loc.getZ());
    }
    
    public void setOffset(final Vector vector) {
        if (vector == null) {
            throw new IllegalArgumentException("Vector cannot be NULL.");
        }
        this.setOffsetX((float)vector.getX());
        this.setOffsetY((float)vector.getY());
        this.setOffsetZ((float)vector.getZ());
    }
    
    public Vector getOffset() {
        return new Vector(this.getX(), this.getY(), this.getZ());
    }
    
    public float getX() {
        return (float)this.handle.getFloat().read(0);
    }
    
    public void setX(final float value) {
        this.handle.getFloat().write(0, (Object)value);
    }
    
    public float getY() {
        return (float)this.handle.getFloat().read(1);
    }
    
    public void setY(final float value) {
        this.handle.getFloat().write(1, (Object)value);
    }
    
    public float getZ() {
        return (float)this.handle.getFloat().read(2);
    }
    
    public void setZ(final float value) {
        this.handle.getFloat().write(2, (Object)value);
    }
    
    public float getOffsetX() {
        return (float)this.handle.getFloat().read(3);
    }
    
    public void setOffsetX(final float value) {
        this.handle.getFloat().write(3, (Object)value);
    }
    
    public float getOffsetY() {
        return (float)this.handle.getFloat().read(4);
    }
    
    public void setOffsetY(final float value) {
        this.handle.getFloat().write(4, (Object)value);
    }
    
    public float getOffsetZ() {
        return (float)this.handle.getFloat().read(5);
    }
    
    public void setOffsetZ(final float value) {
        this.handle.getFloat().write(5, (Object)value);
    }
    
    public float getParticleSpeed() {
        return (float)this.handle.getFloat().read(6);
    }
    
    public void setParticleSpeed(final float value) {
        this.handle.getFloat().write(6, (Object)value);
    }
    
    public int getNumberOfParticles() {
        return (int)this.handle.getIntegers().read(0);
    }
    
    public void setNumberOfParticles(final int value) {
        this.handle.getIntegers().write(0, (Object)value);
    }
    
    static {
        TYPE = PacketType.Play.Server.WORLD_PARTICLES;
    }
    
    public enum ParticleEffect
    {
        HUGE_EXPLOSION("hugeexplosion"), 
        LARGE_EXPLODE("largeexplode"), 
        FIREWORKS_SPARK("fireworksSpark"), 
        BUBBLE("bubble"), 
        SUSPEND("suspend"), 
        DEPTH_SUSPEND("depthSuspend"), 
        TOWN_AURA("townaura"), 
        CRIT("crit"), 
        MAGIC_CRIT("magicCrit"), 
        MOB_SPELL("mobSpell"), 
        MOB_SPELL_AMBIENT("mobSpellAmbient"), 
        SPELL("spell"), 
        INSTANT_SPELL("instantSpell"), 
        WITCH_MAGIC("witchMagic"), 
        NOTE("note"), 
        PORTAL("portal"), 
        ENCHANTMENT_TABLE("enchantmenttable"), 
        EXPLODE("explode"), 
        FLAME("flame"), 
        LAVA("lava"), 
        FOOTSTEP("footstep"), 
        SPLASH("splash"), 
        LARGE_SMOKE("largesmoke"), 
        CLOUD("cloud"), 
        RED_DUST("reddust"), 
        SNOWBALL_POOF("snowballpoof"), 
        DRIP_WATER("dripWater"), 
        DRIP_LAVA("dripLava"), 
        SNOW_SHOVEL("snowshovel"), 
        SLIME("slime"), 
        HEART("heart"), 
        ANGRY_VILLAGER("angryVillager"), 
        HAPPY_VILLAGER("happerVillager"), 
        ICONCRACK("iconcrack_"), 
        TILECRACK("tilecrack_");
        
        private final String name;
        private static volatile Map<String, ParticleEffect> LOOKUP;
        
        private static Map<String, ParticleEffect> generateLookup() {
            final Map<String, ParticleEffect> created = new HashMap<String, ParticleEffect>();
            for (final ParticleEffect effect : values()) {
                created.put(effect.getParticleName(), effect);
            }
            return created;
        }
        
        private ParticleEffect(final String name) {
            this.name = name;
        }
        
        public static ParticleEffect fromName(final String name) {
            return ParticleEffect.LOOKUP.get(name);
        }
        
        public String getParticleName() {
            return this.name;
        }
        
        static {
            ParticleEffect.LOOKUP = generateLookup();
        }
    }
}
