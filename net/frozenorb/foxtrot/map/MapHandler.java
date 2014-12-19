package net.frozenorb.foxtrot.map;

import org.bukkit.enchantments.*;
import java.io.*;
import com.mongodb.*;
import org.bukkit.craftbukkit.libs.com.google.gson.*;
import net.minecraft.util.org.apache.commons.io.*;
import com.mongodb.util.*;
import org.bukkit.*;
import net.frozenorb.foxtrot.server.*;
import net.frozenorb.foxtrot.listener.*;
import net.frozenorb.foxtrot.scoreboard.*;
import java.util.*;

public class MapHandler
{
    private String scoreboardTitle;
    private Map<Enchantment, Integer> maxEnchantments;
    private double baseLootingMultiplier;
    private double level1LootingMultiplier;
    private double level2LootingMultiplier;
    private double level3LootingMultiplier;
    
    public MapHandler() {
        super();
        this.maxEnchantments = new HashMap<Enchantment, Integer>();
        try {
            final File mapInfo = new File("mapInfo.json");
            if (!mapInfo.exists()) {
                mapInfo.createNewFile();
                final BasicDBObject dbo = new BasicDBObject();
                final BasicDBObject enchants = new BasicDBObject();
                final BasicDBObject looting = new BasicDBObject();
                dbo.put("scoreboardTitle", "&6&lHCTeams &c[Map 1]");
                dbo.put("warzone", 1000);
                dbo.put("border", 3000);
                dbo.put("scoreboardTimersEnabled", true);
                enchants.put("PROTECTION_FALL", 4);
                enchants.put("ARROW_DAMAGE", 2);
                enchants.put("ARROW_INFINITE", 1);
                enchants.put("DIG_SPEED", 5);
                enchants.put("DURABILITY", 3);
                enchants.put("LOOT_BONUS_BLOCKS", 3);
                enchants.put("LOOT_BONUS_MOBS", 3);
                enchants.put("SILK_TOUCH", 1);
                enchants.put("LUCK", 3);
                enchants.put("LURE", 3);
                looting.put("base", 1.0);
                looting.put("level1", 1.2);
                looting.put("level2", 1.4);
                looting.put("level3", 2.0);
                dbo.put("enchants", enchants);
                dbo.put("looting", looting);
                FileUtils.write(mapInfo, (CharSequence)new GsonBuilder().setPrettyPrinting().create().toJson(new JsonParser().parse(dbo.toString())));
            }
            final BasicDBObject dbo = (BasicDBObject)JSON.parse(FileUtils.readFileToString(mapInfo));
            if (dbo != null) {
                this.scoreboardTitle = ChatColor.translateAlternateColorCodes('&', dbo.getString("scoreboardTitle"));
                ServerHandler.WARZONE_RADIUS = dbo.getInt("warzone", 1000);
                BorderListener.BORDER_SIZE = dbo.getInt("border", 3000);
                ScoreboardHandler.scoreboardTimerEnabled = dbo.getBoolean("scoreboardTimersEnabled", true);
                final BasicDBObject enchants = (BasicDBObject)dbo.get("enchants");
                final BasicDBObject looting = (BasicDBObject)dbo.get("looting");
                for (final Map.Entry<String, Object> enchant : enchants.entrySet()) {
                    this.maxEnchantments.put(Enchantment.getByName((String)enchant.getKey()), enchant.getValue());
                }
                this.baseLootingMultiplier = looting.getDouble("base");
                this.level1LootingMultiplier = looting.getDouble("level1");
                this.level2LootingMultiplier = looting.getDouble("level2");
                this.level3LootingMultiplier = looting.getDouble("level3");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public String getScoreboardTitle() {
        return this.scoreboardTitle;
    }
    
    public Map<Enchantment, Integer> getMaxEnchantments() {
        return this.maxEnchantments;
    }
    
    public double getBaseLootingMultiplier() {
        return this.baseLootingMultiplier;
    }
    
    public double getLevel1LootingMultiplier() {
        return this.level1LootingMultiplier;
    }
    
    public double getLevel2LootingMultiplier() {
        return this.level2LootingMultiplier;
    }
    
    public double getLevel3LootingMultiplier() {
        return this.level3LootingMultiplier;
    }
}
