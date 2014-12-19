package com.mongodb;

import org.bson.io.*;
import org.bson.*;
import java.util.*;

@Deprecated
public class LazyWriteableDBObject extends LazyDBObject
{
    private final HashMap<String, Object> writeable;
    
    public LazyWriteableDBObject(final byte[] bytes, final LazyBSONCallback callback) {
        this(bytes, 0, callback);
    }
    
    public LazyWriteableDBObject(final byte[] bytes, final int offset, final LazyBSONCallback callback) {
        super(bytes, offset, callback);
        this.writeable = new HashMap<String, Object>();
    }
    
    public LazyWriteableDBObject(final BSONByteBuffer buff, final LazyBSONCallback cbk) {
        super(buff, cbk);
        this.writeable = new HashMap<String, Object>();
    }
    
    public LazyWriteableDBObject(final BSONByteBuffer buff, final int offset, final LazyBSONCallback cbk) {
        super(buff, offset, cbk);
        this.writeable = new HashMap<String, Object>();
    }
    
    public Object put(final String key, final Object v) {
        return this.writeable.put(key, v);
    }
    
    public void putAll(final BSONObject o) {
        for (final String key : o.keySet()) {
            this.put(key, o.get(key));
        }
    }
    
    public void putAll(final Map m) {
        this.writeable.putAll(m);
    }
    
    public Object get(final String key) {
        final Object o = this.writeable.get(key);
        return (o != null) ? o : super.get(key);
    }
    
    public Object removeField(final String key) {
        final Object o = this.writeable.remove(key);
        return (o != null) ? o : super.removeField(key);
    }
    
    public boolean containsField(final String s) {
        final boolean has = this.writeable.containsKey(s);
        return has ? has : super.containsField(s);
    }
    
    public Set<String> keySet() {
        final Set<String> combined = new HashSet<String>();
        combined.addAll(this.writeable.keySet());
        combined.addAll(super.keySet());
        return combined;
    }
    
    public boolean isEmpty() {
        return this.writeable.isEmpty() || super.isEmpty();
    }
}
