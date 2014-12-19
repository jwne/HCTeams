package com.mongodb;

import com.mongodb.util.*;
import java.util.concurrent.*;

@Deprecated
public class DBPortPool extends SimplePool<DBPort>
{
    final MongoOptions _options;
    private final Semaphore _waitingSem;
    final ServerAddress _addr;
    
    public String getHost() {
        return this._addr.getHost();
    }
    
    public int getPort() {
        return this._addr.getPort();
    }
    
    DBPortPool(final ServerAddress addr, final MongoOptions options) {
        super("DBPortPool-" + addr.toString() + ", options = " + options.toString(), options.connectionsPerHost);
        this._options = options;
        this._addr = addr;
        this._waitingSem = new Semaphore(this._options.connectionsPerHost * this._options.threadsAllowedToBlockForConnectionMultiplier);
    }
    
    public DBPort get() {
        DBPort port = null;
        if (!this._waitingSem.tryAcquire()) {
            throw new SemaphoresOut(this._options.connectionsPerHost * this._options.threadsAllowedToBlockForConnectionMultiplier);
        }
        try {
            port = this.get(this._options.maxWaitTime);
        }
        catch (InterruptedException e) {
            throw new MongoInterruptedException(e);
        }
        finally {
            this._waitingSem.release();
        }
        if (port == null) {
            throw new ConnectionWaitTimeOut(this._options.maxWaitTime);
        }
        return port;
    }
    
    public void cleanup(final DBPort p) {
        p.close();
    }
    
    protected DBPort createNew() {
        return new DBPort(this._addr);
    }
    
    public ServerAddress getServerAddress() {
        return this._addr;
    }
    
    @Deprecated
    public static class NoMoreConnection extends MongoClientException
    {
        private static final long serialVersionUID = -4415279469780082174L;
        
        public NoMoreConnection(final String msg) {
            super(msg);
        }
    }
    
    @Deprecated
    public static class SemaphoresOut extends MongoWaitQueueFullException
    {
        private static final long serialVersionUID = -4415279469780082174L;
        private static final String message = "Concurrent requests for database connection have exceeded limit";
        
        SemaphoresOut() {
            super("Concurrent requests for database connection have exceeded limit");
        }
        
        SemaphoresOut(final int numPermits) {
            super("Concurrent requests for database connection have exceeded limit of " + numPermits);
        }
    }
    
    @Deprecated
    public static class ConnectionWaitTimeOut extends MongoTimeoutException
    {
        private static final long serialVersionUID = -4415279469780082174L;
        
        ConnectionWaitTimeOut(final int timeout) {
            super("Connection wait timeout after " + timeout + " ms");
        }
    }
}
