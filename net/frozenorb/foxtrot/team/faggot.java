package net.frozenorb.foxtrot.team;

import java.text.*;
import org.bson.types.*;
import net.frozenorb.foxtrot.*;
import org.bukkit.entity.*;
import net.frozenorb.foxtrot.team.dtr.bitmask.*;
import java.util.*;
import net.frozenorb.foxtrot.team.claims.*;
import net.frozenorb.foxtrot.jedis.*;
import redis.clients.jedis.*;
import net.frozenorb.foxtrot.team.dtr.*;
import java.math.*;
import org.bukkit.*;
import net.minecraft.util.org.apache.commons.lang3.*;
import net.frozenorb.foxtrot.util.*;
import net.frozenorb.foxtrot.jedis.persist.*;
import java.util.concurrent.*;
import java.io.*;

public class faggot
{
    public static final DecimalFormat DTR_FORMAT;
    public static final int MAX_TEAM_SIZE = 25;
    public static final int MAX_CLAIMS = 2;
    public static final long DTR_REGEN_TIME;
    public static final long RAIDABLE_REGEN_TIME;
    private ObjectId uniqueId;
    private String name;
    private Location hq;
    private String owner;
    private Set<String> members;
    private Set<String> captains;
    private boolean needsSave;
    private boolean loading;
    private Set<String> invitations;
    private double DTR;
    private List<Claim> claims;
    private long raidableCooldown;
    private long deathCooldown;
    private double balance;
    
    public faggot(final String name) {
        super();
        this.owner = null;
        this.members = new HashSet<String>();
        this.captains = new HashSet<String>();
        this.needsSave = false;
        this.loading = false;
        this.invitations = new HashSet<String>();
        this.claims = new ArrayList<Claim>();
        this.name = name;
    }
    
    public void setDTR(final double newDTR) {
        if (this.DTR == newDTR) {
            return;
        }
        if (!this.isLoading()) {
            buttplug.fdsjfhkdsjfdsjhk().getLogger().info("[DTR Change] " + this.getName() + ": " + this.DTR + " --> " + newDTR);
        }
        this.DTR = newDTR;
        this.flagForSave();
    }
    
    public void setName(final String name) {
        this.name = name;
        this.flagForSave();
    }
    
    public String getName(final Player player) {
        if (this.owner == null) {
            if (this.hasDTRBitmask(DTRBitmaskType.SAFE_ZONE)) {
                switch (player.getWorld().getEnvironment()) {
                    case NETHER: {
                        return ChatColor.GREEN + "Nether Spawn";
                    }
                    case THE_END: {
                        if (this.hasDTRBitmask(DTRBitmaskType.DENY_REENTRY)) {
                            return ChatColor.GREEN + "The End Spawn";
                        }
                        return ChatColor.GREEN + "The End Exit";
                    }
                    default: {
                        return ChatColor.GREEN + "Spawn";
                    }
                }
            }
            else if (this.hasDTRBitmask(DTRBitmaskType.ROAD)) {
                return ChatColor.RED + "Road";
            }
        }
        if (this.isMember(player)) {
            return ChatColor.GREEN + this.getName();
        }
        if (this.isAlly(player)) {
            return ChatColor.LIGHT_PURPLE + this.getName();
        }
        return ChatColor.RED + this.getName();
    }
    
    public void addMember(final String member) {
        if (member.equalsIgnoreCase("null")) {
            return;
        }
        this.members.add(member);
        this.flagForSave();
    }
    
    public void addCaptain(final String captain) {
        this.captains.add(captain);
        this.flagForSave();
    }
    
    public void setBalance(final double balance) {
        this.balance = balance;
        this.flagForSave();
    }
    
    public void setRaidableCooldown(final long raidableCooldown) {
        this.raidableCooldown = raidableCooldown;
        this.flagForSave();
    }
    
    public void setDeathCooldown(final long deathCooldown) {
        this.deathCooldown = deathCooldown;
        this.flagForSave();
    }
    
    public void removeCaptain(final String name) {
        final Iterator<String> iterator = this.captains.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().equalsIgnoreCase(name)) {
                iterator.remove();
            }
        }
        this.flagForSave();
    }
    
    public void setOwner(final String owner) {
        final String oldOwner = this.owner;
        this.owner = owner;
        if (owner != null && !owner.equals("null")) {
            this.members.add(owner);
        }
        this.flagForSave();
    }
    
    public void setHQ(final Location hq) {
        this.hq = hq;
        this.flagForSave();
    }
    
    public void disband() {
        try {
            if (this.owner != null) {
                buttplug.fdsjfhkdsjfdsjhk().getBalanceMap().setBalance(this.owner, (int)(buttplug.fdsjfhkdsjfdsjhk().getBalanceMap().getBalance(this.owner) + this.balance));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        for (final String member : this.members) {
            buttplug.fdsjfhkdsjfdsjhk().getDsfjhkdsjhdsjkhfds().getPlayerTeamMap().remove(member.toLowerCase());
        }
        LandBoard.getInstance().clear(this);
        buttplug.fdsjfhkdsjfdsjhk().getDsfjhkdsjhdsjkhfds().getTeamNameMap().remove(this.name.toLowerCase());
        buttplug.fdsjfhkdsjfdsjhk().getDsfjhkdsjhdsjkhfds().getTeamUniqueIdMap().remove(this.uniqueId);
        try {
            buttplug.fdsjfhkdsjfdsjhk().eatmyass((yourmom<Object>)new yourmom<Object>() {
                @Override
                public Object execute(final Jedis jedis) {
                    jedis.del("fox_teams." + faggot.this.name.toLowerCase());
                    return null;
                }
            });
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        this.needsSave = false;
    }
    
    public void rename(final String newName) {
        final String oldName = this.name;
        this.name = newName;
        for (final Claim claim : this.claims) {
            claim.setName(claim.getName().replaceAll(oldName, newName));
        }
        buttplug.fdsjfhkdsjfdsjhk().getDsfjhkdsjhdsjkhfds().getTeamNameMap().remove(oldName.toLowerCase());
        buttplug.fdsjfhkdsjfdsjhk().getDsfjhkdsjhdsjkhfds().getTeamUniqueIdMap().remove(this.uniqueId);
        buttplug.fdsjfhkdsjfdsjhk().getDsfjhkdsjhdsjkhfds().addTeam(this);
        buttplug.fdsjfhkdsjfdsjhk().eatmyass((yourmom<Object>)new yourmom<Object>() {
            @Override
            public Object execute(final Jedis jedis) {
                jedis.del("fox_teams." + oldName.toLowerCase());
                return null;
            }
        });
        this.flagForSave();
    }
    
    public void flagForSave() {
        this.needsSave = true;
    }
    
    public boolean isOwner(final String name) {
        return this.owner != null && this.owner.equalsIgnoreCase(name);
    }
    
    public String getActualPlayerName(final String pName) {
        for (final String str : this.members) {
            if (pName.equalsIgnoreCase(str)) {
                return str;
            }
        }
        return null;
    }
    
    public boolean isMember(final Player player) {
        return this.isMember(player.getName());
    }
    
    public boolean isMember(final String name) {
        for (final String member : this.members) {
            if (name.equalsIgnoreCase(member)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isCaptain(final String name) {
        for (final String member : this.captains) {
            if (name.equalsIgnoreCase(member)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isAlly(final Player player) {
        return this.isAlly(player.getName());
    }
    
    public boolean isAlly(final String name) {
        return false;
    }
    
    public boolean ownsLocation(final Location location) {
        return LandBoard.getInstance().getTeam(location) == this;
    }
    
    public boolean ownsClaim(final Claim claim) {
        return this.claims.contains(claim);
    }
    
    public boolean removeMember(final String name) {
        Iterator<String> membersIterator = this.members.iterator();
        while (membersIterator.hasNext()) {
            final String member = membersIterator.next();
            if (member.equalsIgnoreCase(name)) {
                membersIterator.remove();
                break;
            }
        }
        this.removeCaptain(name);
        if (this.isOwner(name)) {
            membersIterator = this.members.iterator();
            if (membersIterator.hasNext()) {
                this.owner = membersIterator.next();
            }
            else {
                this.owner = null;
            }
        }
        if (this.DTR > this.getMaxDTR()) {
            this.DTR = this.getMaxDTR();
        }
        this.flagForSave();
        return this.owner == null || this.members.size() == 0;
    }
    
    public boolean hasDTRBitmask(final DTRBitmaskType bitmaskType) {
        if (this.getOwner() != null) {
            return false;
        }
        final int dtrInt = (int)this.DTR;
        return (dtrInt & bitmaskType.getBitmask()) == bitmaskType.getBitmask();
    }
    
    public int getOnlineMemberAmount() {
        int amt = 0;
        for (final String members : this.getMembers()) {
            final Player exactPlayer = buttplug.fdsjfhkdsjfdsjhk().getServer().getPlayerExact(members);
            if (exactPlayer != null && !exactPlayer.hasMetadata("invisible")) {
                ++amt;
            }
        }
        return amt;
    }
    
    public List<Player> getOnlineMembers() {
        final List<Player> players = new ArrayList<Player>();
        for (final String member : this.getMembers()) {
            if (member == null) {
                continue;
            }
            final Player exactPlayer = buttplug.fdsjfhkdsjfdsjhk().getServer().getPlayerExact(member);
            if (exactPlayer == null || exactPlayer.hasMetadata("invisible")) {
                continue;
            }
            players.add(exactPlayer);
        }
        return players;
    }
    
    public List<String> getOfflineMembers() {
        final List<String> players = new ArrayList<String>();
        for (final String member : this.getMembers()) {
            if (member == null) {
                continue;
            }
            final Player exactPlayer = buttplug.fdsjfhkdsjfdsjhk().getServer().getPlayerExact(member);
            if (exactPlayer != null && !exactPlayer.hasMetadata("invisible")) {
                continue;
            }
            players.add(member);
        }
        return players;
    }
    
    public int getSize() {
        return this.getMembers().size();
    }
    
    public boolean isRaidable() {
        return this.DTR <= 0.0;
    }
    
    public void playerDeath(final String p, final double dtrLoss) {
        final double newDTR = Math.max(this.DTR - dtrLoss, -0.99);
        for (final Player player : this.getOnlineMembers()) {
            player.sendMessage(ChatColor.RED + "Member Death: " + ChatColor.WHITE + p);
            player.sendMessage(ChatColor.RED + "DTR: " + ChatColor.WHITE + faggot.DTR_FORMAT.format(newDTR));
        }
        buttplug.fdsjfhkdsjfdsjhk().getLogger().info("[TeamDeath] " + this.name + " > " + "Player death: [" + p + "]");
        this.setDTR(newDTR);
        if (this.isRaidable()) {
            this.raidableCooldown = System.currentTimeMillis() + faggot.RAIDABLE_REGEN_TIME;
        }
        DTRHandler.setCooldown(this);
        this.deathCooldown = System.currentTimeMillis() + faggot.DTR_REGEN_TIME;
    }
    
    public BigDecimal getDTRIncrement() {
        return this.getDTRIncrement(this.getOnlineMemberAmount());
    }
    
    public BigDecimal getDTRIncrement(final int playersOnline) {
        final BigDecimal dtrPerHour = new BigDecimal(DTRHandler.getBaseDTRIncrement(this.getSize())).multiply(new BigDecimal(playersOnline));
        return dtrPerHour.divide(new BigDecimal("60"), 5, RoundingMode.HALF_DOWN);
    }
    
    public double getMaxDTR() {
        return DTRHandler.getMaxDTR(this.getSize());
    }
    
    public void load(final String str) {
        this.loading = true;
        final String[] split2;
        final String[] lines = split2 = str.split("\n");
        for (final String line : split2) {
            final String identifier = line.substring(0, line.indexOf(58));
            final String[] lineParts = line.substring(line.indexOf(58) + 1).split(",");
            if (identifier.equalsIgnoreCase("Owner")) {
                if (!lineParts[0].equals("null")) {
                    this.setOwner(lineParts[0]);
                }
            }
            else if (identifier.equalsIgnoreCase("UUID")) {
                this.uniqueId = new ObjectId(lineParts[0]);
            }
            else if (identifier.equalsIgnoreCase("Members")) {
                for (final String name : lineParts) {
                    if (name.length() >= 2 && !name.equalsIgnoreCase("null")) {
                        this.addMember(name.trim());
                    }
                }
            }
            else if (identifier.equalsIgnoreCase("Captains")) {
                for (final String name : lineParts) {
                    if (name.length() >= 2 && !name.equalsIgnoreCase("null")) {
                        this.addCaptain(name.trim());
                    }
                }
            }
            else if (identifier.equalsIgnoreCase("Invited")) {
                for (final String name : lineParts) {
                    if (name.length() >= 2 && !name.equalsIgnoreCase("null")) {
                        this.getInvitations().add(name);
                    }
                }
            }
            else if (identifier.equalsIgnoreCase("HQ")) {
                this.setHQ(this.parseLocation(lineParts));
            }
            else if (identifier.equalsIgnoreCase("DTR")) {
                this.setDTR(Double.valueOf(lineParts[0]));
            }
            else if (identifier.equalsIgnoreCase("Balance")) {
                this.setBalance(Double.valueOf(lineParts[0]));
            }
            else if (identifier.equalsIgnoreCase("DeathCooldown")) {
                this.setDeathCooldown(Long.valueOf(lineParts[0]));
            }
            else if (identifier.equalsIgnoreCase("RaidableCooldown")) {
                this.setRaidableCooldown(Long.valueOf(lineParts[0]));
            }
            else if (identifier.equalsIgnoreCase("FriendlyName")) {
                this.setName(lineParts[0]);
            }
            else if (identifier.equalsIgnoreCase("Claims")) {
                for (String claim : lineParts) {
                    claim = claim.replace("[", "").replace("]", "");
                    if (claim.contains(":")) {
                        final String[] split = claim.split(":");
                        final int x1 = Integer.valueOf(split[0].trim());
                        final int y1 = Integer.valueOf(split[1].trim());
                        final int z1 = Integer.valueOf(split[2].trim());
                        final int x2 = Integer.valueOf(split[3].trim());
                        final int y2 = Integer.valueOf(split[4].trim());
                        final int z2 = Integer.valueOf(split[5].trim());
                        final String name2 = split[6].trim();
                        final String world = split[7].trim();
                        final Claim claimObj = new Claim(world, x1, y1, z1, x2, y2, z2);
                        claimObj.setName(name2);
                        this.getClaims().add(claimObj);
                    }
                }
            }
        }
        if (this.uniqueId == null) {
            this.uniqueId = new ObjectId();
            buttplug.fdsjfhkdsjfdsjhk().getLogger().info("Generating UUID for team " + this.getName() + "...");
        }
        this.loading = false;
        this.needsSave = false;
    }
    
    public String saveString(final boolean toJedis) {
        if (toJedis) {
            this.needsSave = false;
        }
        if (this.loading) {
            return null;
        }
        final StringBuilder teamString = new StringBuilder();
        final StringBuilder members = new StringBuilder();
        final StringBuilder captains = new StringBuilder();
        final StringBuilder invites = new StringBuilder();
        final Location homeLoc = this.getHq();
        for (final String member : this.getMembers()) {
            members.append(member.trim()).append(", ");
        }
        if (members.length() > 2) {
            members.setLength(members.length() - 2);
        }
        for (final String captain : this.getCaptains()) {
            captains.append(captain.trim()).append(", ");
        }
        if (captains.length() > 2) {
            captains.setLength(captains.length() - 2);
        }
        for (final String invite : this.getInvitations()) {
            invites.append(invite.trim()).append(", ");
        }
        if (invites.length() > 2) {
            invites.setLength(invites.length() - 2);
        }
        teamString.append("UUID:").append(this.getUniqueId().toString()).append("\n");
        teamString.append("Owner:").append(this.getOwner()).append('\n');
        teamString.append("Captains:").append(captains.toString()).append('\n');
        teamString.append("Members:").append(members.toString()).append('\n');
        teamString.append("Invited:").append(invites.toString()).append('\n');
        teamString.append("Claims:").append(this.getClaims().toString()).append('\n');
        teamString.append("DTR:").append(this.getDTR()).append('\n');
        teamString.append("Balance:").append(this.getBalance()).append('\n');
        teamString.append("DeathCooldown:").append(this.getDeathCooldown()).append('\n');
        teamString.append("RaidableCooldown:").append(this.getRaidableCooldown()).append('\n');
        teamString.append("FriendlyName:").append(this.getName()).append('\n');
        if (homeLoc != null) {
            teamString.append("HQ:").append(homeLoc.getWorld().getName()).append(",").append(homeLoc.getX()).append(",").append(homeLoc.getY()).append(",").append(homeLoc.getZ()).append(",").append(homeLoc.getYaw()).append(",").append(homeLoc.getPitch()).append('\n');
        }
        return teamString.toString();
    }
    
    public int getMaxClaimAmount() {
        return 2;
    }
    
    private Location parseLocation(final String[] args) {
        if (args.length != 6) {
            return null;
        }
        final World world = buttplug.fdsjfhkdsjfdsjhk().getServer().getWorld(args[0]);
        final double x = Double.parseDouble(args[1]);
        final double y = Double.parseDouble(args[2]);
        final double z = Double.parseDouble(args[3]);
        final float yaw = Float.parseFloat(args[4]);
        final float pitch = Float.parseFloat(args[5]);
        return new Location(world, x, y, z, yaw, pitch);
    }
    
    public void sendTeamInfo(final Player player) {
        final String gray = ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + StringUtils.repeat("-", 53);
        player.sendMessage(gray);
        if (this.owner == null || this.owner.equals("null")) {
            final String hqString = (this.hq == null) ? "None" : (this.hq.getBlockX() + ", " + this.hq.getBlockZ());
            player.sendMessage(ChatColor.BLUE + this.getName());
            player.sendMessage(ChatColor.YELLOW + "Location: " + ChatColor.WHITE + hqString);
            player.sendMessage(gray);
            return;
        }
        final String hqString = (this.hq == null) ? "None" : (this.hq.getBlockX() + ", " + this.hq.getBlockZ());
        player.sendMessage(ChatColor.BLUE + this.getName() + ChatColor.GRAY + " [" + this.getOnlineMemberAmount() + "/" + this.getSize() + "]" + ChatColor.DARK_AQUA + " - " + ChatColor.YELLOW + "HQ: " + ChatColor.WHITE + hqString);
        final KillsMap km = buttplug.fdsjfhkdsjfdsjhk().getKillsMap();
        final Player owner = buttplug.fdsjfhkdsjfdsjhk().getServer().getPlayerExact(this.owner);
        player.sendMessage(ChatColor.YELLOW + "Leader: " + ((owner == null || owner.hasMetadata("invisible")) ? ChatColor.GRAY : ChatColor.GREEN) + this.owner + ChatColor.YELLOW + "[" + ChatColor.GREEN + km.getKills(this.owner) + ChatColor.YELLOW + "]");
        boolean first = true;
        boolean first2 = true;
        final StringBuilder members = new StringBuilder("§eMembers: ");
        final StringBuilder captains = new StringBuilder("§eCaptains: ");
        int captainAmount = 0;
        int memberAmount = 0;
        for (final Player online : this.getOnlineMembers()) {
            if (online.hasMetadata("invisible")) {
                continue;
            }
            StringBuilder toAdd = members;
            if (this.isOwner(online.getName())) {
                continue;
            }
            if (this.isCaptain(online.getName())) {
                toAdd = captains;
                if (!first2) {
                    toAdd.append("§7, ");
                }
                ++captainAmount;
                toAdd.append("§a" + online.getName() + "§e[§a" + km.getKills(online.getName()) + "§e]");
                first2 = false;
            }
            else {
                if (!first) {
                    toAdd.append("§7, ");
                }
                ++memberAmount;
                toAdd.append("§a" + online.getName() + "§e[§a" + km.getKills(online.getName()) + "§e]");
                first = false;
            }
        }
        for (final String offline : this.getOfflineMembers()) {
            StringBuilder toAdd = members;
            if (this.isOwner(offline)) {
                continue;
            }
            if (this.isCaptain(offline)) {
                toAdd = captains;
                if (!first2) {
                    toAdd.append("§7, ");
                }
                else {
                    first2 = false;
                }
                ++captainAmount;
                toAdd.append("§7" + offline + "§e[§a" + km.getKills(offline) + "§e]");
            }
            else {
                if (!first) {
                    toAdd.append("§7, ");
                }
                else {
                    first = false;
                }
                ++memberAmount;
                toAdd.append("§7" + offline + "§e[§a" + km.getKills(offline) + "§e]");
            }
        }
        if (captainAmount > 0) {
            player.sendMessage(captains.toString());
        }
        if (memberAmount > 0) {
            player.sendMessage(members.toString());
        }
        player.sendMessage(ChatColor.YELLOW + "Balance: " + ChatColor.BLUE + "$" + Math.round(this.balance));
        String dtrMsg = ChatColor.YELLOW + "Deaths until Raidable: " + this.getDTRColor() + faggot.DTR_FORMAT.format(this.DTR);
        boolean showTimeUntilRegen = false;
        if (this.getOnlineMemberAmount() == 0) {
            dtrMsg = dtrMsg + ChatColor.GRAY + "\u25c0";
        }
        else if (DTRHandler.isRegenerating(this)) {
            dtrMsg = dtrMsg + ChatColor.GREEN + "\u25b2";
        }
        else if (DTRHandler.isOnCooldown(this)) {
            dtrMsg = dtrMsg + ChatColor.RED + "\u25a0";
            showTimeUntilRegen = true;
        }
        else {
            dtrMsg = dtrMsg + ChatColor.GREEN + "\u25c0";
        }
        player.sendMessage(dtrMsg);
        if (showTimeUntilRegen) {
            final long till = Math.max(this.getRaidableCooldown(), this.getDeathCooldown());
            final int seconds = (int)(till - System.currentTimeMillis()) / 1000;
            player.sendMessage(ChatColor.YELLOW + "Time Until Regen: " + ChatColor.BLUE + TimeUtils.getConvertedTime(seconds).trim());
        }
        player.sendMessage(gray);
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof faggot) {
            return ((faggot)obj).getName().equals(this.getName());
        }
        return super.equals(obj);
    }
    
    public ChatColor getDTRColor() {
        ChatColor dtrColor = ChatColor.GREEN;
        if (this.DTR / this.getMaxDTR() <= 0.25) {
            if (this.isRaidable()) {
                dtrColor = ChatColor.DARK_RED;
            }
            else {
                dtrColor = ChatColor.YELLOW;
            }
        }
        return dtrColor;
    }
    
    public ObjectId getUniqueId() {
        return this.uniqueId;
    }
    
    public void setUniqueId(final ObjectId uniqueId) {
        this.uniqueId = uniqueId;
    }
    
    public String getName() {
        return this.name;
    }
    
    public Location getHq() {
        return this.hq;
    }
    
    public String getOwner() {
        return this.owner;
    }
    
    public Set<String> getMembers() {
        return this.members;
    }
    
    public Set<String> getCaptains() {
        return this.captains;
    }
    
    public boolean isNeedsSave() {
        return this.needsSave;
    }
    
    public boolean isLoading() {
        return this.loading;
    }
    
    public Set<String> getInvitations() {
        return this.invitations;
    }
    
    public double getDTR() {
        return this.DTR;
    }
    
    public List<Claim> getClaims() {
        return this.claims;
    }
    
    public long getRaidableCooldown() {
        return this.raidableCooldown;
    }
    
    public long getDeathCooldown() {
        return this.deathCooldown;
    }
    
    public double getBalance() {
        return this.balance;
    }
    
    static {
        DTR_FORMAT = new DecimalFormat("0.00");
        DTR_REGEN_TIME = TimeUnit.MINUTES.toMillis(60L);
        RAIDABLE_REGEN_TIME = TimeUnit.MINUTES.toMillis(90L);
    }
}
