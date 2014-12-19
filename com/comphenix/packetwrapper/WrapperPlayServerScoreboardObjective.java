package com.comphenix.packetwrapper;

import com.comphenix.protocol.*;
import com.comphenix.protocol.events.*;
import com.comphenix.protocol.reflect.*;

public class WrapperPlayServerScoreboardObjective extends AbstractPacket
{
    public static final PacketType TYPE;
    
    public WrapperPlayServerScoreboardObjective() {
        super(new PacketContainer(WrapperPlayServerScoreboardObjective.TYPE), WrapperPlayServerScoreboardObjective.TYPE);
        this.handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayServerScoreboardObjective(final PacketContainer packet) {
        super(packet, WrapperPlayServerScoreboardObjective.TYPE);
    }
    
    public String getObjectiveName() {
        return (String)this.handle.getStrings().read(0);
    }
    
    public void setObjectiveName(final String value) {
        this.handle.getStrings().write(0, (Object)value);
    }
    
    public String getObjectiveValue() {
        return (String)this.handle.getStrings().read(1);
    }
    
    public void setObjectiveValue(final String value) {
        this.handle.getStrings().write(1, (Object)value);
    }
    
    public byte getPacketMode() {
        return (byte)this.handle.getIntegers().read(0);
    }
    
    public void setPacketMode(final byte value) {
        this.handle.getIntegers().write(0, (Object)(int)value);
    }
    
    static {
        TYPE = PacketType.Play.Server.SCOREBOARD_OBJECTIVE;
    }
    
    public static class Modes extends IntEnum
    {
        public static final int ADD_OBJECTIVE = 0;
        public static final int REMOVE_OBJECTIVE = 1;
        public static final int UPDATE_VALUE = 2;
        private static final Modes INSTANCE;
        
        public static Modes getInstance() {
            return Modes.INSTANCE;
        }
        
        static {
            INSTANCE = new Modes();
        }
    }
}
