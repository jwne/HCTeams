package net.frozenorb.foxtrot.listener;

import org.bukkit.event.entity.*;
import net.frozenorb.foxtrot.*;
import net.frozenorb.foxtrot.team.dtr.bitmask.*;
import org.bukkit.entity.*;
import org.bukkit.potion.*;
import net.frozenorb.foxtrot.server.*;
import org.bukkit.inventory.*;
import java.util.*;
import org.bukkit.event.*;
import org.bukkit.event.player.*;
import org.bukkit.*;
import org.bukkit.event.inventory.*;

public class PotionLimiterListener implements Listener
{
    @EventHandler(priority = EventPriority.HIGH)
    public void onPotionSplash(final PotionSplashEvent event) {
        final ItemStack potion = event.getPotion().getItem();
        for (final LivingEntity livingEntity : event.getAffectedEntities()) {
            if (!buttplug.fdsjfhkdsjfdsjhk().getServerHandler().isEOTW() && DTRBitmaskType.SAFE_ZONE.appliesAt(livingEntity.getLocation())) {
                event.setIntensity(livingEntity, 0.0);
            }
        }
        for (final int i : ServerHandler.DISALLOWED_POTIONS) {
            if (i == potion.getDurability()) {
                event.setCancelled(true);
                return;
            }
        }
        if (event.getPotion().getShooter() instanceof Player) {
            final Iterator<PotionEffect> iterator = event.getPotion().getEffects().iterator();
            if (iterator.hasNext() && Arrays.asList(FoxListener.DEBUFFS).contains(iterator.next().getType()) && (event.getAffectedEntities().size() > 1 || (event.getAffectedEntities().size() == 1 && !event.getAffectedEntities().contains(event.getPotion().getShooter())))) {
                SpawnTagHandler.addSeconds((Player)event.getPotion().getShooter(), 60);
            }
        }
    }
    
    @EventHandler
    public void onPlayerItemConsume(final PlayerItemConsumeEvent event) {
        if (event.getItem().getType() != Material.POTION) {
            return;
        }
        if (ServerHandler.DISALLOWED_POTIONS.contains((int)event.getItem().getDurability())) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.RED + "This potion is not usable!");
        }
    }
    
    @EventHandler
    public void onInventoryClick(final InventoryClickEvent event) {
    }
}
