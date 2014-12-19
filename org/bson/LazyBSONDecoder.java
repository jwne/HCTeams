package org.bson;

import java.io.*;
import org.bson.io.*;

public class LazyBSONDecoder implements BSONDecoder
{
    private static int BYTES_IN_INTEGER;
    
    public BSONObject readObject(final byte[] b) {
        try {
            return this.readObject(new ByteArrayInputStream(b));
        }
        catch (IOException ioe) {
            throw new BSONException("should be impossible", ioe);
        }
    }
    
    public BSONObject readObject(final InputStream in) throws IOException {
        final BSONCallback c = new LazyBSONCallback();
        this.decode(in, c);
        return (BSONObject)c.get();
    }
    
    public int decode(final byte[] b, final BSONCallback callback) {
        try {
            return this.decode(new ByteArrayInputStream(b), callback);
        }
        catch (IOException ioe) {
            throw new BSONException("should be impossible", ioe);
        }
    }
    
    public int decode(final InputStream in, final BSONCallback callback) throws IOException {
        final byte[] objSizeBuffer = new byte[LazyBSONDecoder.BYTES_IN_INTEGER];
        Bits.readFully(in, objSizeBuffer, 0, LazyBSONDecoder.BYTES_IN_INTEGER);
        final int objSize = Bits.readInt(objSizeBuffer);
        final byte[] data = new byte[objSize];
        System.arraycopy(objSizeBuffer, 0, data, 0, LazyBSONDecoder.BYTES_IN_INTEGER);
        Bits.readFully(in, data, LazyBSONDecoder.BYTES_IN_INTEGER, objSize - LazyBSONDecoder.BYTES_IN_INTEGER);
        callback.gotBinary(null, (byte)0, data);
        return objSize;
    }
    
    static {
        LazyBSONDecoder.BYTES_IN_INTEGER = 4;
    }
}
