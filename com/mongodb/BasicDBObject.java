package com.mongodb;

import org.bson.*;
import com.mongodb.util.*;
import java.util.*;

public class BasicDBObject extends BasicBSONObject implements DBObject
{
    private static final long serialVersionUID = -4415279469780082174L;
    private boolean _isPartialObject;
    
    public BasicDBObject() {
        super();
    }
    
    public BasicDBObject(final int size) {
        super(size);
    }
    
    public BasicDBObject(final String key, final Object value) {
        super(key, value);
    }
    
    public BasicDBObject(final Map map) {
        super(map);
    }
    
    public boolean isPartialObject() {
        return this._isPartialObject;
    }
    
    public void markAsPartialObject() {
        this._isPartialObject = true;
    }
    
    public String toString() {
        return JSON.serialize(this);
    }
    
    public BasicDBObject append(final String key, final Object val) {
        this.put(key, val);
        return this;
    }
    
    public Object copy() {
        final BasicDBObject newobj = new BasicDBObject(this.toMap());
        for (final String field : ((HashMap<String, V>)this).keySet()) {
            final Object val = this.get(field);
            if (val instanceof BasicDBObject) {
                newobj.put(field, ((BasicDBObject)val).copy());
            }
            else {
                if (!(val instanceof BasicDBList)) {
                    continue;
                }
                newobj.put(field, ((BasicDBList)val).copy());
            }
        }
        return newobj;
    }
}
