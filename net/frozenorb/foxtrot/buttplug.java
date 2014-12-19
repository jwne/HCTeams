package net.frozenorb.foxtrot;

import org.bukkit.plugin.java.*;
import java.util.*;
import com.mongodb.*;
import net.frozenorb.foxtrot.team.*;
import net.frozenorb.foxtrot.server.*;
import net.frozenorb.foxtrot.map.*;
import net.frozenorb.foxtrot.scoreboard.*;
import net.frozenorb.foxtrot.jedis.persist.*;
import org.apache.commons.pool2.impl.*;
import net.frozenorb.foxtrot.nms.*;
import net.frozenorb.foxtrot.team.dtr.*;
import org.bukkit.plugin.*;
import net.frozenorb.foxtrot.nametag.*;
import com.comphenix.protocol.*;
import com.comphenix.packetwrapper.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import com.comphenix.protocol.events.*;
import org.bukkit.metadata.*;
import net.frozenorb.foxtrot.jedis.*;
import redis.clients.jedis.*;
import net.frozenorb.foxtrot.team.claims.*;
import net.frozenorb.foxtrot.command.*;
import net.frozenorb.foxtrot.deathmessage.*;
import org.bukkit.event.*;
import net.frozenorb.foxtrot.listener.*;
import net.frozenorb.foxtrot.team.commands.team.*;

public class buttplug extends JavaPlugin
{
    private static buttplug instance;
    public static final Random RANDOM;
    private JedisPool jedisPool;
    private MongoClient mongoPool;
    private dsfjhkdsjhdsjkhfds dsfjhkdsjhdsjkhfds;
    private ServerHandler serverHandler;
    private MapHandler mapHandler;
    private ScoreboardHandler scoreboardHandler;
    private PlaytimeMap playtimeMap;
    private DeathbanMap deathbanMap;
    private PvPTimerMap PvPTimerMap;
    private KillsMap killsMap;
    private ChatModeMap chatModeMap;
    private ToggleLightningMap toggleLightningMap;
    private FishingKitMap fishingKitMap;
    private ToggleGlobalChatMap toggleGlobalChatMap;
    private FirstJoinMap firstJoinMap;
    private LastJoinMap lastJoinMap;
    private BalanceMap balanceMap;
    private TransferableLivesMap transferableLivesMap;
    
    public void onEnable() {
        buttplug.instance = this;
        try {
            this.jedisPool = new JedisPool(new JedisPoolConfig(), "localhost");
            this.mongoPool = new MongoClient();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        try {
            EntityRegistrar.registerCustomEntities();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        new DTRHandler().runTaskTimer((Plugin)this, 20L, 1200L);
        new RedisSaveTask().runTaskTimerAsynchronously((Plugin)this, 6000L, 6000L);
        this.setupHandlers();
        this.setupPersistence();
        this.setupListeners();
        for (final Player player : this.getServer().getOnlinePlayers()) {
            this.getPlaytimeMap().playerJoined(player.getName());
            NametagManager.reloadPlayer(player);
            player.removeMetadata("loggedout", (Plugin)fdsjfhkdsjfdsjhk());
        }
        ProtocolLibrary.getProtocolManager().addPacketListener((PacketListener)new PacketAdapter(this, new PacketType[] { WrapperPlayServerOpenSignEntity.TYPE }) {
            public void onPacketSending(final PacketEvent event) {
                final WrapperPlayServerOpenSignEntity packet = new WrapperPlayServerOpenSignEntity(event.getPacket());
                final Player player = event.getPlayer();
                final Location loc = new Location(player.getWorld(), (double)packet.getX(), (double)packet.getY(), (double)packet.getZ());
                if (loc.getBlock().getState().hasMetadata("noSignPacket")) {
                    event.setCancelled(true);
                }
            }
        });
    }
    
    public void onDisable() {
        for (final Player player : fdsjfhkdsjfdsjhk().getServer().getOnlinePlayers()) {
            this.getPlaytimeMap().playerQuit(player.getName(), false);
            NametagManager.getTeamMap().remove(player.getName());
            player.setMetadata("loggedout", (MetadataValue)new FixedMetadataValue((Plugin)this, (Object)true));
        }
        RedisSaveTask.save(false);
        fdsjfhkdsjfdsjhk().getServerHandler().save();
        this.jedisPool.destroy();
    }
    
    public <T> T eatmyass(final yourmom<T> jedisCommand) {
        Jedis jedis = this.jedisPool.getResource();
        T result = null;
        try {
            result = jedisCommand.execute(jedis);
        }
        catch (Exception e) {
            e.printStackTrace();
            if (jedis != null) {
                this.jedisPool.returnBrokenResource(jedis);
                jedis = null;
            }
        }
        finally {
            if (jedis != null) {
                this.jedisPool.returnResource(jedis);
            }
        }
        return result;
    }
    
    public void sendOPMessage(final String message) {
        for (final Player player : fdsjfhkdsjfdsjhk().getServer().getOnlinePlayers()) {
            if (player.isOp()) {
                player.sendMessage(message);
            }
        }
    }
    
    private void setupHandlers() {
        this.dsfjhkdsjhdsjkhfds = new dsfjhkdsjhdsjkhfds();
        LandBoard.getInstance().loadFromTeams();
        this.serverHandler = new ServerHandler();
        this.scoreboardHandler = new ScoreboardHandler();
        this.mapHandler = new MapHandler();
        CommandHandler.init();
        DeathMessageHandler.init();
    }
    
    private void setupListeners() {
        this.getServer().getPluginManager().registerEvents((Listener)new MapListener(), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new AntiGlitchListener(), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new BasicPreventionListener(), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new BorderListener(), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new ChatListener(), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new CombatLoggerListener(), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new CrowbarListener(), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new DeathbanListener(), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new EnchantmentLimiterListener(), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new EnderpearlListener(), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new EndListener(), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new FoundDiamondsListener(), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new FoxListener(), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new KOTHRewardKeyListener(), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new PvPTimerListener(), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new PotionLimiterListener(), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new PortalTrapListener(), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new RoadListener(), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new SpawnListener(), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new SpawnTagListener(), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new StaffUtilsListener(), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new TeamListener(), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new TeamClaimCommand(), (Plugin)this);
    }
    
    private void setupPersistence() {
        (this.playtimeMap = new PlaytimeMap()).loadFromRedis();
        (this.deathbanMap = new DeathbanMap()).loadFromRedis();
        (this.PvPTimerMap = new PvPTimerMap()).loadFromRedis();
        (this.killsMap = new KillsMap()).loadFromRedis();
        (this.chatModeMap = new ChatModeMap()).loadFromRedis();
        (this.toggleLightningMap = new ToggleLightningMap()).loadFromRedis();
        (this.toggleGlobalChatMap = new ToggleGlobalChatMap()).loadFromRedis();
        (this.fishingKitMap = new FishingKitMap()).loadFromRedis();
        (this.transferableLivesMap = new TransferableLivesMap()).loadFromRedis();
        (this.firstJoinMap = new FirstJoinMap()).loadFromRedis();
        (this.lastJoinMap = new LastJoinMap()).loadFromRedis();
        (this.balanceMap = new BalanceMap()).loadFromRedis();
    }
    
    public static buttplug fdsjfhkdsjfdsjhk() {
        return buttplug.instance;
    }
    
    public MongoClient getMongoPool() {
        return this.mongoPool;
    }
    
    public dsfjhkdsjhdsjkhfds getDsfjhkdsjhdsjkhfds() {
        return this.dsfjhkdsjhdsjkhfds;
    }
    
    public ServerHandler getServerHandler() {
        return this.serverHandler;
    }
    
    public MapHandler getMapHandler() {
        return this.mapHandler;
    }
    
    public ScoreboardHandler getScoreboardHandler() {
        return this.scoreboardHandler;
    }
    
    public PlaytimeMap getPlaytimeMap() {
        return this.playtimeMap;
    }
    
    public DeathbanMap getDeathbanMap() {
        return this.deathbanMap;
    }
    
    public PvPTimerMap getPvPTimerMap() {
        return this.PvPTimerMap;
    }
    
    public KillsMap getKillsMap() {
        return this.killsMap;
    }
    
    public ChatModeMap getChatModeMap() {
        return this.chatModeMap;
    }
    
    public ToggleLightningMap getToggleLightningMap() {
        return this.toggleLightningMap;
    }
    
    public FishingKitMap getFishingKitMap() {
        return this.fishingKitMap;
    }
    
    public ToggleGlobalChatMap getToggleGlobalChatMap() {
        return this.toggleGlobalChatMap;
    }
    
    public FirstJoinMap getFirstJoinMap() {
        return this.firstJoinMap;
    }
    
    public LastJoinMap getLastJoinMap() {
        return this.lastJoinMap;
    }
    
    public BalanceMap getBalanceMap() {
        return this.balanceMap;
    }
    
    public TransferableLivesMap getTransferableLivesMap() {
        return this.transferableLivesMap;
    }
    
    static {
        RANDOM = new Random();
    }
}
