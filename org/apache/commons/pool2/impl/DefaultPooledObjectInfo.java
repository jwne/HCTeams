package org.apache.commons.pool2.impl;

import org.apache.commons.pool2.*;
import java.text.*;
import java.io.*;

public class DefaultPooledObjectInfo implements DefaultPooledObjectInfoMBean
{
    private final PooledObject<?> pooledObject;
    
    public DefaultPooledObjectInfo(final PooledObject<?> pooledObject) {
        super();
        this.pooledObject = pooledObject;
    }
    
    @Override
    public long getCreateTime() {
        return this.pooledObject.getCreateTime();
    }
    
    @Override
    public String getCreateTimeFormatted() {
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
        return sdf.format(this.pooledObject.getCreateTime());
    }
    
    @Override
    public long getLastBorrowTime() {
        return this.pooledObject.getLastBorrowTime();
    }
    
    @Override
    public String getLastBorrowTimeFormatted() {
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
        return sdf.format(this.pooledObject.getLastBorrowTime());
    }
    
    @Override
    public String getLastBorrowTrace() {
        final StringWriter sw = new StringWriter();
        this.pooledObject.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }
    
    @Override
    public long getLastReturnTime() {
        return this.pooledObject.getLastReturnTime();
    }
    
    @Override
    public String getLastReturnTimeFormatted() {
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
        return sdf.format(this.pooledObject.getLastReturnTime());
    }
    
    @Override
    public String getPooledObjectType() {
        return this.pooledObject.getObject().getClass().getName();
    }
    
    @Override
    public String getPooledObjectToString() {
        return this.pooledObject.getObject().toString();
    }
    
    @Override
    public long getBorrowedCount() {
        if (this.pooledObject instanceof DefaultPooledObject) {
            return ((DefaultPooledObject)this.pooledObject).getBorrowedCount();
        }
        return -1L;
    }
}
