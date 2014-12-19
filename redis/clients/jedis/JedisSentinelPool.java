package redis.clients.jedis;

import redis.clients.util.*;
import org.apache.commons.pool2.impl.*;
import java.util.logging.*;
import org.apache.commons.pool2.*;
import redis.clients.jedis.exceptions.*;
import java.util.*;
import java.util.concurrent.atomic.*;

public class JedisSentinelPool extends Pool<Jedis>
{
    protected GenericObjectPoolConfig poolConfig;
    protected int timeout;
    protected String password;
    protected int database;
    protected Set<MasterListener> masterListeners;
    protected Logger log;
    private volatile JedisFactory factory;
    private volatile HostAndPort currentHostMaster;
    
    public JedisSentinelPool(final String masterName, final Set<String> sentinels, final GenericObjectPoolConfig poolConfig) {
        this(masterName, sentinels, poolConfig, 2000, null, 0);
    }
    
    public JedisSentinelPool(final String masterName, final Set<String> sentinels) {
        this(masterName, sentinels, new GenericObjectPoolConfig(), 2000, null, 0);
    }
    
    public JedisSentinelPool(final String masterName, final Set<String> sentinels, final String password) {
        this(masterName, sentinels, new GenericObjectPoolConfig(), 2000, password);
    }
    
    public JedisSentinelPool(final String masterName, final Set<String> sentinels, final GenericObjectPoolConfig poolConfig, final int timeout, final String password) {
        this(masterName, sentinels, poolConfig, timeout, password, 0);
    }
    
    public JedisSentinelPool(final String masterName, final Set<String> sentinels, final GenericObjectPoolConfig poolConfig, final int timeout) {
        this(masterName, sentinels, poolConfig, timeout, null, 0);
    }
    
    public JedisSentinelPool(final String masterName, final Set<String> sentinels, final GenericObjectPoolConfig poolConfig, final String password) {
        this(masterName, sentinels, poolConfig, 2000, password);
    }
    
    public JedisSentinelPool(final String masterName, final Set<String> sentinels, final GenericObjectPoolConfig poolConfig, final int timeout, final String password, final int database) {
        super();
        this.timeout = 2000;
        this.database = 0;
        this.masterListeners = new HashSet<MasterListener>();
        this.log = Logger.getLogger(this.getClass().getName());
        this.poolConfig = poolConfig;
        this.timeout = timeout;
        this.password = password;
        this.database = database;
        final HostAndPort master = this.initSentinels(sentinels, masterName);
        this.initPool(master);
    }
    
    @Override
    public void destroy() {
        for (final MasterListener m : this.masterListeners) {
            m.shutdown();
        }
        super.destroy();
    }
    
    public HostAndPort getCurrentHostMaster() {
        return this.currentHostMaster;
    }
    
    private void initPool(final HostAndPort master) {
        if (!master.equals(this.currentHostMaster)) {
            this.currentHostMaster = master;
            if (this.factory == null) {
                this.factory = new JedisFactory(master.getHost(), master.getPort(), this.timeout, this.password, this.database);
                this.initPool(this.poolConfig, this.factory);
            }
            else {
                this.factory.setHostAndPort(this.currentHostMaster);
                this.internalPool.clear();
            }
            this.log.info("Created JedisPool to master at " + master);
        }
    }
    
    private HostAndPort initSentinels(final Set<String> sentinels, final String masterName) {
        HostAndPort master = null;
        boolean sentinelAvailable = false;
        this.log.info("Trying to find master from available Sentinels...");
        for (final String sentinel : sentinels) {
            final HostAndPort hap = this.toHostAndPort(Arrays.asList(sentinel.split(":")));
            this.log.fine("Connecting to Sentinel " + hap);
            Jedis jedis = null;
            try {
                jedis = new Jedis(hap.getHost(), hap.getPort());
                final List<String> masterAddr = jedis.sentinelGetMasterAddrByName(masterName);
                sentinelAvailable = true;
                if (masterAddr != null && masterAddr.size() == 2) {
                    master = this.toHostAndPort(masterAddr);
                    this.log.fine("Found Redis master at " + master);
                    break;
                }
                this.log.warning("Can not get master addr, master name: " + masterName + ". Sentinel: " + hap + ".");
            }
            catch (JedisConnectionException e) {
                this.log.warning("Cannot connect to sentinel running @ " + hap + ". Trying next one.");
            }
            finally {
                if (jedis != null) {
                    jedis.close();
                }
            }
        }
        if (master != null) {
            this.log.info("Redis master running at " + master + ", starting Sentinel listeners...");
            for (final String sentinel : sentinels) {
                final HostAndPort hap = this.toHostAndPort(Arrays.asList(sentinel.split(":")));
                final MasterListener masterListener = new MasterListener(masterName, hap.getHost(), hap.getPort());
                this.masterListeners.add(masterListener);
                masterListener.start();
            }
            return master;
        }
        if (sentinelAvailable) {
            throw new JedisException("Can connect to sentinel, but " + masterName + " seems to be not monitored...");
        }
        throw new JedisConnectionException("All sentinels down, cannot determine where is " + masterName + " master is running...");
    }
    
    private HostAndPort toHostAndPort(final List<String> getMasterAddrByNameResult) {
        final String host = getMasterAddrByNameResult.get(0);
        final int port = Integer.parseInt(getMasterAddrByNameResult.get(1));
        return new HostAndPort(host, port);
    }
    
    @Override
    public Jedis getResource() {
        Jedis jedis;
        while (true) {
            jedis = super.getResource();
            jedis.setDataSource(this);
            final HostAndPort master = this.currentHostMaster;
            final HostAndPort connection = new HostAndPort(jedis.getClient().getHost(), jedis.getClient().getPort());
            if (master.equals(connection)) {
                break;
            }
            this.returnBrokenResource(jedis);
        }
        return jedis;
    }
    
    @Override
    public void returnBrokenResource(final Jedis resource) {
        if (resource != null) {
            this.returnBrokenResourceObject(resource);
        }
    }
    
    @Override
    public void returnResource(final Jedis resource) {
        if (resource != null) {
            resource.resetState();
            this.returnResourceObject(resource);
        }
    }
    
    protected class MasterListener extends Thread
    {
        protected String masterName;
        protected String host;
        protected int port;
        protected long subscribeRetryWaitTimeMillis;
        protected Jedis j;
        protected AtomicBoolean running;
        
        protected MasterListener() {
            super();
            this.subscribeRetryWaitTimeMillis = 5000L;
            this.running = new AtomicBoolean(false);
        }
        
        public MasterListener(final String masterName, final String host, final int port) {
            super();
            this.subscribeRetryWaitTimeMillis = 5000L;
            this.running = new AtomicBoolean(false);
            this.masterName = masterName;
            this.host = host;
            this.port = port;
        }
        
        public MasterListener(final JedisSentinelPool jedisSentinelPool, final String masterName, final String host, final int port, final long subscribeRetryWaitTimeMillis) {
            this(jedisSentinelPool, masterName, host, port);
            this.subscribeRetryWaitTimeMillis = subscribeRetryWaitTimeMillis;
        }
        
        @Override
        public void run() {
            this.running.set(true);
            while (this.running.get()) {
                this.j = new Jedis(this.host, this.port);
                try {
                    this.j.subscribe(new JedisPubSub() {
                        @Override
                        public void onMessage(final String channel, final String message) {
                            JedisSentinelPool.this.log.fine("Sentinel " + MasterListener.this.host + ":" + MasterListener.this.port + " published: " + message + ".");
                            final String[] switchMasterMsg = message.split(" ");
                            if (switchMasterMsg.length > 3) {
                                if (MasterListener.this.masterName.equals(switchMasterMsg[0])) {
                                    JedisSentinelPool.this.initPool(JedisSentinelPool.this.toHostAndPort(Arrays.asList(switchMasterMsg[3], switchMasterMsg[4])));
                                }
                                else {
                                    JedisSentinelPool.this.log.fine("Ignoring message on +switch-master for master name " + switchMasterMsg[0] + ", our master name is " + MasterListener.this.masterName);
                                }
                            }
                            else {
                                JedisSentinelPool.this.log.severe("Invalid message received on Sentinel " + MasterListener.this.host + ":" + MasterListener.this.port + " on channel +switch-master: " + message);
                            }
                        }
                    }, "+switch-master");
                }
                catch (JedisConnectionException e2) {
                    if (this.running.get()) {
                        JedisSentinelPool.this.log.severe("Lost connection to Sentinel at " + this.host + ":" + this.port + ". Sleeping 5000ms and retrying.");
                        try {
                            Thread.sleep(this.subscribeRetryWaitTimeMillis);
                        }
                        catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                    }
                    else {
                        JedisSentinelPool.this.log.fine("Unsubscribing from Sentinel at " + this.host + ":" + this.port);
                    }
                }
            }
        }
        
        public void shutdown() {
            try {
                JedisSentinelPool.this.log.fine("Shutting down listener on " + this.host + ":" + this.port);
                this.running.set(false);
                this.j.disconnect();
            }
            catch (Exception e) {
                JedisSentinelPool.this.log.severe("Caught exception while shutting down: " + e.getMessage());
            }
        }
    }
}
