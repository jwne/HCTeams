package net.frozenorb.foxtrot.server;

import org.bukkit.block.*;
import net.minecraft.util.org.apache.commons.io.*;
import com.mongodb.util.*;
import com.mongodb.*;
import net.frozenorb.foxtrot.*;
import org.bukkit.plugin.*;
import org.bukkit.craftbukkit.libs.com.google.gson.*;
import java.util.concurrent.atomic.*;
import org.bukkit.metadata.*;
import org.bukkit.scheduler.*;
import net.frozenorb.foxtrot.team.claims.*;
import net.frozenorb.foxtrot.team.dtr.bitmask.*;
import net.frozenorb.foxtrot.listener.*;
import net.frozenorb.foxtrot.team.*;
import org.bukkit.util.*;
import java.util.concurrent.*;
import org.bukkit.event.entity.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import net.frozenorb.foxtrot.util.*;
import org.bukkit.*;
import org.bukkit.enchantments.*;
import java.util.*;
import org.bukkit.inventory.meta.*;
import java.text.*;
import org.bukkit.inventory.*;
import com.google.common.collect.*;
import java.io.*;

public class ServerHandler
{
    public static int WARZONE_RADIUS;
    public static final Set<Integer> DISALLOWED_POTIONS;
    private static Map<String, Integer> tasks;
    private Set<String> usedNames;
    private Set<String> highRollers;
    private boolean EOTW;
    private boolean PreEOTW;
    private HashMap<Sign, BukkitRunnable> showSignTasks;
    
    public ServerHandler() {
        super();
        this.usedNames = new HashSet<String>();
        this.highRollers = new HashSet<String>();
        this.EOTW = false;
        this.PreEOTW = false;
        this.showSignTasks = new HashMap<Sign, BukkitRunnable>();
        try {
            final File f = new File("usedNames.json");
            if (!f.exists()) {
                f.createNewFile();
            }
            final BasicDBObject dbo = (BasicDBObject)JSON.parse(FileUtils.readFileToString(f));
            if (dbo != null) {
                for (final Object o : (BasicDBList)dbo.get("names")) {
                    this.usedNames.add((String)o);
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        try {
            final File f = new File("highRollers.json");
            if (!f.exists()) {
                f.createNewFile();
            }
            final BasicDBObject dbo = (BasicDBObject)JSON.parse(FileUtils.readFileToString(f));
            if (dbo != null) {
                for (final Object o : (BasicDBList)dbo.get("names")) {
                    this.highRollers.add((String)o);
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        new BukkitRunnable() {
            public void run() {
                final StringBuilder highRollers = new StringBuilder();
                for (final String highRoller : buttplug.fdsjfhkdsjfdsjhk().getServerHandler().getHighRollers()) {
                    highRollers.append(ChatColor.DARK_PURPLE).append(highRoller).append(ChatColor.GOLD).append(", ");
                }
                if (highRollers.length() > 2) {
                    highRollers.setLength(highRollers.length() - 2);
                    buttplug.fdsjfhkdsjfdsjhk().getServer().broadcastMessage(ChatColor.GOLD + "HCTeams HighRollers: " + highRollers.toString());
                }
            }
        }.runTaskTimer((Plugin)buttplug.fdsjfhkdsjfdsjhk(), 20L, 6000L);
    }
    
    public void save() {
        try {
            final File f = new File("usedNames.json");
            if (!f.exists()) {
                f.createNewFile();
            }
            final BasicDBObject dbo = new BasicDBObject();
            final BasicDBList list = new BasicDBList();
            for (final String n : this.usedNames) {
                ((ArrayList<String>)list).add(n);
            }
            dbo.put("names", list);
            FileUtils.write(f, (CharSequence)new GsonBuilder().setPrettyPrinting().create().toJson(new JsonParser().parse(dbo.toString())));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        try {
            final File f = new File("highRollers.json");
            if (!f.exists()) {
                f.createNewFile();
            }
            final BasicDBObject dbo = new BasicDBObject();
            final BasicDBList list = new BasicDBList();
            for (final String n : this.highRollers) {
                ((ArrayList<String>)list).add(n);
            }
            dbo.put("names", list);
            FileUtils.write(f, (CharSequence)new GsonBuilder().setPrettyPrinting().create().toJson(new JsonParser().parse(dbo.toString())));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public boolean isBannedPotion(final int value) {
        for (final int i : ServerHandler.DISALLOWED_POTIONS) {
            if (i == value) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isWarzone(final Location loc) {
        return loc.getWorld().getEnvironment() == World.Environment.NORMAL && Math.abs(loc.getBlockX()) <= ServerHandler.WARZONE_RADIUS && Math.abs(loc.getBlockZ()) <= ServerHandler.WARZONE_RADIUS;
    }
    
    public void startLogoutSequence(final Player player) {
        player.sendMessage(ChatColor.YELLOW + "§lLogging out... §ePlease wait§c 30§e seconds.");
        final AtomicInteger seconds = new AtomicInteger(30);
        final BukkitTask taskid = new BukkitRunnable() {
            public void run() {
                seconds.set(seconds.get() - 1);
                player.sendMessage(ChatColor.RED + "" + seconds.get() + "§e seconds...");
                if (seconds.get() == 0 && ServerHandler.tasks.containsKey(player.getName())) {
                    ServerHandler.tasks.remove(player.getName());
                    player.setMetadata("loggedout", (MetadataValue)new FixedMetadataValue((Plugin)buttplug.fdsjfhkdsjfdsjhk(), (Object)true));
                    player.kickPlayer("§cYou have been safely logged out of the server!");
                    this.cancel();
                }
            }
        }.runTaskTimer((Plugin)buttplug.fdsjfhkdsjfdsjhk(), 20L, 20L);
        if (ServerHandler.tasks.containsKey(player.getName())) {
            Bukkit.getScheduler().cancelTask((int)ServerHandler.tasks.remove(player.getName()));
        }
        ServerHandler.tasks.put(player.getName(), taskid.getTaskId());
    }
    
    public RegionData getRegion(final Location location) {
        return this.getRegion(LandBoard.getInstance().getTeam(location), location);
    }
    
    public RegionData getRegion(final faggot ownerTo, final Location location) {
        if (ownerTo != null && ownerTo.getOwner() == null) {
            if (ownerTo.hasDTRBitmask(DTRBitmaskType.SAFE_ZONE)) {
                return new RegionData(RegionType.SPAWN, ownerTo);
            }
            if (ownerTo.hasDTRBitmask(DTRBitmaskType.ROAD)) {
                return new RegionData(RegionType.ROAD, ownerTo);
            }
        }
        if (ownerTo != null) {
            return new RegionData(RegionType.CLAIMED_LAND, ownerTo);
        }
        if (this.isWarzone(location)) {
            return new RegionData(RegionType.WARZONE, null);
        }
        return new RegionData(RegionType.WILDNERNESS, null);
    }
    
    public void beginWarp(final Player player, final faggot team, final int price) {
        final boolean enemyCheckBypass = player.getGameMode() == GameMode.CREATIVE || player.hasMetadata("invisible") || (!buttplug.fdsjfhkdsjfdsjhk().getServerHandler().isEOTW() && DTRBitmaskType.SAFE_ZONE.appliesAt(player.getLocation()));
        final dsfjhkdsjhdsjkhfds tm = buttplug.fdsjfhkdsjfdsjhk().getDsfjhkdsjhdsjkhfds();
        final double bal = tm.getPlayerTeam(player.getName()).getBalance();
        if (bal < price) {
            player.sendMessage(ChatColor.RED + "This costs §e$" + price + "§c while your team has only §e$" + bal + "§c!");
            return;
        }
        if (!enemyCheckBypass) {
            if (!enemyCheckBypass && EnderpearlListener.getEnderpearlCooldown().containsKey(player.getName()) && EnderpearlListener.getEnderpearlCooldown().get(player.getName()) > System.currentTimeMillis()) {
                player.sendMessage(ChatColor.RED + "You cannot warp while your enderpearl cooldown is active!");
                return;
            }
            boolean enemyWithinRange = false;
            for (final Entity e : player.getNearbyEntities(30.0, 256.0, 30.0)) {
                if (e instanceof Player) {
                    final Player other = (Player)e;
                    if (other.hasMetadata("invisible")) {
                        continue;
                    }
                    if (buttplug.fdsjfhkdsjfdsjhk().getPvPTimerMap().hasTimer(other.getName())) {
                        continue;
                    }
                    if (tm.getPlayerTeam(other.getName()) != tm.getPlayerTeam(player.getName())) {
                        enemyWithinRange = true;
                        break;
                    }
                    continue;
                }
            }
            if (enemyWithinRange) {
                player.sendMessage(ChatColor.RED + "You cannot warp because an enemy is nearby!");
                return;
            }
            if (player.getHealth() <= player.getMaxHealth() - 1.0) {
                player.sendMessage(ChatColor.RED + "You cannot warp because you do not have full health!");
                return;
            }
            if (player.getFoodLevel() != 20) {
                player.sendMessage(ChatColor.RED + "You cannot warp because you do not have full hunger!");
                return;
            }
            final faggot inClaim = LandBoard.getInstance().getTeam(player.getLocation());
            if (inClaim != null && inClaim.getOwner() != null && !inClaim.isMember(player.getName())) {
                player.sendMessage(ChatColor.RED + "You may not go to your faction headquarters from an enemy's claim!");
                return;
            }
        }
        if (buttplug.fdsjfhkdsjfdsjhk().getPvPTimerMap().hasTimer(player.getName()) || buttplug.fdsjfhkdsjfdsjhk().getPvPTimerMap().getTimer(player.getName()) == -10L) {
            buttplug.fdsjfhkdsjfdsjhk().getPvPTimerMap().removeTimer(player.getName());
        }
        player.sendMessage(ChatColor.YELLOW + "§d$" + price + " §ehas been deducted from your team balance.");
        tm.getPlayerTeam(player.getName()).setBalance(tm.getPlayerTeam(player.getName()).getBalance() - price);
        player.teleport(team.getHq());
    }
    
    public boolean isUnclaimed(final Location loc) {
        return LandBoard.getInstance().getClaim(loc) == null && !this.isWarzone(loc);
    }
    
    public boolean isAdminOverride(final Player player) {
        return player.getGameMode() == GameMode.CREATIVE;
    }
    
    public Location getSpawnLocation() {
        return Bukkit.getWorld("world").getSpawnLocation().add(new Vector(0.5, 1.0, 0.5));
    }
    
    public boolean isUnclaimedOrRaidable(final Location loc) {
        final faggot owner = LandBoard.getInstance().getTeam(loc);
        return owner == null || owner.isRaidable();
    }
    
    public float getDTRLossAt(final Location loc) {
        final faggot ownerTo = LandBoard.getInstance().getTeam(loc);
        if (ownerTo != null && ownerTo.hasDTRBitmask(DTRBitmaskType.HALF_DTR_LOSS)) {
            return 0.5f;
        }
        return 1.0f;
    }
    
    public int getDeathBanAt(final String playerName, final Location loc) {
        if (this.isPreEOTW()) {
            return (int)TimeUnit.DAYS.toSeconds(1000L);
        }
        final faggot ownerTo = LandBoard.getInstance().getTeam(loc);
        if (ownerTo != null && ownerTo.getOwner() == null) {
            if (ownerTo.hasDTRBitmask(DTRBitmaskType.FIVE_MINUTE_DEATHBAN)) {
                return (int)TimeUnit.MINUTES.toSeconds(5L);
            }
            if (ownerTo.hasDTRBitmask(DTRBitmaskType.FIFTEEN_MINUTE_DEATHBAN)) {
                return (int)TimeUnit.MINUTES.toSeconds(15L);
            }
        }
        return (int)TimeUnit.HOURS.toSeconds(3L);
    }
    
    public void disablePlayerAttacking(final Player p, final int seconds) {
        if (seconds == 10) {
            p.sendMessage(ChatColor.GRAY + "You cannot attack for " + seconds + " seconds.");
        }
        final Listener l = (Listener)new Listener() {
            @EventHandler
            public void onPlayerDamage(final EntityDamageByEntityEvent e) {
                if (e.getDamager() instanceof Player && (e.getEntity() instanceof Player || e.getEntity() instanceof Cow) && ((Player)e.getDamager()).getName().equals(p.getName())) {
                    e.setCancelled(true);
                }
            }
        };
        Bukkit.getPluginManager().registerEvents(l, (Plugin)buttplug.fdsjfhkdsjfdsjhk());
        Bukkit.getScheduler().runTaskLater((Plugin)buttplug.fdsjfhkdsjfdsjhk(), (Runnable)new Runnable() {
            @Override
            public void run() {
                HandlerList.unregisterAll(l);
            }
        }, (long)(seconds * 20));
    }
    
    public boolean isSpawnBufferZone(final Location loc) {
        if (loc.getWorld().getEnvironment() != World.Environment.NORMAL) {
            return false;
        }
        final int radius = 300;
        final int x = loc.getBlockX();
        final int z = loc.getBlockZ();
        return x < radius && x > -radius && z < radius && z > -radius;
    }
    
    public boolean isNetherBufferZone(final Location loc) {
        if (loc.getWorld().getEnvironment() != World.Environment.NETHER) {
            return false;
        }
        final int radius = 150;
        final int x = loc.getBlockX();
        final int z = loc.getBlockZ();
        return x < radius && x > -radius && z < radius && z > -radius;
    }
    
    public void handleShopSign(final Sign sign, final Player player) {
        final ItemStack itemStack = sign.getLine(2).contains("Crowbar") ? InvUtils.CROWBAR : new ItemStack(Material.valueOf(sign.getLine(2).toLowerCase().replace(" ", "")));
        if (itemStack == null) {
            System.err.println(sign.getLine(2).toLowerCase().replace(" ", ""));
            return;
        }
        if (sign.getLine(0).toLowerCase().contains("buy")) {
            int price = 0;
            int amount = 0;
            try {
                price = Integer.parseInt(sign.getLine(3).replace("$", "").replace(",", ""));
                amount = Integer.parseInt(sign.getLine(1));
            }
            catch (NumberFormatException e) {
                return;
            }
            if (buttplug.fdsjfhkdsjfdsjhk().getBalanceMap().getBalance(player.getName()) >= price) {
                if (player.getInventory().firstEmpty() != -1) {
                    buttplug.fdsjfhkdsjfdsjhk().getBalanceMap().setBalance(player.getName(), buttplug.fdsjfhkdsjfdsjhk().getBalanceMap().getBalance(player.getName()) - price);
                    itemStack.setAmount(amount);
                    player.getInventory().addItem(new ItemStack[] { itemStack });
                    player.updateInventory();
                    this.showSignPacket(player, sign, "§aBOUGHT§r " + amount, "for §a$" + NumberFormat.getNumberInstance(Locale.US).format(price), "New Balance:", "§a$" + NumberFormat.getNumberInstance(Locale.US).format(buttplug.fdsjfhkdsjfdsjhk().getBalanceMap().getBalance(player.getName())));
                }
                else {
                    this.showSignPacket(player, sign, "§c§lError!", "", "§cNo space", "§cin inventory!");
                }
            }
            else {
                this.showSignPacket(player, sign, "§cInsufficient", "§cfunds for", sign.getLine(2), sign.getLine(3));
            }
        }
        else if (sign.getLine(0).toLowerCase().contains("sell")) {
            double pricePerItem = 0.0;
            int amount2 = 0;
            try {
                final int price2 = Integer.parseInt(sign.getLine(3).replace("$", "").replace(",", ""));
                amount2 = Integer.parseInt(sign.getLine(1));
                pricePerItem = price2 / amount2;
            }
            catch (NumberFormatException e2) {
                return;
            }
            final int amountInInventory = Math.min(amount2, this.countItems(player, itemStack.getType(), itemStack.getDurability()));
            if (amountInInventory == 0) {
                this.showSignPacket(player, sign, "§cYou do not", "§chave any", sign.getLine(2), "§con you!");
            }
            else {
                final int totalPrice = (int)(amountInInventory * pricePerItem);
                this.removeItem(player, itemStack, amountInInventory);
                player.updateInventory();
                buttplug.fdsjfhkdsjfdsjhk().getBalanceMap().setBalance(player.getName(), buttplug.fdsjfhkdsjfdsjhk().getBalanceMap().getBalance(player.getName()) + totalPrice);
                this.showSignPacket(player, sign, "§aSOLD§r " + amountInInventory, "for §a$" + NumberFormat.getNumberInstance(Locale.US).format(totalPrice), "New Balance:", "§a$" + NumberFormat.getNumberInstance(Locale.US).format(buttplug.fdsjfhkdsjfdsjhk().getBalanceMap().getBalance(player.getName())));
            }
        }
    }
    
    public void handleKitSign(final Sign sign, final Player player) {
        final String kit = ChatColor.stripColor(sign.getLine(1));
        if (kit.equalsIgnoreCase("Fishing")) {
            final int uses = buttplug.fdsjfhkdsjfdsjhk().getFishingKitMap().getUses(player.getName());
            if (uses == 3) {
                this.showSignPacket(player, sign, "§aFishing Kit:", "", "§cAlready used", "§c3/3 times!");
            }
            else {
                final ItemStack rod = new ItemStack(Material.FISHING_ROD);
                rod.addEnchantment(Enchantment.LURE, 2);
                player.getInventory().addItem(new ItemStack[] { rod });
                player.updateInventory();
                player.sendMessage(ChatColor.GOLD + "Equipped the " + ChatColor.WHITE + "Fishing" + ChatColor.GOLD + " kit!");
                buttplug.fdsjfhkdsjfdsjhk().getFishingKitMap().setUses(player.getName(), uses + 1);
                this.showSignPacket(player, sign, "§aFishing Kit:", "§bEquipped!", "", "§dUses: §e" + uses + "/3");
            }
        }
    }
    
    public void removeItem(final Player p, final ItemStack it, final int amount) {
        final boolean specialDamage = it.getType().getMaxDurability() == 0;
        for (int a = 0; a < amount; ++a) {
            for (final ItemStack i : p.getInventory()) {
                if (i != null && i.getType() == it.getType() && (!specialDamage || it.getDurability() == i.getDurability())) {
                    if (i.getAmount() == 1) {
                        p.getInventory().clear(p.getInventory().first(i));
                        break;
                    }
                    i.setAmount(i.getAmount() - 1);
                    break;
                }
            }
        }
    }
    
    public ItemStack generateDeathSign(final String killed, final String killer) {
        final ItemStack deathsign = new ItemStack(Material.SIGN);
        final ItemMeta meta = deathsign.getItemMeta();
        final ArrayList<String> lore = new ArrayList<String>();
        lore.add("§4" + killed);
        lore.add("§eSlain By:");
        lore.add("§a" + killer);
        final DateFormat sdf = new SimpleDateFormat("M/d HH:mm:ss");
        lore.add(sdf.format(new Date()).replace(" AM", "").replace(" PM", ""));
        meta.setLore((List)lore);
        meta.setDisplayName("§dDeath Sign");
        deathsign.setItemMeta(meta);
        return deathsign;
    }
    
    public ItemStack generateKOTHSign(final String koth, final String capper) {
        final ItemStack kothsign = new ItemStack(Material.SIGN);
        final ItemMeta meta = kothsign.getItemMeta();
        final ArrayList<String> lore = new ArrayList<String>();
        lore.add("§9" + koth);
        lore.add("§eCaptured By:");
        lore.add("§a" + capper);
        final DateFormat sdf = new SimpleDateFormat("M/d HH:mm:ss");
        lore.add(sdf.format(new Date()).replace(" AM", "").replace(" PM", ""));
        meta.setLore((List)lore);
        meta.setDisplayName("§dKOTH Capture Sign");
        kothsign.setItemMeta(meta);
        return kothsign;
    }
    
    public void showSignPacket(final Player player, final Sign sign, final String... lines) {
        player.sendSignChange(sign.getLocation(), lines);
        if (this.showSignTasks.containsKey(sign)) {
            this.showSignTasks.remove(sign).cancel();
        }
        final BukkitRunnable br = new BukkitRunnable() {
            public void run() {
                sign.update();
                ServerHandler.this.showSignTasks.remove(sign);
            }
        };
        this.showSignTasks.put(sign, br);
        br.runTaskLater((Plugin)buttplug.fdsjfhkdsjfdsjhk(), 90L);
    }
    
    public int countItems(final Player player, final Material material, final int damageValue) {
        final PlayerInventory inventory = player.getInventory();
        final ItemStack[] items = inventory.getContents();
        int amount = 0;
        for (final ItemStack item : items) {
            if (item != null) {
                final boolean specialDamage = material.getMaxDurability() == 0;
                if (item.getType() != null && item.getType() == material && (!specialDamage || item.getDurability() == (short)damageValue)) {
                    amount += item.getAmount();
                }
            }
        }
        return amount;
    }
    
    public static Map<String, Integer> getTasks() {
        return ServerHandler.tasks;
    }
    
    public Set<String> getUsedNames() {
        return this.usedNames;
    }
    
    public Set<String> getHighRollers() {
        return this.highRollers;
    }
    
    public boolean isEOTW() {
        return this.EOTW;
    }
    
    public void setEOTW(final boolean EOTW) {
        this.EOTW = EOTW;
    }
    
    public boolean isPreEOTW() {
        return this.PreEOTW;
    }
    
    public void setPreEOTW(final boolean PreEOTW) {
        this.PreEOTW = PreEOTW;
    }
    
    static {
        ServerHandler.WARZONE_RADIUS = 1000;
        DISALLOWED_POTIONS = Sets.newHashSet((Object[])new Integer[] { 8193, 8225, 8257, 16385, 16417, 16449, 8200, 8232, 8264, 16392, 16424, 16456, 8201, 8233, 8265, 16393, 16425, 16457, 8204, 8236, 8268, 16396, 16428, 16460, 8238, 8270, 16430, 16462, 16398, 8238, 8228, 8260, 16420, 16452, 8234, 8266, 16426, 16458 });
        ServerHandler.tasks = new HashMap<String, Integer>();
    }
}
