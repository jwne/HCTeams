package com.mongodb.util;

import com.mongodb.*;

@Deprecated
public class ConnectionPoolStatisticsBean
{
    private final int total;
    private final int inUse;
    private final InUseConnectionBean[] inUseConnections;
    
    public ConnectionPoolStatisticsBean(final int total, final int inUse, final InUseConnectionBean[] inUseConnections) {
        super();
        this.total = total;
        this.inUse = inUse;
        this.inUseConnections = inUseConnections;
    }
    
    public int getTotal() {
        return this.total;
    }
    
    public int getInUse() {
        return this.inUse;
    }
    
    public InUseConnectionBean[] getInUseConnections() {
        return this.inUseConnections;
    }
}
