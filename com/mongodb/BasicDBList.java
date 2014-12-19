package com.mongodb;

import org.bson.types.*;
import com.mongodb.util.*;

public class BasicDBList extends BasicBSONList implements DBObject
{
    private static final long serialVersionUID = -4415279469780082174L;
    private boolean _isPartialObject;
    
    public String toString() {
        return JSON.serialize(this);
    }
    
    public boolean isPartialObject() {
        return this._isPartialObject;
    }
    
    public void markAsPartialObject() {
        this._isPartialObject = true;
    }
    
    public Object copy() {
        final BasicDBList newobj = new BasicDBList();
        for (int i = 0; i < this.size(); ++i) {
            Object val = this.get(i);
            if (val instanceof BasicDBObject) {
                val = ((BasicDBObject)val).copy();
            }
            else if (val instanceof BasicDBList) {
                val = ((BasicDBList)val).copy();
            }
            newobj.add(val);
        }
        return newobj;
    }
}
