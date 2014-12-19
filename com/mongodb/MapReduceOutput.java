package com.mongodb;

public class MapReduceOutput
{
    final CommandResult _commandResult;
    final String _collname;
    String _dbname;
    final Iterable<DBObject> _resultSet;
    final DBCollection _coll;
    final BasicDBObject _counts;
    final DBObject _cmd;
    
    public MapReduceOutput(final DBCollection from, final DBObject cmd, final CommandResult raw) {
        super();
        this._dbname = null;
        this._commandResult = raw;
        this._cmd = cmd;
        if (raw.containsField("results")) {
            this._coll = null;
            this._collname = null;
            this._resultSet = (Iterable<DBObject>)raw.get("results");
        }
        else {
            final Object res = raw.get("result");
            if (res instanceof String) {
                this._collname = (String)res;
            }
            else {
                final BasicDBObject output = (BasicDBObject)res;
                this._collname = output.getString("collection");
                this._dbname = output.getString("db");
            }
            DB db = from._db;
            if (this._dbname != null) {
                db = db.getSisterDB(this._dbname);
            }
            (this._coll = db.getCollection(this._collname)).setOptions(this._coll.getOptions() & 0xFFFFFFFB);
            this._resultSet = this._coll.find();
        }
        this._counts = (BasicDBObject)raw.get("counts");
    }
    
    public Iterable<DBObject> results() {
        return this._resultSet;
    }
    
    public void drop() {
        if (this._coll != null) {
            this._coll.drop();
        }
    }
    
    public DBCollection getOutputCollection() {
        return this._coll;
    }
    
    @Deprecated
    public BasicDBObject getRaw() {
        return this._commandResult;
    }
    
    @Deprecated
    public CommandResult getCommandResult() {
        return this._commandResult;
    }
    
    public DBObject getCommand() {
        return this._cmd;
    }
    
    @Deprecated
    public ServerAddress getServerUsed() {
        return this._commandResult.getServerUsed();
    }
    
    public String toString() {
        return this._commandResult.toString();
    }
    
    public final String getCollectionName() {
        return this._collname;
    }
    
    public String getDatabaseName() {
        return this._dbname;
    }
    
    public int getDuration() {
        return this._commandResult.getInt("timeMillis");
    }
    
    public int getInputCount() {
        return this._counts.getInt("input");
    }
    
    public int getOutputCount() {
        return this._counts.getInt("output");
    }
    
    public int getEmitCount() {
        return this._counts.getInt("emit");
    }
}
