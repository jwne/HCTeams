package com.mongodb;

import org.bson.types.*;
import org.bson.*;
import java.util.*;
import java.util.logging.*;

public class DefaultDBCallback extends BasicBSONCallback implements DBCallback
{
    public static DBCallbackFactory FACTORY;
    private LinkedList<String> _nameStack;
    final DBCollection _collection;
    final DB _db;
    static final Logger LOGGER;
    
    public DefaultDBCallback(final DBCollection coll) {
        super();
        this._collection = coll;
        this._db = ((this._collection == null) ? null : this._collection.getDB());
    }
    
    public void gotDBRef(final String name, final String ns, final ObjectId id) {
        if (id.equals(Bytes.COLLECTION_REF_ID)) {
            this.cur().put(name, this._collection);
        }
        else {
            this.cur().put(name, new DBRef(this._db, ns, id));
        }
    }
    
    @Deprecated
    public void objectStart(final boolean array, final String name) {
        this._nameStack.addLast(name);
        super.objectStart(array, name);
    }
    
    public Object objectDone() {
        final BSONObject o = (BSONObject)super.objectDone();
        String lastName = null;
        if (this._nameStack.size() > 0) {
            lastName = this._nameStack.removeLast();
        }
        if (!(o instanceof List) && lastName != null && o.containsField("$ref") && o.containsField("$id")) {
            return this.cur().put(lastName, new DBRef(this._db, o));
        }
        return o;
    }
    
    public BSONObject create() {
        return this._create(null);
    }
    
    public BSONObject create(final boolean array, final List<String> path) {
        if (array) {
            return new BasicDBList();
        }
        return this._create(path);
    }
    
    private DBObject _create(final List<String> path) {
        Class c = null;
        if (this._collection != null && this._collection._objectClass != null) {
            if (path == null || path.size() == 0) {
                c = this._collection._objectClass;
            }
            else {
                final StringBuilder buf = new StringBuilder();
                for (int i = 0; i < path.size(); ++i) {
                    if (i > 0) {
                        buf.append(".");
                    }
                    buf.append(path.get(i));
                }
                c = this._collection.getInternalClass(buf.toString());
            }
        }
        if (c != null) {
            try {
                return c.newInstance();
            }
            catch (InstantiationException ie) {
                DefaultDBCallback.LOGGER.log(Level.FINE, "can't create a: " + c, ie);
                throw new MongoInternalException("can't instantiate a : " + c, ie);
            }
            catch (IllegalAccessException iae) {
                DefaultDBCallback.LOGGER.log(Level.FINE, "can't create a: " + c, iae);
                throw new MongoInternalException("can't instantiate a : " + c, iae);
            }
        }
        return new BasicDBObject();
    }
    
    DBObject dbget() {
        return (DBObject)this.get();
    }
    
    public void reset() {
        this._nameStack = new LinkedList<String>();
        super.reset();
    }
    
    static {
        DefaultDBCallback.FACTORY = new DefaultFactory();
        LOGGER = Logger.getLogger("com.mongo.DECODING");
    }
    
    static class DefaultFactory implements DBCallbackFactory
    {
        public DBCallback create(final DBCollection collection) {
            return new DefaultDBCallback(collection);
        }
    }
}
