package com.mongodb;

import java.util.logging.*;
import org.bson.*;
import java.util.*;
import org.bson.types.*;

public class LazyDBCallback extends LazyBSONCallback implements DBCallback
{
    final DBCollection _collection;
    final DB _db;
    private static final Logger log;
    
    public LazyDBCallback(final DBCollection collection) {
        super();
        this._collection = collection;
        this._db = ((this._collection == null) ? null : this._collection.getDB());
    }
    
    public Object createObject(final byte[] data, final int offset) {
        final LazyDBObject o = new LazyDBObject(data, offset, this);
        final Iterator it = o.keySet().iterator();
        if (it.hasNext() && it.next().equals("$ref") && o.containsField("$id")) {
            return new DBRef(this._db, o);
        }
        return o;
    }
    
    public List createArray(final byte[] data, final int offset) {
        return new LazyDBList(data, offset, this);
    }
    
    public Object createDBRef(final String ns, final ObjectId id) {
        return new DBRef(this._db, ns, id);
    }
    
    static {
        log = Logger.getLogger(LazyDBCallback.class.getName());
    }
}
