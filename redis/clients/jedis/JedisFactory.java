package redis.clients.jedis;

import java.util.concurrent.atomic.*;
import org.apache.commons.pool2.*;
import org.apache.commons.pool2.impl.*;

class JedisFactory implements PooledObjectFactory<Jedis>
{
    private final AtomicReference<HostAndPort> hostAndPort;
    private final int timeout;
    private final String password;
    private final int database;
    private final String clientName;
    
    public JedisFactory(final String host, final int port, final int timeout, final String password, final int database) {
        this(host, port, timeout, password, database, null);
    }
    
    public JedisFactory(final String host, final int port, final int timeout, final String password, final int database, final String clientName) {
        super();
        (this.hostAndPort = new AtomicReference<HostAndPort>()).set(new HostAndPort(host, port));
        this.timeout = timeout;
        this.password = password;
        this.database = database;
        this.clientName = clientName;
    }
    
    public void setHostAndPort(final HostAndPort hostAndPort) {
        this.hostAndPort.set(hostAndPort);
    }
    
    @Override
    public void activateObject(final PooledObject<Jedis> pooledJedis) throws Exception {
        final BinaryJedis jedis = pooledJedis.getObject();
        if (jedis.getDB() != this.database) {
            jedis.select(this.database);
        }
    }
    
    @Override
    public void destroyObject(final PooledObject<Jedis> pooledJedis) throws Exception {
        final BinaryJedis jedis = pooledJedis.getObject();
        if (jedis.isConnected()) {
            try {
                try {
                    jedis.quit();
                }
                catch (Exception ex) {}
                jedis.disconnect();
            }
            catch (Exception ex2) {}
        }
    }
    
    @Override
    public PooledObject<Jedis> makeObject() throws Exception {
        final HostAndPort hostAndPort = this.hostAndPort.get();
        final Jedis jedis = new Jedis(hostAndPort.getHost(), hostAndPort.getPort(), this.timeout);
        jedis.connect();
        if (null != this.password) {
            jedis.auth(this.password);
        }
        if (this.database != 0) {
            jedis.select(this.database);
        }
        if (this.clientName != null) {
            jedis.clientSetname(this.clientName);
        }
        return new DefaultPooledObject<Jedis>(jedis);
    }
    
    @Override
    public void passivateObject(final PooledObject<Jedis> pooledJedis) throws Exception {
    }
    
    @Override
    public boolean validateObject(final PooledObject<Jedis> pooledJedis) {
        final BinaryJedis jedis = pooledJedis.getObject();
        try {
            final HostAndPort hostAndPort = this.hostAndPort.get();
            final String connectionHost = jedis.getClient().getHost();
            final int connectionPort = jedis.getClient().getPort();
            return hostAndPort.getHost().equals(connectionHost) && hostAndPort.getPort() == connectionPort && jedis.isConnected() && jedis.ping().equals("PONG");
        }
        catch (Exception e) {
            return false;
        }
    }
}
