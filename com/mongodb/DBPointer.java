package com.mongodb;

import org.bson.types.*;

@Deprecated
public class DBPointer extends DBRefBase
{
    private static final long serialVersionUID = -1977838613745447826L;
    final DBObject _parent;
    final String _fieldName;
    
    public DBPointer(final String ns, final ObjectId id) {
        this(null, null, null, ns, id);
    }
    
    DBPointer(final DBObject parent, final String fieldName, final DB db, final String ns, final ObjectId id) {
        super(db, ns, id);
        this._parent = parent;
        this._fieldName = fieldName;
    }
    
    public String toString() {
        return "{ \"$ref\" : \"" + this._ns + "\", \"$id\" : ObjectId(\"" + this._id + "\") }";
    }
    
    public ObjectId getId() {
        return (ObjectId)this._id;
    }
}
