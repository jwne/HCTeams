package com.comphenix.packetwrapper;

import com.comphenix.protocol.*;
import org.bukkit.event.inventory.*;
import com.comphenix.protocol.events.*;
import java.util.*;

public class WrapperPlayServerOpenWindow extends AbstractPacket
{
    public static final PacketType TYPE;
    private static List<InventoryType> inventoryByID;
    
    public WrapperPlayServerOpenWindow() {
        super(new PacketContainer(WrapperPlayServerOpenWindow.TYPE), WrapperPlayServerOpenWindow.TYPE);
        this.handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayServerOpenWindow(final PacketContainer packet) {
        super(packet, WrapperPlayServerOpenWindow.TYPE);
    }
    
    public byte getWindowId() {
        return (byte)this.handle.getIntegers().read(0);
    }
    
    public void setWindowId(final byte value) {
        this.handle.getIntegers().write(0, (Object)(int)value);
    }
    
    public InventoryType getInventoryType() {
        final int id = (int)this.handle.getIntegers().read(1);
        if (id >= 0 && id <= WrapperPlayServerOpenWindow.inventoryByID.size()) {
            return WrapperPlayServerOpenWindow.inventoryByID.get(id);
        }
        throw new IllegalArgumentException("Cannot find inventory type " + id);
    }
    
    public void setInventoryType(final InventoryType value) {
        final int id = WrapperPlayServerOpenWindow.inventoryByID.indexOf(value);
        if (id > 0) {
            this.handle.getIntegers().write(1, (Object)id);
            return;
        }
        throw new IllegalArgumentException("Cannot find the ID of " + value);
    }
    
    public String getWindowTitle() {
        return (String)this.handle.getStrings().read(0);
    }
    
    public void setWindowTitle(final String value) {
        this.handle.getStrings().write(0, (Object)value);
    }
    
    public byte getNumberOfSlots() {
        return (byte)this.handle.getIntegers().read(2);
    }
    
    public void setNumberOfSlots(final byte value) {
        this.handle.getIntegers().write(2, (Object)(int)value);
    }
    
    public void setTitleExact(final boolean value) {
        this.handle.getSpecificModifier((Class)Boolean.TYPE).write(0, (Object)value);
    }
    
    public boolean isTitleExact() {
        return (boolean)this.handle.getSpecificModifier((Class)Boolean.TYPE).read(0);
    }
    
    public int getEntityId() {
        return (int)this.handle.getIntegers().read(3);
    }
    
    public void setEntityId(final int value) {
        this.handle.getIntegers().write(3, (Object)value);
    }
    
    static {
        TYPE = PacketType.Play.Server.OPEN_WINDOW;
        WrapperPlayServerOpenWindow.inventoryByID = Arrays.asList(InventoryType.CHEST, InventoryType.WORKBENCH, InventoryType.FURNACE, InventoryType.DISPENSER, InventoryType.ENCHANTING, InventoryType.BREWING, InventoryType.MERCHANT, InventoryType.BEACON, InventoryType.ANVIL);
    }
}
