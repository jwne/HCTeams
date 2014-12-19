package net.frozenorb.foxtrot.deathtracker;

import org.bukkit.entity.*;
import java.io.*;
import org.bukkit.craftbukkit.libs.com.google.gson.*;
import net.minecraft.util.org.apache.commons.io.*;
import net.frozenorb.foxtrot.*;
import com.mongodb.*;
import org.bukkit.potion.*;
import net.frozenorb.foxtrot.util.*;
import net.frozenorb.foxtrot.team.claims.*;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_7_R3.entity.*;
import net.frozenorb.foxtrot.listener.*;
import net.frozenorb.foxtrot.server.*;
import net.frozenorb.foxtrot.deathmessage.*;
import net.frozenorb.foxtrot.deathmessage.objects.*;
import net.frozenorb.foxtrot.deathmessage.trackers.*;
import net.frozenorb.foxtrot.team.*;
import java.util.*;

public class DeathTracker
{
    public static void logDeath(final Player player, final Player killer) {
        final BasicDBObject deathData = generateDeathData(player, killer);
        final File logToFolder = new File("deathtracker" + File.separator + player.getName());
        final File logTo = new File(logToFolder, player.getName() + "-" + ((killer == null) ? "Environment" : killer.getName()) + "-" + new Date().toString() + ".log");
        try {
            logTo.getParentFile().mkdirs();
            logTo.createNewFile();
            FileUtils.write(logTo, (CharSequence)new GsonBuilder().setPrettyPrinting().create().toJson(new JsonParser().parse(deathData.toString())));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static BasicDBObject generateDeathData(final Player player, final Player killer) {
        final long start = System.currentTimeMillis();
        final BasicDBObject deathData = new BasicDBObject();
        BasicDBObject killerData = null;
        final BasicDBObject playerData = new BasicDBObject();
        final BasicDBObject serverData = new BasicDBObject();
        serverData.put("Playercount", buttplug.fdsjfhkdsjfdsjhk().getServer().getOnlinePlayers().length);
        if (killer != null) {
            killerData = new BasicDBObject();
            final BasicDBList potionEffects = new BasicDBList();
            for (final PotionEffect potionEffect : killer.getActivePotionEffects()) {
                final BasicDBObject potionEffectDBObject = new BasicDBObject();
                potionEffectDBObject.put("Type", potionEffect.getType().getName());
                potionEffectDBObject.put("Tier", potionEffect.getAmplifier() + 1);
                potionEffectDBObject.put("Duration", (potionEffect.getAmplifier() > 1000000L) ? "Infinite" : TimeUtils.getMMSS(potionEffect.getAmplifier() / 20));
                ((ArrayList<BasicDBObject>)potionEffects).add(potionEffectDBObject);
            }
            final BasicDBObject locationData = new BasicDBObject();
            locationData.put("World", killer.getLocation().getWorld().getName());
            locationData.put("X", killer.getLocation().getX());
            locationData.put("Y", killer.getLocation().getY());
            locationData.put("Z", killer.getLocation().getZ());
            final faggot claimOwner = LandBoard.getInstance().getTeam(killer.getLocation());
            if (claimOwner == null) {
                locationData.put("Claim", "N/A");
            }
            else {
                locationData.put("Claim", claimOwner.getName());
            }
            killerData.put("PotionEffects", potionEffects);
            killerData.put("HeldItem", (killer.getItemInHand() == null || killer.getItemInHand().getType() == Material.AIR) ? "N/A" : killer.getItemInHand().getType().name());
            killerData.put("Location", locationData);
            killerData.put("Health", killer.getHealth());
            killerData.put("Name", killer.getName());
            killerData.put("UUID", killer.getUniqueId().toString());
            killerData.put("Ping", ((CraftPlayer)killer).getHandle().ping);
            final faggot playerTeam = buttplug.fdsjfhkdsjfdsjhk().getDsfjhkdsjhdsjkhfds().getPlayerTeam(killer.getName());
            if (playerTeam == null) {
                killerData.put("Team", "N/A");
            }
            else {
                final BasicDBObject teamData = new BasicDBObject();
                final long till = Math.max(playerTeam.getRaidableCooldown(), playerTeam.getDeathCooldown());
                final int seconds = (int)(till - System.currentTimeMillis()) / 1000;
                teamData.put("RegenTime", TimeUtils.getConvertedTime(seconds).trim());
                teamData.put("Name", playerTeam.getName());
                teamData.put("DTR", playerTeam.getDTR());
                teamData.put("MembersOnline", playerTeam.getOnlineMemberAmount());
                killerData.put("Team", teamData);
            }
            if (EnderpearlListener.getEnderpearlCooldown().containsKey(killer.getName()) && EnderpearlListener.getEnderpearlCooldown().get(killer.getName()) >= System.currentTimeMillis()) {
                killerData.put("EnderpearlCooldown", (EnderpearlListener.getEnderpearlCooldown().get(killer.getName()) - System.currentTimeMillis()) / 1000.0f);
            }
            else {
                killerData.put("EnderpearlCooldown", 0);
            }
            if (SpawnTagHandler.isTagged(killer)) {
                killerData.put("SpawnTag", SpawnTagHandler.getTag(killer) / 1000.0f);
            }
            else {
                killerData.put("SpawnTag", 0);
            }
        }
        final BasicDBList potionEffects = new BasicDBList();
        for (final PotionEffect potionEffect : player.getActivePotionEffects()) {
            final BasicDBObject potionEffectDBObject = new BasicDBObject();
            potionEffectDBObject.put("Type", potionEffect.getType().getName());
            potionEffectDBObject.put("Tier", potionEffect.getAmplifier() + 1);
            potionEffectDBObject.put("Duration", (potionEffect.getAmplifier() > 1000000L) ? "Infinite" : TimeUtils.getMMSS(potionEffect.getAmplifier() / 20));
            ((ArrayList<BasicDBObject>)potionEffects).add(potionEffectDBObject);
        }
        final BasicDBObject locationData = new BasicDBObject();
        locationData.put("World", player.getLocation().getWorld().getName());
        locationData.put("X", player.getLocation().getX());
        locationData.put("Y", player.getLocation().getY());
        locationData.put("Z", player.getLocation().getZ());
        final faggot claimOwner = LandBoard.getInstance().getTeam(player.getLocation());
        if (claimOwner == null) {
            locationData.put("Claim", "N/A");
        }
        else {
            locationData.put("Claim", claimOwner.getName());
        }
        playerData.put("PotionEffects", potionEffects);
        playerData.put("HeldItem", (player.getItemInHand() == null || player.getItemInHand().getType() == Material.AIR) ? "N/A" : player.getItemInHand().getType().name());
        playerData.put("Location", locationData);
        playerData.put("Health", player.getHealth());
        playerData.put("Name", player.getName());
        playerData.put("UUID", player.getUniqueId().toString());
        playerData.put("Ping", ((CraftPlayer)player).getHandle().ping);
        final faggot playerTeam = buttplug.fdsjfhkdsjfdsjhk().getDsfjhkdsjhdsjkhfds().getPlayerTeam(player.getName());
        if (playerTeam == null) {
            playerData.put("Team", "N/A");
        }
        else {
            final BasicDBObject teamData = new BasicDBObject();
            final long till = Math.max(playerTeam.getRaidableCooldown(), playerTeam.getDeathCooldown());
            final int seconds = (int)(till - System.currentTimeMillis()) / 1000;
            teamData.put("RegenTime", TimeUtils.getConvertedTime(seconds).trim());
            teamData.put("Name", playerTeam.getName());
            teamData.put("DTR", playerTeam.getDTR());
            teamData.put("MembersOnline", playerTeam.getOnlineMemberAmount());
            playerData.put("Team", teamData);
        }
        if (EnderpearlListener.getEnderpearlCooldown().containsKey(player.getName()) && EnderpearlListener.getEnderpearlCooldown().get(player.getName()) >= System.currentTimeMillis()) {
            playerData.put("EnderpearlCooldown", (EnderpearlListener.getEnderpearlCooldown().get(player.getName()) - System.currentTimeMillis()) / 1000.0f);
        }
        else {
            playerData.put("EnderpearlCooldown", 0);
        }
        if (SpawnTagHandler.isTagged(player)) {
            playerData.put("SpawnTag", SpawnTagHandler.getTag(player) / 1000.0f);
        }
        else {
            playerData.put("SpawnTag", 0);
        }
        final BasicDBList damageRecord = new BasicDBList();
        final List<Damage> records = DeathMessageHandler.getDamage(player);
        for (final Damage record : records) {
            if (System.currentTimeMillis() - record.getTime() > 30000L) {
                continue;
            }
            final BasicDBObject recordDBObject = new BasicDBObject();
            recordDBObject.put("Class", record.getClass().getSimpleName());
            recordDBObject.put("TimeBeforeDeath", (System.currentTimeMillis() - record.getTime()) / 1000.0f);
            recordDBObject.put("Damage", record.getDamage());
            recordDBObject.put("Description", record.getDescription());
            recordDBObject.put("Health", record.getHealthAfter());
            if (record instanceof PlayerDamage) {
                recordDBObject.put("Damager", ((PlayerDamage)record).getDamager());
            }
            else if (record instanceof MobDamage) {
                recordDBObject.put("EntityType", ((MobDamage)record).getMobType().name());
            }
            if (record instanceof ArrowTracker.ArrowDamageByPlayer) {
                final ArrowTracker.ArrowDamageByPlayer damage = (ArrowTracker.ArrowDamageByPlayer)record;
                final BasicDBObject locationData2 = new BasicDBObject();
                locationData2.put("World", damage.getShotFrom().getWorld().getName());
                locationData2.put("X", damage.getShotFrom().getX());
                locationData2.put("Y", damage.getShotFrom().getY());
                locationData2.put("Z", damage.getShotFrom().getZ());
                recordDBObject.put("Distance", damage.getDistance());
                recordDBObject.put("ShotFrom", locationData2);
            }
            ((ArrayList<BasicDBObject>)damageRecord).add(recordDBObject);
        }
        playerData.put("DamageRecord", damageRecord);
        deathData.put("Date", new Date().toString());
        deathData.put("Killer", (killerData == null) ? "N/A" : killerData);
        deathData.put("Player", playerData);
        deathData.put("Server", serverData);
        System.out.println("Generated death log in " + (System.currentTimeMillis() - start) + "ms.");
        return deathData;
    }
}
