package redis.clients.jedis;

import redis.clients.jedis.exceptions.*;
import java.util.*;

public abstract class BinaryJedisPubSub
{
    private int subscribedChannels;
    private Client client;
    
    public BinaryJedisPubSub() {
        super();
        this.subscribedChannels = 0;
    }
    
    public void onMessage(final byte[] channel, final byte[] message) {
    }
    
    public void onPMessage(final byte[] pattern, final byte[] channel, final byte[] message) {
    }
    
    public void onSubscribe(final byte[] channel, final int subscribedChannels) {
    }
    
    public void onUnsubscribe(final byte[] channel, final int subscribedChannels) {
    }
    
    public void onPUnsubscribe(final byte[] pattern, final int subscribedChannels) {
    }
    
    public void onPSubscribe(final byte[] pattern, final int subscribedChannels) {
    }
    
    public void unsubscribe() {
        this.client.unsubscribe();
        this.client.flush();
    }
    
    public void unsubscribe(final byte[]... channels) {
        this.client.unsubscribe(channels);
        this.client.flush();
    }
    
    public void subscribe(final byte[]... channels) {
        this.client.subscribe(channels);
        this.client.flush();
    }
    
    public void psubscribe(final byte[]... patterns) {
        this.client.psubscribe(patterns);
        this.client.flush();
    }
    
    public void punsubscribe() {
        this.client.punsubscribe();
        this.client.flush();
    }
    
    public void punsubscribe(final byte[]... patterns) {
        this.client.punsubscribe(patterns);
        this.client.flush();
    }
    
    public boolean isSubscribed() {
        return this.subscribedChannels > 0;
    }
    
    public void proceedWithPatterns(final Client client, final byte[]... patterns) {
        (this.client = client).psubscribe(patterns);
        this.process(client);
    }
    
    public void proceed(final Client client, final byte[]... channels) {
        (this.client = client).subscribe(channels);
        this.process(client);
    }
    
    private void process(final Client client) {
        do {
            final List<Object> reply = client.getObjectMultiBulkReply();
            final Object firstObj = reply.get(0);
            if (!(firstObj instanceof byte[])) {
                throw new JedisException("Unknown message type: " + firstObj);
            }
            final byte[] resp = (byte[])firstObj;
            if (Arrays.equals(Protocol.Keyword.SUBSCRIBE.raw, resp)) {
                this.subscribedChannels = (int)(Object)reply.get(2);
                final byte[] bchannel = reply.get(1);
                this.onSubscribe(bchannel, this.subscribedChannels);
            }
            else if (Arrays.equals(Protocol.Keyword.UNSUBSCRIBE.raw, resp)) {
                this.subscribedChannels = (int)(Object)reply.get(2);
                final byte[] bchannel = reply.get(1);
                this.onUnsubscribe(bchannel, this.subscribedChannels);
            }
            else if (Arrays.equals(Protocol.Keyword.MESSAGE.raw, resp)) {
                final byte[] bchannel = reply.get(1);
                final byte[] bmesg = reply.get(2);
                this.onMessage(bchannel, bmesg);
            }
            else if (Arrays.equals(Protocol.Keyword.PMESSAGE.raw, resp)) {
                final byte[] bpattern = reply.get(1);
                final byte[] bchannel2 = reply.get(2);
                final byte[] bmesg2 = reply.get(3);
                this.onPMessage(bpattern, bchannel2, bmesg2);
            }
            else if (Arrays.equals(Protocol.Keyword.PSUBSCRIBE.raw, resp)) {
                this.subscribedChannels = (int)(Object)reply.get(2);
                final byte[] bpattern = reply.get(1);
                this.onPSubscribe(bpattern, this.subscribedChannels);
            }
            else {
                if (!Arrays.equals(Protocol.Keyword.PUNSUBSCRIBE.raw, resp)) {
                    throw new JedisException("Unknown message type: " + firstObj);
                }
                this.subscribedChannels = (int)(Object)reply.get(2);
                final byte[] bpattern = reply.get(1);
                this.onPUnsubscribe(bpattern, this.subscribedChannels);
            }
        } while (this.isSubscribed());
    }
    
    public int getSubscribedChannels() {
        return this.subscribedChannels;
    }
}
