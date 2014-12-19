package org.apache.commons.pool2.impl;

public class GenericKeyedObjectPoolConfig extends BaseObjectPoolConfig
{
    public static final int DEFAULT_MAX_TOTAL_PER_KEY = 8;
    public static final int DEFAULT_MAX_TOTAL = -1;
    public static final int DEFAULT_MIN_IDLE_PER_KEY = 0;
    public static final int DEFAULT_MAX_IDLE_PER_KEY = 8;
    private int minIdlePerKey;
    private int maxIdlePerKey;
    private int maxTotalPerKey;
    private int maxTotal;
    
    public GenericKeyedObjectPoolConfig() {
        super();
        this.minIdlePerKey = 0;
        this.maxIdlePerKey = 8;
        this.maxTotalPerKey = 8;
        this.maxTotal = -1;
    }
    
    public int getMaxTotal() {
        return this.maxTotal;
    }
    
    public void setMaxTotal(final int maxTotal) {
        this.maxTotal = maxTotal;
    }
    
    public int getMaxTotalPerKey() {
        return this.maxTotalPerKey;
    }
    
    public void setMaxTotalPerKey(final int maxTotalPerKey) {
        this.maxTotalPerKey = maxTotalPerKey;
    }
    
    public int getMinIdlePerKey() {
        return this.minIdlePerKey;
    }
    
    public void setMinIdlePerKey(final int minIdlePerKey) {
        this.minIdlePerKey = minIdlePerKey;
    }
    
    public int getMaxIdlePerKey() {
        return this.maxIdlePerKey;
    }
    
    public void setMaxIdlePerKey(final int maxIdlePerKey) {
        this.maxIdlePerKey = maxIdlePerKey;
    }
    
    public GenericKeyedObjectPoolConfig clone() {
        try {
            return (GenericKeyedObjectPoolConfig)super.clone();
        }
        catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
