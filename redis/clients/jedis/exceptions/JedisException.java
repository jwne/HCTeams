package redis.clients.jedis.exceptions;

public class JedisException extends RuntimeException
{
    private static final long serialVersionUID = -2946266495682282677L;
    
    public JedisException(final String message) {
        super(message);
    }
    
    public JedisException(final Throwable e) {
        super(e);
    }
    
    public JedisException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
