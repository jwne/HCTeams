package com.mongodb.util;

import java.util.*;
import java.lang.ref.*;

@Deprecated
public class WeakBag<T> implements Iterable<T>
{
    private final List<MyRef> _refs;
    
    public WeakBag() {
        super();
        this._refs = new LinkedList<MyRef>();
    }
    
    public void add(final T t) {
        this._refs.add(new MyRef(t));
    }
    
    public boolean remove(final T t) {
        final Iterator<MyRef> i = this._refs.iterator();
        while (i.hasNext()) {
            final MyRef ref = i.next();
            if (ref == null) {
                continue;
            }
            final T me = ref.get();
            if (me == null) {
                i.remove();
            }
            else {
                if (me == t) {
                    i.remove();
                    return true;
                }
                continue;
            }
        }
        return false;
    }
    
    public boolean contains(final T t) {
        for (final MyRef ref : this._refs) {
            final T me = ref.get();
            if (me == t) {
                return true;
            }
        }
        return false;
    }
    
    public int size() {
        this.clean();
        return this._refs.size();
    }
    
    public void clear() {
        this._refs.clear();
    }
    
    public void clean() {
        final Iterator<MyRef> i = this._refs.iterator();
        while (i.hasNext()) {
            final MyRef ref = i.next();
            if (ref.get() == null) {
                i.remove();
            }
        }
    }
    
    public Iterator<T> iterator() {
        return this.getAll().iterator();
    }
    
    public List<T> getAll() {
        final List<T> l = new ArrayList<T>();
        final Iterator<MyRef> i = this._refs.iterator();
        while (i.hasNext()) {
            final MyRef ref = i.next();
            final T t = ref.get();
            if (t == null) {
                i.remove();
            }
            else {
                l.add(t);
            }
        }
        return l;
    }
    
    class MyRef extends WeakReference<T>
    {
        MyRef(final T t) {
            super(t);
        }
    }
}
