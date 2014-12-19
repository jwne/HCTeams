package com.comphenix.packetwrapper;

import com.comphenix.protocol.*;
import com.comphenix.protocol.events.*;
import org.bukkit.*;

public class WrapperPlayServerGameStateChange extends AbstractPacket
{
    public static final PacketType TYPE;
    
    public WrapperPlayServerGameStateChange() {
        super(new PacketContainer(WrapperPlayServerGameStateChange.TYPE), WrapperPlayServerGameStateChange.TYPE);
        this.handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayServerGameStateChange(final PacketContainer packet) {
        super(packet, WrapperPlayServerGameStateChange.TYPE);
    }
    
    public int getReason() {
        return (int)this.handle.getIntegers().read(0);
    }
    
    public void setReason(final int value) {
        this.handle.getIntegers().write(0, (Object)value);
    }
    
    public GameMode getGameMode() {
        return GameMode.getByValue((int)this.handle.getIntegers().read(1));
    }
    
    public void setGameMode(final GameMode value) {
        this.handle.getIntegers().write(1, (Object)value.getValue());
    }
    
    static {
        TYPE = PacketType.Play.Server.GAME_STATE_CHANGE;
    }
    
    public static class Reasons
    {
        public static final int INVALID_BED = 0;
        public static final int BEGIN_RAINING = 1;
        public static final int END_RAINING = 2;
        public static final int CHANGE_GAME_MODE = 3;
        public static final int ENTER_CREDITS = 4;
        public static final int DEMO_MESSAGES = 5;
        public static final int ARROW_HITTING_PLAYER = 6;
        public static final int SKY_FADE_VALUE = 7;
        public static final int SKY_FADE_TIME = 8;
        private static final Reasons INSTANCE;
        
        public static Reasons getInstance() {
            return Reasons.INSTANCE;
        }
        
        static {
            INSTANCE = new Reasons();
        }
    }
}
