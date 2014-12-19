package redis.clients.jedis;

import redis.clients.jedis.exceptions.*;

@Deprecated
public abstract class TransactionBlock extends Transaction
{
    public TransactionBlock(final Client client) {
        super(client);
    }
    
    public TransactionBlock() {
        super();
    }
    
    public abstract void execute() throws JedisException;
    
    public void setClient(final Client client) {
        this.client = client;
    }
}
