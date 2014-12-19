package net.frozenorb.foxtrot.serialization.serializers;

import net.frozenorb.foxtrot.serialization.*;
import com.mongodb.*;
import org.bukkit.*;
import org.bukkit.inventory.*;
import java.util.*;

public class InventorySerializer implements JSONSerializer<Inventory>
{
    @Override
    public BasicDBObject serialize(final Inventory o) {
        final BasicDBObject full = new BasicDBObject();
        final BasicDBObject inv = new BasicDBObject();
        final ItemStackSerializer serializer = new ItemStackSerializer();
        for (int i = 0; i < o.getSize(); ++i) {
            final ItemStack item = o.getItem(i);
            if (item != null && item.getType() != Material.AIR) {
                inv.put(i + "", serializer.serialize(item));
            }
        }
        full.put("inventory", inv);
        return full;
    }
    
    @Override
    public Inventory deserialize(final BasicDBObject dbobj) {
        final Inventory inv = Bukkit.createInventory((InventoryHolder)null, 36);
        final BasicDBObject contents = (BasicDBObject)dbobj.get("inventory");
        for (final Map.Entry<String, Object> str : contents.entrySet()) {
            final int slot = Integer.parseInt(str.getKey());
            final ItemStack item = new ItemStackSerializer().deserialize((BasicDBObject)str.getValue());
            if (item.getAmount() == 0) {
                item.setAmount(1);
            }
            inv.setItem(slot, item);
        }
        return inv;
    }
}
