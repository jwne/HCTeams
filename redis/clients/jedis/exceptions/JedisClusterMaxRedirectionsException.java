package redis.clients.jedis.exceptions;

public class JedisClusterMaxRedirectionsException extends JedisDataException
{
    private static final long serialVersionUID = 3878126572474819403L;
    
    public JedisClusterMaxRedirectionsException(final Throwable cause) {
        super(cause);
    }
    
    public JedisClusterMaxRedirectionsException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public JedisClusterMaxRedirectionsException(final String message) {
        super(message);
    }
}
