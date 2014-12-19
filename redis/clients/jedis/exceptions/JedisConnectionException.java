package redis.clients.jedis.exceptions;

public class JedisConnectionException extends JedisException
{
    private static final long serialVersionUID = 3878126572474819403L;
    
    public JedisConnectionException(final String message) {
        super(message);
    }
    
    public JedisConnectionException(final Throwable cause) {
        super(cause);
    }
    
    public JedisConnectionException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
