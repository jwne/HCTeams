package net.frozenorb.foxtrot.util;

import org.bukkit.inventory.*;
import org.bukkit.craftbukkit.v1_7_R3.inventory.*;
import net.minecraft.server.v1_7_R3.*;

public class NMSMethods
{
    public static int getPotionResult(final int origdata, final ItemStack ingredient) {
        return getPotionResult(origdata, CraftItemStack.asNMSCopy(ingredient));
    }
    
    private static int getPotionResult(final int origdata, final net.minecraft.server.v1_7_R3.ItemStack ingredient) {
        final int newdata = getBrewResult(origdata, ingredient);
        if (origdata <= 0 || origdata != newdata) {
            return (origdata != newdata) ? newdata : origdata;
        }
        return origdata;
    }
    
    private static int getBrewResult(final int i, final net.minecraft.server.v1_7_R3.ItemStack itemstack) {
        return (itemstack == null) ? i : (itemstack.getItem().m(itemstack) ? PotionBrewer.a(i, itemstack.getItem().i(itemstack)) : i);
    }
}
