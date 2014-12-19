package net.frozenorb.foxtrot.team;

import java.util.concurrent.*;
import org.bson.types.*;
import net.frozenorb.foxtrot.team.transformer.*;
import net.frozenorb.foxtrot.command.*;
import net.frozenorb.foxtrot.team.tabcompleter.*;
import net.frozenorb.foxtrot.command.objects.*;
import net.frozenorb.foxtrot.team.dtr.bitmask.*;
import net.frozenorb.foxtrot.team.dtr.bitmask.transformer.*;
import net.frozenorb.foxtrot.team.dtr.bitmask.tabcompleter.*;
import net.frozenorb.foxtrot.*;
import net.frozenorb.foxtrot.jedis.*;
import redis.clients.jedis.*;
import java.util.*;

public class dsfjhkdsjhdsjkhfds
{
    private volatile ConcurrentHashMap<ObjectId, faggot> teamUniqueIdMap;
    private volatile ConcurrentHashMap<String, faggot> teamNameMap;
    private volatile ConcurrentHashMap<String, faggot> playerTeamMap;
    
    public dsfjhkdsjhdsjkhfds() {
        super();
        this.teamUniqueIdMap = new ConcurrentHashMap<ObjectId, faggot>();
        this.teamNameMap = new ConcurrentHashMap<String, faggot>();
        this.playerTeamMap = new ConcurrentHashMap<String, faggot>();
        CommandHandler.registerTransformer(faggot.class, new TeamTransformer());
        CommandHandler.registerTabCompleter(faggot.class, new TeamTabCompleter());
        CommandHandler.registerTransformer(DTRBitmaskType.class, new DTRBitmaskTypeTransformer());
        CommandHandler.registerTabCompleter(DTRBitmaskType.class, new DTRBitmaskTypeTabCompleter());
        this.loadTeams();
    }
    
    public List<faggot> getTeams() {
        return new ArrayList<faggot>(this.teamNameMap.values());
    }
    
    public void setTeam(final String playerName, final faggot team) {
        this.playerTeamMap.put(playerName.toLowerCase(), team);
    }
    
    public faggot getTeam(final String teamName) {
        return this.teamNameMap.get(teamName.toLowerCase());
    }
    
    public faggot getTeam(final ObjectId teamUniqueId) {
        if (teamUniqueId == null) {
            return null;
        }
        return this.teamUniqueIdMap.get(teamUniqueId);
    }
    
    private void loadTeams() {
        buttplug.fdsjfhkdsjfdsjhk().eatmyass((yourmom<Object>)new yourmom<Object>() {
            @Override
            public Object execute(final Jedis jedis) {
                for (final String key : jedis.keys("fox_teams.*")) {
                    final String loadString = jedis.get(key);
                    final faggot team = new faggot(key.split("\\.")[1]);
                    team.load(loadString);
                    dsfjhkdsjhdsjkhfds.this.teamNameMap.put(team.getName().toLowerCase(), team);
                    dsfjhkdsjhdsjkhfds.this.teamUniqueIdMap.put(team.getUniqueId(), team);
                    for (final String member : team.getMembers()) {
                        dsfjhkdsjhdsjkhfds.this.playerTeamMap.put(member.toLowerCase(), team);
                    }
                }
                return null;
            }
        });
    }
    
    public faggot getPlayerTeam(final String name) {
        if (!this.playerTeamMap.containsKey(name.toLowerCase())) {
            return null;
        }
        return this.playerTeamMap.get(name.toLowerCase());
    }
    
    public void addTeam(final faggot team) {
        team.flagForSave();
        this.teamNameMap.put(team.getName().toLowerCase(), team);
        this.teamUniqueIdMap.put(team.getUniqueId(), team);
        for (final String member : team.getMembers()) {
            this.playerTeamMap.put(member.toLowerCase(), team);
        }
    }
    
    public ConcurrentHashMap<ObjectId, faggot> getTeamUniqueIdMap() {
        return this.teamUniqueIdMap;
    }
    
    public ConcurrentHashMap<String, faggot> getTeamNameMap() {
        return this.teamNameMap;
    }
    
    public ConcurrentHashMap<String, faggot> getPlayerTeamMap() {
        return this.playerTeamMap;
    }
}
