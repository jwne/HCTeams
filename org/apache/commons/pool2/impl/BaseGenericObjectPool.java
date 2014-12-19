package org.apache.commons.pool2.impl;

import java.util.concurrent.atomic.*;
import org.apache.commons.pool2.*;
import java.lang.management.*;
import javax.management.*;
import java.io.*;
import java.util.*;

public abstract class BaseGenericObjectPool<T>
{
    public static final int MEAN_TIMING_STATS_CACHE_SIZE = 100;
    private volatile int maxTotal;
    private volatile boolean blockWhenExhausted;
    private volatile long maxWaitMillis;
    private volatile boolean lifo;
    private volatile boolean testOnCreate;
    private volatile boolean testOnBorrow;
    private volatile boolean testOnReturn;
    private volatile boolean testWhileIdle;
    private volatile long timeBetweenEvictionRunsMillis;
    private volatile int numTestsPerEvictionRun;
    private volatile long minEvictableIdleTimeMillis;
    private volatile long softMinEvictableIdleTimeMillis;
    private volatile EvictionPolicy<T> evictionPolicy;
    final Object closeLock;
    volatile boolean closed;
    final Object evictionLock;
    private Evictor evictor;
    Iterator<PooledObject<T>> evictionIterator;
    private final ClassLoader factoryClassLoader;
    private final ObjectName oname;
    private final String creationStackTrace;
    private final AtomicLong borrowedCount;
    private final AtomicLong returnedCount;
    final AtomicLong createdCount;
    final AtomicLong destroyedCount;
    final AtomicLong destroyedByEvictorCount;
    final AtomicLong destroyedByBorrowValidationCount;
    private final LinkedList<Long> activeTimes;
    private final LinkedList<Long> idleTimes;
    private final LinkedList<Long> waitTimes;
    private final Object maxBorrowWaitTimeMillisLock;
    private volatile long maxBorrowWaitTimeMillis;
    private SwallowedExceptionListener swallowedExceptionListener;
    
    public BaseGenericObjectPool(final BaseObjectPoolConfig config, final String jmxNameBase, final String jmxNamePrefix) {
        super();
        this.maxTotal = -1;
        this.blockWhenExhausted = true;
        this.maxWaitMillis = -1L;
        this.lifo = true;
        this.testOnCreate = false;
        this.testOnBorrow = false;
        this.testOnReturn = false;
        this.testWhileIdle = false;
        this.timeBetweenEvictionRunsMillis = -1L;
        this.numTestsPerEvictionRun = 3;
        this.minEvictableIdleTimeMillis = 1800000L;
        this.softMinEvictableIdleTimeMillis = -1L;
        this.closeLock = new Object();
        this.closed = false;
        this.evictionLock = new Object();
        this.evictor = null;
        this.evictionIterator = null;
        this.borrowedCount = new AtomicLong(0L);
        this.returnedCount = new AtomicLong(0L);
        this.createdCount = new AtomicLong(0L);
        this.destroyedCount = new AtomicLong(0L);
        this.destroyedByEvictorCount = new AtomicLong(0L);
        this.destroyedByBorrowValidationCount = new AtomicLong(0L);
        this.activeTimes = new LinkedList<Long>();
        this.idleTimes = new LinkedList<Long>();
        this.waitTimes = new LinkedList<Long>();
        this.maxBorrowWaitTimeMillisLock = new Object();
        this.maxBorrowWaitTimeMillis = 0L;
        this.swallowedExceptionListener = null;
        if (config.getJmxEnabled()) {
            this.oname = this.jmxRegister(config, jmxNameBase, jmxNamePrefix);
        }
        else {
            this.oname = null;
        }
        this.creationStackTrace = this.getStackTrace(new Exception());
        this.factoryClassLoader = Thread.currentThread().getContextClassLoader();
        this.initStats();
    }
    
    public final int getMaxTotal() {
        return this.maxTotal;
    }
    
    public final void setMaxTotal(final int maxTotal) {
        this.maxTotal = maxTotal;
    }
    
    public final boolean getBlockWhenExhausted() {
        return this.blockWhenExhausted;
    }
    
    public final void setBlockWhenExhausted(final boolean blockWhenExhausted) {
        this.blockWhenExhausted = blockWhenExhausted;
    }
    
    public final long getMaxWaitMillis() {
        return this.maxWaitMillis;
    }
    
    public final void setMaxWaitMillis(final long maxWaitMillis) {
        this.maxWaitMillis = maxWaitMillis;
    }
    
    public final boolean getLifo() {
        return this.lifo;
    }
    
    public final void setLifo(final boolean lifo) {
        this.lifo = lifo;
    }
    
    public final boolean getTestOnCreate() {
        return this.testOnCreate;
    }
    
    public final void setTestOnCreate(final boolean testOnCreate) {
        this.testOnCreate = testOnCreate;
    }
    
    public final boolean getTestOnBorrow() {
        return this.testOnBorrow;
    }
    
    public final void setTestOnBorrow(final boolean testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
    }
    
    public final boolean getTestOnReturn() {
        return this.testOnReturn;
    }
    
    public final void setTestOnReturn(final boolean testOnReturn) {
        this.testOnReturn = testOnReturn;
    }
    
    public final boolean getTestWhileIdle() {
        return this.testWhileIdle;
    }
    
    public final void setTestWhileIdle(final boolean testWhileIdle) {
        this.testWhileIdle = testWhileIdle;
    }
    
    public final long getTimeBetweenEvictionRunsMillis() {
        return this.timeBetweenEvictionRunsMillis;
    }
    
    public final void setTimeBetweenEvictionRunsMillis(final long timeBetweenEvictionRunsMillis) {
        this.startEvictor(this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis);
    }
    
    public final int getNumTestsPerEvictionRun() {
        return this.numTestsPerEvictionRun;
    }
    
    public final void setNumTestsPerEvictionRun(final int numTestsPerEvictionRun) {
        this.numTestsPerEvictionRun = numTestsPerEvictionRun;
    }
    
    public final long getMinEvictableIdleTimeMillis() {
        return this.minEvictableIdleTimeMillis;
    }
    
    public final void setMinEvictableIdleTimeMillis(final long minEvictableIdleTimeMillis) {
        this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
    }
    
    public final long getSoftMinEvictableIdleTimeMillis() {
        return this.softMinEvictableIdleTimeMillis;
    }
    
    public final void setSoftMinEvictableIdleTimeMillis(final long softMinEvictableIdleTimeMillis) {
        this.softMinEvictableIdleTimeMillis = softMinEvictableIdleTimeMillis;
    }
    
    public final String getEvictionPolicyClassName() {
        return this.evictionPolicy.getClass().getName();
    }
    
    public final void setEvictionPolicyClassName(final String evictionPolicyClassName) {
        try {
            final Class<?> clazz = Class.forName(evictionPolicyClassName);
            final Object policy = clazz.newInstance();
            if (policy instanceof EvictionPolicy) {
                final EvictionPolicy<T> evicPolicy = (EvictionPolicy<T>)policy;
                this.evictionPolicy = evicPolicy;
            }
        }
        catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Unable to create EvictionPolicy instance of type " + evictionPolicyClassName, e);
        }
        catch (InstantiationException e2) {
            throw new IllegalArgumentException("Unable to create EvictionPolicy instance of type " + evictionPolicyClassName, e2);
        }
        catch (IllegalAccessException e3) {
            throw new IllegalArgumentException("Unable to create EvictionPolicy instance of type " + evictionPolicyClassName, e3);
        }
    }
    
    public abstract void close();
    
    public final boolean isClosed() {
        return this.closed;
    }
    
    public abstract void evict() throws Exception;
    
    final EvictionPolicy<T> getEvictionPolicy() {
        return this.evictionPolicy;
    }
    
    final void assertOpen() throws IllegalStateException {
        if (this.isClosed()) {
            throw new IllegalStateException("Pool not open");
        }
    }
    
    final void startEvictor(final long delay) {
        synchronized (this.evictionLock) {
            if (null != this.evictor) {
                EvictionTimer.cancel(this.evictor);
                this.evictor = null;
                this.evictionIterator = null;
            }
            if (delay > 0L) {
                EvictionTimer.schedule(this.evictor = new Evictor(), delay, delay);
            }
        }
    }
    
    abstract void ensureMinIdle() throws Exception;
    
    public final ObjectName getJmxName() {
        return this.oname;
    }
    
    public final String getCreationStackTrace() {
        return this.creationStackTrace;
    }
    
    public final long getBorrowedCount() {
        return this.borrowedCount.get();
    }
    
    public final long getReturnedCount() {
        return this.returnedCount.get();
    }
    
    public final long getCreatedCount() {
        return this.createdCount.get();
    }
    
    public final long getDestroyedCount() {
        return this.destroyedCount.get();
    }
    
    public final long getDestroyedByEvictorCount() {
        return this.destroyedByEvictorCount.get();
    }
    
    public final long getDestroyedByBorrowValidationCount() {
        return this.destroyedByBorrowValidationCount.get();
    }
    
    public final long getMeanActiveTimeMillis() {
        return this.getMeanFromStatsCache(this.activeTimes);
    }
    
    public final long getMeanIdleTimeMillis() {
        return this.getMeanFromStatsCache(this.idleTimes);
    }
    
    public final long getMeanBorrowWaitTimeMillis() {
        return this.getMeanFromStatsCache(this.waitTimes);
    }
    
    public final long getMaxBorrowWaitTimeMillis() {
        return this.maxBorrowWaitTimeMillis;
    }
    
    public abstract int getNumIdle();
    
    public final SwallowedExceptionListener getSwallowedExceptionListener() {
        return this.swallowedExceptionListener;
    }
    
    public final void setSwallowedExceptionListener(final SwallowedExceptionListener swallowedExceptionListener) {
        this.swallowedExceptionListener = swallowedExceptionListener;
    }
    
    final void swallowException(final Exception e) {
        final SwallowedExceptionListener listener = this.getSwallowedExceptionListener();
        if (listener == null) {
            return;
        }
        try {
            listener.onSwallowException(e);
        }
        catch (OutOfMemoryError oome) {
            throw oome;
        }
        catch (VirtualMachineError vme) {
            throw vme;
        }
        catch (Throwable t) {}
    }
    
    final void updateStatsBorrow(final PooledObject<T> p, final long waitTime) {
        this.borrowedCount.incrementAndGet();
        synchronized (this.idleTimes) {
            this.idleTimes.add(p.getIdleTimeMillis());
            this.idleTimes.poll();
        }
        synchronized (this.waitTimes) {
            this.waitTimes.add(waitTime);
            this.waitTimes.poll();
        }
        synchronized (this.maxBorrowWaitTimeMillisLock) {
            if (waitTime > this.maxBorrowWaitTimeMillis) {
                this.maxBorrowWaitTimeMillis = waitTime;
            }
        }
    }
    
    final void updateStatsReturn(final long activeTime) {
        this.returnedCount.incrementAndGet();
        synchronized (this.activeTimes) {
            this.activeTimes.add(activeTime);
            this.activeTimes.poll();
        }
    }
    
    final void jmxUnregister() {
        if (this.oname != null) {
            try {
                ManagementFactory.getPlatformMBeanServer().unregisterMBean(this.oname);
            }
            catch (MBeanRegistrationException e) {
                this.swallowException(e);
            }
            catch (InstanceNotFoundException e2) {
                this.swallowException(e2);
            }
        }
    }
    
    private ObjectName jmxRegister(final BaseObjectPoolConfig config, final String jmxNameBase, String jmxNamePrefix) {
        ObjectName objectName = null;
        final MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        int i = 1;
        boolean registered = false;
        String base = config.getJmxNameBase();
        if (base == null) {
            base = jmxNameBase;
        }
        while (!registered) {
            try {
                ObjectName objName;
                if (i == 1) {
                    objName = new ObjectName(base + jmxNamePrefix);
                }
                else {
                    objName = new ObjectName(base + jmxNamePrefix + i);
                }
                mbs.registerMBean(this, objName);
                objectName = objName;
                registered = true;
            }
            catch (MalformedObjectNameException e) {
                if ("pool".equals(jmxNamePrefix) && jmxNameBase.equals(base)) {
                    registered = true;
                }
                else {
                    jmxNamePrefix = "pool";
                    base = jmxNameBase;
                }
            }
            catch (InstanceAlreadyExistsException e2) {
                ++i;
            }
            catch (MBeanRegistrationException e3) {
                registered = true;
            }
            catch (NotCompliantMBeanException e4) {
                registered = true;
            }
        }
        return objectName;
    }
    
    private String getStackTrace(final Exception e) {
        final Writer w = new StringWriter();
        final PrintWriter pw = new PrintWriter(w);
        e.printStackTrace(pw);
        return w.toString();
    }
    
    private long getMeanFromStatsCache(final LinkedList<Long> cache) {
        final List<Long> times = new ArrayList<Long>(100);
        synchronized (cache) {
            times.addAll(cache);
        }
        double result = 0.0;
        int counter = 0;
        for (final Long time : times) {
            if (time != null) {
                ++counter;
                result = result * ((counter - 1) / counter) + time / counter;
            }
        }
        return (long)result;
    }
    
    private void initStats() {
        for (int i = 0; i < 100; ++i) {
            this.activeTimes.add(null);
            this.idleTimes.add(null);
            this.waitTimes.add(null);
        }
    }
    
    class Evictor extends TimerTask
    {
        @Override
        public void run() {
            final ClassLoader savedClassLoader = Thread.currentThread().getContextClassLoader();
            try {
                Thread.currentThread().setContextClassLoader(BaseGenericObjectPool.this.factoryClassLoader);
                try {
                    BaseGenericObjectPool.this.evict();
                }
                catch (Exception e) {
                    BaseGenericObjectPool.this.swallowException(e);
                }
                catch (OutOfMemoryError oome) {
                    oome.printStackTrace(System.err);
                }
                try {
                    BaseGenericObjectPool.this.ensureMinIdle();
                }
                catch (Exception e) {
                    BaseGenericObjectPool.this.swallowException(e);
                }
            }
            finally {
                Thread.currentThread().setContextClassLoader(savedClassLoader);
            }
        }
    }
}
