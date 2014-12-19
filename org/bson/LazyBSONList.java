package org.bson;

import org.bson.io.*;
import java.util.*;

public class LazyBSONList extends LazyBSONObject implements List
{
    public LazyBSONList(final byte[] bytes, final LazyBSONCallback callback) {
        super(bytes, callback);
    }
    
    public LazyBSONList(final byte[] bytes, final int offset, final LazyBSONCallback callback) {
        super(bytes, offset, callback);
    }
    
    public LazyBSONList(final BSONByteBuffer buffer, final LazyBSONCallback callback) {
        super(buffer, callback);
    }
    
    public LazyBSONList(final BSONByteBuffer buffer, final int offset, final LazyBSONCallback callback) {
        super(buffer, offset, callback);
    }
    
    public boolean contains(final Object arg0) {
        return this.indexOf(arg0) > -1;
    }
    
    public boolean containsAll(final Collection arg0) {
        for (final Object obj : arg0) {
            if (!this.contains(obj)) {
                return false;
            }
        }
        return true;
    }
    
    public Object get(final int pos) {
        return this.get("" + pos);
    }
    
    public Iterator iterator() {
        return new LazyBSONListIterator();
    }
    
    public int indexOf(final Object arg0) {
        int pos = 0;
        for (final Object curr : this) {
            if (arg0.equals(curr)) {
                return pos;
            }
            ++pos;
        }
        return -1;
    }
    
    public int lastIndexOf(final Object arg0) {
        int pos = 0;
        int lastFound = -1;
        for (final Object curr : this) {
            if (arg0.equals(curr)) {
                lastFound = pos;
            }
            ++pos;
        }
        return lastFound;
    }
    
    public int size() {
        return this.getElements().size();
    }
    
    public ListIterator listIterator(final int arg0) {
        throw new UnsupportedOperationException("Not Supported");
    }
    
    public ListIterator listIterator() {
        throw new UnsupportedOperationException("Not Supported");
    }
    
    public boolean add(final Object arg0) {
        throw new UnsupportedOperationException("Read Only");
    }
    
    public void add(final int arg0, final Object arg1) {
        throw new UnsupportedOperationException("Read Only");
    }
    
    public boolean addAll(final Collection arg0) {
        throw new UnsupportedOperationException("Read Only");
    }
    
    public boolean addAll(final int arg0, final Collection arg1) {
        throw new UnsupportedOperationException("Read Only");
    }
    
    public void clear() {
        throw new UnsupportedOperationException("Read Only");
    }
    
    public boolean remove(final Object arg0) {
        throw new UnsupportedOperationException("Read Only");
    }
    
    public Object remove(final int arg0) {
        throw new UnsupportedOperationException("Read Only");
    }
    
    public boolean removeAll(final Collection arg0) {
        throw new UnsupportedOperationException("Read Only");
    }
    
    public boolean retainAll(final Collection arg0) {
        throw new UnsupportedOperationException("Read Only");
    }
    
    public Object set(final int arg0, final Object arg1) {
        throw new UnsupportedOperationException("Read Only");
    }
    
    public List subList(final int arg0, final int arg1) {
        throw new UnsupportedOperationException("Not Supported");
    }
    
    public Object[] toArray() {
        throw new UnsupportedOperationException("Not Supported");
    }
    
    public Object[] toArray(final Object[] arg0) {
        throw new UnsupportedOperationException("Not Supported");
    }
    
    public class LazyBSONListIterator implements Iterator
    {
        List<ElementRecord> elements;
        int pos;
        
        public LazyBSONListIterator() {
            super();
            this.pos = 0;
            this.elements = LazyBSONList.this.getElements();
        }
        
        public boolean hasNext() {
            return this.pos < this.elements.size();
        }
        
        public Object next() {
            return LazyBSONList.this.getElementValue(this.elements.get(this.pos++));
        }
        
        public void remove() {
            throw new UnsupportedOperationException("Read Only");
        }
    }
}
