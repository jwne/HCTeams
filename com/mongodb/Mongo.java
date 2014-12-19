package com.mongodb;

import java.util.logging.*;
import org.bson.util.*;
import org.bson.io.*;
import java.net.*;
import java.util.concurrent.*;
import java.util.*;

public class Mongo
{
    static Logger logger;
    @Deprecated
    public static final int MAJOR_VERSION = 2;
    @Deprecated
    public static final int MINOR_VERSION = 13;
    private static final String FULL_VERSION = "2.13.0-rc0";
    static int cleanerIntervalMS;
    private static final String ADMIN_DATABASE_NAME = "admin";
    final MongoOptions _options;
    final DBTCPConnector _connector;
    final ConcurrentMap<String, DB> _dbs;
    private WriteConcern _concern;
    private ReadPreference _readPref;
    final Bytes.OptionHolder _netOptions;
    final CursorCleanerThread _cleaner;
    final MongoAuthority _authority;
    SimplePool<PoolOutputBuffer> _bufferPool;
    
    @Deprecated
    public static int getMajorVersion() {
        return 2;
    }
    
    @Deprecated
    public static int getMinorVersion() {
        return 13;
    }
    
    @Deprecated
    public static DB connect(final DBAddress addr) {
        return new Mongo(addr).getDB(addr.getDBName());
    }
    
    public Mongo() throws UnknownHostException {
        this(new ServerAddress());
    }
    
    public Mongo(final String host) throws UnknownHostException {
        this(new ServerAddress(host));
    }
    
    public Mongo(final String host, final MongoOptions options) throws UnknownHostException {
        this(new ServerAddress(host), options);
    }
    
    public Mongo(final String host, final int port) throws UnknownHostException {
        this(new ServerAddress(host, port));
    }
    
    public Mongo(final ServerAddress addr) {
        this(addr, new MongoOptions());
    }
    
    public Mongo(final ServerAddress addr, final MongoOptions options) {
        this(MongoAuthority.direct(addr), options);
    }
    
    public Mongo(final ServerAddress left, final ServerAddress right) {
        this(left, right, new MongoOptions());
    }
    
    public Mongo(final ServerAddress left, final ServerAddress right, final MongoOptions options) {
        this(MongoAuthority.dynamicSet(Arrays.asList(left, right)), options);
    }
    
    public Mongo(final List<ServerAddress> seeds) {
        this(seeds, new MongoOptions());
    }
    
    public Mongo(final List<ServerAddress> seeds, final MongoOptions options) {
        this(MongoAuthority.dynamicSet(seeds), options);
    }
    
    public Mongo(final MongoURI uri) throws UnknownHostException {
        this(getMongoAuthorityFromURI(uri), uri.getOptions());
    }
    
    Mongo(final MongoAuthority authority, final MongoOptions options) {
        super();
        this._dbs = new ConcurrentHashMap<String, DB>();
        this._concern = WriteConcern.NORMAL;
        this._readPref = ReadPreference.primary();
        this._netOptions = new Bytes.OptionHolder(null);
        this._bufferPool = new SimplePool<PoolOutputBuffer>(1000) {
            protected PoolOutputBuffer createNew() {
                return new PoolOutputBuffer();
            }
        };
        Mongo.logger.info("Creating Mongo instance (driver version " + this.getVersion() + ") with authority " + authority + " and options " + options);
        this._authority = authority;
        this._options = options;
        this._applyMongoOptions();
        (this._connector = new DBTCPConnector(this)).start();
        if (this._options.cursorFinalizerEnabled) {
            (this._cleaner = new CursorCleanerThread()).start();
        }
        else {
            this._cleaner = null;
        }
    }
    
    public DB getDB(final String dbname) {
        DB db = this._dbs.get(dbname);
        if (db != null) {
            return db;
        }
        db = new DBApiLayer(this, dbname, this._connector);
        final DB temp = this._dbs.putIfAbsent(dbname, db);
        if (temp != null) {
            return temp;
        }
        return db;
    }
    
    public Collection<DB> getUsedDatabases() {
        return this._dbs.values();
    }
    
    public List<String> getDatabaseNames() {
        final BasicDBObject cmd = new BasicDBObject();
        cmd.put("listDatabases", 1);
        final CommandResult res = this.getDB("admin").command(cmd, this.getOptions());
        res.throwOnError();
        final List l = (List)res.get("databases");
        final List<String> list = new ArrayList<String>();
        for (final Object o : l) {
            list.add(((BasicDBObject)o).getString("name"));
        }
        return list;
    }
    
    public void dropDatabase(final String dbName) {
        this.getDB(dbName).dropDatabase();
    }
    
    public String getVersion() {
        return "2.13.0-rc0";
    }
    
    @Deprecated
    public String debugString() {
        return this._connector.debugString();
    }
    
    public String getConnectPoint() {
        return this._connector.getConnectPoint();
    }
    
    @Deprecated
    public DBTCPConnector getConnector() {
        return this._connector;
    }
    
    public ReplicaSetStatus getReplicaSetStatus() {
        return this._connector.getReplicaSetStatus();
    }
    
    public ServerAddress getAddress() {
        return this._connector.getAddress();
    }
    
    public List<ServerAddress> getAllAddress() {
        final List<ServerAddress> result = this._connector.getAllAddress();
        if (result == null) {
            return Arrays.asList(this.getAddress());
        }
        return result;
    }
    
    public List<ServerAddress> getServerAddressList() {
        return this._connector.getServerAddressList();
    }
    
    public void close() {
        try {
            this._connector.close();
        }
        catch (Throwable t) {}
        if (this._cleaner != null) {
            this._cleaner.interrupt();
            try {
                this._cleaner.join();
            }
            catch (InterruptedException ex) {}
        }
    }
    
    public void setWriteConcern(final WriteConcern concern) {
        this._concern = concern;
    }
    
    public WriteConcern getWriteConcern() {
        return this._concern;
    }
    
    public void setReadPreference(final ReadPreference preference) {
        this._readPref = preference;
    }
    
    public ReadPreference getReadPreference() {
        return this._readPref;
    }
    
    @Deprecated
    public void slaveOk() {
        this.addOption(4);
    }
    
    public void addOption(final int option) {
        this._netOptions.add(option);
    }
    
    public void setOptions(final int options) {
        this._netOptions.set(options);
    }
    
    public void resetOptions() {
        this._netOptions.reset();
    }
    
    public int getOptions() {
        return this._netOptions.get();
    }
    
    void _applyMongoOptions() {
        if (this._options.slaveOk) {
            this.slaveOk();
        }
        if (this._options.getReadPreference() != null) {
            this.setReadPreference(this._options.getReadPreference());
        }
        this.setWriteConcern(this._options.getWriteConcern());
    }
    
    @Deprecated
    public MongoOptions getMongoOptions() {
        return this._options;
    }
    
    public int getMaxBsonObjectSize() {
        return this._connector.getMaxBsonObjectSize();
    }
    
    boolean isMongosConnection() {
        return this._connector.isMongosConnection();
    }
    
    private static MongoAuthority getMongoAuthorityFromURI(final MongoURI uri) throws UnknownHostException {
        if (uri.getHosts().size() == 1) {
            return MongoAuthority.direct(new ServerAddress(uri.getHosts().get(0)), uri.getCredentials());
        }
        final List<ServerAddress> replicaSetSeeds = new ArrayList<ServerAddress>(uri.getHosts().size());
        for (final String host : uri.getHosts()) {
            replicaSetSeeds.add(new ServerAddress(host));
        }
        return MongoAuthority.dynamicSet(replicaSetSeeds, uri.getCredentials());
    }
    
    public CommandResult fsync(final boolean async) {
        final DBObject cmd = new BasicDBObject("fsync", 1);
        if (async) {
            cmd.put("async", 1);
        }
        final CommandResult result = this.getDB("admin").command(cmd);
        result.throwOnError();
        return result;
    }
    
    public CommandResult fsyncAndLock() {
        final DBObject cmd = new BasicDBObject("fsync", 1);
        cmd.put("lock", 1);
        final CommandResult result = this.getDB("admin").command(cmd);
        result.throwOnError();
        return result;
    }
    
    public DBObject unlock() {
        final DB db = this.getDB("admin");
        final DBCollection col = db.getCollection("$cmd.sys.unlock");
        return col.findOne();
    }
    
    public boolean isLocked() {
        final DB db = this.getDB("admin");
        final DBCollection col = db.getCollection("$cmd.sys.inprog");
        final BasicDBObject res = (BasicDBObject)col.findOne();
        return res.containsField("fsyncLock") && res.getInt("fsyncLock") == 1;
    }
    
    public String toString() {
        return "Mongo{authority=" + this._authority + ", options=" + this._options + '}';
    }
    
    MongoAuthority getAuthority() {
        return this._authority;
    }
    
    static {
        Mongo.logger = Logger.getLogger(Bytes.LOGGER.getName() + ".Mongo");
        Mongo.cleanerIntervalMS = Integer.parseInt(System.getProperty("com.mongodb.cleanerIntervalMS", "1000"));
    }
    
    public static class Holder
    {
        private static Holder _default;
        private final ConcurrentMap<String, Mongo> _mongos;
        
        public Holder() {
            super();
            this._mongos = new ConcurrentHashMap<String, Mongo>();
        }
        
        @Deprecated
        public Mongo connect(final MongoURI uri) throws UnknownHostException {
            return this.connect(uri.toClientURI());
        }
        
        public Mongo connect(final MongoClientURI uri) throws UnknownHostException {
            final String key = this.toKey(uri);
            Mongo client = this._mongos.get(key);
            if (client == null) {
                final Mongo newbie = new MongoClient(uri);
                client = this._mongos.putIfAbsent(key, newbie);
                if (client == null) {
                    client = newbie;
                }
                else {
                    newbie.close();
                }
            }
            return client;
        }
        
        private String toKey(final MongoClientURI uri) {
            return uri.toString();
        }
        
        public static Holder singleton() {
            return Holder._default;
        }
        
        static {
            Holder._default = new Holder();
        }
    }
    
    class CursorCleanerThread extends Thread
    {
        CursorCleanerThread() {
            super();
            this.setDaemon(true);
            this.setName("MongoCleaner" + this.hashCode());
        }
        
        public void run() {
            while (Mongo.this._connector.isOpen()) {
                try {
                    try {
                        Thread.sleep(Mongo.cleanerIntervalMS);
                    }
                    catch (InterruptedException ex) {}
                    for (final DB db : Mongo.this._dbs.values()) {
                        db.cleanCursors(true);
                    }
                }
                catch (Throwable t) {}
            }
        }
    }
}
