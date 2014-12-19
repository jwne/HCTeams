package com.mongodb;

import java.io.*;

public class WriteResult
{
    private long _lastCall;
    private WriteConcern _lastConcern;
    private CommandResult _lastErrorResult;
    private final DB _db;
    private final DBPort _port;
    private final boolean _lazy;
    
    WriteResult(final CommandResult o, final WriteConcern concern) {
        super();
        this._lastErrorResult = o;
        this._lastConcern = concern;
        this._lazy = false;
        this._port = null;
        this._db = null;
    }
    
    WriteResult(final DB db, final DBPort p, final WriteConcern concern) {
        super();
        this._db = db;
        this._port = p;
        this._lastCall = p.getUsageCount();
        this._lastConcern = concern;
        this._lazy = true;
    }
    
    @Deprecated
    public CommandResult getCachedLastError() {
        return this._lastErrorResult;
    }
    
    @Deprecated
    public WriteConcern getLastConcern() {
        return this._lastConcern;
    }
    
    @Deprecated
    public synchronized CommandResult getLastError() {
        return this.getLastError(null);
    }
    
    @Deprecated
    public synchronized CommandResult getLastError(final WriteConcern concern) {
        if (this._lastErrorResult != null && (concern == null || (this._lastConcern != null && this._lastConcern.getW() >= concern.getW()))) {
            return this._lastErrorResult;
        }
        if (this._port == null) {
            throw new IllegalStateException("Don't have a port to obtain a write result, and existing one is not good enough.");
        }
        try {
            this._lastErrorResult = this._port.tryGetLastError(this._db, this._lastCall, (concern == null) ? new WriteConcern() : concern);
        }
        catch (IOException ioe) {
            throw new MongoException.Network(ioe.getMessage(), ioe);
        }
        if (this._lastErrorResult == null) {
            throw new IllegalStateException("The connection may have been used since this write, cannot obtain a result");
        }
        this._lastConcern = concern;
        ++this._lastCall;
        return this._lastErrorResult;
    }
    
    @Deprecated
    public String getError() {
        final Object foo = this.getField("err");
        if (foo == null) {
            return null;
        }
        return foo.toString();
    }
    
    public int getN() {
        return this.getLastError().getInt("n");
    }
    
    public Object getUpsertedId() {
        return this.getLastError().get("upserted");
    }
    
    public boolean isUpdateOfExisting() {
        return this.getLastError().getBoolean("updatedExisting");
    }
    
    @Deprecated
    public Object getField(final String name) {
        return this.getLastError().get(name);
    }
    
    @Deprecated
    public boolean isLazy() {
        return this._lazy;
    }
    
    public String toString() {
        final CommandResult res = this.getCachedLastError();
        if (res != null) {
            return res.toString();
        }
        return "N/A";
    }
}
