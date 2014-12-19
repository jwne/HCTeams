package redis.clients.jedis;

import redis.clients.util.*;
import redis.clients.jedis.exceptions.*;

public abstract class JedisClusterCommand<T>
{
    private JedisClusterConnectionHandler connectionHandler;
    private int commandTimeout;
    private int redirections;
    private ThreadLocal<Jedis> askConnection;
    
    public JedisClusterCommand(final JedisClusterConnectionHandler connectionHandler, final int timeout, final int maxRedirections) {
        super();
        this.askConnection = new ThreadLocal<Jedis>();
        this.connectionHandler = connectionHandler;
        this.commandTimeout = timeout;
        this.redirections = maxRedirections;
    }
    
    public abstract T execute(final Jedis p0);
    
    public T run(final String key) {
        if (key == null) {
            throw new JedisClusterException("No way to dispatch this command to Redis Cluster.");
        }
        return this.runWithRetries(key, this.redirections, false, false);
    }
    
    private T runWithRetries(final String key, final int redirections, final boolean tryRandomNode, boolean asking) {
        if (redirections <= 0) {
            throw new JedisClusterMaxRedirectionsException("Too many Cluster redirections?");
        }
        Jedis connection = null;
        try {
            if (asking) {
                connection = this.askConnection.get();
                connection.asking();
                asking = false;
            }
            else if (tryRandomNode) {
                connection = this.connectionHandler.getConnection();
            }
            else {
                connection = this.connectionHandler.getConnectionFromSlot(JedisClusterCRC16.getSlot(key));
            }
            return this.execute(connection);
        }
        catch (JedisConnectionException jce) {
            if (tryRandomNode) {
                throw jce;
            }
            this.releaseConnection(connection, true);
            connection = null;
            return this.runWithRetries(key, redirections - 1, true, asking);
        }
        catch (JedisRedirectionException jre) {
            if (jre instanceof JedisAskDataException) {
                asking = true;
                this.askConnection.set(this.connectionHandler.getConnectionFromNode(jre.getTargetNode()));
            }
            else {
                if (!(jre instanceof JedisMovedDataException)) {
                    throw new JedisClusterException(jre);
                }
                this.connectionHandler.renewSlotCache();
            }
            this.releaseConnection(connection, false);
            connection = null;
            return this.runWithRetries(key, redirections - 1, false, asking);
        }
        finally {
            this.releaseConnection(connection, false);
        }
    }
    
    private void releaseConnection(final Jedis connection, final boolean broken) {
        if (connection != null) {
            if (broken) {
                this.connectionHandler.returnBrokenConnection(connection);
            }
            else {
                this.connectionHandler.returnConnection(connection);
            }
        }
    }
}
