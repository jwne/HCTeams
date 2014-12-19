package redis.clients.jedis.exceptions;

public class JedisDataException extends JedisException
{
    private static final long serialVersionUID = 3878126572474819403L;
    
    public JedisDataException(final String message) {
        super(message);
    }
    
    public JedisDataException(final Throwable cause) {
        super(cause);
    }
    
    public JedisDataException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
