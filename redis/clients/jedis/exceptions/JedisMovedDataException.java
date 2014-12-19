package redis.clients.jedis.exceptions;

import redis.clients.jedis.*;

public class JedisMovedDataException extends JedisRedirectionException
{
    private static final long serialVersionUID = 3878126572474819403L;
    
    public JedisMovedDataException(final String message, final HostAndPort targetNode, final int slot) {
        super(message, targetNode, slot);
    }
    
    public JedisMovedDataException(final Throwable cause, final HostAndPort targetNode, final int slot) {
        super(cause, targetNode, slot);
    }
    
    public JedisMovedDataException(final String message, final Throwable cause, final HostAndPort targetNode, final int slot) {
        super(message, cause, targetNode, slot);
    }
}
