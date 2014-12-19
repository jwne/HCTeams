package com.mongodb;

import java.util.*;

public class BasicDBObjectBuilder
{
    private final LinkedList<DBObject> _stack;
    
    public BasicDBObjectBuilder() {
        super();
        (this._stack = new LinkedList<DBObject>()).add(new BasicDBObject());
    }
    
    public static BasicDBObjectBuilder start() {
        return new BasicDBObjectBuilder();
    }
    
    public static BasicDBObjectBuilder start(final String key, final Object val) {
        return new BasicDBObjectBuilder().add(key, val);
    }
    
    public static BasicDBObjectBuilder start(final Map map) {
        final BasicDBObjectBuilder b = new BasicDBObjectBuilder();
        for (final Map.Entry entry : map.entrySet()) {
            b.add(entry.getKey().toString(), entry.getValue());
        }
        return b;
    }
    
    public BasicDBObjectBuilder append(final String key, final Object val) {
        this._cur().put(key, val);
        return this;
    }
    
    public BasicDBObjectBuilder add(final String key, final Object val) {
        return this.append(key, val);
    }
    
    public BasicDBObjectBuilder push(final String key) {
        final BasicDBObject o = new BasicDBObject();
        this._cur().put(key, o);
        this._stack.addLast(o);
        return this;
    }
    
    public BasicDBObjectBuilder pop() {
        if (this._stack.size() <= 1) {
            throw new IllegalArgumentException("can't pop last element");
        }
        this._stack.removeLast();
        return this;
    }
    
    public DBObject get() {
        return this._stack.getFirst();
    }
    
    public boolean isEmpty() {
        return this._stack.getFirst().size() == 0;
    }
    
    private DBObject _cur() {
        return this._stack.getLast();
    }
}
