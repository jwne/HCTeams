package org.bson;

import com.mongodb.*;
import org.bson.io.*;
import com.mongodb.util.*;

@Deprecated
public class LazyDBList extends LazyBSONList implements DBObject
{
    private static final long serialVersionUID = -4415279469780082174L;
    private boolean _isPartialObject;
    
    public LazyDBList(final byte[] data, final LazyBSONCallback callback) {
        super(data, callback);
    }
    
    public LazyDBList(final byte[] data, final int offset, final LazyBSONCallback callback) {
        super(data, offset, callback);
    }
    
    public LazyDBList(final BSONByteBuffer buffer, final LazyBSONCallback callback) {
        super(buffer, callback);
    }
    
    public LazyDBList(final BSONByteBuffer buffer, final int offset, final LazyBSONCallback callback) {
        super(buffer, offset, callback);
    }
    
    public String toString() {
        return JSON.serialize(this);
    }
    
    public boolean isPartialObject() {
        return this._isPartialObject;
    }
    
    public void markAsPartialObject() {
        this._isPartialObject = true;
    }
}
