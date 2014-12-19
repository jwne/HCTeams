package org.bson.util;

import java.util.*;
import java.util.concurrent.*;

@Deprecated
public abstract class SimplePool<T>
{
    final int _max;
    private Queue<T> _stored;
    
    public SimplePool(final int max) {
        super();
        this._stored = new ConcurrentLinkedQueue<T>();
        this._max = max;
    }
    
    public SimplePool() {
        super();
        this._stored = new ConcurrentLinkedQueue<T>();
        this._max = 1000;
    }
    
    protected abstract T createNew();
    
    protected boolean ok(final T t) {
        return true;
    }
    
    public T get() {
        final T t = this._stored.poll();
        if (t != null) {
            return t;
        }
        return this.createNew();
    }
    
    public void done(final T t) {
        if (!this.ok(t)) {
            return;
        }
        if (this._stored.size() > this._max) {
            return;
        }
        this._stored.add(t);
    }
}
