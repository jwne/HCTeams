package com.mongodb;

import java.util.concurrent.*;
import java.util.*;

class ConcurrentPool<T>
{
    private final int maxSize;
    private final ItemFactory<T> itemFactory;
    private final Deque<T> available;
    private final Semaphore permits;
    private volatile boolean closed;
    
    public ConcurrentPool(final int maxSize, final ItemFactory<T> itemFactory) {
        super();
        this.available = new ConcurrentLinkedDeque<T>();
        this.maxSize = maxSize;
        this.itemFactory = itemFactory;
        this.permits = new Semaphore(maxSize, true);
    }
    
    public void release(final T t) {
        this.release(t, false);
    }
    
    public void release(final T t, final boolean prune) {
        if (t == null) {
            throw new IllegalArgumentException("Can not return a null item to the pool");
        }
        if (this.closed) {
            this.close(t);
            return;
        }
        if (prune) {
            this.close(t);
        }
        else {
            this.available.addLast(t);
        }
        this.releasePermit();
    }
    
    public T get() {
        return this.get(-1L, TimeUnit.MILLISECONDS);
    }
    
    public T get(final long timeout, final TimeUnit timeUnit) {
        if (this.closed) {
            throw new IllegalStateException("The pool is closed");
        }
        if (!this.acquirePermit(timeout, timeUnit)) {
            throw new MongoTimeoutException(String.format("Timeout waiting for a pooled item after %d %s", timeout, timeUnit));
        }
        T t = this.available.pollLast();
        if (t == null) {
            t = this.createNewAndReleasePermitIfFailure();
        }
        return t;
    }
    
    public void prune() {
        for (int currentAvailableCount = this.getAvailableCount(), numAttempts = 0; numAttempts < currentAvailableCount; ++numAttempts) {
            if (!this.acquirePermit(10L, TimeUnit.MILLISECONDS)) {
                break;
            }
            final T cur = this.available.pollFirst();
            if (cur == null) {
                this.releasePermit();
                break;
            }
            this.release(cur, this.itemFactory.shouldPrune(cur));
        }
    }
    
    public void ensureMinSize(final int minSize) {
        while (this.getCount() < minSize && this.acquirePermit(10L, TimeUnit.MILLISECONDS)) {
            this.release(this.createNewAndReleasePermitIfFailure());
        }
    }
    
    private T createNewAndReleasePermitIfFailure() {
        try {
            final T newMember = this.itemFactory.create();
            if (newMember == null) {
                throw new MongoInternalException("The factory for the pool created a null item");
            }
            return newMember;
        }
        catch (RuntimeException e) {
            this.permits.release();
            throw e;
        }
    }
    
    protected boolean acquirePermit(final long timeout, final TimeUnit timeUnit) {
        try {
            if (this.closed) {
                return false;
            }
            if (timeout >= 0L) {
                return this.permits.tryAcquire(timeout, timeUnit);
            }
            this.permits.acquire();
            return true;
        }
        catch (InterruptedException e) {
            throw new MongoInterruptedException("Interrupted acquiring a permit to retrieve an item from the pool ", e);
        }
    }
    
    protected void releasePermit() {
        this.permits.release();
    }
    
    public void close() {
        this.closed = true;
        final Iterator<T> iter = this.available.iterator();
        while (iter.hasNext()) {
            final T t = iter.next();
            this.close(t);
            iter.remove();
        }
    }
    
    public int getMaxSize() {
        return this.maxSize;
    }
    
    public int getInUseCount() {
        return this.maxSize - this.permits.availablePermits();
    }
    
    public int getAvailableCount() {
        return this.available.size();
    }
    
    public int getCount() {
        return this.getInUseCount() + this.getAvailableCount();
    }
    
    public String toString() {
        final StringBuilder buf = new StringBuilder();
        buf.append("pool: ").append(" maxSize: ").append(this.maxSize).append(" availableCount ").append(this.getAvailableCount()).append(" inUseCount ").append(this.getInUseCount());
        return buf.toString();
    }
    
    private void close(final T t) {
        try {
            this.itemFactory.close(t);
        }
        catch (RuntimeException ex) {}
    }
    
    interface ItemFactory<T>
    {
        T create();
        
        void close(T p0);
        
        boolean shouldPrune(T p0);
    }
}
