package org.apache.commons.pool2.impl;

import java.lang.ref.*;
import org.apache.commons.pool2.*;
import java.util.*;

public class SoftReferenceObjectPool<T> extends BaseObjectPool<T>
{
    private final PooledObjectFactory<T> factory;
    private final ReferenceQueue<T> refQueue;
    private int numActive;
    private long destroyCount;
    private long createCount;
    private final LinkedBlockingDeque<PooledSoftReference<T>> idleReferences;
    private final ArrayList<PooledSoftReference<T>> allReferences;
    
    public SoftReferenceObjectPool(final PooledObjectFactory<T> factory) {
        super();
        this.refQueue = new ReferenceQueue<T>();
        this.numActive = 0;
        this.destroyCount = 0L;
        this.createCount = 0L;
        this.idleReferences = new LinkedBlockingDeque<PooledSoftReference<T>>();
        this.allReferences = new ArrayList<PooledSoftReference<T>>();
        this.factory = factory;
    }
    
    @Override
    public synchronized T borrowObject() throws Exception {
        this.assertOpen();
        T obj = null;
        boolean newlyCreated = false;
        PooledSoftReference<T> ref = null;
        while (null == obj) {
            if (this.idleReferences.isEmpty()) {
                if (null == this.factory) {
                    throw new NoSuchElementException();
                }
                newlyCreated = true;
                obj = this.factory.makeObject().getObject();
                ++this.createCount;
                ref = new PooledSoftReference<T>(new SoftReference<T>(obj));
                this.allReferences.add(ref);
            }
            else {
                ref = this.idleReferences.pollFirst();
                obj = ref.getObject();
                ref.getReference().clear();
                ref.setReference(new SoftReference<T>(obj));
            }
            if (null != this.factory && null != obj) {
                try {
                    this.factory.activateObject(ref);
                    if (!this.factory.validateObject(ref)) {
                        throw new Exception("ValidateObject failed");
                    }
                    continue;
                }
                catch (Throwable t) {
                    PoolUtils.checkRethrow(t);
                    try {
                        this.destroy(ref);
                    }
                    catch (Throwable t2) {
                        PoolUtils.checkRethrow(t2);
                    }
                    finally {
                        obj = null;
                    }
                    if (newlyCreated) {
                        throw new NoSuchElementException("Could not create a validated object, cause: " + t.getMessage());
                    }
                    continue;
                }
            }
        }
        ++this.numActive;
        ref.allocate();
        return obj;
    }
    
    @Override
    public synchronized void returnObject(final T obj) throws Exception {
        boolean success = !this.isClosed();
        final PooledSoftReference<T> ref = this.findReference(obj);
        if (ref == null) {
            throw new IllegalStateException("Returned object not currently part of this pool");
        }
        if (this.factory != null) {
            if (!this.factory.validateObject(ref)) {
                success = false;
            }
            else {
                try {
                    this.factory.passivateObject(ref);
                }
                catch (Exception e) {
                    success = false;
                }
            }
        }
        final boolean shouldDestroy = !success;
        --this.numActive;
        if (success) {
            ref.deallocate();
            this.idleReferences.add(ref);
        }
        this.notifyAll();
        if (shouldDestroy && this.factory != null) {
            try {
                this.destroy(ref);
            }
            catch (Exception ex) {}
        }
    }
    
    @Override
    public synchronized void invalidateObject(final T obj) throws Exception {
        final PooledSoftReference<T> ref = this.findReference(obj);
        if (ref == null) {
            throw new IllegalStateException("Object to invalidate is not currently part of this pool");
        }
        if (this.factory != null) {
            this.destroy(ref);
        }
        --this.numActive;
        this.notifyAll();
    }
    
    @Override
    public synchronized void addObject() throws Exception {
        this.assertOpen();
        if (this.factory == null) {
            throw new IllegalStateException("Cannot add objects without a factory.");
        }
        final T obj = this.factory.makeObject().getObject();
        ++this.createCount;
        final PooledSoftReference<T> ref = new PooledSoftReference<T>(new SoftReference<T>(obj, this.refQueue));
        this.allReferences.add(ref);
        boolean success = true;
        if (!this.factory.validateObject(ref)) {
            success = false;
        }
        else {
            this.factory.passivateObject(ref);
        }
        final boolean shouldDestroy = !success;
        if (success) {
            this.idleReferences.add(ref);
            this.notifyAll();
        }
        if (shouldDestroy) {
            try {
                this.destroy(ref);
            }
            catch (Exception ex) {}
        }
    }
    
    @Override
    public synchronized int getNumIdle() {
        this.pruneClearedReferences();
        return this.idleReferences.size();
    }
    
    @Override
    public synchronized int getNumActive() {
        return this.numActive;
    }
    
    @Override
    public synchronized void clear() {
        if (null != this.factory) {
            final Iterator<PooledSoftReference<T>> iter = this.idleReferences.iterator();
            while (iter.hasNext()) {
                try {
                    final PooledSoftReference<T> ref = iter.next();
                    if (null == ref.getObject()) {
                        continue;
                    }
                    this.factory.destroyObject(ref);
                }
                catch (Exception e) {}
            }
        }
        this.idleReferences.clear();
        this.pruneClearedReferences();
    }
    
    @Override
    public void close() {
        super.close();
        this.clear();
    }
    
    public synchronized PooledObjectFactory<T> getFactory() {
        return this.factory;
    }
    
    private void pruneClearedReferences() {
        this.removeClearedReferences(this.idleReferences.iterator());
        this.removeClearedReferences(this.allReferences.iterator());
        while (this.refQueue.poll() != null) {}
    }
    
    private PooledSoftReference<T> findReference(final T obj) {
        for (final PooledSoftReference<T> reference : this.allReferences) {
            if (reference.getObject() != null && reference.getObject().equals(obj)) {
                return reference;
            }
        }
        return null;
    }
    
    private void destroy(final PooledSoftReference<T> toDestroy) throws Exception {
        toDestroy.invalidate();
        this.idleReferences.remove(toDestroy);
        this.allReferences.remove(toDestroy);
        try {
            this.factory.destroyObject(toDestroy);
        }
        finally {
            ++this.destroyCount;
            toDestroy.getReference().clear();
        }
    }
    
    private void removeClearedReferences(final Iterator<PooledSoftReference<T>> iterator) {
        while (iterator.hasNext()) {
            final PooledSoftReference<T> ref = iterator.next();
            if (ref.getReference() == null || ref.getReference().isEnqueued()) {
                iterator.remove();
            }
        }
    }
}
