package com.mongodb;

import java.net.*;

public class DBAddress extends ServerAddress
{
    final String _db;
    
    public DBAddress(final String urlFormat) throws UnknownHostException {
        super(_getHostSection(urlFormat));
        _check(urlFormat, "urlFormat");
        this._db = _fixName(_getDBSection(urlFormat));
        _check(this._host, "host");
        _check(this._db, "db");
    }
    
    static String _getHostSection(final String urlFormat) {
        if (urlFormat == null) {
            throw new NullPointerException("urlFormat can't be null");
        }
        final int idx = urlFormat.indexOf("/");
        if (idx >= 0) {
            return urlFormat.substring(0, idx);
        }
        return null;
    }
    
    static String _getDBSection(final String urlFormat) {
        if (urlFormat == null) {
            throw new NullPointerException("urlFormat can't be null");
        }
        final int idx = urlFormat.indexOf("/");
        if (idx >= 0) {
            return urlFormat.substring(idx + 1);
        }
        return urlFormat;
    }
    
    static String _fixName(String name) {
        name = name.replace('.', '-');
        return name;
    }
    
    public DBAddress(final DBAddress other, final String dbname) throws UnknownHostException {
        this(other._host, other._port, dbname);
    }
    
    public DBAddress(final String host, final String dbname) throws UnknownHostException {
        this(host, 27017, dbname);
    }
    
    public DBAddress(final String host, final int port, final String dbname) throws UnknownHostException {
        super(host, port);
        this._db = dbname.trim();
    }
    
    public DBAddress(final InetAddress addr, final int port, final String dbname) {
        super(addr, port);
        _check(dbname, "name");
        this._db = dbname.trim();
    }
    
    static void _check(String thing, final String name) {
        if (thing == null) {
            throw new NullPointerException(name + " can't be null ");
        }
        thing = thing.trim();
        if (thing.length() == 0) {
            throw new IllegalArgumentException(name + " can't be empty");
        }
    }
    
    public int hashCode() {
        return super.hashCode() + this._db.hashCode();
    }
    
    public boolean equals(final Object other) {
        if (other instanceof DBAddress) {
            final DBAddress a = (DBAddress)other;
            return a._port == this._port && a._db.equals(this._db) && a._host.equals(this._host);
        }
        return other instanceof ServerAddress && other.equals(this);
    }
    
    public DBAddress getSister(final String name) {
        try {
            return new DBAddress(this._host, this._port, name);
        }
        catch (UnknownHostException uh) {
            throw new MongoInternalException("shouldn't be possible", uh);
        }
    }
    
    public String getDBName() {
        return this._db;
    }
    
    public String toString() {
        return super.toString() + "/" + this._db;
    }
}
