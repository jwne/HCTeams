package com.mongodb;

import org.bson.util.annotations.*;
import java.net.*;

@Immutable
public class ServerAddress
{
    final String _host;
    final int _port;
    
    public ServerAddress() throws UnknownHostException {
        this(defaultHost(), defaultPort());
    }
    
    public ServerAddress(final String host) throws UnknownHostException {
        this(host, defaultPort());
    }
    
    public ServerAddress(String host, int port) throws UnknownHostException {
        super();
        if (host == null) {
            host = defaultHost();
        }
        host = host.trim();
        if (host.length() == 0) {
            host = defaultHost();
        }
        if (host.startsWith("[")) {
            final int idx = host.indexOf("]");
            if (idx == -1) {
                throw new IllegalArgumentException("an IPV6 address must be encosed with '[' and ']' according to RFC 2732.");
            }
            final int portIdx = host.indexOf("]:");
            if (portIdx != -1) {
                if (port != defaultPort()) {
                    throw new IllegalArgumentException("can't specify port in construct and via host");
                }
                port = Integer.parseInt(host.substring(portIdx + 2));
            }
            host = host.substring(1, idx);
        }
        else {
            final int idx = host.indexOf(":");
            if (idx > 0) {
                if (port != defaultPort()) {
                    throw new IllegalArgumentException("can't specify port in construct and via host");
                }
                try {
                    port = Integer.parseInt(host.substring(idx + 1));
                }
                catch (NumberFormatException e) {
                    throw new MongoException("host and port should be specified in host:port format");
                }
                host = host.substring(0, idx).trim();
            }
        }
        this._host = host;
        this._port = port;
    }
    
    public ServerAddress(final InetAddress addr) {
        this(new InetSocketAddress(addr, defaultPort()));
    }
    
    public ServerAddress(final InetAddress addr, final int port) {
        this(new InetSocketAddress(addr, port));
    }
    
    public ServerAddress(final InetSocketAddress addr) {
        super();
        this._host = addr.getHostName();
        this._port = addr.getPort();
    }
    
    public boolean sameHost(String host) {
        final int idx = host.indexOf(":");
        int port = defaultPort();
        if (idx > 0) {
            port = Integer.parseInt(host.substring(idx + 1));
            host = host.substring(0, idx);
        }
        return this._port == port && this._host.equalsIgnoreCase(host);
    }
    
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final ServerAddress that = (ServerAddress)o;
        return this._port == that._port && this._host.equals(that._host);
    }
    
    public int hashCode() {
        int result = this._host.hashCode();
        result = 31 * result + this._port;
        return result;
    }
    
    public String getHost() {
        return this._host;
    }
    
    public int getPort() {
        return this._port;
    }
    
    public InetSocketAddress getSocketAddress() throws UnknownHostException {
        return new InetSocketAddress(InetAddress.getByName(this._host), this._port);
    }
    
    public String toString() {
        return this._host + ":" + this._port;
    }
    
    public static String defaultHost() {
        return "127.0.0.1";
    }
    
    public static int defaultPort() {
        return 27017;
    }
}
