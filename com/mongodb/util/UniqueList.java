package com.mongodb.util;

import java.util.*;

@Deprecated
public class UniqueList<T> extends ArrayList<T>
{
    private static final long serialVersionUID = -4415279469780082174L;
    
    public boolean add(final T t) {
        return !this.contains(t) && super.add(t);
    }
    
    public boolean addAll(final Collection<? extends T> c) {
        boolean added = false;
        for (final T t : c) {
            added = (added || this.add(t));
        }
        return added;
    }
}
