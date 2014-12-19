package org.apache.commons.pool2.impl;

import java.lang.ref.*;

public class PooledSoftReference<T> extends DefaultPooledObject<T>
{
    private volatile SoftReference<T> reference;
    
    public PooledSoftReference(final SoftReference<T> reference) {
        super(null);
        this.reference = reference;
    }
    
    @Override
    public T getObject() {
        return this.reference.get();
    }
    
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append("Referenced Object: ");
        result.append(this.getObject().toString());
        result.append(", State: ");
        synchronized (this) {
            result.append(this.getState().toString());
        }
        return result.toString();
    }
    
    public synchronized SoftReference<T> getReference() {
        return this.reference;
    }
    
    public synchronized void setReference(final SoftReference<T> reference) {
        this.reference = reference;
    }
}
