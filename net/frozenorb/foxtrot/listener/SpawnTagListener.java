package net.frozenorb.foxtrot.listener;

import org.bukkit.event.entity.*;
import org.bukkit.entity.*;
import net.frozenorb.foxtrot.server.*;
import org.bukkit.event.*;

public class SpawnTagListener implements Listener
{
    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDamageByEntity(final EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player) || event.isCancelled()) {
            return;
        }
        Player damager = null;
        if (event.getDamager() instanceof Player) {
            damager = (Player)event.getDamager();
        }
        else if (event.getDamager() instanceof Projectile) {
            final Projectile projectile = (Projectile)event.getDamager();
            if (projectile.getShooter() instanceof Player) {
                damager = (Player)projectile.getShooter();
            }
        }
        if (damager != null && damager != event.getEntity()) {
            SpawnTagHandler.addSeconds(damager, 60);
        }
    }
}
