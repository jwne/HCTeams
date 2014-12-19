package com.mongodb;

import java.io.*;

@Deprecated
public class DBRefBase implements Serializable
{
    private static final long serialVersionUID = 3031885741395465814L;
    final Object _id;
    final String _ns;
    final transient DB _db;
    private boolean _loadedPointedTo;
    private DBObject _pointedTo;
    
    public DBRefBase(final String ns, final Object id) {
        super();
        this._loadedPointedTo = false;
        this._db = null;
        this._ns = ns.intern();
        this._id = id;
    }
    
    public DBRefBase(final DB db, final String ns, final Object id) {
        super();
        this._loadedPointedTo = false;
        this._db = db;
        this._ns = ns.intern();
        this._id = id;
    }
    
    protected DBRefBase() {
        super();
        this._loadedPointedTo = false;
        this._id = null;
        this._ns = null;
        this._db = null;
    }
    
    @Deprecated
    public DBObject fetch() throws MongoException {
        if (this._loadedPointedTo) {
            return this._pointedTo;
        }
        if (this._db == null) {
            throw new MongoInternalException("no db");
        }
        final DBCollection coll = this._db.getCollectionFromString(this._ns);
        this._pointedTo = coll.findOne(this._id);
        this._loadedPointedTo = true;
        return this._pointedTo;
    }
    
    public String toString() {
        return "{ \"$ref\" : \"" + this._ns + "\", \"$id\" : \"" + this._id + "\" }";
    }
    
    public Object getId() {
        return this._id;
    }
    
    public String getCollectionName() {
        return this._ns;
    }
    
    @Deprecated
    public String getRef() {
        return this._ns;
    }
    
    @Deprecated
    public DB getDB() {
        return this._db;
    }
    
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final DBRefBase dbRefBase = (DBRefBase)o;
        Label_0062: {
            if (this._id != null) {
                if (this._id.equals(dbRefBase._id)) {
                    break Label_0062;
                }
            }
            else if (dbRefBase._id == null) {
                break Label_0062;
            }
            return false;
        }
        if (this._ns != null) {
            if (this._ns.equals(dbRefBase._ns)) {
                return true;
            }
        }
        else if (dbRefBase._ns == null) {
            return true;
        }
        return false;
    }
    
    public int hashCode() {
        int result = (this._id != null) ? this._id.hashCode() : 0;
        result = 31 * result + ((this._ns != null) ? this._ns.hashCode() : 0);
        return result;
    }
}
