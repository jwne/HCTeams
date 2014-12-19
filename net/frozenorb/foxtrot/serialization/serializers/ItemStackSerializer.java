package net.frozenorb.foxtrot.serialization.serializers;

import net.frozenorb.foxtrot.serialization.*;
import org.bukkit.inventory.*;
import com.mongodb.*;
import org.bukkit.enchantments.*;
import org.bukkit.inventory.meta.*;
import org.bukkit.*;
import java.util.*;

public class ItemStackSerializer implements JSONSerializer<ItemStack>
{
    @Override
    public BasicDBObject serialize(final ItemStack o) {
        if (o == null) {
            return new BasicDBObject("type", "AIR").append("amount", 1).append("data", 0);
        }
        final BasicDBObject item = new BasicDBObject("type", o.getType().toString()).append("amount", Math.max(o.getAmount(), o.getMaxStackSize())).append("data", o.getDurability());
        final BasicDBList enchants = new BasicDBList();
        for (final Map.Entry<Enchantment, Integer> entry : o.getEnchantments().entrySet()) {
            ((ArrayList<BasicDBObject>)enchants).add(new BasicDBObject("enchantment", entry.getKey().getName()).append("level", entry.getValue()));
        }
        if (o.getEnchantments().size() > 0) {
            item.append("enchants", enchants);
        }
        if (o.hasItemMeta()) {
            final ItemMeta m = o.getItemMeta();
            final BasicDBObject meta = new BasicDBObject("displayName", m.getDisplayName());
            item.append("meta", meta);
        }
        return item;
    }
    
    @Override
    public ItemStack deserialize(final BasicDBObject dbobj) {
        final Material type = Material.valueOf(dbobj.getString("type"));
        final ItemStack item = new ItemStack(type, dbobj.getInt("amount"));
        item.setDurability(Short.parseShort(dbobj.getString("data")));
        if (dbobj.containsField("enchants")) {
            final BasicDBList enchs = (BasicDBList)dbobj.get("enchants");
            for (final Object o : enchs) {
                final BasicDBObject enchant = (BasicDBObject)o;
                item.addUnsafeEnchantment(Enchantment.getByName(enchant.getString("enchantment")), enchant.getInt("level"));
            }
        }
        if (dbobj.containsField("meta")) {
            final BasicDBObject meta = (BasicDBObject)dbobj.get("meta");
            final ItemMeta m = item.getItemMeta();
            if (meta.containsField("displayName")) {
                m.setDisplayName(meta.getString("displayName"));
            }
            item.setItemMeta(m);
        }
        return item;
    }
}
