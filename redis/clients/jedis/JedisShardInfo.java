package redis.clients.jedis;

import redis.clients.util.*;
import java.net.*;

public class JedisShardInfo extends ShardInfo<Jedis>
{
    private int timeout;
    private String host;
    private int port;
    private String password;
    private String name;
    
    @Override
    public String toString() {
        return this.host + ":" + this.port + "*" + this.getWeight();
    }
    
    public String getHost() {
        return this.host;
    }
    
    public int getPort() {
        return this.port;
    }
    
    public JedisShardInfo(final String host) {
        super(1);
        this.password = null;
        this.name = null;
        final URI uri = URI.create(host);
        if (uri.getScheme() != null && uri.getScheme().equals("redis")) {
            this.host = uri.getHost();
            this.port = uri.getPort();
            this.password = uri.getUserInfo().split(":", 2)[1];
        }
        else {
            this.host = host;
            this.port = 6379;
        }
    }
    
    public JedisShardInfo(final String host, final String name) {
        this(host, 6379, name);
    }
    
    public JedisShardInfo(final String host, final int port) {
        this(host, port, 2000);
    }
    
    public JedisShardInfo(final String host, final int port, final String name) {
        this(host, port, 2000, name);
    }
    
    public JedisShardInfo(final String host, final int port, final int timeout) {
        this(host, port, timeout, 1);
    }
    
    public JedisShardInfo(final String host, final int port, final int timeout, final String name) {
        this(host, port, timeout, 1);
        this.name = name;
    }
    
    public JedisShardInfo(final String host, final int port, final int timeout, final int weight) {
        super(weight);
        this.password = null;
        this.name = null;
        this.host = host;
        this.port = port;
        this.timeout = timeout;
    }
    
    public JedisShardInfo(final URI uri) {
        super(1);
        this.password = null;
        this.name = null;
        this.host = uri.getHost();
        this.port = uri.getPort();
        this.password = uri.getUserInfo().split(":", 2)[1];
    }
    
    public String getPassword() {
        return this.password;
    }
    
    public void setPassword(final String auth) {
        this.password = auth;
    }
    
    public int getTimeout() {
        return this.timeout;
    }
    
    public void setTimeout(final int timeout) {
        this.timeout = timeout;
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    public Jedis createResource() {
        return new Jedis(this);
    }
}
