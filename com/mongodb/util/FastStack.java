package com.mongodb.util;

import java.util.*;

@Deprecated
public class FastStack<T>
{
    private final List<T> _data;
    
    public FastStack() {
        super();
        this._data = new ArrayList<T>();
    }
    
    public void push(final T t) {
        this._data.add(t);
    }
    
    public T peek() {
        return this._data.get(this._data.size() - 1);
    }
    
    public T pop() {
        return this._data.remove(this._data.size() - 1);
    }
    
    public int size() {
        return this._data.size();
    }
    
    public void clear() {
        this._data.clear();
    }
    
    public T get(final int i) {
        return this._data.get(i);
    }
    
    public String toString() {
        return this._data.toString();
    }
}
