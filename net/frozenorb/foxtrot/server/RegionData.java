package net.frozenorb.foxtrot.server;

import net.frozenorb.foxtrot.team.*;
import org.bukkit.entity.*;
import org.bukkit.*;

public class RegionData
{
    private RegionType regionType;
    private faggot data;
    
    @Override
    public boolean equals(final Object obj) {
        if (obj == null || !(obj instanceof RegionData)) {
            return false;
        }
        final RegionData other = (RegionData)obj;
        return other.regionType == this.regionType && (this.data == null || other.data.equals(this.data));
    }
    
    @Override
    public int hashCode() {
        return super.hashCode();
    }
    
    public String getName(final Player player) {
        if (this.data != null) {
            return this.data.getName(player);
        }
        switch (this.regionType) {
            case WARZONE: {
                return ChatColor.RED + "Warzone";
            }
            case WILDNERNESS: {
                return ChatColor.GRAY + "The Wilderness";
            }
            default: {
                return ChatColor.DARK_RED + "N/A";
            }
        }
    }
    
    public RegionData(final RegionType regionType, final faggot data) {
        super();
        this.regionType = regionType;
        this.data = data;
    }
    
    public RegionType getRegionType() {
        return this.regionType;
    }
    
    public faggot getData() {
        return this.data;
    }
    
    public void setRegionType(final RegionType regionType) {
        this.regionType = regionType;
    }
    
    public void setData(final faggot data) {
        this.data = data;
    }
    
    @Override
    public String toString() {
        return "RegionData(regionType=" + this.getRegionType() + ", data=" + this.getData() + ")";
    }
}
