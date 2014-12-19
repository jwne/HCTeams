package com.mongodb.util;

import java.util.concurrent.*;
import java.util.*;

@Deprecated
public abstract class SimplePool<T>
{
    protected final String _name;
    protected final int _size;
    protected final List<T> _avail;
    protected final Set<T> _out;
    private final Semaphore _sem;
    private boolean _closed;
    
    public SimplePool(final String name, final int size) {
        super();
        this._avail = new ArrayList<T>();
        this._out = new HashSet<T>();
        this._name = name;
        this._size = size;
        this._sem = new Semaphore(size);
    }
    
    protected abstract T createNew();
    
    public void cleanup(final T t) {
    }
    
    protected int pick(final int recommended, final boolean couldCreate) {
        return recommended;
    }
    
    public void done(final T t) {
        synchronized (this) {
            if (this._closed) {
                this.cleanup(t);
                return;
            }
            this.assertConditions();
            if (!this._out.remove(t)) {
                throw new RuntimeException("trying to put something back in the pool wasn't checked out");
            }
            this._avail.add(t);
        }
        this._sem.release();
    }
    
    private void assertConditions() {
        assert this.getTotal() <= this.getMaxSize();
    }
    
    public void remove(final T t) {
        this.done(t);
    }
    
    public T get() throws InterruptedException {
        return this.get(-1L);
    }
    
    public T get(final long waitTime) throws InterruptedException {
        if (!this.permitAcquired(waitTime)) {
            return null;
        }
        synchronized (this) {
            this.assertConditions();
            final int toTake = this.pick(this._avail.size() - 1, this.getTotal() < this.getMaxSize());
            T t;
            if (toTake >= 0) {
                t = this._avail.remove(toTake);
            }
            else {
                t = this.createNewAndReleasePermitIfFailure();
            }
            this._out.add(t);
            return t;
        }
    }
    
    private T createNewAndReleasePermitIfFailure() {
        try {
            final T newMember = this.createNew();
            if (newMember == null) {
                throw new IllegalStateException("null pool members are not allowed");
            }
            return newMember;
        }
        catch (RuntimeException e) {
            this._sem.release();
            throw e;
        }
        catch (Error e2) {
            this._sem.release();
            throw e2;
        }
    }
    
    private boolean permitAcquired(final long waitTime) throws InterruptedException {
        if (waitTime > 0L) {
            return this._sem.tryAcquire(waitTime, TimeUnit.MILLISECONDS);
        }
        if (waitTime < 0L) {
            this._sem.acquire();
            return true;
        }
        return this._sem.tryAcquire();
    }
    
    protected synchronized void close() {
        this._closed = true;
        for (final T t : this._avail) {
            this.cleanup(t);
        }
        this._avail.clear();
        this._out.clear();
    }
    
    public String getName() {
        return this._name;
    }
    
    public synchronized int getTotal() {
        return this._avail.size() + this._out.size();
    }
    
    public synchronized int getInUse() {
        return this._out.size();
    }
    
    public synchronized int getAvailable() {
        return this._avail.size();
    }
    
    public int getMaxSize() {
        return this._size;
    }
    
    public synchronized String toString() {
        final StringBuilder buf = new StringBuilder();
        buf.append("pool: ").append(this._name).append(" maxToKeep: ").append(this._size).append(" avail ").append(this._avail.size()).append(" out ").append(this._out.size());
        return buf.toString();
    }
}
