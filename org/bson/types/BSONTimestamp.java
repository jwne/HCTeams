package org.bson.types;

import java.io.*;
import java.util.*;

public class BSONTimestamp implements Comparable<BSONTimestamp>, Serializable
{
    private static final long serialVersionUID = -3268482672267936464L;
    static final boolean D;
    final int _inc;
    final Date _time;
    
    public BSONTimestamp() {
        super();
        this._inc = 0;
        this._time = null;
    }
    
    public BSONTimestamp(final int time, final int inc) {
        super();
        this._time = new Date(time * 1000L);
        this._inc = inc;
    }
    
    public int getTime() {
        if (this._time == null) {
            return 0;
        }
        return (int)(this._time.getTime() / 1000L);
    }
    
    public int getInc() {
        return this._inc;
    }
    
    public String toString() {
        return "TS time:" + this._time + " inc:" + this._inc;
    }
    
    public int compareTo(final BSONTimestamp ts) {
        if (this.getTime() != ts.getTime()) {
            return this.getTime() - ts.getTime();
        }
        return this.getInc() - ts.getInc();
    }
    
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = 31 * result + this._inc;
        result = 31 * result + this.getTime();
        return result;
    }
    
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof BSONTimestamp) {
            final BSONTimestamp t2 = (BSONTimestamp)obj;
            return this.getTime() == t2.getTime() && this.getInc() == t2.getInc();
        }
        return false;
    }
    
    static {
        D = Boolean.getBoolean("DEBUG.DBTIMESTAMP");
    }
}
