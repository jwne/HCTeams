package org.apache.commons.pool2.impl;

public class GenericObjectPoolConfig extends BaseObjectPoolConfig
{
    public static final int DEFAULT_MAX_TOTAL = 8;
    public static final int DEFAULT_MAX_IDLE = 8;
    public static final int DEFAULT_MIN_IDLE = 0;
    private int maxTotal;
    private int maxIdle;
    private int minIdle;
    
    public GenericObjectPoolConfig() {
        super();
        this.maxTotal = 8;
        this.maxIdle = 8;
        this.minIdle = 0;
    }
    
    public int getMaxTotal() {
        return this.maxTotal;
    }
    
    public void setMaxTotal(final int maxTotal) {
        this.maxTotal = maxTotal;
    }
    
    public int getMaxIdle() {
        return this.maxIdle;
    }
    
    public void setMaxIdle(final int maxIdle) {
        this.maxIdle = maxIdle;
    }
    
    public int getMinIdle() {
        return this.minIdle;
    }
    
    public void setMinIdle(final int minIdle) {
        this.minIdle = minIdle;
    }
    
    public GenericObjectPoolConfig clone() {
        try {
            return (GenericObjectPoolConfig)super.clone();
        }
        catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
