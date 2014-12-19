package redis.clients.jedis.exceptions;

import redis.clients.jedis.*;

public class JedisRedirectionException extends JedisDataException
{
    private static final long serialVersionUID = 3878126572474819403L;
    private HostAndPort targetNode;
    private int slot;
    
    public JedisRedirectionException(final String message, final HostAndPort targetNode, final int slot) {
        super(message);
        this.targetNode = targetNode;
        this.slot = slot;
    }
    
    public JedisRedirectionException(final Throwable cause, final HostAndPort targetNode, final int slot) {
        super(cause);
        this.targetNode = targetNode;
        this.slot = slot;
    }
    
    public JedisRedirectionException(final String message, final Throwable cause, final HostAndPort targetNode, final int slot) {
        super(message, cause);
        this.targetNode = targetNode;
        this.slot = slot;
    }
    
    public HostAndPort getTargetNode() {
        return this.targetNode;
    }
    
    public int getSlot() {
        return this.slot;
    }
}
