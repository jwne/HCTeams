package net.frozenorb.foxtrot.listener;

import net.frozenorb.foxtrot.util.*;
import org.bukkit.event.*;
import org.bukkit.event.block.*;
import org.bukkit.*;
import org.bukkit.event.inventory.*;
import net.frozenorb.foxtrot.*;
import java.text.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import java.util.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.entity.*;

public class EnchantmentLimiterListener implements Listener
{
    private Map<String, Long> lastArmorCheck;
    private Map<String, Long> lastSwordCheck;
    private final char[] allowed;
    
    public EnchantmentLimiterListener() {
        super();
        this.lastArmorCheck = new HashMap<String, Long>();
        this.lastSwordCheck = new HashMap<String, Long>();
        this.allowed = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ!@#$%^&*()-_'".toCharArray();
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDamage(final EntityDamageEvent event) {
        if (event.getEntity() instanceof Player && !event.isCancelled() && this.checkArmor((Player)event.getEntity())) {
            final ItemStack[] armor = ((Player)event.getEntity()).getInventory().getArmorContents();
            boolean fixed = false;
            for (int i = 0; i < armor.length; ++i) {
                if (InvUtils.conformEnchants(armor[i], true)) {
                    fixed = true;
                }
            }
            if (fixed) {
                ((Player)event.getEntity()).sendMessage(ChatColor.YELLOW + "We detected that your armor had some illegal enchantments, and have reduced the invalid enchantments.");
            }
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDamageByEntity(final EntityDamageByEntityEvent event) {
        if (!event.isCancelled() && event.getDamager() instanceof Player && this.checkSword((Player)event.getDamager())) {
            final Player player = (Player)event.getDamager();
            final ItemStack hand = player.getItemInHand();
            if (InvUtils.conformEnchants(hand, true)) {
                player.setItemInHand(hand);
                player.sendMessage(ChatColor.YELLOW + "We detected that your sword had some illegal enchantments, and have reduced the invalid enchantments.");
            }
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerInteract(final PlayerInteractEvent event) {
        if ((event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) && !event.isCancelled() && event.getItem() != null && event.getItem().getType() == Material.BOW) {
            final ItemStack hand = event.getPlayer().getItemInHand();
            if (InvUtils.conformEnchants(hand, true)) {
                event.getPlayer().setItemInHand(hand);
                event.getPlayer().sendMessage(ChatColor.YELLOW + "We detected that your bow had some illegal enchantments, and have reduced the invalid enchantments.");
            }
        }
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onInventoryClick(final InventoryClickEvent event) {
        final HumanEntity humanEntity = event.getWhoClicked();
        if (!(humanEntity instanceof Player)) {
            return;
        }
        final Player player = (Player)humanEntity;
        final Inventory inventory = event.getInventory();
        if (event.getInventory().getType() == InventoryType.MERCHANT) {
            for (final ItemStack item : event.getInventory()) {
                if (item != null) {
                    InvUtils.conformEnchants(item, true);
                }
            }
        }
        if (!(inventory instanceof AnvilInventory)) {
            return;
        }
        final InventoryView view = event.getView();
        if (event.getRawSlot() != view.convertSlot(event.getRawSlot()) || event.getRawSlot() != 2) {
            return;
        }
        ItemStack item = event.getCurrentItem();
        final ItemStack baseItem = inventory.getItem(0);
        if (item == null) {
            return;
        }
        final ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasDisplayName()) {
            return;
        }
        final String displayName = this.fixName(meta.getDisplayName());
        if (baseItem.hasItemMeta() && baseItem.getItemMeta().getDisplayName() != null && buttplug.fdsjfhkdsjfdsjhk().getServerHandler().getUsedNames().contains(this.fixName(baseItem.getItemMeta().getDisplayName())) && !baseItem.getItemMeta().getDisplayName().equals(meta.getDisplayName())) {
            event.setCancelled(true);
            player.sendMessage(ChatColor.RED + "You cannot rename an item with a name!");
            return;
        }
        if (buttplug.fdsjfhkdsjfdsjhk().getServerHandler().getUsedNames().contains(displayName) && (!baseItem.hasItemMeta() || baseItem.getItemMeta().getDisplayName() == null || !baseItem.getItemMeta().getDisplayName().equals(meta.getDisplayName()))) {
            event.setCancelled(true);
            player.sendMessage(ChatColor.RED + "An item with that name already exists.");
        }
        else {
            List<String> lore = new ArrayList<String>();
            boolean hasForgedMeta = false;
            if (meta.hasLore()) {
                for (final String s : meta.getLore()) {
                    if (s.toLowerCase().contains("forged")) {
                        hasForgedMeta = true;
                    }
                }
            }
            if (meta.getLore() != null && !hasForgedMeta) {
                lore = (List<String>)meta.getLore();
            }
            final DateFormat sdf = DateFormat.getDateTimeInstance();
            lore.add(0, "§eForged by " + player.getDisplayName() + "§e on " + sdf.format(new Date()));
            meta.setLore((List)lore);
            item.setItemMeta(meta);
            event.setCurrentItem(item);
            buttplug.fdsjfhkdsjfdsjhk().getServerHandler().getUsedNames().add(displayName);
            buttplug.fdsjfhkdsjfdsjhk().getServerHandler().save();
            player.sendMessage(ChatColor.GREEN + "Claimed the name '" + displayName + "'.");
        }
    }
    
    private String fixName(final String name) {
        final String b = name.toLowerCase().trim();
        final char[] charArray = b.toCharArray();
        final StringBuilder result = new StringBuilder();
        for (final char c : charArray) {
            for (final char a : this.allowed) {
                if (c == a) {
                    result.append(a);
                }
            }
        }
        return result.toString();
    }
    
    @EventHandler
    public void onEntityDeathEvent(final EntityDeathEvent event) {
        final Iterator<ItemStack> iter = event.getDrops().iterator();
        while (iter.hasNext()) {
            InvUtils.conformEnchants(iter.next(), true);
        }
    }
    
    @EventHandler
    public void onPlayerFishEvent(final PlayerFishEvent event) {
        if (event.getCaught() instanceof Item) {
            InvUtils.conformEnchants(((Item)event.getCaught()).getItemStack(), true);
        }
    }
    
    public boolean checkArmor(final Player player) {
        final boolean check = !this.lastArmorCheck.containsKey(player.getName()) || System.currentTimeMillis() - this.lastArmorCheck.get(player.getName()) > 5000L;
        if (check) {
            this.lastArmorCheck.put(player.getName(), System.currentTimeMillis());
        }
        return check;
    }
    
    public boolean checkSword(final Player player) {
        final boolean check = !this.lastSwordCheck.containsKey(player.getName()) || System.currentTimeMillis() - this.lastSwordCheck.get(player.getName()) > 5000L;
        if (check) {
            this.lastSwordCheck.put(player.getName(), System.currentTimeMillis());
        }
        return check;
    }
}
