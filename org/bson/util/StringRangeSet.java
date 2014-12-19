package org.bson.util;

import java.util.*;

@Deprecated
public class StringRangeSet implements Set<String>
{
    private final int size;
    private static final int NUMSTR_LEN = 100;
    private static final String[] NUMSTRS;
    
    public StringRangeSet(final int size) {
        super();
        this.size = size;
    }
    
    public int size() {
        return this.size;
    }
    
    public Iterator<String> iterator() {
        return new Iterator<String>() {
            int index = 0;
            
            public boolean hasNext() {
                return this.index < StringRangeSet.this.size;
            }
            
            public String next() {
                if (this.index < 100) {
                    return StringRangeSet.NUMSTRS[this.index++];
                }
                return String.valueOf(this.index++);
            }
            
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
    
    public boolean add(final String e) {
        throw new UnsupportedOperationException();
    }
    
    public boolean addAll(final Collection<? extends String> c) {
        throw new UnsupportedOperationException();
    }
    
    public void clear() {
        throw new UnsupportedOperationException();
    }
    
    public boolean contains(final Object o) {
        final int t = Integer.parseInt(String.valueOf(o));
        return t >= 0 && t < this.size;
    }
    
    public boolean containsAll(final Collection<?> c) {
        for (final Object o : c) {
            if (!this.contains(o)) {
                return false;
            }
        }
        return true;
    }
    
    public boolean isEmpty() {
        return false;
    }
    
    public boolean remove(final Object o) {
        throw new UnsupportedOperationException();
    }
    
    public boolean removeAll(final Collection<?> c) {
        throw new UnsupportedOperationException();
    }
    
    public boolean retainAll(final Collection<?> c) {
        throw new UnsupportedOperationException();
    }
    
    public Object[] toArray() {
        final String[] array = new String[this.size()];
        for (int i = 0; i < this.size; ++i) {
            if (i < 100) {
                array[i] = StringRangeSet.NUMSTRS[i];
            }
            else {
                array[i] = String.valueOf(i);
            }
        }
        return array;
    }
    
    public <T> T[] toArray(final T[] a) {
        throw new UnsupportedOperationException();
    }
    
    static {
        NUMSTRS = new String[100];
        for (int i = 0; i < 100; ++i) {
            StringRangeSet.NUMSTRS[i] = String.valueOf(i);
        }
    }
}
