package redis.clients.jedis;

public abstract class JedisMonitor
{
    protected Client client;
    
    public void proceed(final Client client) {
        (this.client = client).setTimeoutInfinite();
        do {
            final String command = client.getBulkReply();
            this.onCommand(command);
        } while (client.isConnected());
    }
    
    public abstract void onCommand(final String p0);
}
