package com.mongodb;

import java.io.*;
import java.util.*;
import java.lang.reflect.*;

public class WriteConcern implements Serializable
{
    private static final long serialVersionUID = 1884671104750417011L;
    @Deprecated
    public static final WriteConcern ERRORS_IGNORED;
    public static final WriteConcern ACKNOWLEDGED;
    public static final WriteConcern UNACKNOWLEDGED;
    public static final WriteConcern FSYNCED;
    public static final WriteConcern JOURNALED;
    public static final WriteConcern REPLICA_ACKNOWLEDGED;
    @Deprecated
    public static final WriteConcern NONE;
    public static final WriteConcern NORMAL;
    public static final WriteConcern SAFE;
    public static final WriteConcern MAJORITY;
    public static final WriteConcern FSYNC_SAFE;
    public static final WriteConcern JOURNAL_SAFE;
    public static final WriteConcern REPLICAS_SAFE;
    private static Map<String, WriteConcern> _namedConcerns;
    Object _w;
    final int _wtimeout;
    final boolean _fsync;
    final boolean _j;
    final boolean _continueOnError;
    
    public WriteConcern() {
        this(0);
    }
    
    public WriteConcern(final int w) {
        this(w, 0, false);
    }
    
    public WriteConcern(final String w) {
        this(w, 0, false, false);
    }
    
    public WriteConcern(final int w, final int wtimeout) {
        this(w, wtimeout, false);
    }
    
    public WriteConcern(final boolean fsync) {
        this(1, 0, fsync);
    }
    
    public WriteConcern(final int w, final int wtimeout, final boolean fsync) {
        this(w, wtimeout, fsync, false);
    }
    
    public WriteConcern(final int w, final int wtimeout, final boolean fsync, final boolean j) {
        this(w, wtimeout, fsync, j, false);
    }
    
    public WriteConcern(final int w, final int wtimeout, final boolean fsync, final boolean j, final boolean continueOnError) {
        super();
        this._w = w;
        this._wtimeout = wtimeout;
        this._fsync = fsync;
        this._j = j;
        this._continueOnError = continueOnError;
    }
    
    public WriteConcern(final String w, final int wtimeout, final boolean fsync, final boolean j) {
        this(w, wtimeout, fsync, j, false);
    }
    
    public WriteConcern(final String w, final int wtimeout, final boolean fsync, final boolean j, final boolean continueOnError) {
        super();
        if (w == null) {
            throw new IllegalArgumentException("w can not be null");
        }
        this._w = w;
        this._wtimeout = wtimeout;
        this._fsync = fsync;
        this._j = j;
        this._continueOnError = continueOnError;
    }
    
    @Deprecated
    public BasicDBObject getCommand() {
        final BasicDBObject command = new BasicDBObject("getlasterror", 1);
        if ((this._w instanceof Integer && (int)this._w > 1) || this._w instanceof String) {
            command.put("w", this._w);
        }
        this.addWTimeout(command);
        this.addFSync(command);
        this.addJ(command);
        return command;
    }
    
    boolean useServerDefault() {
        return this._w.equals(1) && this._wtimeout == 0 && !this._fsync && !this._j;
    }
    
    BasicDBObject asDBObject() {
        final BasicDBObject document = new BasicDBObject();
        document.put("w", this._w);
        this.addWTimeout(document);
        this.addFSync(document);
        this.addJ(document);
        return document;
    }
    
    private void addJ(final BasicDBObject document) {
        if (this._j) {
            document.put("j", true);
        }
    }
    
    private void addFSync(final BasicDBObject document) {
        if (this._fsync) {
            document.put("fsync", true);
        }
    }
    
    private void addWTimeout(final BasicDBObject document) {
        if (this._wtimeout > 0) {
            document.put("wtimeout", this._wtimeout);
        }
    }
    
    @Deprecated
    public void setWObject(final Object w) {
        if (!(w instanceof Integer) && !(w instanceof String)) {
            throw new IllegalArgumentException("The w parameter must be an int or a String");
        }
        this._w = w;
    }
    
    public Object getWObject() {
        return this._w;
    }
    
    public int getW() {
        return (int)this._w;
    }
    
    public String getWString() {
        return this._w.toString();
    }
    
    public int getWtimeout() {
        return this._wtimeout;
    }
    
    public boolean getFsync() {
        return this._fsync;
    }
    
    public boolean fsync() {
        return this._fsync;
    }
    
    @Deprecated
    public boolean raiseNetworkErrors() {
        if (this._w instanceof Integer) {
            return (int)this._w >= 0;
        }
        return this._w != null;
    }
    
    public boolean callGetLastError() {
        if (this._w instanceof Integer) {
            return (int)this._w > 0;
        }
        return this._w != null;
    }
    
    public static WriteConcern valueOf(final String name) {
        if (WriteConcern._namedConcerns == null) {
            final HashMap<String, WriteConcern> newMap = new HashMap<String, WriteConcern>(8, 1.0f);
            for (final Field f : WriteConcern.class.getFields()) {
                if (Modifier.isStatic(f.getModifiers()) && f.getType().equals(WriteConcern.class)) {
                    try {
                        final String key = f.getName().toLowerCase();
                        newMap.put(key, (WriteConcern)f.get(null));
                    }
                    catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            WriteConcern._namedConcerns = newMap;
        }
        return WriteConcern._namedConcerns.get(name.toLowerCase());
    }
    
    public String toString() {
        return "WriteConcern " + this.getCommand() + " / (Continue on error? " + this.getContinueOnErrorForInsert() + ")";
    }
    
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final WriteConcern that = (WriteConcern)o;
        return this._continueOnError == that._continueOnError && this._fsync == that._fsync && this._j == that._j && this._wtimeout == that._wtimeout && this._w.equals(that._w);
    }
    
    public int hashCode() {
        int result = this._w.hashCode();
        result = 31 * result + this._wtimeout;
        result = 31 * result + (this._fsync ? 1 : 0);
        result = 31 * result + (this._j ? 1 : 0);
        result = 31 * result + (this._continueOnError ? 1 : 0);
        return result;
    }
    
    public boolean getJ() {
        return this._j;
    }
    
    @Deprecated
    public WriteConcern continueOnError(final boolean continueOnError) {
        if (this._w instanceof Integer) {
            return new WriteConcern((int)this._w, this._wtimeout, this._fsync, this._j, continueOnError);
        }
        if (this._w instanceof String) {
            return new WriteConcern((String)this._w, this._wtimeout, this._fsync, this._j, continueOnError);
        }
        throw new IllegalStateException("The w parameter must be an int or a String");
    }
    
    @Deprecated
    public boolean getContinueOnError() {
        return this._continueOnError;
    }
    
    @Deprecated
    public WriteConcern continueOnErrorForInsert(final boolean continueOnErrorForInsert) {
        return this.continueOnError(continueOnErrorForInsert);
    }
    
    @Deprecated
    public boolean getContinueOnErrorForInsert() {
        return this.getContinueOnError();
    }
    
    public static Majority majorityWriteConcern(final int wtimeout, final boolean fsync, final boolean j) {
        return new Majority(wtimeout, fsync, j);
    }
    
    static {
        ERRORS_IGNORED = new WriteConcern(-1);
        ACKNOWLEDGED = new WriteConcern(1);
        UNACKNOWLEDGED = new WriteConcern(0);
        FSYNCED = new WriteConcern(true);
        JOURNALED = new WriteConcern(1, 0, false, true);
        REPLICA_ACKNOWLEDGED = new WriteConcern(2);
        NONE = new WriteConcern(-1);
        NORMAL = new WriteConcern(0);
        SAFE = new WriteConcern(1);
        MAJORITY = new Majority();
        FSYNC_SAFE = new WriteConcern(true);
        JOURNAL_SAFE = new WriteConcern(1, 0, false, true);
        REPLICAS_SAFE = new WriteConcern(2);
        WriteConcern._namedConcerns = null;
    }
    
    public static class Majority extends WriteConcern
    {
        private static final long serialVersionUID = -4128295115883875212L;
        
        public Majority() {
            super("majority", 0, false, false);
        }
        
        public Majority(final int wtimeout, final boolean fsync, final boolean j) {
            super("majority", wtimeout, fsync, j);
        }
        
        public String toString() {
            return "[Majority] WriteConcern " + this.getCommand();
        }
    }
}
