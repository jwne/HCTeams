package com.comphenix.packetwrapper;

import com.comphenix.protocol.*;
import com.comphenix.protocol.events.*;
import java.util.*;
import com.comphenix.protocol.reflect.*;

public class WrapperPlayServerScoreboardTeam extends AbstractPacket
{
    public static final PacketType TYPE;
    
    public WrapperPlayServerScoreboardTeam() {
        super(new PacketContainer(WrapperPlayServerScoreboardTeam.TYPE), WrapperPlayServerScoreboardTeam.TYPE);
        this.handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayServerScoreboardTeam(final PacketContainer packet) {
        super(packet, WrapperPlayServerScoreboardTeam.TYPE);
    }
    
    public String getTeamName() {
        return (String)this.handle.getStrings().read(0);
    }
    
    public void setTeamName(final String value) {
        this.handle.getStrings().write(0, (Object)value);
    }
    
    public byte getPacketMode() {
        return (byte)this.handle.getIntegers().read(0);
    }
    
    public void setPacketMode(final byte value) {
        this.handle.getIntegers().write(0, (Object)(int)value);
    }
    
    public String getTeamDisplayName() {
        return (String)this.handle.getStrings().read(1);
    }
    
    public void setTeamDisplayName(final String value) {
        this.handle.getStrings().write(1, (Object)value);
    }
    
    public String getTeamPrefix() {
        return (String)this.handle.getStrings().read(2);
    }
    
    public void setTeamPrefix(final String value) {
        this.handle.getStrings().write(2, (Object)value);
    }
    
    public String getTeamSuffix() {
        return (String)this.handle.getStrings().read(3);
    }
    
    public void setTeamSuffix(final String value) {
        this.handle.getStrings().write(3, (Object)value);
    }
    
    public byte getFriendlyFire() {
        return (byte)this.handle.getIntegers().read(1);
    }
    
    public void setFriendlyFire(final byte value) {
        this.handle.getIntegers().write(1, (Object)(int)value);
    }
    
    public Collection<String> getPlayers() {
        return (Collection<String>)this.handle.getSpecificModifier((Class)Collection.class).read(0);
    }
    
    public void setPlayers(final Collection<String> players) {
        this.handle.getSpecificModifier((Class)Collection.class).write(0, (Object)players);
    }
    
    static {
        TYPE = PacketType.Play.Server.SCOREBOARD_TEAM;
    }
    
    public static class Modes extends IntEnum
    {
        public static final int TEAM_CREATED = 0;
        public static final int TEAM_REMOVED = 1;
        public static final int TEAM_UPDATED = 2;
        public static final int PLAYERS_ADDED = 3;
        public static final int PLAYERS_REMOVED = 4;
        private static final Modes INSTANCE;
        
        public static Modes getInstance() {
            return Modes.INSTANCE;
        }
        
        static {
            INSTANCE = new Modes();
        }
    }
}
