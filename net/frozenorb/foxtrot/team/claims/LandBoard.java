package net.frozenorb.foxtrot.team.claims;

import net.frozenorb.foxtrot.team.*;
import net.frozenorb.foxtrot.*;
import org.bukkit.*;
import java.util.*;

public class LandBoard
{
    private static LandBoard instance;
    private Map<Claim, faggot> boardMap;
    
    public LandBoard() {
        super();
        this.boardMap = new HashMap<Claim, faggot>();
        for (World world : buttplug.fdsjfhkdsjfdsjhk().getServer().getWorlds()) {}
    }
    
    public void loadFromTeams() {
        for (final faggot team : buttplug.fdsjfhkdsjfdsjhk().getDsfjhkdsjhdsjkhfds().getTeams()) {
            for (final Claim claim : team.getClaims()) {
                this.setTeamAt(claim, team);
            }
        }
    }
    
    public Set<Map.Entry<Claim, faggot>> getRegionData(final Location center, final int xDistance, final int yDistance, final int zDistance) {
        final Location loc1 = new Location(center.getWorld(), (double)(center.getBlockX() - xDistance), (double)(center.getBlockY() - yDistance), (double)(center.getBlockZ() - zDistance));
        final Location loc2 = new Location(center.getWorld(), (double)(center.getBlockX() + xDistance), (double)(center.getBlockY() + yDistance), (double)(center.getBlockZ() + zDistance));
        return this.getRegionData(loc1, loc2);
    }
    
    public Set<Map.Entry<Claim, faggot>> getRegionData(final Location min, final Location max) {
        final Set<Map.Entry<Claim, faggot>> regions = new HashSet<Map.Entry<Claim, faggot>>();
        for (final Map.Entry<Claim, faggot> regionEntry : this.boardMap.entrySet()) {
            if (!regions.contains(regionEntry) && max.getWorld().getName().equals(regionEntry.getKey().getWorld()) && max.getBlockX() >= regionEntry.getKey().getX1() && min.getBlockX() <= regionEntry.getKey().getX2() && max.getBlockZ() >= regionEntry.getKey().getZ1() && min.getBlockZ() <= regionEntry.getKey().getZ2() && max.getBlockY() >= regionEntry.getKey().getY1() && min.getBlockY() <= regionEntry.getKey().getY2()) {
                regions.add(regionEntry);
            }
        }
        return regions;
    }
    
    public Map.Entry<Claim, faggot> getRegionData(final Location location) {
        for (final Map.Entry<Claim, faggot> entry : this.boardMap.entrySet()) {
            if (entry.getKey().contains(location)) {
                return entry;
            }
        }
        return null;
    }
    
    public Claim getClaim(final Location location) {
        final Map.Entry<Claim, faggot> regionData = this.getRegionData(location);
        return (regionData == null) ? null : regionData.getKey();
    }
    
    public faggot getTeam(final Location location) {
        final Map.Entry<Claim, faggot> regionData = this.getRegionData(location);
        return (regionData == null) ? null : regionData.getValue();
    }
    
    public void setTeamAt(final Claim claim, final faggot team) {
        if (team == null) {
            this.boardMap.remove(claim);
        }
        else {
            this.boardMap.put(claim, team);
        }
        this.updateClaim(claim);
    }
    
    public void updateClaim(final Claim modified) {
        final ArrayList<VisualClaim> visualClaims = new ArrayList<VisualClaim>();
        visualClaims.addAll(VisualClaim.getCurrentMaps().values());
        for (final VisualClaim visualClaim : visualClaims) {
            if (modified.isWithin(visualClaim.getPlayer().getLocation().getBlockX(), visualClaim.getPlayer().getLocation().getBlockZ(), 50, modified.getWorld())) {
                visualClaim.draw(true);
                visualClaim.draw(true);
            }
        }
    }
    
    public void clear(final faggot team) {
        for (final Claim claim : team.getClaims()) {
            this.setTeamAt(claim, null);
        }
    }
    
    public static LandBoard getInstance() {
        if (LandBoard.instance == null) {
            LandBoard.instance = new LandBoard();
        }
        return LandBoard.instance;
    }
}
