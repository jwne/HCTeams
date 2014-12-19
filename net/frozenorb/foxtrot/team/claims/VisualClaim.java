package net.frozenorb.foxtrot.team.claims;

import org.bukkit.entity.*;
import lombok.*;
import net.frozenorb.foxtrot.*;
import org.bukkit.plugin.*;
import net.frozenorb.foxtrot.team.*;
import net.frozenorb.foxtrot.team.commands.team.*;
import java.util.*;
import org.bukkit.*;
import org.bukkit.event.block.*;
import org.bukkit.event.*;
import org.bukkit.event.player.*;

public class VisualClaim implements Listener
{
    public static final int MAP_RADIUS = 50;
    public static final Material[] MAP_MATERIALS;
    private static Map<String, VisualClaim> currentMaps;
    private static Map<String, VisualClaim> visualClaims;
    private static Map<String, List<Location>> packetBlocksSent;
    private static Map<String, List<Location>> mapBlocksSent;
    @NonNull
    private Player player;
    @NonNull
    private VisualClaimType type;
    @NonNull
    private boolean bypass;
    private Location corner1;
    private Location corner2;
    
    public void draw(final boolean silent) {
        if (VisualClaim.currentMaps.containsKey(this.player.getName()) && this.type == VisualClaimType.MAP) {
            VisualClaim.currentMaps.get(this.player.getName()).cancel(true);
            if (!silent) {
                this.player.sendMessage(ChatColor.YELLOW + "Claim pillars have been hidden!");
            }
            return;
        }
        if (VisualClaim.visualClaims.containsKey(this.player.getName()) && (this.type == VisualClaimType.CREATE || this.type == VisualClaimType.RESIZE)) {
            VisualClaim.visualClaims.get(this.player.getName()).cancel(true);
        }
        switch (this.type) {
            case CREATE:
            case RESIZE: {
                VisualClaim.visualClaims.put(this.player.getName(), this);
                break;
            }
            case MAP: {
                VisualClaim.currentMaps.put(this.player.getName(), this);
                break;
            }
        }
        buttplug.fdsjfhkdsjfdsjhk().getServer().getPluginManager().registerEvents((Listener)this, (Plugin)buttplug.fdsjfhkdsjfdsjhk());
        switch (this.type) {
            case CREATE: {
                this.player.sendMessage(ChatColor.YELLOW + "Faction land claim started.");
                break;
            }
            case RESIZE: {
                this.player.sendMessage(ChatColor.RED + "Faction land resizing isn't yet supported.");
                break;
            }
            case MAP: {
                int claimIteration = 0;
                final Map<Map.Entry<Claim, faggot>, Material> sendMaps = new HashMap<Map.Entry<Claim, faggot>, Material>();
                for (final Map.Entry<Claim, faggot> regionData : LandBoard.getInstance().getRegionData(this.player.getLocation(), 50, 256, 50)) {
                    final Material mat = this.getMaterial(claimIteration);
                    ++claimIteration;
                    this.drawClaim(regionData.getKey(), mat);
                    sendMaps.put(regionData, mat);
                }
                if (sendMaps.isEmpty()) {
                    if (!silent) {
                        this.player.sendMessage(ChatColor.YELLOW + "There are no claims within " + 50 + " blocks of you!");
                    }
                    this.cancel(true);
                }
                if (!silent) {
                    for (final Map.Entry<Map.Entry<Claim, faggot>, Material> claim : sendMaps.entrySet()) {
                        this.player.sendMessage(ChatColor.YELLOW + "Land " + ChatColor.BLUE + claim.getKey().getKey().getName() + ChatColor.GREEN + "(" + ChatColor.AQUA + claim.getValue().name() + ChatColor.GREEN + ") " + ChatColor.YELLOW + "is claimed by " + ChatColor.BLUE + claim.getKey().getValue().getName());
                    }
                    break;
                }
                break;
            }
        }
    }
    
    public boolean containsOtherClaim(final Claim claim) {
        if (!buttplug.fdsjfhkdsjfdsjhk().getServerHandler().isUnclaimed(claim.getMaximumPoint())) {
            return true;
        }
        if (!buttplug.fdsjfhkdsjfdsjhk().getServerHandler().isUnclaimed(claim.getMinimumPoint())) {
            return true;
        }
        if (Math.abs(claim.getX1() - claim.getX2()) == 0 || Math.abs(claim.getZ1() - claim.getZ2()) == 0) {
            return false;
        }
        for (final Coordinate location : claim) {
            if (!buttplug.fdsjfhkdsjfdsjhk().getServerHandler().isUnclaimed(new Location(buttplug.fdsjfhkdsjfdsjhk().getServer().getWorld(claim.getWorld()), (double)location.getX(), 80.0, (double)location.getZ()))) {
                return true;
            }
        }
        return false;
    }
    
    public Set<Claim> touchesOtherClaim(final Claim claim) {
        final Set<Claim> touchingClaims = new HashSet<Claim>();
        for (final Coordinate coordinate : claim.outset(Claim.CuboidDirection.Horizontal, 1)) {
            final Location loc = new Location(buttplug.fdsjfhkdsjfdsjhk().getServer().getWorld(claim.getWorld()), (double)coordinate.getX(), 80.0, (double)coordinate.getZ());
            final Claim cc = LandBoard.getInstance().getClaim(loc);
            if (cc != null) {
                touchingClaims.add(cc);
            }
        }
        return touchingClaims;
    }
    
    public void setLoc(final int locationId, final Location clicked) {
        final faggot playerTeam = buttplug.fdsjfhkdsjfdsjhk().getDsfjhkdsjhdsjkhfds().getPlayerTeam(this.player.getName());
        if (playerTeam == null) {
            this.player.sendMessage(ChatColor.RED + "You have to be on a team to claim land!");
            this.cancel(true);
            return;
        }
        if (locationId == 1) {
            if (this.corner2 != null && this.isIllegal(new Claim(clicked, this.corner2))) {
                return;
            }
            this.clearPillarAt(this.corner1);
            this.corner1 = clicked;
        }
        else if (locationId == 2) {
            if (this.corner1 != null && this.isIllegal(new Claim(this.corner1, clicked))) {
                return;
            }
            this.clearPillarAt(this.corner2);
            this.corner2 = clicked;
        }
        this.player.sendMessage(ChatColor.YELLOW + "Set claim's location " + ChatColor.LIGHT_PURPLE + locationId + ChatColor.YELLOW + " to " + ChatColor.GREEN + "(" + ChatColor.WHITE + clicked.getBlockX() + ", " + clicked.getBlockY() + ", " + clicked.getBlockZ() + ChatColor.GREEN + ")" + ChatColor.YELLOW + ".");
        buttplug.fdsjfhkdsjfdsjhk().getServer().getScheduler().runTaskLater((Plugin)buttplug.fdsjfhkdsjfdsjhk(), () -> this.erectPillar(clicked, Material.EMERALD_BLOCK), 1L);
        final int price = this.getPrice();
        if (price != -1) {
            final int x = Math.abs(this.corner1.getBlockX() - this.corner2.getBlockX());
            final int z = Math.abs(this.corner1.getBlockZ() - this.corner2.getBlockZ());
            if (price > playerTeam.getBalance() && !this.bypass) {
                this.player.sendMessage(ChatColor.YELLOW + "Claim cost: " + ChatColor.RED + "$" + price + ChatColor.YELLOW + ", Current size: (" + ChatColor.WHITE + x + ", " + z + ChatColor.YELLOW + "), " + ChatColor.WHITE + x * z + ChatColor.YELLOW + " blocks");
            }
            else {
                this.player.sendMessage(ChatColor.YELLOW + "Claim cost: " + ChatColor.GREEN + "$" + price + ChatColor.YELLOW + ", Current size: (" + ChatColor.WHITE + x + ", " + z + ChatColor.YELLOW + "), " + ChatColor.WHITE + x * z + ChatColor.YELLOW + " blocks");
            }
        }
    }
    
    public void cancel(final boolean complete) {
        if (complete && (this.type == VisualClaimType.CREATE || this.type == VisualClaimType.RESIZE)) {
            this.clearPillarAt(this.corner1);
            this.clearPillarAt(this.corner2);
        }
        if (this.type == VisualClaimType.CREATE || this.type == VisualClaimType.RESIZE) {
            this.player.getInventory().remove(TeamClaimCommand.SELECTION_WAND);
        }
        HandlerList.unregisterAll((Listener)this);
        switch (this.type) {
            case MAP: {
                VisualClaim.currentMaps.remove(this.player.getName());
                if (VisualClaim.mapBlocksSent.containsKey(this.player.getName())) {
                    VisualClaim.mapBlocksSent.get(this.player.getName()).forEach(l -> this.player.sendBlockChange(l, l.getBlock().getType(), l.getBlock().getData()));
                }
                VisualClaim.mapBlocksSent.remove(this.player.getName());
                break;
            }
            case CREATE:
            case RESIZE: {
                VisualClaim.visualClaims.remove(this.player.getName());
                break;
            }
        }
        if (VisualClaim.packetBlocksSent.containsKey(this.player.getName())) {
            VisualClaim.packetBlocksSent.get(this.player.getName()).forEach(l -> this.player.sendBlockChange(l, l.getBlock().getType(), l.getBlock().getData()));
        }
        VisualClaim.packetBlocksSent.remove(this.player.getName());
    }
    
    public void purchaseClaim() {
        if (buttplug.fdsjfhkdsjfdsjhk().getDsfjhkdsjhdsjkhfds().getPlayerTeam(this.player.getName()) == null) {
            this.player.sendMessage(ChatColor.RED + "You have to be on a team to claim land!");
            this.cancel(true);
            return;
        }
        if (this.corner1 != null && this.corner2 != null) {
            final int price = this.getPrice();
            final faggot team = buttplug.fdsjfhkdsjfdsjhk().getDsfjhkdsjhdsjkhfds().getPlayerTeam(this.player.getName());
            if (!this.bypass && team.getClaims().size() >= team.getMaxClaimAmount()) {
                this.player.sendMessage(ChatColor.RED + "Your team has the maximum amount of claims, which is " + team.getMaxClaimAmount() + ".");
                return;
            }
            if (!this.bypass && !team.isCaptain(this.player.getName()) && !team.isOwner(this.player.getName())) {
                this.player.sendMessage(ChatColor.RED + "Only team captains can claim land.");
                return;
            }
            if (!this.bypass && team.getBalance() < price) {
                this.player.sendMessage(ChatColor.RED + "Your team does not have enough money to do this!");
                return;
            }
            if (!this.bypass && team.isRaidable()) {
                this.player.sendMessage(ChatColor.RED + "You cannot claim land while raidable.");
                return;
            }
            final Claim claim = new Claim(this.corner1, this.corner2);
            if (this.isIllegal(claim)) {
                return;
            }
            claim.setName(team.getName() + "_" + (100 + buttplug.RANDOM.nextInt(800)));
            claim.setY1(0);
            claim.setY2(256);
            LandBoard.getInstance().setTeamAt(claim, team);
            team.getClaims().add(claim);
            team.flagForSave();
            this.player.sendMessage(ChatColor.YELLOW + "You have claimed this land for your team!");
            if (!this.bypass) {
                team.setBalance(team.getBalance() - price);
                this.player.sendMessage(ChatColor.YELLOW + "Your team's new balance is " + ChatColor.WHITE + "$" + (int)team.getBalance() + ChatColor.LIGHT_PURPLE + " (Price: $" + price + ")");
            }
            this.cancel(true);
        }
        else {
            this.player.sendMessage(ChatColor.RED + "You have not selected both corners of your claim yet!");
        }
    }
    
    public int getPrice() {
        if (this.corner1 == null || this.corner2 == null) {
            return -1;
        }
        return Claim.getPrice(new Claim(this.corner1, this.corner2), buttplug.fdsjfhkdsjfdsjhk().getDsfjhkdsjhdsjkhfds().getPlayerTeam(this.player.getName()), true);
    }
    
    private void drawClaim(final Claim claim, final Material material) {
        for (final Location loc : claim.getCornerLocations()) {
            this.erectPillar(loc, material);
        }
    }
    
    private void erectPillar(final Location loc, final Material mat) {
        final Location set = loc.clone();
        List<Location> locs = new ArrayList<Location>();
        if (this.type == VisualClaimType.MAP) {
            if (VisualClaim.mapBlocksSent.containsKey(this.player.getName())) {
                locs = VisualClaim.mapBlocksSent.get(this.player.getName());
            }
        }
        else if (VisualClaim.packetBlocksSent.containsKey(this.player.getName())) {
            locs = VisualClaim.packetBlocksSent.get(this.player.getName());
        }
        for (int i = 0; i < 256; ++i) {
            set.setY((double)i);
            if (set.getBlock().getType() == Material.AIR || set.getBlock().getType().isTransparent()) {
                if (i % 5 == 0) {
                    this.player.sendBlockChange(set, mat, (byte)0);
                }
                else {
                    this.player.sendBlockChange(set, Material.GLASS, (byte)0);
                }
                locs.add(set.clone());
            }
        }
        if (this.type == VisualClaimType.MAP) {
            VisualClaim.mapBlocksSent.put(this.player.getName(), locs);
        }
        else {
            VisualClaim.packetBlocksSent.put(this.player.getName(), locs);
        }
    }
    
    private void clearPillarAt(final Location loc) {
        if (VisualClaim.packetBlocksSent.containsKey(this.player.getName()) && loc != null) {
            VisualClaim.packetBlocksSent.get(this.player.getName()).removeIf(l -> {
                if (l.getBlockX() == loc.getBlockX() && l.getBlockZ() == loc.getBlockZ()) {
                    this.player.sendBlockChange(l, l.getBlock().getType(), l.getBlock().getData());
                    return true;
                }
                return false;
            });
        }
    }
    
    public boolean isIllegal(final Claim claim) {
        final faggot team = buttplug.fdsjfhkdsjfdsjhk().getDsfjhkdsjhdsjkhfds().getPlayerTeam(this.player.getName());
        if (!this.bypass && this.containsOtherClaim(claim)) {
            this.player.sendMessage(ChatColor.RED + "This claim contains unclaimable land!");
            return true;
        }
        if (!this.bypass && this.player.getWorld().getEnvironment() != World.Environment.NORMAL) {
            this.player.sendMessage(ChatColor.RED + "Land can only be claimed in the overworld.");
            return true;
        }
        final Set<Claim> touching = this.touchesOtherClaim(claim);
        final Set<Claim> cloneCheck = new HashSet<Claim>();
        touching.forEach(tee -> cloneCheck.add(tee.clone()));
        final boolean contains = cloneCheck.removeIf(c -> team.ownsClaim(c));
        if (!this.bypass && team.getClaims().size() > 0 && !contains) {
            this.player.sendMessage(ChatColor.RED + "All of your claims must be touching each other!");
            return true;
        }
        if (!this.bypass && (touching.size() > 1 || (touching.size() == 1 && !contains))) {
            this.player.sendMessage(ChatColor.RED + "Your claim must be at least 1 block away from enemy claims!");
            return true;
        }
        final int x = Math.abs(claim.getX1() - claim.getX2());
        final int z = Math.abs(claim.getZ1() - claim.getZ2());
        if (!this.bypass && (x < 4 || z < 4)) {
            this.player.sendMessage(ChatColor.RED + "Your claim is too small! The claim has to be at least (" + ChatColor.WHITE + "5 x 5" + ChatColor.RED + ")!");
            return true;
        }
        if (!this.bypass && (x >= 3 * z || z >= 3 * x)) {
            this.player.sendMessage(ChatColor.RED + "One side of your claim cannot be more than 3 times larger than the other!");
            return true;
        }
        return false;
    }
    
    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent e) {
        if (e.getPlayer() == this.player && (this.type == VisualClaimType.CREATE || this.type == VisualClaimType.RESIZE) && this.player.getItemInHand() != null && this.player.getItemInHand().getType() == Material.WOOD_HOE) {
            e.setCancelled(true);
            e.setUseInteractedBlock(Event.Result.DENY);
            if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if (!this.bypass && !buttplug.fdsjfhkdsjfdsjhk().getServerHandler().isUnclaimed(e.getClickedBlock().getLocation())) {
                    this.player.sendMessage(ChatColor.RED + "You can only claim land in the Wilderness!");
                    return;
                }
                this.setLoc(2, e.getClickedBlock().getLocation());
            }
            else if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
                if (!this.bypass && !buttplug.fdsjfhkdsjfdsjhk().getServerHandler().isUnclaimed(e.getClickedBlock().getLocation())) {
                    this.player.sendMessage(ChatColor.RED + "You can only claim land in the Wilderness!");
                    return;
                }
                if (this.player.isSneaking()) {
                    this.purchaseClaim();
                }
                else {
                    this.setLoc(1, e.getClickedBlock().getLocation());
                }
            }
            else if (e.getAction() == Action.LEFT_CLICK_AIR && this.player.isSneaking()) {
                this.purchaseClaim();
            }
            else if (e.getAction() == Action.RIGHT_CLICK_AIR) {
                this.cancel(false);
                this.player.sendMessage(ChatColor.RED + "You have unset your first and second locations!");
            }
        }
    }
    
    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent e) {
        if (this.player == e.getPlayer()) {
            this.cancel(true);
        }
    }
    
    public Material getMaterial(int iteration) {
        if (iteration == -1) {
            return Material.IRON_BLOCK;
        }
        while (iteration >= VisualClaim.MAP_MATERIALS.length) {
            iteration -= VisualClaim.MAP_MATERIALS.length;
        }
        return VisualClaim.MAP_MATERIALS[iteration];
    }
    
    public static VisualClaim getVisualClaim(final String name) {
        return VisualClaim.visualClaims.get(name);
    }
    
    public VisualClaim(@NonNull final Player player, @NonNull final VisualClaimType type, @NonNull final boolean bypass) {
        super();
        if (player == null) {
            throw new NullPointerException("player");
        }
        if (type == null) {
            throw new NullPointerException("type");
        }
        this.player = player;
        this.type = type;
        this.bypass = bypass;
    }
    
    public static Map<String, VisualClaim> getCurrentMaps() {
        return VisualClaim.currentMaps;
    }
    
    public static Map<String, VisualClaim> getVisualClaims() {
        return VisualClaim.visualClaims;
    }
    
    @NonNull
    public Player getPlayer() {
        return this.player;
    }
    
    static {
        MAP_MATERIALS = new Material[] { Material.DIAMOND_BLOCK, Material.GOLD_BLOCK, Material.LOG, Material.BRICK, Material.WOOD, Material.REDSTONE_BLOCK, Material.LAPIS_BLOCK, Material.CHEST, Material.MELON_BLOCK, Material.STONE, Material.COBBLESTONE, Material.COAL_BLOCK, Material.DIAMOND_ORE, Material.COAL_ORE, Material.GOLD_ORE, Material.REDSTONE_ORE, Material.FURNACE };
        VisualClaim.currentMaps = new HashMap<String, VisualClaim>();
        VisualClaim.visualClaims = new HashMap<String, VisualClaim>();
        VisualClaim.packetBlocksSent = new HashMap<String, List<Location>>();
        VisualClaim.mapBlocksSent = new HashMap<String, List<Location>>();
    }
}
