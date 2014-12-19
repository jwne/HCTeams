package net.frozenorb.foxtrot.nametag;

import java.lang.reflect.*;
import java.util.*;
import org.bukkit.entity.*;
import net.frozenorb.foxtrot.util.*;

public class ScoreboardTeamPacketMod
{
    private static Method getHandle;
    private static Method sendPacket;
    private static Field playerConnection;
    private static Class<?> packetType;
    private Object packet;
    
    public ScoreboardTeamPacketMod(final String name, final String prefix, final String suffix, final Collection players, final int paramInt) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, NoSuchFieldException, InvocationTargetException {
        super();
        this.packet = ScoreboardTeamPacketMod.packetType.newInstance();
        this.setField("a", name);
        this.setField("f", paramInt);
        if (paramInt == 0 || paramInt == 2) {
            this.setField("b", name);
            this.setField("c", prefix);
            this.setField("d", suffix);
            this.setField("g", 3);
        }
        if (paramInt == 0) {
            this.addAll(players);
        }
    }
    
    public ScoreboardTeamPacketMod(final String name, Collection players, final int paramInt) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, NoSuchFieldException, InvocationTargetException {
        super();
        this.packet = ScoreboardTeamPacketMod.packetType.newInstance();
        if (paramInt != 3 && paramInt != 4) {
            throw new IllegalArgumentException("Method must be join or leave for player constructor");
        }
        if (players == null || players.isEmpty()) {
            players = new ArrayList<Object>();
        }
        this.setField("g", 3);
        this.setField("a", name);
        this.setField("f", paramInt);
        this.addAll(players);
    }
    
    public void sendToPlayer(final Player bukkitPlayer) {
        try {
            final Object player = ScoreboardTeamPacketMod.getHandle.invoke(bukkitPlayer, new Object[0]);
            final Object connection = ScoreboardTeamPacketMod.playerConnection.get(player);
            ScoreboardTeamPacketMod.sendPacket.invoke(connection, this.packet);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void setField(final String field, final Object value) {
        try {
            final Field f = this.packet.getClass().getDeclaredField(field);
            f.setAccessible(true);
            f.set(this.packet, value);
            f.setAccessible(false);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private void addAll(final Collection<?> col) throws NoSuchFieldException, IllegalAccessException {
        final Field f = this.packet.getClass().getDeclaredField("e");
        f.setAccessible(true);
        ((Collection)f.get(this.packet)).addAll(col);
    }
    
    static {
        try {
            ScoreboardTeamPacketMod.packetType = Class.forName(ReflectionUtils.getPacketTeamClasspath());
            final Class<?> typeCraftPlayer = Class.forName(ReflectionUtils.getCraftPlayerClasspath());
            final Class<?> typeNMSPlayer = Class.forName(ReflectionUtils.getNMSPlayerClasspath());
            final Class<?> typePlayerConnection = Class.forName(ReflectionUtils.getPlayerConnectionClasspath());
            ScoreboardTeamPacketMod.getHandle = typeCraftPlayer.getMethod("getHandle", (Class<?>[])new Class[0]);
            ScoreboardTeamPacketMod.playerConnection = typeNMSPlayer.getField("playerConnection");
            ScoreboardTeamPacketMod.sendPacket = typePlayerConnection.getMethod("sendPacket", Class.forName(ReflectionUtils.getPacketClasspath()));
        }
        catch (Exception e) {
            System.out.println("Failed to setup reflection for Packet209Mod!");
            e.printStackTrace();
        }
    }
}
