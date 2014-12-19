package redis.clients.jedis;

public class HostAndPort
{
    public static final String LOCALHOST_STR = "localhost";
    private String host;
    private int port;
    
    public HostAndPort(final String host, final int port) {
        super();
        this.host = host;
        this.port = port;
    }
    
    public String getHost() {
        return this.host;
    }
    
    public int getPort() {
        return this.port;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof HostAndPort) {
            final HostAndPort hp = (HostAndPort)obj;
            final String thisHost = this.convertHost(this.host);
            final String hpHost = this.convertHost(hp.host);
            return this.port == hp.port && thisHost.equals(hpHost);
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return 31 * this.convertHost(this.host).hashCode() + this.port;
    }
    
    @Override
    public String toString() {
        return this.host + ":" + this.port;
    }
    
    private String convertHost(final String host) {
        if (host.equals("127.0.0.1")) {
            return "localhost";
        }
        if (host.equals("::1")) {
            return "localhost";
        }
        return host;
    }
}
