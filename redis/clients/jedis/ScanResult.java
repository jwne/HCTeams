package redis.clients.jedis;

import java.util.*;
import redis.clients.util.*;

public class ScanResult<T>
{
    private byte[] cursor;
    private List<T> results;
    
    public ScanResult(final int cursor, final List<T> results) {
        this(Protocol.toByteArray(cursor), results);
    }
    
    public ScanResult(final String cursor, final List<T> results) {
        this(SafeEncoder.encode(cursor), results);
    }
    
    public ScanResult(final byte[] cursor, final List<T> results) {
        super();
        this.cursor = cursor;
        this.results = results;
    }
    
    @Deprecated
    public int getCursor() {
        return Integer.parseInt(this.getStringCursor());
    }
    
    public String getStringCursor() {
        return SafeEncoder.encode(this.cursor);
    }
    
    public byte[] getCursorAsBytes() {
        return this.cursor;
    }
    
    public List<T> getResult() {
        return this.results;
    }
}
