package net.frozenorb.foxtrot.jedis.persist;

import net.frozenorb.foxtrot.jedis.*;
import net.frozenorb.foxtrot.team.chat.*;

public class ChatModeMap extends RedisPersistMap<ChatMode>
{
    public ChatModeMap() {
        super("ChatModes");
    }
    
    @Override
    public String getRedisValue(final ChatMode chatMode) {
        return chatMode.name();
    }
    
    @Override
    public ChatMode getJavaObject(final String str) {
        return ChatMode.valueOf(str);
    }
    
    public ChatMode getChatMode(final String player) {
        return this.contains(player) ? this.getValue(player) : ChatMode.PUBLIC;
    }
    
    public void setChatMode(final String player, final ChatMode chatMode) {
        this.updateValueAsync(player, chatMode);
    }
}
