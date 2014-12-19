package redis.clients.jedis;

import java.util.*;
import redis.clients.util.*;

public class ShardedJedisPipeline extends PipelineBase
{
    private BinaryShardedJedis jedis;
    private List<FutureResult> results;
    private Queue<Client> clients;
    
    public ShardedJedisPipeline() {
        super();
        this.results = new ArrayList<FutureResult>();
        this.clients = new LinkedList<Client>();
    }
    
    public void setShardedJedis(final BinaryShardedJedis jedis) {
        this.jedis = jedis;
    }
    
    public List<Object> getResults() {
        final List<Object> r = new ArrayList<Object>();
        for (final FutureResult fr : this.results) {
            r.add(fr.get());
        }
        return r;
    }
    
    public void sync() {
        for (final Client client : this.clients) {
            this.generateResponse(client.getOne());
        }
    }
    
    public List<Object> syncAndReturnAll() {
        final List<Object> formatted = new ArrayList<Object>();
        for (final Client client : this.clients) {
            formatted.add(this.generateResponse(client.getOne()).get());
        }
        return formatted;
    }
    
    @Deprecated
    public void execute() {
    }
    
    @Override
    protected Client getClient(final String key) {
        final Client client = ((Sharded<Jedis, S>)this.jedis).getShard(key).getClient();
        this.clients.add(client);
        this.results.add(new FutureResult(client));
        return client;
    }
    
    @Override
    protected Client getClient(final byte[] key) {
        final Client client = ((Sharded<Jedis, S>)this.jedis).getShard(key).getClient();
        this.clients.add(client);
        this.results.add(new FutureResult(client));
        return client;
    }
    
    private static class FutureResult
    {
        private Client client;
        
        public FutureResult(final Client client) {
            super();
            this.client = client;
        }
        
        public Object get() {
            return this.client.getOne();
        }
    }
}
