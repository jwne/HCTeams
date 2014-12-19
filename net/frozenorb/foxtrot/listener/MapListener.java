package net.frozenorb.foxtrot.listener;

import org.bukkit.block.*;
import org.bukkit.scheduler.*;
import net.frozenorb.foxtrot.*;
import org.bukkit.plugin.*;
import org.bukkit.event.inventory.*;
import org.bukkit.event.*;
import org.bukkit.event.entity.*;
import org.bukkit.enchantments.*;
import org.bukkit.entity.*;

public class MapListener implements Listener
{
    private void startUpdate(final Furnace tile, final int increase) {
        new BukkitRunnable() {
            public void run() {
                if (tile.getCookTime() > 0 || tile.getBurnTime() > 0) {
                    tile.setCookTime((short)(tile.getCookTime() + increase));
                    tile.update();
                }
                else {
                    this.cancel();
                }
            }
        }.runTaskTimer((Plugin)buttplug.fdsjfhkdsjfdsjhk(), 1L, 1L);
    }
    
    @EventHandler
    public void onFurnaceBurn(final FurnaceBurnEvent event) {
        this.startUpdate((Furnace)event.getBlock().getState(), buttplug.RANDOM.nextBoolean() ? 1 : 2);
    }
    
    @EventHandler
    public void onEntityDeath(final EntityDeathEvent event) {
        double multiplier = buttplug.fdsjfhkdsjfdsjhk().getMapHandler().getBaseLootingMultiplier();
        if (event.getEntity().getKiller() != null) {
            final Player player = event.getEntity().getKiller();
            if (player.getItemInHand() != null && player.getItemInHand().containsEnchantment(Enchantment.LOOT_BONUS_MOBS)) {
                switch (player.getItemInHand().getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS)) {
                    case 1: {
                        multiplier = buttplug.fdsjfhkdsjfdsjhk().getMapHandler().getLevel1LootingMultiplier();
                        break;
                    }
                    case 2: {
                        multiplier = buttplug.fdsjfhkdsjfdsjhk().getMapHandler().getLevel2LootingMultiplier();
                        break;
                    }
                    case 3: {
                        multiplier = buttplug.fdsjfhkdsjfdsjhk().getMapHandler().getLevel3LootingMultiplier();
                        break;
                    }
                }
            }
        }
        event.setDroppedExp((int)Math.ceil(event.getDroppedExp() * multiplier));
    }
}
