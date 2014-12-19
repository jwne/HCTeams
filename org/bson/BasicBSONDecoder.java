package org.bson;

import org.bson.types.*;
import org.bson.io.*;
import java.io.*;

public class BasicBSONDecoder implements BSONDecoder
{
    @Deprecated
    protected BSONInput _in;
    @Deprecated
    protected BSONCallback _callback;
    private byte[] _random;
    private byte[] _inputBuffer;
    private PoolOutputBuffer _stringBuffer;
    @Deprecated
    protected int _pos;
    @Deprecated
    protected int _len;
    private static final int MAX_STRING = 33554432;
    private static final String DEFAULT_ENCODING = "UTF-8";
    static final String[] ONE_BYTE_STRINGS;
    
    public BasicBSONDecoder() {
        super();
        this._random = new byte[1024];
        this._inputBuffer = new byte[1024];
        this._stringBuffer = new PoolOutputBuffer();
    }
    
    public BSONObject readObject(final byte[] b) {
        try {
            return this.readObject(new ByteArrayInputStream(b));
        }
        catch (IOException ioe) {
            throw new BSONException("should be impossible", ioe);
        }
    }
    
    public BSONObject readObject(final InputStream in) throws IOException {
        final BasicBSONCallback c = new BasicBSONCallback();
        this.decode(in, c);
        return (BSONObject)c.get();
    }
    
    public int decode(final byte[] b, final BSONCallback callback) {
        try {
            return this._decode(new BSONInput(new ByteArrayInputStream(b)), callback);
        }
        catch (IOException ioe) {
            throw new BSONException("should be impossible", ioe);
        }
    }
    
    public int decode(final InputStream in, final BSONCallback callback) throws IOException {
        return this._decode(new BSONInput(in), callback);
    }
    
    private int _decode(final BSONInput in, final BSONCallback callback) throws IOException {
        if (this._in != null || this._callback != null) {
            throw new IllegalStateException("not ready");
        }
        this._in = in;
        this._callback = callback;
        if (in.numRead() != 0) {
            throw new IllegalArgumentException("i'm confused");
        }
        try {
            final int len = this._in.readInt();
            this._in.setMax(len);
            this._callback.objectStart();
            while (this.decodeElement()) {}
            this._callback.objectDone();
            if (this._in.numRead() != len) {
                throw new IllegalArgumentException("bad data.  lengths don't match read:" + this._in.numRead() + " != len:" + len);
            }
            return len;
        }
        finally {
            this._in = null;
            this._callback = null;
        }
    }
    
    int decode(final boolean first) throws IOException {
        final int start = this._in.numRead();
        final int len = this._in.readInt();
        if (first) {
            this._in.setMax(len);
        }
        this._callback.objectStart();
        while (this.decodeElement()) {}
        this._callback.objectDone();
        final int read = this._in.numRead() - start;
        if (read != len) {
            return len;
        }
        return len;
    }
    
    boolean decodeElement() throws IOException {
        final byte type = this._in.read();
        if (type == 0) {
            return false;
        }
        final String name = this._in.readCStr();
        switch (type) {
            case 10: {
                this._callback.gotNull(name);
                break;
            }
            case 6: {
                this._callback.gotUndefined(name);
                break;
            }
            case 8: {
                this._callback.gotBoolean(name, this._in.read() > 0);
                break;
            }
            case 1: {
                this._callback.gotDouble(name, this._in.readDouble());
                break;
            }
            case 16: {
                this._callback.gotInt(name, this._in.readInt());
                break;
            }
            case 18: {
                this._callback.gotLong(name, this._in.readLong());
                break;
            }
            case 14: {
                this._callback.gotSymbol(name, this._in.readUTF8String());
                break;
            }
            case 2: {
                this._callback.gotString(name, this._in.readUTF8String());
                break;
            }
            case 7: {
                this._callback.gotObjectId(name, new ObjectId(this._in.readIntBE(), this._in.readIntBE(), this._in.readIntBE()));
                break;
            }
            case 12: {
                this._in.readInt();
                final String ns = this._in.readCStr();
                final ObjectId theOID = new ObjectId(this._in.readInt(), this._in.readInt(), this._in.readInt());
                this._callback.gotDBRef(name, ns, theOID);
                break;
            }
            case 9: {
                this._callback.gotDate(name, this._in.readLong());
                break;
            }
            case 11: {
                this._callback.gotRegex(name, this._in.readCStr(), this._in.readCStr());
                break;
            }
            case 5: {
                this._binary(name);
                break;
            }
            case 13: {
                this._callback.gotCode(name, this._in.readUTF8String());
                break;
            }
            case 15: {
                this._in.readInt();
                this._callback.gotCodeWScope(name, this._in.readUTF8String(), this._readBasicObject());
                break;
            }
            case 4: {
                this._in.readInt();
                this._callback.arrayStart(name);
                while (this.decodeElement()) {}
                this._callback.arrayDone();
                break;
            }
            case 3: {
                this._in.readInt();
                this._callback.objectStart(name);
                while (this.decodeElement()) {}
                this._callback.objectDone();
                break;
            }
            case 17: {
                final int i = this._in.readInt();
                final int time = this._in.readInt();
                this._callback.gotTimestamp(name, time, i);
                break;
            }
            case -1: {
                this._callback.gotMinKey(name);
                break;
            }
            case Byte.MAX_VALUE: {
                this._callback.gotMaxKey(name);
                break;
            }
            default: {
                throw new UnsupportedOperationException("BSONDecoder doesn't understand type : " + type + " name: " + name);
            }
        }
        return true;
    }
    
    @Deprecated
    protected void _binary(final String name) throws IOException {
        final int totalLen = this._in.readInt();
        final byte bType = this._in.read();
        switch (bType) {
            case 0: {
                final byte[] data = new byte[totalLen];
                this._in.fill(data);
                this._callback.gotBinary(name, bType, data);
            }
            case 2: {
                final int len = this._in.readInt();
                if (len + 4 != totalLen) {
                    throw new IllegalArgumentException("bad data size subtype 2 len: " + len + " totalLen: " + totalLen);
                }
                final byte[] data2 = new byte[len];
                this._in.fill(data2);
                this._callback.gotBinary(name, bType, data2);
            }
            case 3: {
                if (totalLen != 16) {
                    throw new IllegalArgumentException("bad data size subtype 3 len: " + totalLen + " != 16");
                }
                final long part1 = this._in.readLong();
                final long part2 = this._in.readLong();
                this._callback.gotUUID(name, part1, part2);
            }
            default: {
                final byte[] data = new byte[totalLen];
                this._in.fill(data);
                this._callback.gotBinary(name, bType, data);
            }
        }
    }
    
    Object _readBasicObject() throws IOException {
        this._in.readInt();
        final BSONCallback save = this._callback;
        final BSONCallback _basic = this._callback.createBSONCallback();
        (this._callback = _basic).reset();
        _basic.objectStart();
        while (this.decodeElement()) {}
        this._callback = save;
        return _basic.get();
    }
    
    private static final boolean _isAscii(final byte b) {
        return b >= 0 && b <= 127;
    }
    
    static void _fillRange(byte min, final byte max) {
        while (min < max) {
            String s = "";
            s += (char)min;
            BasicBSONDecoder.ONE_BYTE_STRINGS[min] = s;
            ++min;
        }
    }
    
    static {
        ONE_BYTE_STRINGS = new String[128];
        _fillRange((byte)48, (byte)57);
        _fillRange((byte)97, (byte)122);
        _fillRange((byte)65, (byte)90);
    }
    
    @Deprecated
    protected class BSONInput
    {
        int _read;
        final InputStream _raw;
        int _max;
        
        public BSONInput(final InputStream in) {
            super();
            this._max = 4;
            this._raw = in;
            this._read = 0;
            BasicBSONDecoder.this._pos = 0;
            BasicBSONDecoder.this._len = 0;
        }
        
        protected int _need(final int num) throws IOException {
            if (BasicBSONDecoder.this._len - BasicBSONDecoder.this._pos >= num) {
                final int ret = BasicBSONDecoder.this._pos;
                final BasicBSONDecoder this$0 = BasicBSONDecoder.this;
                this$0._pos += num;
                this._read += num;
                return ret;
            }
            if (num >= BasicBSONDecoder.this._inputBuffer.length) {
                throw new IllegalArgumentException("you can't need that much");
            }
            final int remaining = BasicBSONDecoder.this._len - BasicBSONDecoder.this._pos;
            if (BasicBSONDecoder.this._pos > 0) {
                System.arraycopy(BasicBSONDecoder.this._inputBuffer, BasicBSONDecoder.this._pos, BasicBSONDecoder.this._inputBuffer, 0, remaining);
                BasicBSONDecoder.this._pos = 0;
                BasicBSONDecoder.this._len = remaining;
            }
            int x;
            BasicBSONDecoder this$;
            for (int maxToRead = Math.min(this._max - this._read - remaining, BasicBSONDecoder.this._inputBuffer.length - BasicBSONDecoder.this._len); maxToRead > 0; maxToRead -= x, this$ = BasicBSONDecoder.this, this$._len += x) {
                x = this._raw.read(BasicBSONDecoder.this._inputBuffer, BasicBSONDecoder.this._len, maxToRead);
                if (x <= 0) {
                    throw new IOException("unexpected EOF");
                }
            }
            final int ret2 = BasicBSONDecoder.this._pos;
            final BasicBSONDecoder this$2 = BasicBSONDecoder.this;
            this$2._pos += num;
            this._read += num;
            return ret2;
        }
        
        public int readInt() throws IOException {
            return Bits.readInt(BasicBSONDecoder.this._inputBuffer, this._need(4));
        }
        
        public int readIntBE() throws IOException {
            return Bits.readIntBE(BasicBSONDecoder.this._inputBuffer, this._need(4));
        }
        
        public long readLong() throws IOException {
            return Bits.readLong(BasicBSONDecoder.this._inputBuffer, this._need(8));
        }
        
        public double readDouble() throws IOException {
            return Double.longBitsToDouble(this.readLong());
        }
        
        public byte read() throws IOException {
            if (BasicBSONDecoder.this._pos < BasicBSONDecoder.this._len) {
                ++this._read;
                return BasicBSONDecoder.this._inputBuffer[BasicBSONDecoder.this._pos++];
            }
            return BasicBSONDecoder.this._inputBuffer[this._need(1)];
        }
        
        public void fill(final byte[] b) throws IOException {
            this.fill(b, b.length);
        }
        
        public void fill(final byte[] b, int len) throws IOException {
            final int have = BasicBSONDecoder.this._len - BasicBSONDecoder.this._pos;
            final int tocopy = Math.min(len, have);
            System.arraycopy(BasicBSONDecoder.this._inputBuffer, BasicBSONDecoder.this._pos, b, 0, tocopy);
            final BasicBSONDecoder this$0 = BasicBSONDecoder.this;
            this$0._pos += tocopy;
            this._read += tocopy;
            len -= tocopy;
            int off = tocopy;
            while (len > 0) {
                final int x = this._raw.read(b, off, len);
                if (x <= 0) {
                    throw new IOException("unexpected EOF");
                }
                this._read += x;
                off += x;
                len -= x;
            }
        }
        
        protected boolean _isAscii(final byte b) {
            return b >= 0 && b <= 127;
        }
        
        public String readCStr() throws IOException {
            boolean isAscii = true;
            BasicBSONDecoder.this._random[0] = this.read();
            if (BasicBSONDecoder.this._random[0] == 0) {
                return "";
            }
            BasicBSONDecoder.this._random[1] = this.read();
            if (BasicBSONDecoder.this._random[1] == 0) {
                final String out = BasicBSONDecoder.ONE_BYTE_STRINGS[BasicBSONDecoder.this._random[0]];
                return (out != null) ? out : new String(BasicBSONDecoder.this._random, 0, 1, "UTF-8");
            }
            BasicBSONDecoder.this._stringBuffer.reset();
            BasicBSONDecoder.this._stringBuffer.write(BasicBSONDecoder.this._random, 0, 2);
            isAscii = (this._isAscii(BasicBSONDecoder.this._random[0]) && this._isAscii(BasicBSONDecoder.this._random[1]));
            byte b;
            while ((b = this.read()) != 0) {
                BasicBSONDecoder.this._stringBuffer.write(b);
                isAscii = (isAscii && this._isAscii(b));
            }
            String out2 = null;
            if (isAscii) {
                out2 = BasicBSONDecoder.this._stringBuffer.asAscii();
            }
            else {
                try {
                    out2 = BasicBSONDecoder.this._stringBuffer.asString("UTF-8");
                }
                catch (UnsupportedOperationException e) {
                    throw new BSONException("impossible", e);
                }
            }
            BasicBSONDecoder.this._stringBuffer.reset();
            return out2;
        }
        
        public String readUTF8String() throws IOException {
            final int size = this.readInt();
            if (size <= 0 || size > 33554432) {
                throw new BSONException("bad string size: " + size);
            }
            if (size < BasicBSONDecoder.this._inputBuffer.length / 2) {
                if (size == 1) {
                    this.read();
                    return "";
                }
                return new String(BasicBSONDecoder.this._inputBuffer, this._need(size), size - 1, "UTF-8");
            }
            else {
                final byte[] b = (size < BasicBSONDecoder.this._random.length) ? BasicBSONDecoder.this._random : new byte[size];
                this.fill(b, size);
                try {
                    return new String(b, 0, size - 1, "UTF-8");
                }
                catch (UnsupportedEncodingException uee) {
                    throw new BSONException("impossible", uee);
                }
            }
        }
        
        public int numRead() {
            return this._read;
        }
        
        public int getPos() {
            return BasicBSONDecoder.this._pos;
        }
        
        public int getMax() {
            return this._max;
        }
        
        public void setMax(final int _max) {
            this._max = _max;
        }
    }
}
