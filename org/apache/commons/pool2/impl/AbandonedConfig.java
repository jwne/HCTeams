package org.apache.commons.pool2.impl;

import java.io.*;

public class AbandonedConfig
{
    private boolean removeAbandonedOnBorrow;
    private boolean removeAbandonedOnMaintenance;
    private int removeAbandonedTimeout;
    private boolean logAbandoned;
    private PrintWriter logWriter;
    private boolean useUsageTracking;
    
    public AbandonedConfig() {
        super();
        this.removeAbandonedOnBorrow = false;
        this.removeAbandonedOnMaintenance = false;
        this.removeAbandonedTimeout = 300;
        this.logAbandoned = false;
        this.logWriter = new PrintWriter(System.out);
        this.useUsageTracking = false;
    }
    
    public boolean getRemoveAbandonedOnBorrow() {
        return this.removeAbandonedOnBorrow;
    }
    
    public void setRemoveAbandonedOnBorrow(final boolean removeAbandonedOnBorrow) {
        this.removeAbandonedOnBorrow = removeAbandonedOnBorrow;
    }
    
    public boolean getRemoveAbandonedOnMaintenance() {
        return this.removeAbandonedOnMaintenance;
    }
    
    public void setRemoveAbandonedOnMaintenance(final boolean removeAbandonedOnMaintenance) {
        this.removeAbandonedOnMaintenance = removeAbandonedOnMaintenance;
    }
    
    public int getRemoveAbandonedTimeout() {
        return this.removeAbandonedTimeout;
    }
    
    public void setRemoveAbandonedTimeout(final int removeAbandonedTimeout) {
        this.removeAbandonedTimeout = removeAbandonedTimeout;
    }
    
    public boolean getLogAbandoned() {
        return this.logAbandoned;
    }
    
    public void setLogAbandoned(final boolean logAbandoned) {
        this.logAbandoned = logAbandoned;
    }
    
    public PrintWriter getLogWriter() {
        return this.logWriter;
    }
    
    public void setLogWriter(final PrintWriter logWriter) {
        this.logWriter = logWriter;
    }
    
    public boolean getUseUsageTracking() {
        return this.useUsageTracking;
    }
    
    public void setUseUsageTracking(final boolean useUsageTracking) {
        this.useUsageTracking = useUsageTracking;
    }
}
