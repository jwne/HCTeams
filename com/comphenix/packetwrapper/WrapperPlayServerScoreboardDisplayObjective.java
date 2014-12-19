package com.comphenix.packetwrapper;

import com.comphenix.protocol.*;
import com.comphenix.protocol.events.*;
import com.comphenix.protocol.reflect.*;

public class WrapperPlayServerScoreboardDisplayObjective extends AbstractPacket
{
    public static final PacketType TYPE;
    
    public WrapperPlayServerScoreboardDisplayObjective() {
        super(new PacketContainer(WrapperPlayServerScoreboardDisplayObjective.TYPE), WrapperPlayServerScoreboardDisplayObjective.TYPE);
        this.handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayServerScoreboardDisplayObjective(final PacketContainer packet) {
        super(packet, WrapperPlayServerScoreboardDisplayObjective.TYPE);
    }
    
    public byte getPosition() {
        return (byte)this.handle.getIntegers().read(0);
    }
    
    public void setPosition(final byte value) {
        this.handle.getIntegers().write(0, (Object)(int)value);
    }
    
    public String getScoreName() {
        return (String)this.handle.getStrings().read(0);
    }
    
    public void setScoreName(final String value) {
        this.handle.getStrings().write(0, (Object)value);
    }
    
    static {
        TYPE = PacketType.Play.Server.SCOREBOARD_DISPLAY_OBJECTIVE;
    }
    
    public static class Positions extends IntEnum
    {
        public static final int LIST = 0;
        public static final int SIDEBAR = 1;
        public static final int BELOW_NAME = 2;
        private static final Positions INSTANCE;
        
        public static Positions getInstance() {
            return Positions.INSTANCE;
        }
        
        static {
            INSTANCE = new Positions();
        }
    }
}
