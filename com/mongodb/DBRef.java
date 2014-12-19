package com.mongodb;

import org.bson.*;

public class DBRef extends DBRefBase
{
    private static final long serialVersionUID = -849581217713362618L;
    
    public DBRef(final String ns, final Object id) {
        this(null, ns, id);
    }
    
    public DBRef(final DB db, final BSONObject o) {
        super(db, o.get("$ref").toString(), o.get("$id"));
    }
    
    public DBRef(final DB db, final String ns, final Object id) {
        super(db, ns, id);
    }
    
    @Deprecated
    public static DBObject fetch(final DB db, final DBObject ref) {
        final String ns;
        final Object id;
        if ((ns = (String)ref.get("$ref")) != null && (id = ref.get("$id")) != null) {
            return db.getCollection(ns).findOne(new BasicDBObject("_id", id));
        }
        return null;
    }
}
