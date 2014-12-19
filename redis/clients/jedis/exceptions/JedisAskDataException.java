package redis.clients.jedis.exceptions;

import redis.clients.jedis.*;

public class JedisAskDataException extends JedisRedirectionException
{
    private static final long serialVersionUID = 3878126572474819403L;
    
    public JedisAskDataException(final Throwable cause, final HostAndPort targetHost, final int slot) {
        super(cause, targetHost, slot);
    }
    
    public JedisAskDataException(final String message, final Throwable cause, final HostAndPort targetHost, final int slot) {
        super(message, cause, targetHost, slot);
    }
    
    public JedisAskDataException(final String message, final HostAndPort targetHost, final int slot) {
        super(message, targetHost, slot);
    }
}
