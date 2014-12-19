package net.frozenorb.foxtrot.listener;

import org.spigotmc.*;
import net.frozenorb.foxtrot.*;
import net.frozenorb.foxtrot.team.claims.*;
import net.frozenorb.foxtrot.team.dtr.bitmask.*;
import net.frozenorb.foxtrot.team.*;
import org.bukkit.event.*;
import net.frozenorb.foxtrot.team.commands.team.*;
import net.frozenorb.foxtrot.nametag.*;
import net.frozenorb.foxtrot.command.commands.*;
import org.bukkit.event.player.*;
import org.bukkit.potion.*;
import org.bukkit.inventory.*;
import org.bukkit.plugin.*;
import org.bukkit.metadata.*;
import org.bukkit.entity.*;
import org.bukkit.block.*;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import net.frozenorb.foxtrot.server.*;
import net.frozenorb.foxtrot.util.*;
import org.bukkit.inventory.meta.*;
import org.bukkit.craftbukkit.v1_7_R3.*;
import org.bukkit.craftbukkit.v1_7_R3.entity.*;
import net.minecraft.server.v1_7_R3.*;
import java.util.*;
import org.bukkit.*;

public class FoxListener implements Listener
{
    private CustomTimingsHandler pmeSafeLogout;
    private CustomTimingsHandler pmePvPTimer;
    private CustomTimingsHandler pmeTeamGrab;
    private CustomTimingsHandler pmeRegionGrab;
    private CustomTimingsHandler pmeRegionNotify;
    private CustomTimingsHandler pmeRegionNotifyHM;
    private CustomTimingsHandler pmeRegionNotifySpawn;
    private CustomTimingsHandler pmeRegionNotifyBM;
    public static final PotionEffectType[] DEBUFFS;
    public static final Material[] NO_INTERACT_WITH;
    public static final Material[] NO_INTERACT_WITH_SPAWN;
    public static final Material[] NO_INTERACT;
    public static final Material[] NO_INTERACT_IN_SPAWN;
    public static final Material[] NON_TRANSPARENT_ATTACK_DISABLING_BLOCKS;
    
    public FoxListener() {
        super();
        this.pmeSafeLogout = new CustomTimingsHandler("Foxtrot - PME Safe Logout");
        this.pmePvPTimer = new CustomTimingsHandler("Foxtrot - PME PvP Time ");
        this.pmeTeamGrab = new CustomTimingsHandler("Foxtrot - PME Team Grab");
        this.pmeRegionGrab = new CustomTimingsHandler("Foxtrot - PME Region Gra");
        this.pmeRegionNotify = new CustomTimingsHandler("Foxtrot - PME Region Notify");
        this.pmeRegionNotifyHM = new CustomTimingsHandler("Foxtrot - PME Region Notify HM");
        this.pmeRegionNotifySpawn = new CustomTimingsHandler("Foxtrot - PME Region Notify Spawn");
        this.pmeRegionNotifyBM = new CustomTimingsHandler("Foxtrot - PME Region Notify BM");
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerMove(final PlayerMoveEvent event) {
        if (event.isCancelled()) {
            return;
        }
        if (event.getFrom().getBlockX() == event.getTo().getBlockX() && event.getFrom().getBlockY() == event.getTo().getBlockY() && event.getFrom().getBlockZ() == event.getTo().getBlockZ()) {
            return;
        }
        this.pmeSafeLogout.startTiming();
        if (ServerHandler.getTasks().containsKey(event.getPlayer().getName())) {
            buttplug.fdsjfhkdsjfdsjhk().getServer().getScheduler().cancelTask((int)ServerHandler.getTasks().get(event.getPlayer().getName()));
            ServerHandler.getTasks().remove(event.getPlayer().getName());
            event.getPlayer().sendMessage(ChatColor.YELLOW + "§lLOGOUT §c§lCANCELLED!");
        }
        this.pmeSafeLogout.stopTiming();
        this.pmeTeamGrab.startTiming();
        final faggot ownerTo = LandBoard.getInstance().getTeam(event.getTo());
        this.pmeTeamGrab.stopTiming();
        this.pmePvPTimer.startTiming();
        if (buttplug.fdsjfhkdsjfdsjhk().getPvPTimerMap().hasTimer(event.getPlayer().getName()) && ownerTo != null && ownerTo.isMember(event.getPlayer().getName())) {
            buttplug.fdsjfhkdsjfdsjhk().getPvPTimerMap().removeTimer(event.getPlayer().getName());
        }
        this.pmePvPTimer.stopTiming();
        this.pmeTeamGrab.startTiming();
        final faggot ownerFrom = LandBoard.getInstance().getTeam(event.getFrom());
        this.pmeTeamGrab.stopTiming();
        final ServerHandler sm = buttplug.fdsjfhkdsjfdsjhk().getServerHandler();
        this.pmeRegionGrab.startTiming();
        final RegionData from = sm.getRegion(ownerFrom, event.getFrom());
        final RegionData to = sm.getRegion(ownerTo, event.getTo());
        this.pmeRegionGrab.stopTiming();
        this.pmeRegionNotify.startTiming();
        if (!from.equals(to)) {
            this.pmeRegionNotifyHM.startTiming();
            if (!to.getRegionType().getMoveHandler().handleMove(event)) {
                return;
            }
            this.pmeRegionNotifyHM.stopTiming();
            this.pmeRegionNotifySpawn.startTiming();
            if (from.getRegionType() == RegionType.SPAWN && buttplug.fdsjfhkdsjfdsjhk().getPvPTimerMap().getTimer(event.getPlayer().getName()) == -10L) {
                buttplug.fdsjfhkdsjfdsjhk().getPvPTimerMap().createTimer(event.getPlayer().getName(), 1800);
            }
            this.pmeRegionNotifySpawn.stopTiming();
            this.pmeRegionNotifyBM.startTiming();
            final boolean fromReduceDeathban = from.getData() != null && (from.getData().hasDTRBitmask(DTRBitmaskType.FIVE_MINUTE_DEATHBAN) || from.getData().hasDTRBitmask(DTRBitmaskType.FIFTEEN_MINUTE_DEATHBAN) || from.getData().hasDTRBitmask(DTRBitmaskType.SAFE_ZONE));
            final boolean toReduceDeathban = to.getData() != null && (to.getData().hasDTRBitmask(DTRBitmaskType.FIVE_MINUTE_DEATHBAN) || to.getData().hasDTRBitmask(DTRBitmaskType.FIFTEEN_MINUTE_DEATHBAN) || to.getData().hasDTRBitmask(DTRBitmaskType.SAFE_ZONE));
            this.pmeRegionNotifyBM.stopTiming();
            final String fromStr = "§eNow leaving: " + from.getName(event.getPlayer()) + (fromReduceDeathban ? "§e(§aNon-Deathban§e)" : "§e(§cDeathban§e)");
            final String toStr = "§eNow entering: " + to.getName(event.getPlayer()) + (toReduceDeathban ? "§e(§aNon-Deathban§e)" : "§e(§cDeathban§e)");
            event.getPlayer().sendMessage(new String[] { fromStr, toStr });
        }
        this.pmeRegionNotify.stopTiming();
    }
    
    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent event) {
        event.getPlayer().getInventory().remove(TeamClaimCommand.SELECTION_WAND);
        event.setQuitMessage((String)null);
        buttplug.fdsjfhkdsjfdsjhk().getPlaytimeMap().playerQuit(event.getPlayer().getName(), true);
        NametagManager.getTeamMap().remove(event.getPlayer().getName());
        buttplug.fdsjfhkdsjfdsjhk().getScoreboardHandler().remove(event.getPlayer());
    }
    
    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final String name = player.getName();
        NametagManager.initPlayer(event.getPlayer());
        NametagManager.sendTeamsToPlayer(event.getPlayer());
        NametagManager.reloadPlayer(event.getPlayer());
        event.setJoinMessage((String)null);
        buttplug.fdsjfhkdsjfdsjhk().getPlaytimeMap().playerJoined(event.getPlayer().getName());
        buttplug.fdsjfhkdsjfdsjhk().getLastJoinMap().setLastJoin(event.getPlayer().getName());
        if (!event.getPlayer().hasPlayedBefore()) {
            buttplug.fdsjfhkdsjfdsjhk().getFirstJoinMap().setFirstJoin(event.getPlayer().getName());
            buttplug.fdsjfhkdsjfdsjhk().getBalanceMap().setBalance(event.getPlayer().getName(), 100);
            event.getPlayer().teleport(buttplug.fdsjfhkdsjfdsjhk().getServerHandler().getSpawnLocation());
        }
        if (!buttplug.fdsjfhkdsjfdsjhk().getPvPTimerMap().contains(name)) {
            buttplug.fdsjfhkdsjfdsjhk().getPvPTimerMap().pendingTimer(player.getName());
        }
        if (buttplug.fdsjfhkdsjfdsjhk().getPvPTimerMap().getTimer(name) == -10L) {
            player.sendMessage(ChatColor.YELLOW + "You have still not activated your 30 minute PVP timer! Walk out of spawn to activate it!");
        }
        buttplug.fdsjfhkdsjfdsjhk().getScoreboardHandler().update(event.getPlayer());
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityDamage(final EntityDamageEvent event) {
        if (event.isCancelled()) {
            return;
        }
        if (event.getEntity() instanceof Player) {
            final Player p = (Player)event.getEntity();
            if (ServerHandler.getTasks().containsKey(p.getName())) {
                Bukkit.getScheduler().cancelTask((int)ServerHandler.getTasks().get(p.getName()));
                ServerHandler.getTasks().remove(p.getName());
                p.sendMessage(ChatColor.YELLOW + "§lLOGOUT §c§lCANCELLED!");
            }
        }
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerLogin(final PlayerLoginEvent event) {
        if (ToggleDonorOnlyCommand.donorOnly && !event.getPlayer().hasPermission("foxtrot.donator")) {
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, ChatColor.RED + "The server is full.");
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerLogin2(final PlayerLoginEvent event) {
        if (event.getResult() == PlayerLoginEvent.Result.KICK_FULL && event.getPlayer().hasPermission("foxtrot.joinfull")) {
            event.setResult(PlayerLoginEvent.Result.ALLOWED);
        }
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onProjetileInteract(final PlayerInteractEvent event) {
        if (event.isCancelled()) {
            return;
        }
        final Player p = event.getPlayer();
        if (event.getItem() != null && (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) && event.getItem().getType() == Material.POTION) {
            final ItemStack i = event.getItem();
            if (i.getDurability() != 0) {
                final Potion pot = Potion.fromItemStack(i);
                if (pot.isSplash() && Arrays.asList(FoxListener.DEBUFFS).contains(pot.getType().getEffectType())) {
                    if (!buttplug.fdsjfhkdsjfdsjhk().getServerHandler().isPreEOTW() && buttplug.fdsjfhkdsjfdsjhk().getPvPTimerMap().hasTimer(p.getName())) {
                        p.sendMessage(ChatColor.RED + "You cannot do this while your PVP Timer is active!");
                        p.sendMessage(ChatColor.RED + "Type '" + ChatColor.YELLOW + "/pvp enable" + ChatColor.RED + "' to remove your timer.");
                        event.setCancelled(true);
                        return;
                    }
                    if (!buttplug.fdsjfhkdsjfdsjhk().getServerHandler().isPreEOTW() && DTRBitmaskType.SAFE_ZONE.appliesAt(p.getLocation())) {
                        event.setCancelled(true);
                        event.getPlayer().sendMessage(ChatColor.RED + "You cannot launch debuffs from inside spawn!");
                        event.getPlayer().updateInventory();
                    }
                }
            }
        }
        if (event.getClickedBlock() != null) {
            if (event.getClickedBlock().getType() == Material.ENCHANTMENT_TABLE && event.getAction() == Action.LEFT_CLICK_BLOCK) {
                if (event.getItem() != null && event.getItem().getType() == Material.ENCHANTED_BOOK) {
                    event.getItem().setType(Material.BOOK);
                    event.getPlayer().sendMessage(ChatColor.GREEN + "You reverted this book to its original form!");
                    event.setCancelled(true);
                }
                return;
            }
            if (buttplug.fdsjfhkdsjfdsjhk().getServerHandler().isUnclaimedOrRaidable(event.getClickedBlock().getLocation()) || buttplug.fdsjfhkdsjfdsjhk().getServerHandler().isAdminOverride(event.getPlayer())) {
                return;
            }
            final faggot team = LandBoard.getInstance().getTeam(event.getClickedBlock().getLocation());
            if (DTRBitmaskType.SAFE_ZONE.appliesAt(event.getClickedBlock().getLocation()) && (Arrays.asList(FoxListener.NO_INTERACT_WITH_SPAWN).contains(event.getMaterial()) || Arrays.asList(FoxListener.NO_INTERACT_IN_SPAWN).contains(event.getClickedBlock().getType()))) {
                event.setCancelled(true);
            }
            if (team != null && !team.isMember(event.getPlayer())) {
                if (Arrays.asList(FoxListener.NO_INTERACT).contains(event.getClickedBlock().getType()) || Arrays.asList(FoxListener.NO_INTERACT_WITH).contains(event.getMaterial())) {
                    event.setCancelled(true);
                    event.getPlayer().sendMessage(ChatColor.YELLOW + "You cannot do this in " + team.getName(event.getPlayer()) + ChatColor.YELLOW + "'s territory.");
                    if (event.getMaterial() == Material.TRAP_DOOR || event.getMaterial() == Material.FENCE_GATE || event.getMaterial().name().contains("DOOR")) {
                        buttplug.fdsjfhkdsjfdsjhk().getServerHandler().disablePlayerAttacking(event.getPlayer(), 1);
                    }
                    return;
                }
                if (event.getAction() == Action.PHYSICAL) {
                    event.setCancelled(true);
                }
            }
            else if (event.getMaterial() == Material.LAVA_BUCKET && (team == null || !team.isMember(event.getPlayer()))) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(ChatColor.RED + "You can only do this in your own claims!");
                return;
            }
        }
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getPlayer().getItemInHand().getTypeId() == 333) {
            final Block target = event.getClickedBlock();
            if (target.getTypeId() != 8 && target.getTypeId() != 9) {
                event.getPlayer().sendMessage(ChatColor.RED + "You can only place a boat on water!");
                event.setCancelled(true);
            }
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onSignInteract(final PlayerInteractEvent event) {
        if (event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.SKULL) {
            final Skull sk = (Skull)event.getClickedBlock().getState();
            if (sk.getSkullType() == SkullType.PLAYER) {
                event.getPlayer().sendMessage(ChatColor.YELLOW + "Head of " + ChatColor.WHITE + sk.getOwner() + ChatColor.YELLOW + ".");
            }
        }
        if (event.getClickedBlock() != null && event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock().getState() instanceof Sign) {
            final Sign s = (Sign)event.getClickedBlock().getState();
            if (DTRBitmaskType.SAFE_ZONE.appliesAt(event.getClickedBlock().getLocation())) {
                if (s.getLine(0).contains("Kit")) {
                    buttplug.fdsjfhkdsjfdsjhk().getServerHandler().handleKitSign(s, event.getPlayer());
                }
                else if (s.getLine(0).contains("Buy") || s.getLine(0).contains("Sell")) {
                    buttplug.fdsjfhkdsjfdsjhk().getServerHandler().handleShopSign(s, event.getPlayer());
                }
                event.setCancelled(true);
            }
        }
        if (event.getItem() != null && event.getMaterial() == Material.SIGN && event.getItem().hasItemMeta() && event.getItem().getItemMeta().getLore() != null) {
            final ArrayList<String> lore = (ArrayList<String>)event.getItem().getItemMeta().getLore();
            if (lore.size() > 1 && lore.get(1).contains("§e") && event.getClickedBlock() != null) {
                event.getClickedBlock().getRelative(event.getBlockFace()).getState().setMetadata("noSignPacket", (MetadataValue)new FixedMetadataValue((Plugin)buttplug.fdsjfhkdsjfdsjhk(), (Object)true));
                Bukkit.getScheduler().runTaskLater((Plugin)buttplug.fdsjfhkdsjfdsjhk(), (Runnable)new Runnable() {
                    @Override
                    public void run() {
                        event.getClickedBlock().getRelative(event.getBlockFace()).getState().removeMetadata("noSignPacket", (Plugin)buttplug.fdsjfhkdsjfdsjhk());
                    }
                }, 20L);
            }
        }
    }
    
    @EventHandler
    public void onSignPlace(final BlockPlaceEvent e) {
        final Block block = e.getBlock();
        final ItemStack hand = e.getItemInHand();
        if (hand.getType() == Material.SIGN) {
            if (hand.hasItemMeta() && hand.getItemMeta().getLore() != null) {
                final ArrayList<String> lore = (ArrayList<String>)hand.getItemMeta().getLore();
                if (e.getBlock().getType() == Material.WALL_SIGN || e.getBlock().getType() == Material.SIGN_POST) {
                    final Sign s = (Sign)e.getBlock().getState();
                    for (int i = 0; i < 4; ++i) {
                        s.setLine(i, (String)lore.get(i));
                    }
                    s.setMetadata("deathSign", (MetadataValue)new FixedMetadataValue((Plugin)buttplug.fdsjfhkdsjfdsjhk(), (Object)true));
                    s.update();
                }
            }
        }
        else if (hand.getType() == Material.MOB_SPAWNER && !e.isCancelled() && hand.hasItemMeta() && hand.getItemMeta().hasDisplayName() && hand.getItemMeta().getDisplayName().startsWith(ChatColor.RESET.toString())) {
            final String name = ChatColor.stripColor(hand.getItemMeta().getDisplayName());
            final String entName = name.replace(" Spawner", "");
            final EntityType type = EntityType.valueOf(entName.toUpperCase().replaceAll(" ", "_"));
            final CreatureSpawner spawner = (CreatureSpawner)block.getState();
            spawner.setSpawnedType(type);
            spawner.update();
            e.getPlayer().sendMessage(ChatColor.AQUA + "You placed a " + entName + " spawner!");
        }
    }
    
    @EventHandler
    public void onSignChange(final SignChangeEvent e) {
        if (e.getBlock().getState().hasMetadata("deathSign") || ((Sign)e.getBlock().getState()).getLine(1).contains("§e")) {
            e.setCancelled(true);
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onSignBreak(final BlockBreakEvent e) {
        if (e.isCancelled()) {
            return;
        }
        if ((e.getBlock().getType() == Material.WALL_SIGN || e.getBlock().getType() == Material.SIGN_POST) && (e.getBlock().getState().hasMetadata("deathSign") || (e.getBlock().getState() instanceof Sign && ((Sign)e.getBlock().getState()).getLine(1).contains("§e")))) {
            e.setCancelled(true);
            final Sign sign = (Sign)e.getBlock().getState();
            final ItemStack deathsign = new ItemStack(Material.SIGN);
            final ItemMeta meta = deathsign.getItemMeta();
            if (sign.getLine(1).contains("Captured")) {
                meta.setDisplayName("§dKOTH Capture Sign");
            }
            else {
                meta.setDisplayName("§dDeath Sign");
            }
            final ArrayList<String> lore = new ArrayList<String>();
            for (final String str : sign.getLines()) {
                lore.add(str);
            }
            meta.setLore((List)lore);
            deathsign.setItemMeta(meta);
            e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), deathsign);
            e.getBlock().setType(Material.AIR);
            e.getBlock().getState().removeMetadata("deathSign", (Plugin)buttplug.fdsjfhkdsjfdsjhk());
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerDeath(final PlayerDeathEvent e) {
        final Player player = e.getEntity();
        final Date now = new Date();
        SpawnTagHandler.removeTag(e.getEntity());
        final faggot t = buttplug.fdsjfhkdsjfdsjhk().getDsfjhkdsjhdsjkhfds().getPlayerTeam(e.getEntity().getName());
        if (t != null) {
            t.playerDeath(e.getEntity().getName(), buttplug.fdsjfhkdsjfdsjhk().getServerHandler().getDTRLossAt(e.getEntity().getLocation()));
        }
        final String deathMsg = ChatColor.YELLOW + player.getName() + ChatColor.RESET + " " + ((player.getKiller() != null) ? ("killed by " + ChatColor.YELLOW + player.getKiller().getName()) : "died") + " " + InvUtils.DEATH_TIME_FORMAT.format(now);
        for (final ItemStack armor : player.getInventory().getArmorContents()) {
            if (armor != null && armor.getType() != Material.AIR) {
                InvUtils.addDeath(armor, deathMsg);
            }
        }
        if (e.getEntity().getKiller() != null) {
            final Player killer = e.getEntity().getKiller();
            final ItemStack sword = killer.getItemInHand();
            if (sword.getType().name().contains("SWORD")) {
                int killsIndex = 1;
                final int[] lastKills = { 3, 4, 5 };
                int currentKills = 1;
                final ItemMeta meta = sword.getItemMeta();
                List<String> lore = new ArrayList<String>();
                if (meta.hasLore()) {
                    lore = (List<String>)meta.getLore();
                    boolean hasForgedMeta = false;
                    for (final String s : meta.getLore()) {
                        if (s.toLowerCase().contains("forged")) {
                            hasForgedMeta = true;
                        }
                    }
                    if (hasForgedMeta) {
                        ++killsIndex;
                        for (int i = 0; i < lastKills.length; ++i) {
                            ++lastKills[i];
                        }
                    }
                    if (meta.getLore().size() > killsIndex) {
                        final String killStr = lore.get(killsIndex);
                        currentKills += Integer.parseInt(ChatColor.stripColor(killStr.split(":")[1]).trim());
                    }
                    for (final int j : lastKills) {
                        if (j != lastKills[lastKills.length - 1]) {
                            if (lore.size() > j) {
                                final String atJ = meta.getLore().get(j);
                                if (lore.size() <= j + 1) {
                                    lore.add(null);
                                }
                                lore.set(j + 1, atJ);
                            }
                        }
                    }
                }
                if (lore.size() <= killsIndex) {
                    for (int k = lore.size(); k <= killsIndex + 1; ++k) {
                        lore.add("");
                    }
                }
                lore.set(killsIndex, "§6§lKills:§f " + currentKills);
                final int firsKill = lastKills[0];
                if (lore.size() <= firsKill) {
                    for (int i = lore.size(); i <= firsKill + 1; ++i) {
                        lore.add("");
                    }
                }
                lore.set(firsKill, killer.getDisplayName() + "§e slayed " + e.getEntity().getDisplayName());
                meta.setLore((List)lore);
                sword.setItemMeta(meta);
            }
            if (killer.hasPermission("foxtrot.skulldrop")) {
                final ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short)SkullType.PLAYER.ordinal());
                final SkullMeta meta2 = (SkullMeta)skull.getItemMeta();
                meta2.setOwner(player.getName());
                meta2.setDisplayName(ChatColor.YELLOW + "Head of " + player.getName());
                meta2.setLore((List)Arrays.asList("", deathMsg));
                skull.setItemMeta((ItemMeta)meta2);
                e.getDrops().add(skull);
            }
            for (final ItemStack it : e.getEntity().getKiller().getInventory().addItem(new ItemStack[] { buttplug.fdsjfhkdsjfdsjhk().getServerHandler().generateDeathSign(e.getEntity().getName(), e.getEntity().getKiller().getName()) }).values()) {
                e.getDrops().add(it);
            }
        }
        final Location loc = player.getLocation();
        final EntityLightning entity = new EntityLightning((World)((CraftWorld)loc.getWorld()).getHandle(), loc.getX(), loc.getY(), loc.getZ(), true, false);
        final PacketPlayOutSpawnEntityWeather packet = new PacketPlayOutSpawnEntityWeather((Entity)entity);
        for (final Player online : player.getWorld().getPlayers()) {
            if (online.equals(player)) {
                continue;
            }
            if (!buttplug.fdsjfhkdsjfdsjhk().getToggleLightningMap().isLightningToggled(online.getName())) {
                continue;
            }
            online.playSound(online.getLocation(), Sound.AMBIENCE_THUNDER, 1.0f, 1.0f);
            ((CraftPlayer)online).getHandle().playerConnection.sendPacket((Packet)packet);
        }
        final int bal = buttplug.fdsjfhkdsjfdsjhk().getBalanceMap().getBalance(player.getName());
        buttplug.fdsjfhkdsjfdsjhk().getBalanceMap().setBalance(player.getName(), 0);
        if (player.getKiller() != null) {
            buttplug.fdsjfhkdsjfdsjhk().getBalanceMap().setBalance(player.getKiller().getName(), buttplug.fdsjfhkdsjfdsjhk().getBalanceMap().getBalance(player.getKiller().getName()) + bal);
            player.getKiller().sendMessage(ChatColor.GOLD + "You earned " + ChatColor.BOLD + "$" + bal + ChatColor.GOLD + " for killing " + player.getDisplayName() + ChatColor.GOLD + "!");
        }
    }
    
    static {
        DEBUFFS = new PotionEffectType[] { PotionEffectType.POISON, PotionEffectType.SLOW, PotionEffectType.WEAKNESS, PotionEffectType.HARM, PotionEffectType.WITHER };
        NO_INTERACT_WITH = new Material[] { Material.LAVA_BUCKET, Material.WATER_BUCKET, Material.BUCKET };
        NO_INTERACT_WITH_SPAWN = new Material[] { Material.SNOW_BALL, Material.ENDER_PEARL, Material.EGG, Material.FISHING_ROD };
        NO_INTERACT = new Material[] { Material.FENCE_GATE, Material.FURNACE, Material.BURNING_FURNACE, Material.BREWING_STAND, Material.CHEST, Material.HOPPER, Material.DISPENSER, Material.WOODEN_DOOR, Material.STONE_BUTTON, Material.WOOD_BUTTON, Material.TRAPPED_CHEST, Material.TRAP_DOOR, Material.LEVER, Material.DROPPER, Material.ENCHANTMENT_TABLE, Material.WORKBENCH, Material.BED_BLOCK, Material.ANVIL };
        NO_INTERACT_IN_SPAWN = new Material[] { Material.FENCE_GATE, Material.FURNACE, Material.BURNING_FURNACE, Material.BREWING_STAND, Material.CHEST, Material.HOPPER, Material.DISPENSER, Material.WOODEN_DOOR, Material.STONE_BUTTON, Material.WOOD_BUTTON, Material.TRAPPED_CHEST, Material.TRAP_DOOR, Material.LEVER, Material.DROPPER, Material.ENCHANTMENT_TABLE, Material.BED_BLOCK, Material.ANVIL };
        NON_TRANSPARENT_ATTACK_DISABLING_BLOCKS = new Material[] { Material.GLASS, Material.WOOD_DOOR, Material.IRON_DOOR, Material.FENCE_GATE };
    }
}
