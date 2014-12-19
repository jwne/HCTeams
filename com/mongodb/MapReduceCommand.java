package com.mongodb;

import java.util.*;
import java.util.concurrent.*;
import org.bson.*;

public class MapReduceCommand
{
    final String _input;
    final String _map;
    final String _reduce;
    final String _outputTarget;
    ReadPreference _readPref;
    String _outputDB;
    final OutputType _outputType;
    final DBObject _query;
    String _finalize;
    DBObject _sort;
    int _limit;
    Map<String, Object> _scope;
    Boolean _verbose;
    DBObject _extra;
    private long _maxTimeMS;
    Boolean _jsMode;
    
    public MapReduceCommand(final DBCollection inputCollection, final String map, final String reduce, final String outputCollection, final OutputType type, final DBObject query) {
        super();
        this._outputDB = null;
        this._verbose = true;
        this._input = inputCollection.getName();
        this._map = map;
        this._reduce = reduce;
        this._outputTarget = outputCollection;
        this._outputType = type;
        this._query = query;
    }
    
    public void setVerbose(final Boolean verbose) {
        this._verbose = verbose;
    }
    
    public Boolean isVerbose() {
        return this._verbose;
    }
    
    public String getInput() {
        return this._input;
    }
    
    public String getMap() {
        return this._map;
    }
    
    public String getReduce() {
        return this._reduce;
    }
    
    public String getOutputTarget() {
        return this._outputTarget;
    }
    
    public OutputType getOutputType() {
        return this._outputType;
    }
    
    public String getFinalize() {
        return this._finalize;
    }
    
    public void setFinalize(final String finalize) {
        this._finalize = finalize;
    }
    
    public DBObject getQuery() {
        return this._query;
    }
    
    public DBObject getSort() {
        return this._sort;
    }
    
    public void setSort(final DBObject sort) {
        this._sort = sort;
    }
    
    public int getLimit() {
        return this._limit;
    }
    
    public void setLimit(final int limit) {
        this._limit = limit;
    }
    
    public long getMaxTime(final TimeUnit timeUnit) {
        return timeUnit.convert(this._maxTimeMS, TimeUnit.MILLISECONDS);
    }
    
    public void setMaxTime(final long maxTime, final TimeUnit timeUnit) {
        this._maxTimeMS = TimeUnit.MILLISECONDS.convert(maxTime, timeUnit);
    }
    
    public Map<String, Object> getScope() {
        return this._scope;
    }
    
    public void setScope(final Map<String, Object> scope) {
        this._scope = scope;
    }
    
    public Boolean getJsMode() {
        return this._jsMode;
    }
    
    public void setJsMode(final Boolean jsMode) {
        this._jsMode = jsMode;
    }
    
    public String getOutputDB() {
        return this._outputDB;
    }
    
    public void setOutputDB(final String outputDB) {
        this._outputDB = outputDB;
    }
    
    public DBObject toDBObject() {
        final BasicDBObject cmd = new BasicDBObject();
        cmd.put("mapreduce", this._input);
        cmd.put("map", this._map);
        cmd.put("reduce", this._reduce);
        if (this._verbose != null) {
            cmd.put("verbose", this._verbose);
        }
        final BasicDBObject out = new BasicDBObject();
        switch (this._outputType) {
            case INLINE: {
                out.put("inline", 1);
                break;
            }
            case REPLACE: {
                out.put("replace", this._outputTarget);
                break;
            }
            case MERGE: {
                out.put("merge", this._outputTarget);
                break;
            }
            case REDUCE: {
                out.put("reduce", this._outputTarget);
                break;
            }
        }
        if (this._outputDB != null) {
            out.put("db", this._outputDB);
        }
        cmd.put("out", out);
        if (this._query != null) {
            cmd.put("query", this._query);
        }
        if (this._finalize != null) {
            cmd.put("finalize", this._finalize);
        }
        if (this._sort != null) {
            cmd.put("sort", this._sort);
        }
        if (this._limit > 0) {
            cmd.put("limit", this._limit);
        }
        if (this._scope != null) {
            cmd.put("scope", this._scope);
        }
        if (this._jsMode != null) {
            cmd.put("jsMode", this._jsMode);
        }
        if (this._extra != null) {
            cmd.putAll(this._extra);
        }
        if (this._maxTimeMS != 0L) {
            cmd.put("maxTimeMS", this._maxTimeMS);
        }
        return cmd;
    }
    
    @Deprecated
    public void addExtraOption(final String name, final Object value) {
        if (this._extra == null) {
            this._extra = new BasicDBObject();
        }
        this._extra.put(name, value);
    }
    
    @Deprecated
    public DBObject getExtraOptions() {
        return this._extra;
    }
    
    public void setReadPreference(final ReadPreference preference) {
        this._readPref = preference;
    }
    
    public ReadPreference getReadPreference() {
        return this._readPref;
    }
    
    public String toString() {
        return this.toDBObject().toString();
    }
    
    public enum OutputType
    {
        REPLACE, 
        MERGE, 
        REDUCE, 
        INLINE;
    }
}
