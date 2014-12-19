package net.frozenorb.foxtrot.team.dtr.bitmask;

import org.bukkit.*;
import net.frozenorb.foxtrot.team.claims.*;
import net.frozenorb.foxtrot.team.*;

public enum DTRBitmaskType
{
    SAFE_ZONE(1, "Safe-Zone", "Determines if a region is considered completely safe"), 
    DENY_REENTRY(2, "Deny-Reentry", "Determines if a region can be reentered"), 
    FIFTEEN_MINUTE_DEATHBAN(4, "15m-Deathban", "Determines if a region has a 15m deathban"), 
    FIVE_MINUTE_DEATHBAN(8, "5m-Deathban", "Determines if a region has a 5m deathban"), 
    THIRTY_SECOND_ENDERPEARL_COOLDOWN(16, "30s-Enderpearl-Cooldown", "Determines if a region has a 30s enderpearl cooldown"), 
    ROAD(64, "Road", "Determines if a region is a road"), 
    HALF_DTR_LOSS(2048, "Half-DTR-Loss", "Determines if a region only takes away 0.5 DTR upon death");
    
    private int bitmask;
    private String name;
    private String description;
    
    private DTRBitmaskType(final int bitmask, final String name, final String description) {
        this.bitmask = bitmask;
        this.name = name;
        this.description = description;
    }
    
    public boolean appliesAt(final Location location) {
        final faggot ownerTo = LandBoard.getInstance().getTeam(location);
        return ownerTo != null && ownerTo.getOwner() == null && ownerTo.hasDTRBitmask(this);
    }
    
    public int getBitmask() {
        return this.bitmask;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getDescription() {
        return this.description;
    }
}
