package net.frozenorb.foxtrot.util;

import org.bukkit.inventory.*;
import org.bukkit.enchantments.*;
import net.frozenorb.foxtrot.*;
import org.bukkit.inventory.meta.*;
import java.util.*;
import java.text.*;
import org.bukkit.*;

public class InvUtils
{
    public static final SimpleDateFormat DEATH_TIME_FORMAT;
    public static final ItemStack CROWBAR;
    public static final String CROWBAR_NAME;
    public static final int CROWBAR_PORTALS = 6;
    public static final int CROWBAR_SPAWNERS = 1;
    
    public static boolean conformEnchants(final ItemStack item, final boolean removeUndefined) {
        if (item == null) {
            return false;
        }
        boolean fixed = false;
        final Map<Enchantment, Integer> enchants = (Map<Enchantment, Integer>)item.getEnchantments();
        for (final Enchantment enchantment : enchants.keySet()) {
            final int level = enchants.get(enchantment);
            if (buttplug.fdsjfhkdsjfdsjhk().getMapHandler().getMaxEnchantments().containsKey(enchantment)) {
                final int max = buttplug.fdsjfhkdsjfdsjhk().getMapHandler().getMaxEnchantments().get(enchantment);
                if (level <= max) {
                    continue;
                }
                item.addUnsafeEnchantment(enchantment, max);
                fixed = true;
            }
            else {
                if (!removeUndefined) {
                    continue;
                }
                item.removeEnchantment(enchantment);
                fixed = true;
            }
        }
        return fixed;
    }
    
    public static ItemStack addToPart(final ItemStack item, final String title, final String key, final int max) {
        final ItemMeta meta = item.getItemMeta();
        if (meta.hasLore() && meta.getLore().size() != 0) {
            final List<String> lore = (List<String>)meta.getLore();
            if (lore.contains(title)) {
                final int titleIndex = lore.indexOf(title);
                int keys = 0;
                for (int i = titleIndex; i < lore.size() && !lore.get(i).equals(""); ++i) {
                    ++keys;
                }
                lore.add(titleIndex + 1, key);
                if (keys > max) {
                    lore.remove(titleIndex + keys);
                }
            }
            else {
                lore.add("");
                lore.add(title);
                lore.add(key);
            }
            meta.setLore((List)lore);
        }
        else {
            final List<String> lore = new ArrayList<String>();
            lore.add("");
            lore.add(title);
            lore.add(key);
        }
        item.setItemMeta(meta);
        return item;
    }
    
    public static ItemStack addDeath(final ItemStack item, final String key) {
        return addToPart(item, ChatColor.DARK_RED.toString() + ChatColor.BOLD + "Deaths:", key, 10);
    }
    
    public static List<String> getCrowbarLore(final int portals, final int spawners) {
        final List<String> lore = new ArrayList<String>();
        lore.add("");
        lore.add(ChatColor.YELLOW + "Can Break:");
        lore.add(ChatColor.WHITE + " - " + ChatColor.AQUA + "End Portals: " + ChatColor.YELLOW + "{" + ChatColor.BLUE + portals + ChatColor.YELLOW + "}");
        lore.add(ChatColor.WHITE + " - " + ChatColor.AQUA + "Spawners: " + ChatColor.YELLOW + "{" + ChatColor.BLUE + spawners + ChatColor.YELLOW + "}");
        return lore;
    }
    
    public static List<String> getKOTHRewardKeyLore(final String koth, final int tier) {
        final List<String> lore = new ArrayList<String>();
        final DateFormat sdf = new SimpleDateFormat("M/d HH:mm:ss");
        lore.add("");
        lore.add(ChatColor.WHITE + " - " + ChatColor.AQUA + "Obtained from: " + ChatColor.YELLOW + "{" + ChatColor.BLUE + koth + ChatColor.YELLOW + "}");
        lore.add(ChatColor.WHITE + " - " + ChatColor.AQUA + "Level: " + ChatColor.YELLOW + "{" + ChatColor.BLUE + tier + ChatColor.YELLOW + "}");
        lore.add(ChatColor.WHITE + " - " + ChatColor.AQUA + "Time: " + ChatColor.YELLOW + "{" + ChatColor.BLUE + sdf.format(new Date()).replace(" AM", "").replace(" PM", "") + ChatColor.YELLOW + "}");
        return lore;
    }
    
    public static ItemStack generateKOTHRewardKey(final String koth, final int tier) {
        final ItemStack key = new ItemStack(Material.GOLD_NUGGET);
        final ItemMeta meta = key.getItemMeta();
        meta.setDisplayName(ChatColor.RED + "KOTH Reward Key");
        meta.setLore((List)getKOTHRewardKeyLore(koth, tier));
        key.setItemMeta(meta);
        return key;
    }
    
    public static int getCrowbarUsesPortal(final ItemStack item) {
        return Integer.valueOf(getLoreData(item, 2));
    }
    
    public static int getCrowbarUsesSpawner(final ItemStack item) {
        return Integer.valueOf(getLoreData(item, 3));
    }
    
    public static int getKOTHRewardKeyTier(final ItemStack item) {
        return Integer.valueOf(getLoreData(item, 2));
    }
    
    public static String getLoreData(final ItemStack item, final int index) {
        final List<String> lore = (List<String>)item.getItemMeta().getLore();
        if (index < lore.size()) {
            final String str = ChatColor.stripColor((String)lore.get(index));
            return str.split("\\{")[1].replace("}", "");
        }
        return "";
    }
    
    public static boolean isSimilar(final ItemStack item, final String name) {
        return item.hasItemMeta() && item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName().equals(name);
    }
    
    static {
        DEATH_TIME_FORMAT = new SimpleDateFormat("MM.dd.yy HH:mm");
        CROWBAR_NAME = ChatColor.RED + "Crowbar";
        CROWBAR = new ItemStack(Material.DIAMOND_HOE);
        final ItemMeta meta = InvUtils.CROWBAR.getItemMeta();
        meta.setDisplayName(InvUtils.CROWBAR_NAME);
        meta.setLore((List)getCrowbarLore(6, 1));
        InvUtils.CROWBAR.setItemMeta(meta);
    }
}
