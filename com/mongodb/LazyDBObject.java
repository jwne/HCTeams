package com.mongodb;

import org.bson.util.annotations.*;
import org.bson.*;
import org.bson.io.*;

@Immutable
public class LazyDBObject extends LazyBSONObject implements DBObject
{
    private boolean _partial;
    
    public LazyDBObject(final byte[] bytes, final LazyBSONCallback callback) {
        this(bytes, 0, callback);
    }
    
    public LazyDBObject(final byte[] bytes, final int offset, final LazyBSONCallback callback) {
        super(bytes, offset, callback);
        this._partial = false;
    }
    
    public LazyDBObject(final BSONByteBuffer buff, final LazyBSONCallback cbk) {
        super(buff, cbk);
        this._partial = false;
    }
    
    public LazyDBObject(final BSONByteBuffer buff, final int offset, final LazyBSONCallback cbk) {
        super(buff, offset, cbk);
        this._partial = false;
    }
    
    public void markAsPartialObject() {
        this._partial = true;
    }
    
    public boolean isPartialObject() {
        return this._partial;
    }
}
