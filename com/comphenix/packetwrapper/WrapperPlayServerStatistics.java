package com.comphenix.packetwrapper;

import com.comphenix.protocol.*;
import com.comphenix.protocol.events.*;
import java.util.*;
import com.comphenix.protocol.wrappers.*;

public class WrapperPlayServerStatistics extends AbstractPacket
{
    public static final PacketType TYPE;
    
    public WrapperPlayServerStatistics() {
        super(new PacketContainer(WrapperPlayServerStatistics.TYPE), WrapperPlayServerStatistics.TYPE);
        this.handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayServerStatistics(final PacketContainer packet) {
        super(packet, WrapperPlayServerStatistics.TYPE);
    }
    
    public Map<WrappedStatistic, Integer> getStatistics() {
        return (Map<WrappedStatistic, Integer>)this.handle.getStatisticMaps().read(0);
    }
    
    public void setStatistics(final Map<WrappedStatistic, Integer> changes) {
        this.handle.getStatisticMaps().write(0, (Object)changes);
    }
    
    static {
        TYPE = PacketType.Play.Server.STATISTICS;
    }
}
