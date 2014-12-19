package com.comphenix.packetwrapper;

import com.comphenix.protocol.*;
import com.comphenix.protocol.events.*;
import com.comphenix.protocol.reflect.*;

public class WrapperPlayServerScoreboardScore extends AbstractPacket
{
    public static final PacketType TYPE;
    
    public WrapperPlayServerScoreboardScore() {
        super(new PacketContainer(WrapperPlayServerScoreboardScore.TYPE), WrapperPlayServerScoreboardScore.TYPE);
        this.handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayServerScoreboardScore(final PacketContainer packet) {
        super(packet, WrapperPlayServerScoreboardScore.TYPE);
    }
    
    public String getItemName() {
        return (String)this.handle.getStrings().read(0);
    }
    
    public void setItemName(final String value) {
        this.handle.getStrings().write(0, (Object)value);
    }
    
    public byte getPacketMode() {
        return (byte)this.handle.getIntegers().read(1);
    }
    
    public void setPacketMode(final byte value) {
        this.handle.getIntegers().write(1, (Object)(int)value);
    }
    
    public String getScoreName() {
        return (String)this.handle.getStrings().read(1);
    }
    
    public void setScoreName(final String value) {
        this.handle.getStrings().write(1, (Object)value);
    }
    
    public int getValue() {
        return (int)this.handle.getIntegers().read(0);
    }
    
    public void setValue(final int value) {
        this.handle.getIntegers().write(0, (Object)value);
    }
    
    static {
        TYPE = PacketType.Play.Server.SCOREBOARD_SCORE;
    }
    
    public static class Modes extends IntEnum
    {
        public static final int SET_SCORE = 0;
        public static final int REMOVE_SCORE = 1;
        private static final Modes INSTANCE;
        
        public static Modes getInstance() {
            return Modes.INSTANCE;
        }
        
        static {
            INSTANCE = new Modes();
        }
    }
}
