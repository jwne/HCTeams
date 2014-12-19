package org.apache.commons.pool2;

import java.util.concurrent.locks.*;
import java.util.*;

public final class PoolUtils
{
    public static void checkRethrow(final Throwable t) {
        if (t instanceof ThreadDeath) {
            throw (ThreadDeath)t;
        }
        if (t instanceof VirtualMachineError) {
            throw (VirtualMachineError)t;
        }
    }
    
    public static <T> TimerTask checkMinIdle(final ObjectPool<T> pool, final int minIdle, final long period) throws IllegalArgumentException {
        if (pool == null) {
            throw new IllegalArgumentException("keyedPool must not be null.");
        }
        if (minIdle < 0) {
            throw new IllegalArgumentException("minIdle must be non-negative.");
        }
        final TimerTask task = new ObjectPoolMinIdleTimerTask<Object>(pool, minIdle);
        getMinIdleTimer().schedule(task, 0L, period);
        return task;
    }
    
    public static <K, V> TimerTask checkMinIdle(final KeyedObjectPool<K, V> keyedPool, final K key, final int minIdle, final long period) throws IllegalArgumentException {
        if (keyedPool == null) {
            throw new IllegalArgumentException("keyedPool must not be null.");
        }
        if (key == null) {
            throw new IllegalArgumentException("key must not be null.");
        }
        if (minIdle < 0) {
            throw new IllegalArgumentException("minIdle must be non-negative.");
        }
        final TimerTask task = new KeyedObjectPoolMinIdleTimerTask<Object, Object>(keyedPool, key, minIdle);
        getMinIdleTimer().schedule(task, 0L, period);
        return task;
    }
    
    public static <K, V> Map<K, TimerTask> checkMinIdle(final KeyedObjectPool<K, V> keyedPool, final Collection<K> keys, final int minIdle, final long period) throws IllegalArgumentException {
        if (keys == null) {
            throw new IllegalArgumentException("keys must not be null.");
        }
        final Map<K, TimerTask> tasks = new HashMap<K, TimerTask>(keys.size());
        for (final K key : keys) {
            final TimerTask task = checkMinIdle(keyedPool, key, minIdle, period);
            tasks.put(key, task);
        }
        return tasks;
    }
    
    public static <T> void prefill(final ObjectPool<T> pool, final int count) throws Exception, IllegalArgumentException {
        if (pool == null) {
            throw new IllegalArgumentException("pool must not be null.");
        }
        for (int i = 0; i < count; ++i) {
            pool.addObject();
        }
    }
    
    public static <K, V> void prefill(final KeyedObjectPool<K, V> keyedPool, final K key, final int count) throws Exception, IllegalArgumentException {
        if (keyedPool == null) {
            throw new IllegalArgumentException("keyedPool must not be null.");
        }
        if (key == null) {
            throw new IllegalArgumentException("key must not be null.");
        }
        for (int i = 0; i < count; ++i) {
            keyedPool.addObject(key);
        }
    }
    
    public static <K, V> void prefill(final KeyedObjectPool<K, V> keyedPool, final Collection<K> keys, final int count) throws Exception, IllegalArgumentException {
        if (keys == null) {
            throw new IllegalArgumentException("keys must not be null.");
        }
        final Iterator<K> iter = keys.iterator();
        while (iter.hasNext()) {
            prefill(keyedPool, iter.next(), count);
        }
    }
    
    public static <T> ObjectPool<T> synchronizedPool(final ObjectPool<T> pool) {
        if (pool == null) {
            throw new IllegalArgumentException("pool must not be null.");
        }
        return new SynchronizedObjectPool<T>(pool);
    }
    
    public static <K, V> KeyedObjectPool<K, V> synchronizedPool(final KeyedObjectPool<K, V> keyedPool) {
        return new SynchronizedKeyedObjectPool<K, V>(keyedPool);
    }
    
    public static <T> PooledObjectFactory<T> synchronizedPooledFactory(final PooledObjectFactory<T> factory) {
        return new SynchronizedPooledObjectFactory<T>(factory);
    }
    
    public static <K, V> KeyedPooledObjectFactory<K, V> synchronizedKeyedPooledFactory(final KeyedPooledObjectFactory<K, V> keyedFactory) {
        return new SynchronizedKeyedPooledObjectFactory<K, V>(keyedFactory);
    }
    
    public static <T> ObjectPool<T> erodingPool(final ObjectPool<T> pool) {
        return erodingPool(pool, 1.0f);
    }
    
    public static <T> ObjectPool<T> erodingPool(final ObjectPool<T> pool, final float factor) {
        if (pool == null) {
            throw new IllegalArgumentException("pool must not be null.");
        }
        if (factor <= 0.0f) {
            throw new IllegalArgumentException("factor must be positive.");
        }
        return new ErodingObjectPool<T>(pool, factor);
    }
    
    public static <K, V> KeyedObjectPool<K, V> erodingPool(final KeyedObjectPool<K, V> keyedPool) {
        return erodingPool(keyedPool, 1.0f);
    }
    
    public static <K, V> KeyedObjectPool<K, V> erodingPool(final KeyedObjectPool<K, V> keyedPool, final float factor) {
        return erodingPool(keyedPool, factor, false);
    }
    
    public static <K, V> KeyedObjectPool<K, V> erodingPool(final KeyedObjectPool<K, V> keyedPool, final float factor, final boolean perKey) {
        if (keyedPool == null) {
            throw new IllegalArgumentException("keyedPool must not be null.");
        }
        if (factor <= 0.0f) {
            throw new IllegalArgumentException("factor must be positive.");
        }
        if (perKey) {
            return new ErodingPerKeyKeyedObjectPool<K, V>(keyedPool, factor);
        }
        return new ErodingKeyedObjectPool<K, V>(keyedPool, factor);
    }
    
    private static Timer getMinIdleTimer() {
        return TimerHolder.MIN_IDLE_TIMER;
    }
    
    static class TimerHolder
    {
        static final Timer MIN_IDLE_TIMER;
        
        static {
            MIN_IDLE_TIMER = new Timer(true);
        }
    }
    
    private static class ObjectPoolMinIdleTimerTask<T> extends TimerTask
    {
        private final int minIdle;
        private final ObjectPool<T> pool;
        
        ObjectPoolMinIdleTimerTask(final ObjectPool<T> pool, final int minIdle) throws IllegalArgumentException {
            super();
            if (pool == null) {
                throw new IllegalArgumentException("pool must not be null.");
            }
            this.pool = pool;
            this.minIdle = minIdle;
        }
        
        @Override
        public void run() {
            boolean success = false;
            try {
                if (this.pool.getNumIdle() < this.minIdle) {
                    this.pool.addObject();
                }
                success = true;
            }
            catch (Exception e) {
                this.cancel();
            }
            finally {
                if (!success) {
                    this.cancel();
                }
            }
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("ObjectPoolMinIdleTimerTask");
            sb.append("{minIdle=").append(this.minIdle);
            sb.append(", pool=").append(this.pool);
            sb.append('}');
            return sb.toString();
        }
    }
    
    private static class KeyedObjectPoolMinIdleTimerTask<K, V> extends TimerTask
    {
        private final int minIdle;
        private final K key;
        private final KeyedObjectPool<K, V> keyedPool;
        
        KeyedObjectPoolMinIdleTimerTask(final KeyedObjectPool<K, V> keyedPool, final K key, final int minIdle) throws IllegalArgumentException {
            super();
            if (keyedPool == null) {
                throw new IllegalArgumentException("keyedPool must not be null.");
            }
            this.keyedPool = keyedPool;
            this.key = key;
            this.minIdle = minIdle;
        }
        
        @Override
        public void run() {
            boolean success = false;
            try {
                if (this.keyedPool.getNumIdle(this.key) < this.minIdle) {
                    this.keyedPool.addObject(this.key);
                }
                success = true;
            }
            catch (Exception e) {
                this.cancel();
            }
            finally {
                if (!success) {
                    this.cancel();
                }
            }
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("KeyedObjectPoolMinIdleTimerTask");
            sb.append("{minIdle=").append(this.minIdle);
            sb.append(", key=").append(this.key);
            sb.append(", keyedPool=").append(this.keyedPool);
            sb.append('}');
            return sb.toString();
        }
    }
    
    private static class SynchronizedObjectPool<T> implements ObjectPool<T>
    {
        private final ReentrantReadWriteLock readWriteLock;
        private final ObjectPool<T> pool;
        
        SynchronizedObjectPool(final ObjectPool<T> pool) throws IllegalArgumentException {
            super();
            this.readWriteLock = new ReentrantReadWriteLock();
            if (pool == null) {
                throw new IllegalArgumentException("pool must not be null.");
            }
            this.pool = pool;
        }
        
        @Override
        public T borrowObject() throws Exception, NoSuchElementException, IllegalStateException {
            final ReentrantReadWriteLock.WriteLock writeLock = this.readWriteLock.writeLock();
            writeLock.lock();
            try {
                return this.pool.borrowObject();
            }
            finally {
                writeLock.unlock();
            }
        }
        
        @Override
        public void returnObject(final T obj) {
            final ReentrantReadWriteLock.WriteLock writeLock = this.readWriteLock.writeLock();
            writeLock.lock();
            try {
                this.pool.returnObject(obj);
            }
            catch (Exception e) {}
            finally {
                writeLock.unlock();
            }
        }
        
        @Override
        public void invalidateObject(final T obj) {
            final ReentrantReadWriteLock.WriteLock writeLock = this.readWriteLock.writeLock();
            writeLock.lock();
            try {
                this.pool.invalidateObject(obj);
            }
            catch (Exception e) {}
            finally {
                writeLock.unlock();
            }
        }
        
        @Override
        public void addObject() throws Exception, IllegalStateException, UnsupportedOperationException {
            final ReentrantReadWriteLock.WriteLock writeLock = this.readWriteLock.writeLock();
            writeLock.lock();
            try {
                this.pool.addObject();
            }
            finally {
                writeLock.unlock();
            }
        }
        
        @Override
        public int getNumIdle() {
            final ReentrantReadWriteLock.ReadLock readLock = this.readWriteLock.readLock();
            readLock.lock();
            try {
                return this.pool.getNumIdle();
            }
            finally {
                readLock.unlock();
            }
        }
        
        @Override
        public int getNumActive() {
            final ReentrantReadWriteLock.ReadLock readLock = this.readWriteLock.readLock();
            readLock.lock();
            try {
                return this.pool.getNumActive();
            }
            finally {
                readLock.unlock();
            }
        }
        
        @Override
        public void clear() throws Exception, UnsupportedOperationException {
            final ReentrantReadWriteLock.WriteLock writeLock = this.readWriteLock.writeLock();
            writeLock.lock();
            try {
                this.pool.clear();
            }
            finally {
                writeLock.unlock();
            }
        }
        
        @Override
        public void close() {
            final ReentrantReadWriteLock.WriteLock writeLock = this.readWriteLock.writeLock();
            writeLock.lock();
            try {
                this.pool.close();
            }
            catch (Exception e) {}
            finally {
                writeLock.unlock();
            }
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("SynchronizedObjectPool");
            sb.append("{pool=").append(this.pool);
            sb.append('}');
            return sb.toString();
        }
    }
    
    private static class SynchronizedKeyedObjectPool<K, V> implements KeyedObjectPool<K, V>
    {
        private final ReentrantReadWriteLock readWriteLock;
        private final KeyedObjectPool<K, V> keyedPool;
        
        SynchronizedKeyedObjectPool(final KeyedObjectPool<K, V> keyedPool) throws IllegalArgumentException {
            super();
            this.readWriteLock = new ReentrantReadWriteLock();
            if (keyedPool == null) {
                throw new IllegalArgumentException("keyedPool must not be null.");
            }
            this.keyedPool = keyedPool;
        }
        
        @Override
        public V borrowObject(final K key) throws Exception, NoSuchElementException, IllegalStateException {
            final ReentrantReadWriteLock.WriteLock writeLock = this.readWriteLock.writeLock();
            writeLock.lock();
            try {
                return this.keyedPool.borrowObject(key);
            }
            finally {
                writeLock.unlock();
            }
        }
        
        @Override
        public void returnObject(final K key, final V obj) {
            final ReentrantReadWriteLock.WriteLock writeLock = this.readWriteLock.writeLock();
            writeLock.lock();
            try {
                this.keyedPool.returnObject(key, obj);
            }
            catch (Exception e) {}
            finally {
                writeLock.unlock();
            }
        }
        
        @Override
        public void invalidateObject(final K key, final V obj) {
            final ReentrantReadWriteLock.WriteLock writeLock = this.readWriteLock.writeLock();
            writeLock.lock();
            try {
                this.keyedPool.invalidateObject(key, obj);
            }
            catch (Exception e) {}
            finally {
                writeLock.unlock();
            }
        }
        
        @Override
        public void addObject(final K key) throws Exception, IllegalStateException, UnsupportedOperationException {
            final ReentrantReadWriteLock.WriteLock writeLock = this.readWriteLock.writeLock();
            writeLock.lock();
            try {
                this.keyedPool.addObject(key);
            }
            finally {
                writeLock.unlock();
            }
        }
        
        @Override
        public int getNumIdle(final K key) {
            final ReentrantReadWriteLock.ReadLock readLock = this.readWriteLock.readLock();
            readLock.lock();
            try {
                return this.keyedPool.getNumIdle(key);
            }
            finally {
                readLock.unlock();
            }
        }
        
        @Override
        public int getNumActive(final K key) {
            final ReentrantReadWriteLock.ReadLock readLock = this.readWriteLock.readLock();
            readLock.lock();
            try {
                return this.keyedPool.getNumActive(key);
            }
            finally {
                readLock.unlock();
            }
        }
        
        @Override
        public int getNumIdle() {
            final ReentrantReadWriteLock.ReadLock readLock = this.readWriteLock.readLock();
            readLock.lock();
            try {
                return this.keyedPool.getNumIdle();
            }
            finally {
                readLock.unlock();
            }
        }
        
        @Override
        public int getNumActive() {
            final ReentrantReadWriteLock.ReadLock readLock = this.readWriteLock.readLock();
            readLock.lock();
            try {
                return this.keyedPool.getNumActive();
            }
            finally {
                readLock.unlock();
            }
        }
        
        @Override
        public void clear() throws Exception, UnsupportedOperationException {
            final ReentrantReadWriteLock.WriteLock writeLock = this.readWriteLock.writeLock();
            writeLock.lock();
            try {
                this.keyedPool.clear();
            }
            finally {
                writeLock.unlock();
            }
        }
        
        @Override
        public void clear(final K key) throws Exception, UnsupportedOperationException {
            final ReentrantReadWriteLock.WriteLock writeLock = this.readWriteLock.writeLock();
            writeLock.lock();
            try {
                this.keyedPool.clear(key);
            }
            finally {
                writeLock.unlock();
            }
        }
        
        @Override
        public void close() {
            final ReentrantReadWriteLock.WriteLock writeLock = this.readWriteLock.writeLock();
            writeLock.lock();
            try {
                this.keyedPool.close();
            }
            catch (Exception e) {}
            finally {
                writeLock.unlock();
            }
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("SynchronizedKeyedObjectPool");
            sb.append("{keyedPool=").append(this.keyedPool);
            sb.append('}');
            return sb.toString();
        }
    }
    
    private static class SynchronizedPooledObjectFactory<T> implements PooledObjectFactory<T>
    {
        private final ReentrantReadWriteLock.WriteLock writeLock;
        private final PooledObjectFactory<T> factory;
        
        SynchronizedPooledObjectFactory(final PooledObjectFactory<T> factory) throws IllegalArgumentException {
            super();
            this.writeLock = new ReentrantReadWriteLock().writeLock();
            if (factory == null) {
                throw new IllegalArgumentException("factory must not be null.");
            }
            this.factory = factory;
        }
        
        @Override
        public PooledObject<T> makeObject() throws Exception {
            this.writeLock.lock();
            try {
                return this.factory.makeObject();
            }
            finally {
                this.writeLock.unlock();
            }
        }
        
        @Override
        public void destroyObject(final PooledObject<T> p) throws Exception {
            this.writeLock.lock();
            try {
                this.factory.destroyObject(p);
            }
            finally {
                this.writeLock.unlock();
            }
        }
        
        @Override
        public boolean validateObject(final PooledObject<T> p) {
            this.writeLock.lock();
            try {
                return this.factory.validateObject(p);
            }
            finally {
                this.writeLock.unlock();
            }
        }
        
        @Override
        public void activateObject(final PooledObject<T> p) throws Exception {
            this.writeLock.lock();
            try {
                this.factory.activateObject(p);
            }
            finally {
                this.writeLock.unlock();
            }
        }
        
        @Override
        public void passivateObject(final PooledObject<T> p) throws Exception {
            this.writeLock.lock();
            try {
                this.factory.passivateObject(p);
            }
            finally {
                this.writeLock.unlock();
            }
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("SynchronizedPoolableObjectFactory");
            sb.append("{factory=").append(this.factory);
            sb.append('}');
            return sb.toString();
        }
    }
    
    private static class SynchronizedKeyedPooledObjectFactory<K, V> implements KeyedPooledObjectFactory<K, V>
    {
        private final ReentrantReadWriteLock.WriteLock writeLock;
        private final KeyedPooledObjectFactory<K, V> keyedFactory;
        
        SynchronizedKeyedPooledObjectFactory(final KeyedPooledObjectFactory<K, V> keyedFactory) throws IllegalArgumentException {
            super();
            this.writeLock = new ReentrantReadWriteLock().writeLock();
            if (keyedFactory == null) {
                throw new IllegalArgumentException("keyedFactory must not be null.");
            }
            this.keyedFactory = keyedFactory;
        }
        
        @Override
        public PooledObject<V> makeObject(final K key) throws Exception {
            this.writeLock.lock();
            try {
                return this.keyedFactory.makeObject(key);
            }
            finally {
                this.writeLock.unlock();
            }
        }
        
        @Override
        public void destroyObject(final K key, final PooledObject<V> p) throws Exception {
            this.writeLock.lock();
            try {
                this.keyedFactory.destroyObject(key, p);
            }
            finally {
                this.writeLock.unlock();
            }
        }
        
        @Override
        public boolean validateObject(final K key, final PooledObject<V> p) {
            this.writeLock.lock();
            try {
                return this.keyedFactory.validateObject(key, p);
            }
            finally {
                this.writeLock.unlock();
            }
        }
        
        @Override
        public void activateObject(final K key, final PooledObject<V> p) throws Exception {
            this.writeLock.lock();
            try {
                this.keyedFactory.activateObject(key, p);
            }
            finally {
                this.writeLock.unlock();
            }
        }
        
        @Override
        public void passivateObject(final K key, final PooledObject<V> p) throws Exception {
            this.writeLock.lock();
            try {
                this.keyedFactory.passivateObject(key, p);
            }
            finally {
                this.writeLock.unlock();
            }
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("SynchronizedKeyedPoolableObjectFactory");
            sb.append("{keyedFactory=").append(this.keyedFactory);
            sb.append('}');
            return sb.toString();
        }
    }
    
    private static class ErodingFactor
    {
        private final float factor;
        private transient volatile long nextShrink;
        private transient volatile int idleHighWaterMark;
        
        public ErodingFactor(final float factor) {
            super();
            this.factor = factor;
            this.nextShrink = System.currentTimeMillis() + (long)(900000.0f * factor);
            this.idleHighWaterMark = 1;
        }
        
        public void update(final long now, final int numIdle) {
            final int idle = Math.max(0, numIdle);
            this.idleHighWaterMark = Math.max(idle, this.idleHighWaterMark);
            final float maxInterval = 15.0f;
            final float minutes = 15.0f + -14.0f / this.idleHighWaterMark * idle;
            this.nextShrink = now + (long)(minutes * 60000.0f * this.factor);
        }
        
        public long getNextShrink() {
            return this.nextShrink;
        }
        
        @Override
        public String toString() {
            return "ErodingFactor{factor=" + this.factor + ", idleHighWaterMark=" + this.idleHighWaterMark + '}';
        }
    }
    
    private static class ErodingObjectPool<T> implements ObjectPool<T>
    {
        private final ObjectPool<T> pool;
        private final ErodingFactor factor;
        
        public ErodingObjectPool(final ObjectPool<T> pool, final float factor) {
            super();
            this.pool = pool;
            this.factor = new ErodingFactor(factor);
        }
        
        @Override
        public T borrowObject() throws Exception, NoSuchElementException, IllegalStateException {
            return this.pool.borrowObject();
        }
        
        @Override
        public void returnObject(final T obj) {
            boolean discard = false;
            final long now = System.currentTimeMillis();
            synchronized (this.pool) {
                if (this.factor.getNextShrink() < now) {
                    final int numIdle = this.pool.getNumIdle();
                    if (numIdle > 0) {
                        discard = true;
                    }
                    this.factor.update(now, numIdle);
                }
            }
            try {
                if (discard) {
                    this.pool.invalidateObject(obj);
                }
                else {
                    this.pool.returnObject(obj);
                }
            }
            catch (Exception ex) {}
        }
        
        @Override
        public void invalidateObject(final T obj) {
            try {
                this.pool.invalidateObject(obj);
            }
            catch (Exception ex) {}
        }
        
        @Override
        public void addObject() throws Exception, IllegalStateException, UnsupportedOperationException {
            this.pool.addObject();
        }
        
        @Override
        public int getNumIdle() {
            return this.pool.getNumIdle();
        }
        
        @Override
        public int getNumActive() {
            return this.pool.getNumActive();
        }
        
        @Override
        public void clear() throws Exception, UnsupportedOperationException {
            this.pool.clear();
        }
        
        @Override
        public void close() {
            try {
                this.pool.close();
            }
            catch (Exception ex) {}
        }
        
        @Override
        public String toString() {
            return "ErodingObjectPool{factor=" + this.factor + ", pool=" + this.pool + '}';
        }
    }
    
    private static class ErodingKeyedObjectPool<K, V> implements KeyedObjectPool<K, V>
    {
        private final KeyedObjectPool<K, V> keyedPool;
        private final ErodingFactor erodingFactor;
        
        public ErodingKeyedObjectPool(final KeyedObjectPool<K, V> keyedPool, final float factor) {
            this(keyedPool, new ErodingFactor(factor));
        }
        
        protected ErodingKeyedObjectPool(final KeyedObjectPool<K, V> keyedPool, final ErodingFactor erodingFactor) {
            super();
            if (keyedPool == null) {
                throw new IllegalArgumentException("keyedPool must not be null.");
            }
            this.keyedPool = keyedPool;
            this.erodingFactor = erodingFactor;
        }
        
        @Override
        public V borrowObject(final K key) throws Exception, NoSuchElementException, IllegalStateException {
            return this.keyedPool.borrowObject(key);
        }
        
        @Override
        public void returnObject(final K key, final V obj) throws Exception {
            boolean discard = false;
            final long now = System.currentTimeMillis();
            final ErodingFactor factor = this.getErodingFactor(key);
            synchronized (this.keyedPool) {
                if (factor.getNextShrink() < now) {
                    final int numIdle = this.getNumIdle(key);
                    if (numIdle > 0) {
                        discard = true;
                    }
                    factor.update(now, numIdle);
                }
            }
            try {
                if (discard) {
                    this.keyedPool.invalidateObject(key, obj);
                }
                else {
                    this.keyedPool.returnObject(key, obj);
                }
            }
            catch (Exception ex) {}
        }
        
        protected ErodingFactor getErodingFactor(final K key) {
            return this.erodingFactor;
        }
        
        @Override
        public void invalidateObject(final K key, final V obj) {
            try {
                this.keyedPool.invalidateObject(key, obj);
            }
            catch (Exception ex) {}
        }
        
        @Override
        public void addObject(final K key) throws Exception, IllegalStateException, UnsupportedOperationException {
            this.keyedPool.addObject(key);
        }
        
        @Override
        public int getNumIdle() {
            return this.keyedPool.getNumIdle();
        }
        
        @Override
        public int getNumIdle(final K key) {
            return this.keyedPool.getNumIdle(key);
        }
        
        @Override
        public int getNumActive() {
            return this.keyedPool.getNumActive();
        }
        
        @Override
        public int getNumActive(final K key) {
            return this.keyedPool.getNumActive(key);
        }
        
        @Override
        public void clear() throws Exception, UnsupportedOperationException {
            this.keyedPool.clear();
        }
        
        @Override
        public void clear(final K key) throws Exception, UnsupportedOperationException {
            this.keyedPool.clear(key);
        }
        
        @Override
        public void close() {
            try {
                this.keyedPool.close();
            }
            catch (Exception ex) {}
        }
        
        protected KeyedObjectPool<K, V> getKeyedPool() {
            return this.keyedPool;
        }
        
        @Override
        public String toString() {
            return "ErodingKeyedObjectPool{factor=" + this.erodingFactor + ", keyedPool=" + this.keyedPool + '}';
        }
    }
    
    private static class ErodingPerKeyKeyedObjectPool<K, V> extends ErodingKeyedObjectPool<K, V>
    {
        private final float factor;
        private final Map<K, ErodingFactor> factors;
        
        public ErodingPerKeyKeyedObjectPool(final KeyedObjectPool<K, V> keyedPool, final float factor) {
            super(keyedPool, null);
            this.factors = Collections.synchronizedMap(new HashMap<K, ErodingFactor>());
            this.factor = factor;
        }
        
        @Override
        protected ErodingFactor getErodingFactor(final K key) {
            ErodingFactor eFactor = this.factors.get(key);
            if (eFactor == null) {
                eFactor = new ErodingFactor(this.factor);
                this.factors.put(key, eFactor);
            }
            return eFactor;
        }
        
        @Override
        public String toString() {
            return "ErodingPerKeyKeyedObjectPool{factor=" + this.factor + ", keyedPool=" + this.getKeyedPool() + '}';
        }
    }
}
