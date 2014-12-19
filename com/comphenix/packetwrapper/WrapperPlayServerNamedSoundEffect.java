package com.comphenix.packetwrapper;

import com.comphenix.protocol.*;
import com.comphenix.protocol.events.*;
import org.bukkit.*;
import com.google.common.collect.*;
import java.util.*;
import java.lang.reflect.*;

public class WrapperPlayServerNamedSoundEffect extends AbstractPacket
{
    public static final PacketType TYPE;
    
    public WrapperPlayServerNamedSoundEffect() {
        super(new PacketContainer(WrapperPlayServerNamedSoundEffect.TYPE), WrapperPlayServerNamedSoundEffect.TYPE);
        this.handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayServerNamedSoundEffect(final PacketContainer packet) {
        super(packet, WrapperPlayServerNamedSoundEffect.TYPE);
    }
    
    public String getSoundName() {
        return (String)this.handle.getStrings().read(0);
    }
    
    public void setSoundName(final String value) {
        this.handle.getStrings().write(0, (Object)value);
    }
    
    public Location getEffectPosition(final PacketEvent event) {
        return this.getEffectPosition(event.getPlayer().getWorld());
    }
    
    public Location getEffectPosition(final World world) {
        return new Location(world, this.getEffectPositionX(), this.getEffectPositionY(), this.getEffectPositionZ());
    }
    
    public double getEffectPositionX() {
        return (int)this.handle.getIntegers().read(0) / 8.0;
    }
    
    public void setEffectPositionX(final double value) {
        this.handle.getIntegers().write(0, (Object)(int)(value * 8.0));
    }
    
    public double getEffectPositionY() {
        return (int)this.handle.getIntegers().read(1) / 8.0;
    }
    
    public void setEffectPositionY(final double value) {
        this.handle.getIntegers().write(1, (Object)(int)(value * 8.0));
    }
    
    public double getEffectPositionZ() {
        return (int)this.handle.getIntegers().read(2) / 8.0;
    }
    
    public void setEffectPositionZ(final double value) {
        this.handle.getIntegers().write(2, (Object)(int)(value * 8.0));
    }
    
    public float getVolume() {
        return (float)this.handle.getFloat().read(0);
    }
    
    public void setVolume(final float value) {
        this.handle.getFloat().write(0, (Object)value);
    }
    
    public float getPitch() {
        return (int)this.handle.getIntegers().read(3) / 63.0f;
    }
    
    public void setPitch(final float value) {
        this.handle.getIntegers().write(3, (Object)(int)(value * 63.0f));
    }
    
    static {
        TYPE = PacketType.Play.Server.NAMED_SOUND_EFFECT;
    }
    
    public static class NamedSoundEffects
    {
        public static final String AMBIENT_WEATHER_RAIN = "ambient.weather.rain";
        public static final String AMBIENT_WEATHER_THUNDER = "ambient.weather.thunder";
        public static final String DAMAGE_FALL_BIG = "damage.fallbig";
        public static final String DAMAGE_FALL_SMALL = "damage.fallsmall";
        public static final String FIRE_ACTIVE = "fire.fire";
        public static final String FIRE_IGNITE = "fire.ignite";
        public static final String LIQUID_LAVA = "liquid.lava";
        public static final String LIQUID_LAVA_POP = "liquid.lavapop";
        public static final String LIQUID_SPLASH = "liquid.splash";
        public static final String LIQUID_WATER = "liquid.water";
        public static final String MOB_BLAZE_BREATHE = "mob.blaze.breathe";
        public static final String MOB_BLAZE_DEATH = "mob.blaze.death";
        public static final String MOB_BLAZE_HIT = "mob.blaze.hit";
        public static final String MOB_CAT_HISS = "mob.cat.hiss";
        public static final String MOB_CAT_HITT = "mob.cat.hitt";
        public static final String MOB_CAT_MEOW = "mob.cat.meow";
        public static final String MOB_CAT_PURR = "mob.cat.purr";
        public static final String MOB_CAT_PURREOW = "mob.cat.purreow";
        public static final String MOB_CHICKEN_HURT = "mob.chicken.hurt";
        public static final String MOB_CHICKEN_PLOP = "mob.chicken.plop";
        public static final String MOB_COW_HURT = "mob.cow.hurt";
        public static final String MOB_CREEPER = "mob.creeper";
        public static final String MOB_CREEPER_DEATH = "mob.creeperdeath";
        public static final String MOB_ENDERMEN_DEATH = "mob.endermen.death";
        public static final String MOB_ENDERMEN_HIT = "mob.endermen.hit";
        public static final String MOB_ENDERMEN_IDLE = "mob.endermen.idle";
        public static final String MOB_ENDERMEN_PORTAL = "mob.endermen.portal";
        public static final String MOB_ENDERMEN_SCREAM = "mob.endermen.scream";
        public static final String MOB_ENDERMEN_STARE = "mob.endermen.stare";
        public static final String MOB_GHAST_AFFECTIONATE_SCREAM = "mob.ghast.affectionate scream";
        public static final String MOB_GHAST_CHARGE = "mob.ghast.charge";
        public static final String MOB_GHAST_DEATH = "mob.ghast.death";
        public static final String MOB_GHAST_FIREBALL = "mob.ghast.fireball";
        public static final String MOB_GHAST_MOAN = "mob.ghast.moan";
        public static final String MOB_GHAST_SCREAM = "mob.ghast.scream";
        public static final String MOB_IRONGOLEM_DEATH = "mob.irongolem.death";
        public static final String MOB_IRONGOLEM_HIT = "mob.irongolem.hit";
        public static final String MOB_IRONGOLEM_THROW = "mob.irongolem.throw";
        public static final String MOB_IRONGOLEM_WALK = "mob.irongolem.walk";
        public static final String MOB_MAGMACUBE_BIG = "mob.magmacube.big";
        public static final String MOB_MAGMACUBE_JUMP = "mob.magmacube.jump";
        public static final String MOB_MAGMACUBE_SMALL = "mob.magmacube.small";
        public static final String MOB_PIGDEATH = "mob.pig.death";
        public static final String MOB_SILVERFISH_HIT = "mob.silverfish.hit";
        public static final String MOB_SILVERFISH_KILL = "mob.silverfish.kill";
        public static final String MOB_SILVERFISH_SAY = "mob.silverfish.say";
        public static final String MOB_SILVERFISH_STEP = "mob.silverfish.step";
        public static final String MOB_SKELETON_DEATH = "mob.skeleton.death";
        public static final String MOB_SKELETON_HURT = "mob.skeleton.hurt";
        public static final String MOB_SLIMEATTACK = "mob.slime.attack";
        public static final String MOB_SPIDER_DEATH = "mob.spider.death";
        public static final String MOB_WOLF_BARK = "mob.wolf.bark";
        public static final String MOB_WOLF_DEATH = "mob.wolf.death";
        public static final String MOB_WOLF_GROWL = "mob.wolf.growl";
        public static final String MOB_WOLF_HOWL = "mob.wolf.howl";
        public static final String MOB_WOLF_HURT = "mob.wolf.hurt";
        public static final String MOB_WOLF_PANTING = "mob.wolf.panting";
        public static final String MOB_WOLF_SHAKE = "mob.wolf.shake";
        public static final String MOB_WOLF_WHINE = "mob.wolf.whine";
        public static final String MOB_ZOMBIE_METAL = "mob.zombie.metal";
        public static final String MOB_ZOMBIE_WOOD = "mob.zombie.wood";
        public static final String MOB_ZOMBIE_WOODBREAK = "mob.zombie.woodbreak";
        public static final String MOB_ZOMBIE = "mob.zombie";
        public static final String MOB_ZOMBIEDEATH = "mob.zombie.death";
        public static final String MOB_ZOMBIEHURT = "mob.zombie.hurt";
        public static final String MOB_ZOMBIEPIG_ZPIG = "mob.zombiepig.zpig";
        public static final String MOB_ZOMBIEPIG_ZPIGANGRY = "mob.zombiepig.zpigangry";
        public static final String MOB_ZOMBIEPIG_ZPIGDEATH = "mob.zombiepig.zpigdeath";
        public static final String MOB_ZOMBIEPIG_ZPIGHURT = "mob.zombiepig.zpighurt";
        public static final String NOTE_BASS = "note.bass";
        public static final String NOTE_BASS_ATTACK = "note.bassattack";
        public static final String NOTE_BD = "note.bd";
        public static final String NOTE_HARP = "note.harp";
        public static final String NOTE_HAT = "note.hat";
        public static final String NOTE_PLING = "note.pling";
        public static final String NOTE_SNARE = "note.snare";
        public static final String PORTAL_NEAR = "portal.portal";
        public static final String PORTAL_TRAVEL = "portal.travel";
        public static final String PORTAL_TRIGGER = "portal.trigger";
        public static final String RANDOM_BOW = "random.bow";
        public static final String RANDOM_BOWHIT = "random.bowhit";
        public static final String RANDOM_BREAK = "random.break";
        public static final String RANDOM_BREATH = "random.breath";
        public static final String RANDOM_BURP = "random.burp";
        public static final String RANDOM_CHESTCLOSED = "random.chestclosed";
        public static final String RANDOM_CHESTOPEN = "random.chestopen";
        public static final String RANDOM_CLICK = "random.click";
        public static final String RANDOM_DOOR_CLOSE = "random.door_close";
        public static final String RANDOM_DOOR_OPEN = "random.door_open";
        public static final String RANDOM_DRINK = "random.drink";
        public static final String RANDOM_EAT = "random.eat";
        public static final String RANDOM_EXPLODE = "random.explode";
        public static final String RANDOM_FIZZ = "random.fizz";
        public static final String RANDOM_FUSE = "random.fuse";
        public static final String RANDOM_GLASS = "random.glass";
        public static final String RANDOM_LEVELUP = "random.levelup";
        public static final String RANDOM_OLD_EXPLODE = "random.old_explode";
        public static final String RANDOM_ORB = "random.orb";
        public static final String RANDOM_POP = "random.pop";
        public static final String RANDOM_SPLASH = "random.splash";
        public static final String RANDOM_WOOD_CLICK = "random.wood click";
        public static final String STEP_CLOTH = "step.cloth";
        public static final String STEP_GRASS = "step.grass";
        public static final String STEP_GRAVEL = "step.gravel";
        public static final String STEP_SAND = "step.sand";
        public static final String STEP_SNOW = "step.snow";
        public static final String STEP_STONE = "step.stone";
        public static final String STEP_WOOD = "step.wood";
        public static final String TILE_PISTON_IN = "tile.piston.in";
        public static final String TILE_PISTON_OUT = "tile.piston.out";
        public static final String DAMAGE_HIT = "damage.hit";
        public static final String DIG_CLOTH = "dig.cloth";
        public static final String DIG_GRASS = "dig.grass";
        public static final String DIG_GRAVEL = "dig.gravel";
        public static final String DIG_SAND = "dig.sand";
        public static final String DIG_SNOW = "dig.snow";
        public static final String DIG_STONE = "dig.stone";
        public static final String DIG_WOOD = "dig.wood";
        public static final String LIQUID_SWIM = "liquid.swim";
        public static final String MINECART_BASE = "minecart.base";
        public static final String MINECART_INSIDE = "minecart.inside";
        public static final String MOB_CHICKEN_SAY = "mob.chicken.say";
        public static final String MOB_CHICKEN_STEP = "mob.chicken.step";
        public static final String MOB_COW_SAY = "mob.cow.say";
        public static final String MOB_COW_STEP = "mob.cow.step";
        public static final String MOB_CREEPER_SAY = "mob.creeper.say";
        public static final String MOB_PIG_DEATH = "mob.pig.death";
        public static final String MOB_PIG_SAY = "mob.pig.say";
        public static final String MOB_PIG_STEP = "mob.pig.step";
        public static final String MOB_SHEEP_SAY = "mob.sheep.say";
        public static final String MOB_SHEEP_SHEAR = "mob.sheep.shear";
        public static final String MOB_SHEEP_STEP = "mob.sheep.step";
        public static final String MOB_SKELETON_SAY = "mob.skeleton.say";
        public static final String MOB_SKELETON_STEP = "mob.skeleton.step";
        public static final String MOB_SLIME_ATTACK = "mob.slime.attack";
        public static final String MOB_SLIME_BIG = "mob.slime.big";
        public static final String MOB_SLIME_SMALL = "mob.slime.small";
        public static final String MOB_SPIDER_SAY = "mob.spider.say";
        public static final String MOB_SPIDER_STEP = "mob.spider.step";
        public static final String MOB_WOLF_STEP = "mob.wolf.step";
        public static final String MOB_ZOMBIE_DEATH = "mob.zombie.death";
        public static final String MOB_ZOMBIE_HURT = "mob.zombie.hurt";
        public static final String MOB_ZOMBIE_SAY = "mob.zombie.say";
        public static final String MOB_ZOMBIE_STEP = "mob.zombie.step";
        public static final String RANDOM_CLASSIC_HURT = "random.classic_hurt";
        public static final String STEP_LADDER = "step.ladder";
        public static final String MOB_BAT_DEATH = "mob.bat.death";
        public static final String MOB_BAT_HURT = "mob.bat.hurt";
        public static final String MOB_BAT_IDLE = "mob.bat.idle";
        public static final String MOB_BAT_TAKEOFF = "mob.bat.takeoff";
        public static final String MOB_ENDERDRAGON_END = "mob.enderdragon.end";
        public static final String MOB_ENDERDRAGON_GROWL = "mob.enderdragon.growl";
        public static final String MOB_ENDERDRAGON_HIT = "mob.enderdragon.hit";
        public static final String MOB_ENDERDRAGON_WINGS = "mob.enderdragon.wings";
        public static final String MOB_WITHER_DEATH = "mob.wither.death";
        public static final String MOB_WITHER_HURT = "mob.wither.hurt";
        public static final String MOB_WITHER_IDLE = "mob.wither.idle";
        public static final String MOB_WITHER_SHOOT = "mob.wither.shoot";
        public static final String MOB_WITHER_SPAWN = "mob.wither.spawn";
        public static final String MOB_ZOMBIE_INFECT = "mob.zombie.infect";
        public static final String MOB_ZOMBIE_REMEDY = "mob.zombie.remedy";
        public static final String MOB_ZOMBIE_UNFECT = "mob.zombie.unfect";
        public static final String RANDOM_ANVIL_BREAK = "random.anvil_break";
        public static final String RANDOM_ANVIL_LAND = "random.anvil_land";
        public static final String RANDOM_ANVIL_USE = "random.anvil_use";
        private static String[] values;
        
        public static String[] values() {
            if (NamedSoundEffects.values == null) {
                final List<String> result = (List<String>)Lists.newArrayList();
                for (final Field field : NamedSoundEffects.class.getFields()) {
                    try {
                        result.add((String)field.get(null));
                    }
                    catch (Exception e) {
                        throw new RuntimeException("Cannot read field.", e);
                    }
                }
                NamedSoundEffects.values = result.toArray(new String[0]);
            }
            return NamedSoundEffects.values;
        }
    }
}
