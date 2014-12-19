package com.mongodb;

import java.util.concurrent.*;
import java.util.*;
import java.util.logging.*;

@Deprecated
public class DBApiLayer extends DB
{
    static final int NUM_CURSORS_BEFORE_KILL = 100;
    static final int NUM_CURSORS_PER_BATCH = 20000;
    final String _root;
    final String _rootPlusDot;
    final DBTCPConnector _connector;
    final ConcurrentHashMap<String, DBCollectionImpl> _collections;
    ConcurrentLinkedQueue<DeadCursor> _deadCursorIds;
    
    DBTCPConnector getConnector() {
        return this._connector;
    }
    
    protected DBApiLayer(final Mongo mongo, final String name, final DBConnector connector) {
        super(mongo, name);
        this._collections = new ConcurrentHashMap<String, DBCollectionImpl>();
        this._deadCursorIds = new ConcurrentLinkedQueue<DeadCursor>();
        if (connector == null) {
            throw new IllegalArgumentException("need a connector: " + name);
        }
        this._root = name;
        this._rootPlusDot = this._root + ".";
        this._connector = (DBTCPConnector)connector;
    }
    
    public void requestStart() {
        this._connector.requestStart();
    }
    
    public void requestDone() {
        this._connector.requestDone();
    }
    
    public void requestEnsureConnection() {
        this._connector.requestEnsureConnection();
    }
    
    public WriteResult addUser(final String username, final char[] passwd, final boolean readOnly) {
        this.requestStart();
        try {
            if (this.isServerVersionAtLeast(Arrays.asList(2, 6, 0))) {
                final CommandResult userInfoResult = this.command(new BasicDBObject("usersInfo", username));
                try {
                    userInfoResult.throwOnError();
                }
                catch (MongoException e) {
                    if (e.getCode() != 13) {
                        throw e;
                    }
                }
                final String operationType = (!userInfoResult.containsField("users") || ((List)userInfoResult.get("users")).isEmpty()) ? "createUser" : "updateUser";
                final DBObject userCommandDocument = this.getUserCommandDocument(username, passwd, readOnly, operationType);
                final CommandResult commandResult = this.command(userCommandDocument);
                commandResult.throwOnError();
                return new WriteResult(commandResult, this.getWriteConcern());
            }
            return super.addUser(username, passwd, readOnly);
        }
        finally {
            this.requestDone();
        }
    }
    
    public WriteResult removeUser(final String username) {
        this.requestStart();
        try {
            if (this.isServerVersionAtLeast(Arrays.asList(2, 6, 0))) {
                final CommandResult res = this.command(new BasicDBObject("dropUser", username));
                res.throwOnError();
                return new WriteResult(res, this.getWriteConcern());
            }
            return super.removeUser(username);
        }
        finally {
            this.requestDone();
        }
    }
    
    private DBObject getUserCommandDocument(final String username, final char[] passwd, final boolean readOnly, final String commandName) {
        return new BasicDBObject(commandName, username).append("pwd", this._hash(username, passwd)).append("digestPassword", false).append("roles", Arrays.asList(this.getUserRoleName(readOnly)));
    }
    
    private String getUserRoleName(final boolean readOnly) {
        return this.getName().equals("admin") ? (readOnly ? "readAnyDatabase" : "root") : (readOnly ? "read" : "dbOwner");
    }
    
    protected DBCollectionImpl doGetCollection(final String name) {
        DBCollectionImpl c = this._collections.get(name);
        if (c != null) {
            return c;
        }
        c = new DBCollectionImpl(this, name);
        final DBCollectionImpl old = this._collections.putIfAbsent(name, c);
        return (old != null) ? old : c;
    }
    
    public Set<String> getCollectionNames() {
        this.requestStart();
        try {
            final List<String> collectionNames = new ArrayList<String>();
            if (this.isServerVersionAtLeast(Arrays.asList(2, 7, 7))) {
                final CommandResult res = this.command(new BasicDBObject("listCollections", this.getName()), ReadPreference.primary());
                if (!res.ok() && res.getCode() != 26) {
                    res.throwOnError();
                }
                else {
                    final List<DBObject> collections = (List<DBObject>)res.get("collections");
                    for (final DBObject collectionInfo : collections) {
                        collectionNames.add(collectionInfo.get("name").toString());
                    }
                }
            }
            else {
                final Iterator<DBObject> collections2 = this.getCollection("system.namespaces").find(new BasicDBObject(), null, 0, 0, 0, this.getOptions(), ReadPreference.primary(), null);
                while (collections2.hasNext()) {
                    final String collectionName = collections2.next().get("name").toString();
                    if (!collectionName.contains("$")) {
                        collectionNames.add(collectionName.substring(this.getName().length() + 1));
                    }
                }
            }
            Collections.sort(collectionNames);
            return new LinkedHashSet<String>(collectionNames);
        }
        finally {
            this.requestDone();
        }
    }
    
    public void cleanCursors(final boolean force) {
        final int sz = this._deadCursorIds.size();
        if (sz == 0 || (!force && sz < 100)) {
            return;
        }
        Bytes.LOGGER.info("going to kill cursors : " + sz);
        final Map<ServerAddress, List<Long>> m = new HashMap<ServerAddress, List<Long>>();
        DeadCursor c;
        while ((c = this._deadCursorIds.poll()) != null) {
            List<Long> x = m.get(c.host);
            if (x == null) {
                x = new LinkedList<Long>();
                m.put(c.host, x);
            }
            x.add(c.id);
        }
        for (final Map.Entry<ServerAddress, List<Long>> e : m.entrySet()) {
            try {
                this.killCursors(e.getKey(), e.getValue());
            }
            catch (Throwable t) {
                Bytes.LOGGER.log(Level.WARNING, "can't clean cursors", t);
                for (final Long x2 : e.getValue()) {
                    this._deadCursorIds.add(new DeadCursor(x2, e.getKey()));
                }
            }
        }
    }
    
    void killCursors(final ServerAddress addr, final List<Long> all) {
        if (all == null || all.size() == 0) {
            return;
        }
        OutMessage om = OutMessage.killCursors(this._mongo, Math.min(20000, all.size()));
        int soFar = 0;
        int totalSoFar = 0;
        for (final Long l : all) {
            om.writeLong(l);
            ++totalSoFar;
            if (++soFar >= 20000) {
                this._connector.say(this, om, WriteConcern.NONE);
                om = OutMessage.killCursors(this._mongo, Math.min(20000, all.size() - totalSoFar));
                soFar = 0;
            }
        }
        this._connector.say(this, om, WriteConcern.NONE, addr);
    }
    
    CommandResult doAuthenticate(final MongoCredential credentials) {
        return this._connector.authenticate(credentials);
    }
    
    boolean isServerVersionAtLeast(final List<Integer> versionList) {
        final DBPort primaryPort = this.getConnector().getPrimaryPort();
        try {
            return this.getConnector().getServerDescription(primaryPort.getAddress()).getVersion().compareTo(new ServerVersion(versionList)) >= 0;
        }
        finally {
            this._connector.releasePort(primaryPort);
        }
    }
    
    void addDeadCursor(final DeadCursor deadCursor) {
        this._deadCursorIds.add(deadCursor);
    }
    
    static class DeadCursor
    {
        final long id;
        final ServerAddress host;
        
        DeadCursor(final long a, final ServerAddress b) {
            super();
            this.id = a;
            this.host = b;
        }
    }
}
