package org.apache.commons.pool2.impl;

import java.util.concurrent.*;
import org.apache.commons.pool2.*;
import java.util.concurrent.locks.*;
import java.util.*;
import java.util.concurrent.atomic.*;

public class GenericKeyedObjectPool<K, T> extends BaseGenericObjectPool<T> implements KeyedObjectPool<K, T>, GenericKeyedObjectPoolMXBean<K>
{
    private volatile int maxIdlePerKey;
    private volatile int minIdlePerKey;
    private volatile int maxTotalPerKey;
    private final KeyedPooledObjectFactory<K, T> factory;
    private final Map<K, ObjectDeque<T>> poolMap;
    private final List<K> poolKeyList;
    private final ReadWriteLock keyLock;
    private final AtomicInteger numTotal;
    private Iterator<K> evictionKeyIterator;
    private K evictionKey;
    private static final String ONAME_BASE = "org.apache.commons.pool2:type=GenericKeyedObjectPool,name=";
    
    public GenericKeyedObjectPool(final KeyedPooledObjectFactory<K, T> factory) {
        this(factory, new GenericKeyedObjectPoolConfig());
    }
    
    public GenericKeyedObjectPool(final KeyedPooledObjectFactory<K, T> factory, final GenericKeyedObjectPoolConfig config) {
        super(config, "org.apache.commons.pool2:type=GenericKeyedObjectPool,name=", config.getJmxNamePrefix());
        this.maxIdlePerKey = 8;
        this.minIdlePerKey = 0;
        this.maxTotalPerKey = 8;
        this.poolMap = new ConcurrentHashMap<K, ObjectDeque<T>>();
        this.poolKeyList = new ArrayList<K>();
        this.keyLock = new ReentrantReadWriteLock(true);
        this.numTotal = new AtomicInteger(0);
        this.evictionKeyIterator = null;
        this.evictionKey = null;
        if (factory == null) {
            this.jmxUnregister();
            throw new IllegalArgumentException("factory may not be null");
        }
        this.factory = factory;
        this.setConfig(config);
        this.startEvictor(this.getMinEvictableIdleTimeMillis());
    }
    
    @Override
    public int getMaxTotalPerKey() {
        return this.maxTotalPerKey;
    }
    
    public void setMaxTotalPerKey(final int maxTotalPerKey) {
        this.maxTotalPerKey = maxTotalPerKey;
    }
    
    @Override
    public int getMaxIdlePerKey() {
        return this.maxIdlePerKey;
    }
    
    public void setMaxIdlePerKey(final int maxIdlePerKey) {
        this.maxIdlePerKey = maxIdlePerKey;
    }
    
    public void setMinIdlePerKey(final int minIdlePerKey) {
        this.minIdlePerKey = minIdlePerKey;
    }
    
    @Override
    public int getMinIdlePerKey() {
        final int maxIdlePerKeySave = this.getMaxIdlePerKey();
        if (this.minIdlePerKey > maxIdlePerKeySave) {
            return maxIdlePerKeySave;
        }
        return this.minIdlePerKey;
    }
    
    public void setConfig(final GenericKeyedObjectPoolConfig conf) {
        this.setLifo(conf.getLifo());
        this.setMaxIdlePerKey(conf.getMaxIdlePerKey());
        this.setMaxTotalPerKey(conf.getMaxTotalPerKey());
        this.setMaxTotal(conf.getMaxTotal());
        this.setMinIdlePerKey(conf.getMinIdlePerKey());
        this.setMaxWaitMillis(conf.getMaxWaitMillis());
        this.setBlockWhenExhausted(conf.getBlockWhenExhausted());
        this.setTestOnCreate(conf.getTestOnCreate());
        this.setTestOnBorrow(conf.getTestOnBorrow());
        this.setTestOnReturn(conf.getTestOnReturn());
        this.setTestWhileIdle(conf.getTestWhileIdle());
        this.setNumTestsPerEvictionRun(conf.getNumTestsPerEvictionRun());
        this.setMinEvictableIdleTimeMillis(conf.getMinEvictableIdleTimeMillis());
        this.setSoftMinEvictableIdleTimeMillis(conf.getSoftMinEvictableIdleTimeMillis());
        this.setTimeBetweenEvictionRunsMillis(conf.getTimeBetweenEvictionRunsMillis());
        this.setEvictionPolicyClassName(conf.getEvictionPolicyClassName());
    }
    
    public KeyedPooledObjectFactory<K, T> getFactory() {
        return this.factory;
    }
    
    @Override
    public T borrowObject(final K key) throws Exception {
        return this.borrowObject(key, this.getMaxWaitMillis());
    }
    
    public T borrowObject(final K key, final long borrowMaxWaitMillis) throws Exception {
        this.assertOpen();
        PooledObject<T> p = null;
        final boolean blockWhenExhausted = this.getBlockWhenExhausted();
        long waitTime = 0L;
        final ObjectDeque<T> objectDeque = this.register(key);
        try {
            while (p == null) {
                boolean create = false;
                if (blockWhenExhausted) {
                    p = objectDeque.getIdleObjects().pollFirst();
                    if (p == null) {
                        create = true;
                        p = this.create(key);
                    }
                    if (p == null) {
                        if (borrowMaxWaitMillis < 0L) {
                            p = objectDeque.getIdleObjects().takeFirst();
                        }
                        else {
                            waitTime = System.currentTimeMillis();
                            p = objectDeque.getIdleObjects().pollFirst(borrowMaxWaitMillis, TimeUnit.MILLISECONDS);
                            waitTime = System.currentTimeMillis() - waitTime;
                        }
                    }
                    if (p == null) {
                        throw new NoSuchElementException("Timeout waiting for idle object");
                    }
                    if (!p.allocate()) {
                        p = null;
                    }
                }
                else {
                    p = objectDeque.getIdleObjects().pollFirst();
                    if (p == null) {
                        create = true;
                        p = this.create(key);
                    }
                    if (p == null) {
                        throw new NoSuchElementException("Pool exhausted");
                    }
                    if (!p.allocate()) {
                        p = null;
                    }
                }
                if (p != null) {
                    try {
                        this.factory.activateObject(key, p);
                    }
                    catch (Exception e) {
                        try {
                            this.destroy(key, p, true);
                        }
                        catch (Exception ex) {}
                        p = null;
                        if (create) {
                            final NoSuchElementException nsee = new NoSuchElementException("Unable to activate object");
                            nsee.initCause(e);
                            throw nsee;
                        }
                    }
                    if (p == null || (!this.getTestOnBorrow() && (!create || !this.getTestOnCreate()))) {
                        continue;
                    }
                    boolean validate = false;
                    Throwable validationThrowable = null;
                    try {
                        validate = this.factory.validateObject(key, p);
                    }
                    catch (Throwable t) {
                        PoolUtils.checkRethrow(t);
                        validationThrowable = t;
                    }
                    if (validate) {
                        continue;
                    }
                    try {
                        this.destroy(key, p, true);
                        this.destroyedByBorrowValidationCount.incrementAndGet();
                    }
                    catch (Exception ex2) {}
                    p = null;
                    if (create) {
                        final NoSuchElementException nsee2 = new NoSuchElementException("Unable to validate object");
                        nsee2.initCause(validationThrowable);
                        throw nsee2;
                    }
                    continue;
                }
            }
        }
        finally {
            this.deregister(key);
        }
        this.updateStatsBorrow(p, waitTime);
        return p.getObject();
    }
    
    @Override
    public void returnObject(final K key, final T obj) {
        final ObjectDeque<T> objectDeque = this.poolMap.get(key);
        final PooledObject<T> p = objectDeque.getAllObjects().get(obj);
        if (p == null) {
            throw new IllegalStateException("Returned object not currently part of this pool");
        }
        final long activeTime = p.getActiveTimeMillis();
        if (this.getTestOnReturn() && !this.factory.validateObject(key, p)) {
            try {
                this.destroy(key, p, true);
            }
            catch (Exception e) {
                this.swallowException(e);
            }
            if (((ObjectDeque<Object>)objectDeque).idleObjects.hasTakeWaiters()) {
                try {
                    this.addObject(key);
                }
                catch (Exception e) {
                    this.swallowException(e);
                }
            }
            this.updateStatsReturn(activeTime);
            return;
        }
        try {
            this.factory.passivateObject(key, p);
        }
        catch (Exception e2) {
            this.swallowException(e2);
            try {
                this.destroy(key, p, true);
            }
            catch (Exception e3) {
                this.swallowException(e3);
            }
            if (((ObjectDeque<Object>)objectDeque).idleObjects.hasTakeWaiters()) {
                try {
                    this.addObject(key);
                }
                catch (Exception e3) {
                    this.swallowException(e3);
                }
            }
            this.updateStatsReturn(activeTime);
            return;
        }
        if (!p.deallocate()) {
            throw new IllegalStateException("Object has already been retured to this pool");
        }
        final int maxIdle = this.getMaxIdlePerKey();
        final LinkedBlockingDeque<PooledObject<T>> idleObjects = objectDeque.getIdleObjects();
        Label_0306: {
            if (!this.isClosed()) {
                if (maxIdle <= -1 || maxIdle > idleObjects.size()) {
                    if (this.getLifo()) {
                        idleObjects.addFirst(p);
                        break Label_0306;
                    }
                    idleObjects.addLast(p);
                    break Label_0306;
                }
            }
            try {
                this.destroy(key, p, true);
            }
            catch (Exception e4) {
                this.swallowException(e4);
            }
        }
        if (this.hasBorrowWaiters()) {
            this.reuseCapacity();
        }
        this.updateStatsReturn(activeTime);
    }
    
    @Override
    public void invalidateObject(final K key, final T obj) throws Exception {
        final ObjectDeque<T> objectDeque = this.poolMap.get(key);
        final PooledObject<T> p = objectDeque.getAllObjects().get(obj);
        if (p == null) {
            throw new IllegalStateException("Object not currently part of this pool");
        }
        synchronized (p) {
            if (p.getState() != PooledObjectState.INVALID) {
                this.destroy(key, p, true);
            }
        }
        if (((ObjectDeque<Object>)objectDeque).idleObjects.hasTakeWaiters()) {
            this.addObject(key);
        }
    }
    
    @Override
    public void clear() {
        final Iterator<K> iter = this.poolMap.keySet().iterator();
        while (iter.hasNext()) {
            this.clear(iter.next());
        }
    }
    
    @Override
    public void clear(final K key) {
        final ObjectDeque<T> objectDeque = this.register(key);
        try {
            final LinkedBlockingDeque<PooledObject<T>> idleObjects = objectDeque.getIdleObjects();
            for (PooledObject<T> p = idleObjects.poll(); p != null; p = idleObjects.poll()) {
                try {
                    this.destroy(key, p, true);
                }
                catch (Exception e) {
                    this.swallowException(e);
                }
            }
        }
        finally {
            this.deregister(key);
        }
    }
    
    @Override
    public int getNumActive() {
        return this.numTotal.get() - this.getNumIdle();
    }
    
    @Override
    public int getNumIdle() {
        final Iterator<ObjectDeque<T>> iter = this.poolMap.values().iterator();
        int result = 0;
        while (iter.hasNext()) {
            result += iter.next().getIdleObjects().size();
        }
        return result;
    }
    
    @Override
    public int getNumActive(final K key) {
        final ObjectDeque<T> objectDeque = this.poolMap.get(key);
        if (objectDeque != null) {
            return objectDeque.getAllObjects().size() - objectDeque.getIdleObjects().size();
        }
        return 0;
    }
    
    @Override
    public int getNumIdle(final K key) {
        final ObjectDeque<T> objectDeque = this.poolMap.get(key);
        return (objectDeque != null) ? objectDeque.getIdleObjects().size() : 0;
    }
    
    @Override
    public void close() {
        if (this.isClosed()) {
            return;
        }
        synchronized (this.closeLock) {
            if (this.isClosed()) {
                return;
            }
            this.startEvictor(-1L);
            this.closed = true;
            this.clear();
            this.jmxUnregister();
            final Iterator<ObjectDeque<T>> iter = this.poolMap.values().iterator();
            while (iter.hasNext()) {
                iter.next().getIdleObjects().interuptTakeWaiters();
            }
            this.clear();
        }
    }
    
    public void clearOldest() {
        final Map<PooledObject<T>, K> map = new TreeMap<PooledObject<T>, K>();
        for (final K k : this.poolMap.keySet()) {
            final ObjectDeque<T> queue = this.poolMap.get(k);
            if (queue != null) {
                final LinkedBlockingDeque<PooledObject<T>> idleObjects = queue.getIdleObjects();
                for (final PooledObject<T> p : idleObjects) {
                    map.put(p, k);
                }
            }
        }
        int itemsToRemove = (int)(map.size() * 0.15) + 1;
        for (Iterator<Map.Entry<PooledObject<T>, K>> iter = map.entrySet().iterator(); iter.hasNext() && itemsToRemove > 0; --itemsToRemove) {
            final Map.Entry<PooledObject<T>, K> entry = iter.next();
            final K key = entry.getValue();
            final PooledObject<T> p2 = entry.getKey();
            boolean destroyed = true;
            try {
                destroyed = this.destroy(key, p2, false);
            }
            catch (Exception e) {
                this.swallowException(e);
            }
            if (destroyed) {}
        }
    }
    
    private void reuseCapacity() {
        final int maxTotalPerKeySave = this.getMaxTotalPerKey();
        int maxQueueLength = 0;
        LinkedBlockingDeque<PooledObject<T>> mostLoaded = null;
        K loadedKey = null;
        for (final K k : this.poolMap.keySet()) {
            final ObjectDeque<T> deque = this.poolMap.get(k);
            if (deque != null) {
                final LinkedBlockingDeque<PooledObject<T>> pool = deque.getIdleObjects();
                final int queueLength = pool.getTakeQueueLength();
                if (this.getNumActive(k) >= maxTotalPerKeySave || queueLength <= maxQueueLength) {
                    continue;
                }
                maxQueueLength = queueLength;
                mostLoaded = pool;
                loadedKey = k;
            }
        }
        if (mostLoaded != null) {
            this.register(loadedKey);
            try {
                final PooledObject<T> p = this.create(loadedKey);
                if (p != null) {
                    this.addIdleObject(loadedKey, p);
                }
            }
            catch (Exception e) {
                this.swallowException(e);
            }
            finally {
                this.deregister(loadedKey);
            }
        }
    }
    
    private boolean hasBorrowWaiters() {
        for (final K k : this.poolMap.keySet()) {
            final ObjectDeque<T> deque = this.poolMap.get(k);
            if (deque != null) {
                final LinkedBlockingDeque<PooledObject<T>> pool = deque.getIdleObjects();
                if (pool.hasTakeWaiters()) {
                    return true;
                }
                continue;
            }
        }
        return false;
    }
    
    @Override
    public void evict() throws Exception {
        this.assertOpen();
        if (this.getNumIdle() == 0) {
            return;
        }
        PooledObject<T> underTest = null;
        final EvictionPolicy<T> evictionPolicy = this.getEvictionPolicy();
        synchronized (this.evictionLock) {
            final EvictionConfig evictionConfig = new EvictionConfig(this.getMinEvictableIdleTimeMillis(), this.getSoftMinEvictableIdleTimeMillis(), this.getMinIdlePerKey());
            final boolean testWhileIdle = this.getTestWhileIdle();
            LinkedBlockingDeque<PooledObject<T>> idleObjects = null;
            for (int i = 0, m = this.getNumTests(); i < m; ++i) {
                if (this.evictionIterator == null || !this.evictionIterator.hasNext()) {
                    if (this.evictionKeyIterator == null || !this.evictionKeyIterator.hasNext()) {
                        final List<K> keyCopy = new ArrayList<K>();
                        final Lock readLock = this.keyLock.readLock();
                        readLock.lock();
                        try {
                            keyCopy.addAll((Collection<? extends K>)this.poolKeyList);
                        }
                        finally {
                            readLock.unlock();
                        }
                        this.evictionKeyIterator = keyCopy.iterator();
                    }
                    while (this.evictionKeyIterator.hasNext()) {
                        this.evictionKey = this.evictionKeyIterator.next();
                        final ObjectDeque<T> objectDeque = this.poolMap.get(this.evictionKey);
                        if (objectDeque == null) {
                            continue;
                        }
                        idleObjects = objectDeque.getIdleObjects();
                        if (this.getLifo()) {
                            this.evictionIterator = (Iterator<PooledObject<T>>)idleObjects.descendingIterator();
                        }
                        else {
                            this.evictionIterator = (Iterator<PooledObject<T>>)idleObjects.iterator();
                        }
                        if (this.evictionIterator.hasNext()) {
                            break;
                        }
                        this.evictionIterator = null;
                    }
                }
                if (this.evictionIterator == null) {
                    return;
                }
                try {
                    underTest = (PooledObject<T>)this.evictionIterator.next();
                }
                catch (NoSuchElementException nsee) {
                    --i;
                    this.evictionIterator = null;
                    continue;
                }
                if (!underTest.startEvictionTest()) {
                    --i;
                }
                else if (evictionPolicy.evict(evictionConfig, underTest, this.poolMap.get(this.evictionKey).getIdleObjects().size())) {
                    this.destroy(this.evictionKey, underTest, true);
                    this.destroyedByEvictorCount.incrementAndGet();
                }
                else {
                    if (testWhileIdle) {
                        boolean active = false;
                        try {
                            this.factory.activateObject(this.evictionKey, underTest);
                            active = true;
                        }
                        catch (Exception e) {
                            this.destroy(this.evictionKey, underTest, true);
                            this.destroyedByEvictorCount.incrementAndGet();
                        }
                        if (active) {
                            if (!this.factory.validateObject(this.evictionKey, underTest)) {
                                this.destroy(this.evictionKey, underTest, true);
                                this.destroyedByEvictorCount.incrementAndGet();
                            }
                            else {
                                try {
                                    this.factory.passivateObject(this.evictionKey, underTest);
                                }
                                catch (Exception e) {
                                    this.destroy(this.evictionKey, underTest, true);
                                    this.destroyedByEvictorCount.incrementAndGet();
                                }
                            }
                        }
                    }
                    if (!underTest.endEvictionTest(idleObjects)) {}
                }
            }
        }
    }
    
    private PooledObject<T> create(final K key) throws Exception {
        final int maxTotalPerKeySave = this.getMaxTotalPerKey();
        final int maxTotal = this.getMaxTotal();
        boolean loop = true;
        while (loop) {
            final int newNumTotal = this.numTotal.incrementAndGet();
            if (maxTotal > -1 && newNumTotal > maxTotal) {
                this.numTotal.decrementAndGet();
                if (this.getNumIdle() == 0) {
                    return null;
                }
                this.clearOldest();
            }
            else {
                loop = false;
            }
        }
        final ObjectDeque<T> objectDeque = this.poolMap.get(key);
        final long newCreateCount = objectDeque.getCreateCount().incrementAndGet();
        if ((maxTotalPerKeySave > -1 && newCreateCount > maxTotalPerKeySave) || newCreateCount > 2147483647L) {
            this.numTotal.decrementAndGet();
            objectDeque.getCreateCount().decrementAndGet();
            return null;
        }
        PooledObject<T> p = null;
        try {
            p = this.factory.makeObject(key);
        }
        catch (Exception e) {
            this.numTotal.decrementAndGet();
            objectDeque.getCreateCount().decrementAndGet();
            throw e;
        }
        this.createdCount.incrementAndGet();
        objectDeque.getAllObjects().put(p.getObject(), p);
        return p;
    }
    
    private boolean destroy(final K key, final PooledObject<T> toDestroy, final boolean always) throws Exception {
        final ObjectDeque<T> objectDeque = this.register(key);
        try {
            final boolean isIdle = objectDeque.getIdleObjects().remove(toDestroy);
            if (isIdle || always) {
                objectDeque.getAllObjects().remove(toDestroy.getObject());
                toDestroy.invalidate();
                try {
                    this.factory.destroyObject(key, toDestroy);
                }
                finally {
                    objectDeque.getCreateCount().decrementAndGet();
                    this.destroyedCount.incrementAndGet();
                    this.numTotal.decrementAndGet();
                }
                return true;
            }
            return false;
        }
        finally {
            this.deregister(key);
        }
    }
    
    private ObjectDeque<T> register(final K k) {
        Lock lock = this.keyLock.readLock();
        ObjectDeque<T> objectDeque = null;
        try {
            lock.lock();
            objectDeque = this.poolMap.get(k);
            if (objectDeque == null) {
                lock.unlock();
                lock = this.keyLock.writeLock();
                lock.lock();
                objectDeque = this.poolMap.get(k);
                if (objectDeque == null) {
                    objectDeque = new ObjectDeque<T>();
                    objectDeque.getNumInterested().incrementAndGet();
                    this.poolMap.put(k, objectDeque);
                    this.poolKeyList.add(k);
                }
                else {
                    objectDeque.getNumInterested().incrementAndGet();
                }
            }
            else {
                objectDeque.getNumInterested().incrementAndGet();
            }
        }
        finally {
            lock.unlock();
        }
        return objectDeque;
    }
    
    private void deregister(final K k) {
        final ObjectDeque<T> objectDeque = this.poolMap.get(k);
        final long numInterested = objectDeque.getNumInterested().decrementAndGet();
        if (numInterested == 0L && objectDeque.getCreateCount().get() == 0) {
            final Lock writeLock = this.keyLock.writeLock();
            writeLock.lock();
            try {
                if (objectDeque.getCreateCount().get() == 0 && objectDeque.getNumInterested().get() == 0L) {
                    this.poolMap.remove(k);
                    this.poolKeyList.remove(k);
                }
            }
            finally {
                writeLock.unlock();
            }
        }
    }
    
    @Override
    void ensureMinIdle() throws Exception {
        final int minIdlePerKeySave = this.getMinIdlePerKey();
        if (minIdlePerKeySave < 1) {
            return;
        }
        for (final K k : this.poolMap.keySet()) {
            this.ensureMinIdle(k);
        }
    }
    
    private void ensureMinIdle(final K key) throws Exception {
        final ObjectDeque<T> objectDeque = this.poolMap.get(key);
        for (int deficit = this.calculateDeficit(objectDeque), i = 0; i < deficit && this.calculateDeficit(objectDeque) > 0; ++i) {
            this.addObject(key);
        }
    }
    
    @Override
    public void addObject(final K key) throws Exception {
        this.assertOpen();
        this.register(key);
        try {
            final PooledObject<T> p = this.create(key);
            this.addIdleObject(key, p);
        }
        finally {
            this.deregister(key);
        }
    }
    
    private void addIdleObject(final K key, final PooledObject<T> p) throws Exception {
        if (p != null) {
            this.factory.passivateObject(key, p);
            final LinkedBlockingDeque<PooledObject<T>> idleObjects = this.poolMap.get(key).getIdleObjects();
            if (this.getLifo()) {
                idleObjects.addFirst(p);
            }
            else {
                idleObjects.addLast(p);
            }
        }
    }
    
    public void preparePool(final K key) throws Exception {
        final int minIdlePerKeySave = this.getMinIdlePerKey();
        if (minIdlePerKeySave < 1) {
            return;
        }
        this.ensureMinIdle(key);
    }
    
    private int getNumTests() {
        final int totalIdle = this.getNumIdle();
        final int numTests = this.getNumTestsPerEvictionRun();
        if (numTests >= 0) {
            return Math.min(numTests, totalIdle);
        }
        return (int)Math.ceil(totalIdle / Math.abs((double)numTests));
    }
    
    private int calculateDeficit(final ObjectDeque<T> objectDeque) {
        if (objectDeque == null) {
            return this.getMinIdlePerKey();
        }
        final int maxTotal = this.getMaxTotal();
        final int maxTotalPerKeySave = this.getMaxTotalPerKey();
        int objectDefecit = 0;
        objectDefecit = this.getMinIdlePerKey() - objectDeque.getIdleObjects().size();
        if (maxTotalPerKeySave > 0) {
            final int growLimit = Math.max(0, maxTotalPerKeySave - objectDeque.getIdleObjects().size());
            objectDefecit = Math.min(objectDefecit, growLimit);
        }
        if (maxTotal > 0) {
            final int growLimit = Math.max(0, maxTotal - this.getNumActive() - this.getNumIdle());
            objectDefecit = Math.min(objectDefecit, growLimit);
        }
        return objectDefecit;
    }
    
    @Override
    public Map<String, Integer> getNumActivePerKey() {
        final HashMap<String, Integer> result = new HashMap<String, Integer>();
        for (final Map.Entry<K, ObjectDeque<T>> entry : this.poolMap.entrySet()) {
            if (entry != null) {
                final K key = entry.getKey();
                final ObjectDeque<T> objectDequeue = entry.getValue();
                if (key == null || objectDequeue == null) {
                    continue;
                }
                result.put(key.toString(), objectDequeue.getAllObjects().size() - objectDequeue.getIdleObjects().size());
            }
        }
        return result;
    }
    
    @Override
    public int getNumWaiters() {
        int result = 0;
        if (this.getBlockWhenExhausted()) {
            final Iterator<ObjectDeque<T>> iter = this.poolMap.values().iterator();
            while (iter.hasNext()) {
                result += iter.next().getIdleObjects().getTakeQueueLength();
            }
        }
        return result;
    }
    
    @Override
    public Map<String, Integer> getNumWaitersByKey() {
        final Map<String, Integer> result = new HashMap<String, Integer>();
        for (final K key : this.poolMap.keySet()) {
            final ObjectDeque<T> queue = this.poolMap.get(key);
            if (queue != null) {
                if (this.getBlockWhenExhausted()) {
                    result.put(key.toString(), queue.getIdleObjects().getTakeQueueLength());
                }
                else {
                    result.put(key.toString(), 0);
                }
            }
        }
        return result;
    }
    
    @Override
    public Map<String, List<DefaultPooledObjectInfo>> listAllObjects() {
        final Map<String, List<DefaultPooledObjectInfo>> result = new HashMap<String, List<DefaultPooledObjectInfo>>();
        for (final K key : this.poolMap.keySet()) {
            final ObjectDeque<T> queue = this.poolMap.get(key);
            if (queue != null) {
                final List<DefaultPooledObjectInfo> list = new ArrayList<DefaultPooledObjectInfo>();
                result.put(key.toString(), list);
                for (final PooledObject<T> p : queue.getAllObjects().values()) {
                    list.add(new DefaultPooledObjectInfo(p));
                }
            }
        }
        return result;
    }
    
    private class ObjectDeque<S>
    {
        private final LinkedBlockingDeque<PooledObject<S>> idleObjects;
        private final AtomicInteger createCount;
        private final Map<S, PooledObject<S>> allObjects;
        private final AtomicLong numInterested;
        
        private ObjectDeque() {
            super();
            this.idleObjects = new LinkedBlockingDeque<PooledObject<S>>();
            this.createCount = new AtomicInteger(0);
            this.allObjects = new ConcurrentHashMap<S, PooledObject<S>>();
            this.numInterested = new AtomicLong(0L);
        }
        
        public LinkedBlockingDeque<PooledObject<S>> getIdleObjects() {
            return this.idleObjects;
        }
        
        public AtomicInteger getCreateCount() {
            return this.createCount;
        }
        
        public AtomicLong getNumInterested() {
            return this.numInterested;
        }
        
        public Map<S, PooledObject<S>> getAllObjects() {
            return this.allObjects;
        }
    }
}
