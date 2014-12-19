package org.apache.commons.pool2.impl;

public abstract class BaseObjectPoolConfig implements Cloneable
{
    public static final boolean DEFAULT_LIFO = true;
    public static final long DEFAULT_MAX_WAIT_MILLIS = -1L;
    public static final long DEFAULT_MIN_EVICTABLE_IDLE_TIME_MILLIS = 1800000L;
    public static final long DEFAULT_SOFT_MIN_EVICTABLE_IDLE_TIME_MILLIS = -1L;
    public static final int DEFAULT_NUM_TESTS_PER_EVICTION_RUN = 3;
    public static final boolean DEFAULT_TEST_ON_CREATE = false;
    public static final boolean DEFAULT_TEST_ON_BORROW = false;
    public static final boolean DEFAULT_TEST_ON_RETURN = false;
    public static final boolean DEFAULT_TEST_WHILE_IDLE = false;
    public static final long DEFAULT_TIME_BETWEEN_EVICTION_RUNS_MILLIS = -1L;
    public static final boolean DEFAULT_BLOCK_WHEN_EXHAUSTED = true;
    public static final boolean DEFAULT_JMX_ENABLE = true;
    public static final String DEFAULT_JMX_NAME_PREFIX = "pool";
    public static final String DEFAULT_JMX_NAME_BASE;
    public static final String DEFAULT_EVICTION_POLICY_CLASS_NAME = "org.apache.commons.pool2.impl.DefaultEvictionPolicy";
    private boolean lifo;
    private long maxWaitMillis;
    private long minEvictableIdleTimeMillis;
    private long softMinEvictableIdleTimeMillis;
    private int numTestsPerEvictionRun;
    private String evictionPolicyClassName;
    private boolean testOnCreate;
    private boolean testOnBorrow;
    private boolean testOnReturn;
    private boolean testWhileIdle;
    private long timeBetweenEvictionRunsMillis;
    private boolean blockWhenExhausted;
    private boolean jmxEnabled;
    private String jmxNamePrefix;
    private String jmxNameBase;
    
    public BaseObjectPoolConfig() {
        super();
        this.lifo = true;
        this.maxWaitMillis = -1L;
        this.minEvictableIdleTimeMillis = 1800000L;
        this.softMinEvictableIdleTimeMillis = 1800000L;
        this.numTestsPerEvictionRun = 3;
        this.evictionPolicyClassName = "org.apache.commons.pool2.impl.DefaultEvictionPolicy";
        this.testOnCreate = false;
        this.testOnBorrow = false;
        this.testOnReturn = false;
        this.testWhileIdle = false;
        this.timeBetweenEvictionRunsMillis = -1L;
        this.blockWhenExhausted = true;
        this.jmxEnabled = true;
        this.jmxNamePrefix = "pool";
        this.jmxNameBase = "pool";
    }
    
    public boolean getLifo() {
        return this.lifo;
    }
    
    public void setLifo(final boolean lifo) {
        this.lifo = lifo;
    }
    
    public long getMaxWaitMillis() {
        return this.maxWaitMillis;
    }
    
    public void setMaxWaitMillis(final long maxWaitMillis) {
        this.maxWaitMillis = maxWaitMillis;
    }
    
    public long getMinEvictableIdleTimeMillis() {
        return this.minEvictableIdleTimeMillis;
    }
    
    public void setMinEvictableIdleTimeMillis(final long minEvictableIdleTimeMillis) {
        this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
    }
    
    public long getSoftMinEvictableIdleTimeMillis() {
        return this.softMinEvictableIdleTimeMillis;
    }
    
    public void setSoftMinEvictableIdleTimeMillis(final long softMinEvictableIdleTimeMillis) {
        this.softMinEvictableIdleTimeMillis = softMinEvictableIdleTimeMillis;
    }
    
    public int getNumTestsPerEvictionRun() {
        return this.numTestsPerEvictionRun;
    }
    
    public void setNumTestsPerEvictionRun(final int numTestsPerEvictionRun) {
        this.numTestsPerEvictionRun = numTestsPerEvictionRun;
    }
    
    public boolean getTestOnCreate() {
        return this.testOnCreate;
    }
    
    public void setTestOnCreate(final boolean testOnCreate) {
        this.testOnCreate = testOnCreate;
    }
    
    public boolean getTestOnBorrow() {
        return this.testOnBorrow;
    }
    
    public void setTestOnBorrow(final boolean testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
    }
    
    public boolean getTestOnReturn() {
        return this.testOnReturn;
    }
    
    public void setTestOnReturn(final boolean testOnReturn) {
        this.testOnReturn = testOnReturn;
    }
    
    public boolean getTestWhileIdle() {
        return this.testWhileIdle;
    }
    
    public void setTestWhileIdle(final boolean testWhileIdle) {
        this.testWhileIdle = testWhileIdle;
    }
    
    public long getTimeBetweenEvictionRunsMillis() {
        return this.timeBetweenEvictionRunsMillis;
    }
    
    public void setTimeBetweenEvictionRunsMillis(final long timeBetweenEvictionRunsMillis) {
        this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
    }
    
    public String getEvictionPolicyClassName() {
        return this.evictionPolicyClassName;
    }
    
    public void setEvictionPolicyClassName(final String evictionPolicyClassName) {
        this.evictionPolicyClassName = evictionPolicyClassName;
    }
    
    public boolean getBlockWhenExhausted() {
        return this.blockWhenExhausted;
    }
    
    public void setBlockWhenExhausted(final boolean blockWhenExhausted) {
        this.blockWhenExhausted = blockWhenExhausted;
    }
    
    public boolean getJmxEnabled() {
        return this.jmxEnabled;
    }
    
    public void setJmxEnabled(final boolean jmxEnabled) {
        this.jmxEnabled = jmxEnabled;
    }
    
    public String getJmxNameBase() {
        return this.jmxNameBase;
    }
    
    public void setJmxNameBase(final String jmxNameBase) {
        this.jmxNameBase = jmxNameBase;
    }
    
    public String getJmxNamePrefix() {
        return this.jmxNamePrefix;
    }
    
    public void setJmxNamePrefix(final String jmxNamePrefix) {
        this.jmxNamePrefix = jmxNamePrefix;
    }
    
    static {
        DEFAULT_JMX_NAME_BASE = null;
    }
}
