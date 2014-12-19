package redis.clients.jedis.exceptions;

public class JedisClusterException extends JedisDataException
{
    private static final long serialVersionUID = 3878126572474819403L;
    
    public JedisClusterException(final Throwable cause) {
        super(cause);
    }
    
    public JedisClusterException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public JedisClusterException(final String message) {
        super(message);
    }
}
