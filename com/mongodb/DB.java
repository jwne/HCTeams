package com.mongodb;

import org.bson.*;
import java.io.*;
import com.mongodb.util.*;
import java.util.*;

public abstract class DB
{
    private static final Set<String> _obedientCommands;
    final Mongo _mongo;
    final String _name;
    @Deprecated
    protected boolean _readOnly;
    private WriteConcern _concern;
    private ReadPreference _readPref;
    final Bytes.OptionHolder _options;
    private volatile CommandResult authenticationTestCommandResult;
    
    public DB(final Mongo mongo, final String name) {
        super();
        this._readOnly = false;
        if (!this.isValidName(name)) {
            throw new IllegalArgumentException("Invalid database name format. Database name is either empty or it contains spaces.");
        }
        this._mongo = mongo;
        this._name = name;
        this._options = new Bytes.OptionHolder(this._mongo._netOptions);
    }
    
    ReadPreference getCommandReadPreference(final DBObject command, final ReadPreference requestedPreference) {
        if (this._mongo.getReplicaSetStatus() == null) {
            return requestedPreference;
        }
        final String comString = command.keySet().iterator().next();
        if (comString.equals("getnonce") || comString.equals("authenticate")) {
            return ReadPreference.primaryPreferred();
        }
        boolean primaryRequired;
        if (comString.equals("mapreduce")) {
            final Object out = command.get("out");
            if (out instanceof BSONObject) {
                final BSONObject outMap = (BSONObject)out;
                primaryRequired = (outMap.get("inline") == null);
            }
            else {
                primaryRequired = true;
            }
        }
        else if (comString.equals("aggregate")) {
            final List<DBObject> pipeline = (List<DBObject>)command.get("pipeline");
            primaryRequired = (pipeline.get(pipeline.size() - 1).get("$out") != null);
        }
        else {
            primaryRequired = !DB._obedientCommands.contains(comString.toLowerCase());
        }
        if (primaryRequired) {
            return ReadPreference.primary();
        }
        if (requestedPreference == null) {
            return ReadPreference.primary();
        }
        return requestedPreference;
    }
    
    @Deprecated
    public abstract void requestStart();
    
    @Deprecated
    public abstract void requestDone();
    
    @Deprecated
    public abstract void requestEnsureConnection();
    
    protected abstract DBCollection doGetCollection(final String p0);
    
    public DBCollection getCollection(final String name) {
        final DBCollection c = this.doGetCollection(name);
        return c;
    }
    
    public DBCollection createCollection(final String name, final DBObject options) {
        if (options != null) {
            final DBObject createCmd = new BasicDBObject("create", name);
            createCmd.putAll(options);
            final CommandResult result = this.command(createCmd);
            result.throwOnError();
        }
        return this.getCollection(name);
    }
    
    public DBCollection getCollectionFromString(String s) {
        DBCollection foo = null;
        for (int idx = s.indexOf("."); idx >= 0; idx = s.indexOf(".")) {
            final String b = s.substring(0, idx);
            s = s.substring(idx + 1);
            if (foo == null) {
                foo = this.getCollection(b);
            }
            else {
                foo = foo.getCollection(b);
            }
        }
        if (foo != null) {
            return foo.getCollection(s);
        }
        return this.getCollection(s);
    }
    
    public CommandResult command(final DBObject cmd) {
        return this.command(cmd, 0);
    }
    
    public CommandResult command(final DBObject cmd, final DBEncoder encoder) {
        return this.command(cmd, 0, encoder);
    }
    
    @Deprecated
    public CommandResult command(final DBObject cmd, final int options, final DBEncoder encoder) {
        return this.command(cmd, options, this.getReadPreference(), encoder);
    }
    
    @Deprecated
    public CommandResult command(final DBObject cmd, final int options, final ReadPreference readPreference) {
        return this.command(cmd, options, readPreference, DefaultDBEncoder.FACTORY.create());
    }
    
    @Deprecated
    public CommandResult command(DBObject cmd, final int options, final ReadPreference readPreference, final DBEncoder encoder) {
        final ReadPreference effectiveReadPrefs = this.getCommandReadPreference(cmd, readPreference);
        cmd = this.wrapCommand(cmd, effectiveReadPrefs);
        final QueryResultIterator i = this.getCollection("$cmd").find(cmd, new BasicDBObject(), 0, -1, 0, options, effectiveReadPrefs, DefaultDBDecoder.FACTORY.create(), encoder);
        if (!i.hasNext()) {
            return null;
        }
        final CommandResult cr = new CommandResult(i.getServerAddress());
        cr.putAll(i.next());
        return cr;
    }
    
    public CommandResult command(final DBObject cmd, final ReadPreference readPreference, final DBEncoder encoder) {
        return this.command(cmd, 0, readPreference, encoder);
    }
    
    private DBObject wrapCommand(DBObject cmd, final ReadPreference readPreference) {
        if (this.getMongo().isMongosConnection() && !ReadPreference.primary().equals(readPreference) && !ReadPreference.secondaryPreferred().equals(readPreference) && cmd instanceof BasicDBObject) {
            cmd = new BasicDBObject("$query", cmd).append("$readPreference", readPreference.toDBObject());
        }
        return cmd;
    }
    
    @Deprecated
    public CommandResult command(final DBObject cmd, final int options) {
        return this.command(cmd, options, this.getReadPreference());
    }
    
    public CommandResult command(final DBObject cmd, final ReadPreference readPreference) {
        return this.command(cmd, 0, readPreference);
    }
    
    public CommandResult command(final String cmd) {
        return this.command(new BasicDBObject(cmd, Boolean.TRUE));
    }
    
    @Deprecated
    public CommandResult command(final String cmd, final int options) {
        return this.command(new BasicDBObject(cmd, Boolean.TRUE), options);
    }
    
    public CommandResult command(final String cmd, final ReadPreference readPreference) {
        return this.command(new BasicDBObject(cmd, Boolean.TRUE), 0, readPreference);
    }
    
    public CommandResult doEval(final String code, final Object... args) {
        return this.command(BasicDBObjectBuilder.start().add("$eval", code).add("args", args).get());
    }
    
    public Object eval(final String code, final Object... args) {
        final CommandResult res = this.doEval(code, args);
        res.throwOnError();
        return res.get("retval");
    }
    
    public CommandResult getStats() {
        final CommandResult result = this.command("dbstats");
        result.throwOnError();
        return result;
    }
    
    public String getName() {
        return this._name;
    }
    
    @Deprecated
    public void setReadOnly(final Boolean b) {
        this._readOnly = b;
    }
    
    public abstract Set<String> getCollectionNames();
    
    public boolean collectionExists(final String collectionName) {
        if (collectionName == null || "".equals(collectionName)) {
            return false;
        }
        final Set<String> collections = this.getCollectionNames();
        if (collections.isEmpty()) {
            return false;
        }
        for (final String collection : collections) {
            if (collectionName.equalsIgnoreCase(collection)) {
                return true;
            }
        }
        return false;
    }
    
    public String toString() {
        return this._name;
    }
    
    @Deprecated
    public CommandResult getLastError() {
        return this.command(new BasicDBObject("getlasterror", 1));
    }
    
    @Deprecated
    public CommandResult getLastError(final WriteConcern concern) {
        return this.command(concern.getCommand());
    }
    
    @Deprecated
    public CommandResult getLastError(final int w, final int wtimeout, final boolean fsync) {
        return this.command(new WriteConcern(w, wtimeout, fsync).getCommand());
    }
    
    public void setWriteConcern(final WriteConcern writeConcern) {
        if (writeConcern == null) {
            throw new IllegalArgumentException();
        }
        this._concern = writeConcern;
    }
    
    public WriteConcern getWriteConcern() {
        if (this._concern != null) {
            return this._concern;
        }
        return this._mongo.getWriteConcern();
    }
    
    public void setReadPreference(final ReadPreference readPreference) {
        this._readPref = readPreference;
    }
    
    public ReadPreference getReadPreference() {
        if (this._readPref != null) {
            return this._readPref;
        }
        return this._mongo.getReadPreference();
    }
    
    public void dropDatabase() {
        final CommandResult res = this.command(new BasicDBObject("dropDatabase", 1));
        res.throwOnError();
        this._mongo._dbs.remove(this.getName());
    }
    
    @Deprecated
    public boolean isAuthenticated() {
        return this.getAuthenticationCredentials() != null;
    }
    
    @Deprecated
    public boolean authenticate(final String username, final char[] password) {
        return this.authenticateCommandHelper(username, password).failure == null;
    }
    
    @Deprecated
    public synchronized CommandResult authenticateCommand(final String username, final char[] password) {
        final CommandResultPair commandResultPair = this.authenticateCommandHelper(username, password);
        if (commandResultPair.failure != null) {
            throw commandResultPair.failure;
        }
        return commandResultPair.result;
    }
    
    private CommandResultPair authenticateCommandHelper(final String username, final char[] password) {
        final MongoCredential credentials = MongoCredential.createCredential(username, this.getName(), password);
        if (this.getAuthenticationCredentials() != null) {
            if (!this.getAuthenticationCredentials().equals(credentials)) {
                throw new IllegalStateException("can't authenticate twice on the same database");
            }
            if (this.authenticationTestCommandResult != null) {
                return new CommandResultPair(this.authenticationTestCommandResult);
            }
        }
        try {
            this.authenticationTestCommandResult = this.doAuthenticate(credentials);
            return new CommandResultPair(this.authenticationTestCommandResult);
        }
        catch (CommandFailureException commandFailureException) {
            return new CommandResultPair(commandFailureException);
        }
    }
    
    abstract CommandResult doAuthenticate(final MongoCredential p0);
    
    @Deprecated
    public WriteResult addUser(final String username, final char[] passwd) {
        return this.addUser(username, passwd, false);
    }
    
    @Deprecated
    public WriteResult addUser(final String username, final char[] passwd, final boolean readOnly) {
        final DBCollection c = this.getCollection("system.users");
        DBObject o = c.findOne(new BasicDBObject("user", username));
        if (o == null) {
            o = new BasicDBObject("user", username);
        }
        o.put("pwd", this._hash(username, passwd));
        o.put("readOnly", readOnly);
        return c.save(o);
    }
    
    @Deprecated
    public WriteResult removeUser(final String username) {
        final DBCollection c = this.getCollection("system.users");
        return c.remove(new BasicDBObject("user", username));
    }
    
    String _hash(final String username, final char[] passwd) {
        final ByteArrayOutputStream bout = new ByteArrayOutputStream(username.length() + 20 + passwd.length);
        try {
            bout.write(username.getBytes());
            bout.write(":mongo:".getBytes());
            for (int i = 0; i < passwd.length; ++i) {
                if (passwd[i] >= '\u0080') {
                    throw new IllegalArgumentException("can't handle non-ascii passwords yet");
                }
                bout.write((byte)passwd[i]);
            }
        }
        catch (IOException ioe) {
            throw new RuntimeException("impossible", ioe);
        }
        return Util.hexMD5(bout.toByteArray());
    }
    
    @Deprecated
    public CommandResult getPreviousError() {
        return this.command(new BasicDBObject("getpreverror", 1));
    }
    
    @Deprecated
    public void resetError() {
        this.command(new BasicDBObject("reseterror", 1));
    }
    
    @Deprecated
    public void forceError() {
        this.command(new BasicDBObject("forceerror", 1));
    }
    
    public Mongo getMongo() {
        return this._mongo;
    }
    
    public DB getSisterDB(final String name) {
        return this._mongo.getDB(name);
    }
    
    @Deprecated
    public void slaveOk() {
        this.addOption(4);
    }
    
    public void addOption(final int option) {
        this._options.add(option);
    }
    
    public void setOptions(final int options) {
        this._options.set(options);
    }
    
    public void resetOptions() {
        this._options.reset();
    }
    
    public int getOptions() {
        return this._options.get();
    }
    
    private boolean isValidName(final String dbname) {
        return dbname.length() != 0 && !dbname.contains(" ");
    }
    
    @Deprecated
    public abstract void cleanCursors(final boolean p0);
    
    MongoCredential getAuthenticationCredentials() {
        return this.getMongo().getAuthority().getCredentialsStore().get(this.getName());
    }
    
    static {
        (_obedientCommands = new HashSet<String>()).add("group");
        DB._obedientCommands.add("aggregate");
        DB._obedientCommands.add("collstats");
        DB._obedientCommands.add("dbstats");
        DB._obedientCommands.add("count");
        DB._obedientCommands.add("distinct");
        DB._obedientCommands.add("geonear");
        DB._obedientCommands.add("geosearch");
        DB._obedientCommands.add("geowalk");
        DB._obedientCommands.add("text");
        DB._obedientCommands.add("parallelcollectionscan");
        DB._obedientCommands.add("listIndexes");
        DB._obedientCommands.add("listCollections");
    }
    
    class CommandResultPair
    {
        CommandResult result;
        CommandFailureException failure;
        
        public CommandResultPair(final CommandResult result) {
            super();
            this.result = result;
        }
        
        public CommandResultPair(final CommandFailureException failure) {
            super();
            this.failure = failure;
        }
    }
}
