package com.comphenix.packetwrapper;

import com.comphenix.protocol.*;
import com.comphenix.protocol.events.*;
import com.comphenix.protocol.wrappers.*;

public class WrapperPlayClientSettings extends AbstractPacket
{
    public static final PacketType TYPE;
    
    public WrapperPlayClientSettings() {
        super(new PacketContainer(WrapperPlayClientSettings.TYPE), WrapperPlayClientSettings.TYPE);
        this.handle.getModifier().writeDefaults();
    }
    
    public WrapperPlayClientSettings(final PacketContainer packet) {
        super(packet, WrapperPlayClientSettings.TYPE);
    }
    
    public String getLocale() {
        return (String)this.handle.getStrings().read(0);
    }
    
    public void setLocale(final String value) {
        this.handle.getStrings().write(0, (Object)value);
    }
    
    public byte getViewDistance() {
        return (byte)this.handle.getIntegers().read(0);
    }
    
    public void setViewDistance(final byte value) {
        this.handle.getIntegers().write(0, (Object)(int)value);
    }
    
    public EnumWrappers.ChatVisibility getChatVisibility() {
        return (EnumWrappers.ChatVisibility)this.handle.getChatVisibilities().read(0);
    }
    
    public void setChatFlags(final EnumWrappers.ChatVisibility value) {
        this.handle.getChatVisibilities().write(0, (Object)value);
    }
    
    public boolean getChatColours() {
        return (boolean)this.handle.getSpecificModifier((Class)Boolean.TYPE).read(0);
    }
    
    public void setChatColours(final boolean value) {
        this.handle.getSpecificModifier((Class)Boolean.TYPE).write(0, (Object)value);
    }
    
    public EnumWrappers.Difficulty getDifficulty() {
        return (EnumWrappers.Difficulty)this.handle.getDifficulties().read(0);
    }
    
    public void setDifficulty(final EnumWrappers.Difficulty difficulty) {
        this.handle.getDifficulties().write(0, (Object)difficulty);
    }
    
    public boolean getShowCape() {
        return (boolean)this.handle.getSpecificModifier((Class)Boolean.TYPE).read(1);
    }
    
    public void setShowCape(final boolean value) {
        this.handle.getSpecificModifier((Class)Boolean.TYPE).write(1, (Object)value);
    }
    
    static {
        TYPE = PacketType.Play.Client.SETTINGS;
    }
}
