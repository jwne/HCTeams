package org.bson;

import com.mongodb.*;
import java.util.*;
import org.bson.types.*;

public class LazyBSONCallback extends EmptyBSONCallback
{
    private Object _root;
    
    public void reset() {
        this._root = null;
    }
    
    public Object get() {
        return this._root;
    }
    
    public void gotBinary(final String name, final byte type, final byte[] data) {
        this.setRootObject(this.createObject(data, 0));
    }
    
    @Deprecated
    public void setRootObject(final Object root) {
        this._root = root;
    }
    
    public Object createObject(final byte[] bytes, final int offset) {
        return new LazyDBObject(bytes, offset, this);
    }
    
    public List createArray(final byte[] bytes, final int offset) {
        return new LazyBSONList(bytes, offset, this);
    }
    
    public Object createDBRef(final String ns, final ObjectId id) {
        return new BasicBSONObject("$ns", ns).append("$id", id);
    }
}
