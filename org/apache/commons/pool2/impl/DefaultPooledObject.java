package org.apache.commons.pool2.impl;

import org.apache.commons.pool2.*;
import java.io.*;
import java.text.*;
import java.util.*;

public class DefaultPooledObject<T> implements PooledObject<T>
{
    private final T object;
    private PooledObjectState state;
    private final long createTime;
    private volatile long lastBorrowTime;
    private volatile long lastUseTime;
    private volatile long lastReturnTime;
    private volatile boolean logAbandoned;
    private volatile Exception borrowedBy;
    private volatile Exception usedBy;
    private volatile long borrowedCount;
    
    public DefaultPooledObject(final T object) {
        super();
        this.state = PooledObjectState.IDLE;
        this.createTime = System.currentTimeMillis();
        this.lastBorrowTime = this.createTime;
        this.lastUseTime = this.createTime;
        this.lastReturnTime = this.createTime;
        this.logAbandoned = false;
        this.borrowedBy = null;
        this.usedBy = null;
        this.borrowedCount = 0L;
        this.object = object;
    }
    
    @Override
    public T getObject() {
        return this.object;
    }
    
    @Override
    public long getCreateTime() {
        return this.createTime;
    }
    
    @Override
    public long getActiveTimeMillis() {
        final long rTime = this.lastReturnTime;
        final long bTime = this.lastBorrowTime;
        if (rTime > bTime) {
            return rTime - bTime;
        }
        return System.currentTimeMillis() - bTime;
    }
    
    @Override
    public long getIdleTimeMillis() {
        return System.currentTimeMillis() - this.lastReturnTime;
    }
    
    @Override
    public long getLastBorrowTime() {
        return this.lastBorrowTime;
    }
    
    @Override
    public long getLastReturnTime() {
        return this.lastReturnTime;
    }
    
    public long getBorrowedCount() {
        return this.borrowedCount;
    }
    
    @Override
    public long getLastUsedTime() {
        if (this.object instanceof TrackedUse) {
            return Math.max(((TrackedUse)this.object).getLastUsed(), this.lastUseTime);
        }
        return this.lastUseTime;
    }
    
    @Override
    public int compareTo(final PooledObject<T> other) {
        final long lastActiveDiff = this.getLastReturnTime() - other.getLastReturnTime();
        if (lastActiveDiff == 0L) {
            return System.identityHashCode(this) - System.identityHashCode(other);
        }
        return (int)Math.min(Math.max(lastActiveDiff, -2147483648L), 2147483647L);
    }
    
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append("Object: ");
        result.append(this.object.toString());
        result.append(", State: ");
        synchronized (this) {
            result.append(this.state.toString());
        }
        return result.toString();
    }
    
    @Override
    public synchronized boolean startEvictionTest() {
        if (this.state == PooledObjectState.IDLE) {
            this.state = PooledObjectState.EVICTION;
            return true;
        }
        return false;
    }
    
    @Override
    public synchronized boolean endEvictionTest(final Deque<PooledObject<T>> idleQueue) {
        if (this.state == PooledObjectState.EVICTION) {
            this.state = PooledObjectState.IDLE;
            return true;
        }
        if (this.state == PooledObjectState.EVICTION_RETURN_TO_HEAD) {
            this.state = PooledObjectState.IDLE;
            if (!idleQueue.offerFirst(this)) {}
        }
        return false;
    }
    
    @Override
    public synchronized boolean allocate() {
        if (this.state == PooledObjectState.IDLE) {
            this.state = PooledObjectState.ALLOCATED;
            this.lastBorrowTime = System.currentTimeMillis();
            this.lastUseTime = this.lastBorrowTime;
            ++this.borrowedCount;
            if (this.logAbandoned) {
                this.borrowedBy = new AbandonedObjectCreatedException();
            }
            return true;
        }
        if (this.state == PooledObjectState.EVICTION) {
            this.state = PooledObjectState.EVICTION_RETURN_TO_HEAD;
            return false;
        }
        return false;
    }
    
    @Override
    public synchronized boolean deallocate() {
        if (this.state == PooledObjectState.ALLOCATED || this.state == PooledObjectState.RETURNING) {
            this.state = PooledObjectState.IDLE;
            this.lastReturnTime = System.currentTimeMillis();
            if (this.borrowedBy != null) {
                this.borrowedBy = null;
            }
            return true;
        }
        return false;
    }
    
    @Override
    public synchronized void invalidate() {
        this.state = PooledObjectState.INVALID;
    }
    
    @Override
    public void use() {
        this.lastUseTime = System.currentTimeMillis();
        this.usedBy = new Exception("The last code to use this object was:");
    }
    
    @Override
    public void printStackTrace(final PrintWriter writer) {
        final Exception borrowedByCopy = this.borrowedBy;
        if (borrowedByCopy != null) {
            borrowedByCopy.printStackTrace(writer);
        }
        final Exception usedByCopy = this.usedBy;
        if (usedByCopy != null) {
            usedByCopy.printStackTrace(writer);
        }
    }
    
    @Override
    public synchronized PooledObjectState getState() {
        return this.state;
    }
    
    @Override
    public synchronized void markAbandoned() {
        this.state = PooledObjectState.ABANDONED;
    }
    
    @Override
    public synchronized void markReturning() {
        this.state = PooledObjectState.RETURNING;
    }
    
    @Override
    public void setLogAbandoned(final boolean logAbandoned) {
        this.logAbandoned = logAbandoned;
    }
    
    static class AbandonedObjectCreatedException extends Exception
    {
        private static final long serialVersionUID = 7398692158058772916L;
        private static final SimpleDateFormat format;
        private final long _createdTime;
        
        public AbandonedObjectCreatedException() {
            super();
            this._createdTime = System.currentTimeMillis();
        }
        
        @Override
        public String getMessage() {
            final String msg;
            synchronized (AbandonedObjectCreatedException.format) {
                msg = AbandonedObjectCreatedException.format.format(new Date(this._createdTime));
            }
            return msg;
        }
        
        static {
            format = new SimpleDateFormat("'Pooled object created' yyyy-MM-dd HH:mm:ss Z 'by the following code has not been returned to the pool:'");
        }
    }
}
