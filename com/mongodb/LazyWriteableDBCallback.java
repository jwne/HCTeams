package com.mongodb;

import org.bson.*;
import java.util.*;

@Deprecated
public class LazyWriteableDBCallback extends LazyDBCallback
{
    public LazyWriteableDBCallback(final DBCollection collection) {
        super(collection);
    }
    
    public Object createObject(final byte[] data, final int offset) {
        final LazyWriteableDBObject o = new LazyWriteableDBObject(data, offset, this);
        final Iterator it = o.keySet().iterator();
        if (it.hasNext() && it.next().equals("$ref") && o.containsField("$id")) {
            return new DBRef(this._db, o);
        }
        return o;
    }
}
