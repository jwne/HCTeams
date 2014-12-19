package redis.clients.jedis;

import redis.clients.util.*;
import java.net.*;
import java.io.*;
import java.util.*;
import redis.clients.jedis.exceptions.*;

public class Connection implements Closeable
{
    private String host;
    private int port;
    private Socket socket;
    private RedisOutputStream outputStream;
    private RedisInputStream inputStream;
    private int pipelinedCommands;
    private int timeout;
    private boolean broken;
    
    public Socket getSocket() {
        return this.socket;
    }
    
    public int getTimeout() {
        return this.timeout;
    }
    
    public void setTimeout(final int timeout) {
        this.timeout = timeout;
    }
    
    public void setTimeoutInfinite() {
        try {
            if (!this.isConnected()) {
                this.connect();
            }
            this.socket.setSoTimeout(0);
        }
        catch (SocketException ex) {
            this.broken = true;
            throw new JedisConnectionException(ex);
        }
    }
    
    public void rollbackTimeout() {
        try {
            this.socket.setSoTimeout(this.timeout);
        }
        catch (SocketException ex) {
            this.broken = true;
            throw new JedisConnectionException(ex);
        }
    }
    
    public Connection(final String host) {
        super();
        this.port = 6379;
        this.pipelinedCommands = 0;
        this.timeout = 2000;
        this.broken = false;
        this.host = host;
    }
    
    protected Connection sendCommand(final Protocol.Command cmd, final String... args) {
        final byte[][] bargs = new byte[args.length][];
        for (int i = 0; i < args.length; ++i) {
            bargs[i] = SafeEncoder.encode(args[i]);
        }
        return this.sendCommand(cmd, bargs);
    }
    
    protected Connection sendCommand(final Protocol.Command cmd, final byte[]... args) {
        try {
            this.connect();
            Protocol.sendCommand(this.outputStream, cmd, args);
            ++this.pipelinedCommands;
            return this;
        }
        catch (JedisConnectionException ex) {
            this.broken = true;
            throw ex;
        }
    }
    
    protected Connection sendCommand(final Protocol.Command cmd) {
        try {
            this.connect();
            Protocol.sendCommand(this.outputStream, cmd);
            ++this.pipelinedCommands;
            return this;
        }
        catch (JedisConnectionException ex) {
            this.broken = true;
            throw ex;
        }
    }
    
    public Connection(final String host, final int port) {
        super();
        this.port = 6379;
        this.pipelinedCommands = 0;
        this.timeout = 2000;
        this.broken = false;
        this.host = host;
        this.port = port;
    }
    
    public String getHost() {
        return this.host;
    }
    
    public void setHost(final String host) {
        this.host = host;
    }
    
    public int getPort() {
        return this.port;
    }
    
    public void setPort(final int port) {
        this.port = port;
    }
    
    public Connection() {
        super();
        this.port = 6379;
        this.pipelinedCommands = 0;
        this.timeout = 2000;
        this.broken = false;
    }
    
    public void connect() {
        if (!this.isConnected()) {
            try {
                (this.socket = new Socket()).setReuseAddress(true);
                this.socket.setKeepAlive(true);
                this.socket.setTcpNoDelay(true);
                this.socket.setSoLinger(true, 0);
                this.socket.connect(new InetSocketAddress(this.host, this.port), this.timeout);
                this.socket.setSoTimeout(this.timeout);
                this.outputStream = new RedisOutputStream(this.socket.getOutputStream());
                this.inputStream = new RedisInputStream(this.socket.getInputStream());
            }
            catch (IOException ex) {
                this.broken = true;
                throw new JedisConnectionException(ex);
            }
        }
    }
    
    @Override
    public void close() {
        this.disconnect();
    }
    
    public void disconnect() {
        if (this.isConnected()) {
            try {
                this.inputStream.close();
                if (!this.socket.isClosed()) {
                    this.outputStream.close();
                    this.socket.close();
                }
            }
            catch (IOException ex) {
                this.broken = true;
                throw new JedisConnectionException(ex);
            }
        }
    }
    
    public boolean isConnected() {
        return this.socket != null && this.socket.isBound() && !this.socket.isClosed() && this.socket.isConnected() && !this.socket.isInputShutdown() && !this.socket.isOutputShutdown();
    }
    
    public String getStatusCodeReply() {
        this.flush();
        --this.pipelinedCommands;
        final byte[] resp = (byte[])this.readProtocolWithCheckingBroken();
        if (null == resp) {
            return null;
        }
        return SafeEncoder.encode(resp);
    }
    
    public String getBulkReply() {
        final byte[] result = this.getBinaryBulkReply();
        if (null != result) {
            return SafeEncoder.encode(result);
        }
        return null;
    }
    
    public byte[] getBinaryBulkReply() {
        this.flush();
        --this.pipelinedCommands;
        return (byte[])this.readProtocolWithCheckingBroken();
    }
    
    public Long getIntegerReply() {
        this.flush();
        --this.pipelinedCommands;
        return (Long)this.readProtocolWithCheckingBroken();
    }
    
    public List<String> getMultiBulkReply() {
        return BuilderFactory.STRING_LIST.build(this.getBinaryMultiBulkReply());
    }
    
    public List<byte[]> getBinaryMultiBulkReply() {
        this.flush();
        --this.pipelinedCommands;
        return (List<byte[]>)this.readProtocolWithCheckingBroken();
    }
    
    public void resetPipelinedCount() {
        this.pipelinedCommands = 0;
    }
    
    public List<Object> getRawObjectMultiBulkReply() {
        return (List<Object>)this.readProtocolWithCheckingBroken();
    }
    
    public List<Object> getObjectMultiBulkReply() {
        this.flush();
        --this.pipelinedCommands;
        return this.getRawObjectMultiBulkReply();
    }
    
    public List<Long> getIntegerMultiBulkReply() {
        this.flush();
        --this.pipelinedCommands;
        return (List<Long>)this.readProtocolWithCheckingBroken();
    }
    
    public List<Object> getAll() {
        return this.getAll(0);
    }
    
    public List<Object> getAll(final int except) {
        final List<Object> all = new ArrayList<Object>();
        this.flush();
        while (this.pipelinedCommands > except) {
            try {
                all.add(this.readProtocolWithCheckingBroken());
            }
            catch (JedisDataException e) {
                all.add(e);
            }
            --this.pipelinedCommands;
        }
        return all;
    }
    
    public Object getOne() {
        this.flush();
        --this.pipelinedCommands;
        return this.readProtocolWithCheckingBroken();
    }
    
    public boolean isBroken() {
        return this.broken;
    }
    
    protected void flush() {
        try {
            this.outputStream.flush();
        }
        catch (IOException ex) {
            this.broken = true;
            throw new JedisConnectionException(ex);
        }
    }
    
    protected Object readProtocolWithCheckingBroken() {
        try {
            return Protocol.read(this.inputStream);
        }
        catch (JedisConnectionException exc) {
            this.broken = true;
            throw exc;
        }
    }
}
