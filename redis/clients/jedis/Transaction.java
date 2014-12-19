package redis.clients.jedis;

import redis.clients.jedis.exceptions.*;
import java.util.*;

public class Transaction extends MultiKeyPipelineBase
{
    protected boolean inTransaction;
    
    protected Transaction() {
        super();
        this.inTransaction = true;
    }
    
    public Transaction(final Client client) {
        super();
        this.inTransaction = true;
        this.client = client;
    }
    
    @Override
    protected Client getClient(final String key) {
        return this.client;
    }
    
    @Override
    protected Client getClient(final byte[] key) {
        return this.client;
    }
    
    public List<Object> exec() {
        this.client.exec();
        this.client.getAll(1);
        final List<Object> unformatted = this.client.getObjectMultiBulkReply();
        if (unformatted == null) {
            return null;
        }
        final List<Object> formatted = new ArrayList<Object>();
        for (final Object o : unformatted) {
            try {
                formatted.add(this.generateResponse(o).get());
            }
            catch (JedisDataException e) {
                formatted.add(e);
            }
        }
        return formatted;
    }
    
    public List<Response<?>> execGetResponse() {
        this.client.exec();
        this.client.getAll(1);
        final List<Object> unformatted = this.client.getObjectMultiBulkReply();
        if (unformatted == null) {
            return null;
        }
        final List<Response<?>> response = new ArrayList<Response<?>>();
        for (final Object o : unformatted) {
            response.add(this.generateResponse(o));
        }
        return response;
    }
    
    public String discard() {
        this.client.discard();
        this.client.getAll(1);
        this.inTransaction = false;
        this.clean();
        return this.client.getStatusCodeReply();
    }
}
