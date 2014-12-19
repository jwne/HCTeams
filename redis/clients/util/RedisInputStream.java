package redis.clients.util;

import redis.clients.jedis.exceptions.*;
import java.io.*;

public class RedisInputStream extends FilterInputStream
{
    protected final byte[] buf;
    protected int count;
    protected int limit;
    
    public RedisInputStream(final InputStream in, final int size) {
        super(in);
        if (size <= 0) {
            throw new IllegalArgumentException("Buffer size <= 0");
        }
        this.buf = new byte[size];
    }
    
    public RedisInputStream(final InputStream in) {
        this(in, 8192);
    }
    
    public byte readByte() throws JedisConnectionException {
        this.ensureFill();
        return this.buf[this.count++];
    }
    
    public String readLine() {
        final StringBuilder sb = new StringBuilder();
        while (true) {
            this.ensureFill();
            final byte b = this.buf[this.count++];
            if (b == 13) {
                this.ensureFill();
                final byte c = this.buf[this.count++];
                if (c == 10) {
                    break;
                }
                sb.append((char)b);
                sb.append((char)c);
            }
            else {
                sb.append((char)b);
            }
        }
        final String reply = sb.toString();
        if (reply.length() == 0) {
            throw new JedisConnectionException("It seems like server has closed the connection.");
        }
        return reply;
    }
    
    public byte[] readLineBytes() {
        this.ensureFill();
        int pos = this.count;
        final byte[] buf = this.buf;
        while (pos != this.limit) {
            if (buf[pos++] == 13) {
                if (pos == this.limit) {
                    return this.readLineBytesSlowly();
                }
                if (buf[pos++] == 10) {
                    final int N = pos - this.count - 2;
                    final byte[] line = new byte[N];
                    System.arraycopy(buf, this.count, line, 0, N);
                    this.count = pos;
                    return line;
                }
                continue;
            }
        }
        return this.readLineBytesSlowly();
    }
    
    private byte[] readLineBytesSlowly() {
        ByteArrayOutputStream bout = null;
        while (true) {
            this.ensureFill();
            final byte b = this.buf[this.count++];
            if (b == 13) {
                this.ensureFill();
                final byte c = this.buf[this.count++];
                if (c == 10) {
                    break;
                }
                if (bout == null) {
                    bout = new ByteArrayOutputStream(16);
                }
                bout.write(b);
                bout.write(c);
            }
            else {
                if (bout == null) {
                    bout = new ByteArrayOutputStream(16);
                }
                bout.write(b);
            }
        }
        return (bout == null) ? new byte[0] : bout.toByteArray();
    }
    
    public int readIntCrLf() {
        return (int)this.readLongCrLf();
    }
    
    public long readLongCrLf() {
        final byte[] buf = this.buf;
        this.ensureFill();
        final boolean isNeg = buf[this.count] == 45;
        if (isNeg) {
            ++this.count;
        }
        long value = 0L;
        while (true) {
            this.ensureFill();
            final int b = buf[this.count++];
            if (b == 13) {
                break;
            }
            value = value * 10L + b - 48L;
        }
        this.ensureFill();
        if (buf[this.count++] != 10) {
            throw new JedisConnectionException("Unexpected character!");
        }
        return isNeg ? (-value) : value;
    }
    
    @Override
    public int read(final byte[] b, final int off, final int len) throws JedisConnectionException {
        this.ensureFill();
        final int length = Math.min(this.limit - this.count, len);
        System.arraycopy(this.buf, this.count, b, off, length);
        this.count += length;
        return length;
    }
    
    private void ensureFill() throws JedisConnectionException {
        if (this.count >= this.limit) {
            try {
                this.limit = this.in.read(this.buf);
                this.count = 0;
                if (this.limit == -1) {
                    throw new JedisConnectionException("Unexpected end of stream.");
                }
            }
            catch (IOException e) {
                throw new JedisConnectionException(e);
            }
        }
    }
}
