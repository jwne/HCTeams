package redis.clients.jedis;

import redis.clients.jedis.exceptions.*;
import redis.clients.util.*;
import java.util.*;

public abstract class JedisPubSub
{
    private int subscribedChannels;
    private volatile Client client;
    
    public JedisPubSub() {
        super();
        this.subscribedChannels = 0;
    }
    
    public void onMessage(final String channel, final String message) {
    }
    
    public void onPMessage(final String pattern, final String channel, final String message) {
    }
    
    public void onSubscribe(final String channel, final int subscribedChannels) {
    }
    
    public void onUnsubscribe(final String channel, final int subscribedChannels) {
    }
    
    public void onPUnsubscribe(final String pattern, final int subscribedChannels) {
    }
    
    public void onPSubscribe(final String pattern, final int subscribedChannels) {
    }
    
    public void unsubscribe() {
        if (this.client == null) {
            throw new JedisConnectionException("JedisPubSub was not subscribed to a Jedis instance.");
        }
        this.client.unsubscribe();
        this.client.flush();
    }
    
    public void unsubscribe(final String... channels) {
        if (this.client == null) {
            throw new JedisConnectionException("JedisPubSub is not subscribed to a Jedis instance.");
        }
        this.client.unsubscribe(channels);
        this.client.flush();
    }
    
    public void subscribe(final String... channels) {
        if (this.client == null) {
            throw new JedisConnectionException("JedisPubSub is not subscribed to a Jedis instance.");
        }
        this.client.subscribe(channels);
        this.client.flush();
    }
    
    public void psubscribe(final String... patterns) {
        if (this.client == null) {
            throw new JedisConnectionException("JedisPubSub is not subscribed to a Jedis instance.");
        }
        this.client.psubscribe(patterns);
        this.client.flush();
    }
    
    public void punsubscribe() {
        if (this.client == null) {
            throw new JedisConnectionException("JedisPubSub is not subscribed to a Jedis instance.");
        }
        this.client.punsubscribe();
        this.client.flush();
    }
    
    public void punsubscribe(final String... patterns) {
        if (this.client == null) {
            throw new JedisConnectionException("JedisPubSub is not subscribed to a Jedis instance.");
        }
        this.client.punsubscribe(patterns);
        this.client.flush();
    }
    
    public boolean isSubscribed() {
        return this.subscribedChannels > 0;
    }
    
    public void proceedWithPatterns(final Client client, final String... patterns) {
        (this.client = client).psubscribe(patterns);
        client.flush();
        this.process(client);
    }
    
    public void proceed(final Client client, final String... channels) {
        (this.client = client).subscribe(channels);
        client.flush();
        this.process(client);
    }
    
    private void process(final Client client) {
        do {
            final List<Object> reply = client.getRawObjectMultiBulkReply();
            final Object firstObj = reply.get(0);
            if (!(firstObj instanceof byte[])) {
                throw new JedisException("Unknown message type: " + firstObj);
            }
            final byte[] resp = (byte[])firstObj;
            if (Arrays.equals(Protocol.Keyword.SUBSCRIBE.raw, resp)) {
                this.subscribedChannels = (int)(Object)reply.get(2);
                final byte[] bchannel = reply.get(1);
                final String strchannel = (bchannel == null) ? null : SafeEncoder.encode(bchannel);
                this.onSubscribe(strchannel, this.subscribedChannels);
            }
            else if (Arrays.equals(Protocol.Keyword.UNSUBSCRIBE.raw, resp)) {
                this.subscribedChannels = (int)(Object)reply.get(2);
                final byte[] bchannel = reply.get(1);
                final String strchannel = (bchannel == null) ? null : SafeEncoder.encode(bchannel);
                this.onUnsubscribe(strchannel, this.subscribedChannels);
            }
            else if (Arrays.equals(Protocol.Keyword.MESSAGE.raw, resp)) {
                final byte[] bchannel = reply.get(1);
                final byte[] bmesg = reply.get(2);
                final String strchannel2 = (bchannel == null) ? null : SafeEncoder.encode(bchannel);
                final String strmesg = (bmesg == null) ? null : SafeEncoder.encode(bmesg);
                this.onMessage(strchannel2, strmesg);
            }
            else if (Arrays.equals(Protocol.Keyword.PMESSAGE.raw, resp)) {
                final byte[] bpattern = reply.get(1);
                final byte[] bchannel2 = reply.get(2);
                final byte[] bmesg2 = reply.get(3);
                final String strpattern = (bpattern == null) ? null : SafeEncoder.encode(bpattern);
                final String strchannel3 = (bchannel2 == null) ? null : SafeEncoder.encode(bchannel2);
                final String strmesg2 = (bmesg2 == null) ? null : SafeEncoder.encode(bmesg2);
                this.onPMessage(strpattern, strchannel3, strmesg2);
            }
            else if (Arrays.equals(Protocol.Keyword.PSUBSCRIBE.raw, resp)) {
                this.subscribedChannels = (int)(Object)reply.get(2);
                final byte[] bpattern = reply.get(1);
                final String strpattern2 = (bpattern == null) ? null : SafeEncoder.encode(bpattern);
                this.onPSubscribe(strpattern2, this.subscribedChannels);
            }
            else {
                if (!Arrays.equals(Protocol.Keyword.PUNSUBSCRIBE.raw, resp)) {
                    throw new JedisException("Unknown message type: " + firstObj);
                }
                this.subscribedChannels = (int)(Object)reply.get(2);
                final byte[] bpattern = reply.get(1);
                final String strpattern2 = (bpattern == null) ? null : SafeEncoder.encode(bpattern);
                this.onPUnsubscribe(strpattern2, this.subscribedChannels);
            }
        } while (this.isSubscribed());
        this.client = null;
        client.resetPipelinedCount();
    }
    
    public int getSubscribedChannels() {
        return this.subscribedChannels;
    }
}
