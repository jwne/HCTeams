package org.bson;

import org.bson.io.*;
import java.io.*;
import org.bson.types.*;

@Deprecated
public class NewBSONDecoder implements BSONDecoder
{
    private static final int MAX_STRING = 33554432;
    private static final String DEFAULT_ENCODING = "UTF-8";
    private byte[] _data;
    private int _length;
    private int _pos;
    private BSONCallback _callback;
    
    public NewBSONDecoder() {
        super();
        this._pos = 0;
    }
    
    public BSONObject readObject(final byte[] pData) {
        this._length = pData.length;
        final BasicBSONCallback c = new BasicBSONCallback();
        this.decode(pData, c);
        return (BSONObject)c.get();
    }
    
    public BSONObject readObject(final InputStream pIn) throws IOException {
        this._length = Bits.readInt(pIn);
        if (this._data == null || this._data.length < this._length) {
            this._data = new byte[this._length];
        }
        new DataInputStream(pIn).readFully(this._data, 4, this._length - 4);
        return this.readObject(this._data);
    }
    
    public int decode(final byte[] pData, final BSONCallback pCallback) {
        this._data = pData;
        this._pos = 4;
        this._callback = pCallback;
        this._decode();
        return this._length;
    }
    
    public int decode(final InputStream pIn, final BSONCallback pCallback) throws IOException {
        this._length = Bits.readInt(pIn);
        if (this._data == null || this._data.length < this._length) {
            this._data = new byte[this._length];
        }
        new DataInputStream(pIn).readFully(this._data, 4, this._length - 4);
        return this.decode(this._data, pCallback);
    }
    
    private final void _decode() {
        this._callback.objectStart();
        while (this.decodeElement()) {}
        this._callback.objectDone();
    }
    
    private final String readCstr() {
        int length = 0;
        final int offset = this._pos;
        while (this._data[this._pos++] != 0) {
            ++length;
        }
        try {
            return new String(this._data, offset, length, "UTF-8");
        }
        catch (UnsupportedEncodingException uee) {
            return new String(this._data, offset, length);
        }
    }
    
    private final String readUtf8Str() {
        final int length = Bits.readInt(this._data, this._pos);
        this._pos += 4;
        if (length <= 0 || length > 33554432) {
            throw new BSONException("String invalid - corruption");
        }
        try {
            final String str = new String(this._data, this._pos, length - 1, "UTF-8");
            this._pos += length;
            return str;
        }
        catch (UnsupportedEncodingException uee) {
            throw new BSONException("What is in the db", uee);
        }
    }
    
    private final Object _readBasicObject() {
        this._pos += 4;
        final BSONCallback save = this._callback;
        final BSONCallback _basic = this._callback.createBSONCallback();
        (this._callback = _basic).reset();
        _basic.objectStart();
        while (this.decodeElement()) {}
        this._callback = save;
        return _basic.get();
    }
    
    private final void _binary(final String pName) {
        final int totalLen = Bits.readInt(this._data, this._pos);
        this._pos += 4;
        final byte bType = this._data[this._pos];
        ++this._pos;
        switch (bType) {
            case 0: {
                final byte[] data = new byte[totalLen];
                System.arraycopy(this._data, this._pos, data, 0, totalLen);
                this._pos += totalLen;
                this._callback.gotBinary(pName, bType, data);
            }
            case 2: {
                final int len = Bits.readInt(this._data, this._pos);
                this._pos += 4;
                if (len + 4 != totalLen) {
                    throw new IllegalArgumentException("bad data size subtype 2 len: " + len + " totalLen: " + totalLen);
                }
                final byte[] data2 = new byte[len];
                System.arraycopy(this._data, this._pos, data2, 0, len);
                this._pos += len;
                this._callback.gotBinary(pName, bType, data2);
            }
            case 3: {
                if (totalLen != 16) {
                    throw new IllegalArgumentException("bad data size subtype 3 len: " + totalLen + " != 16");
                }
                final long part1 = Bits.readLong(this._data, this._pos);
                this._pos += 8;
                final long part2 = Bits.readLong(this._data, this._pos);
                this._pos += 8;
                this._callback.gotUUID(pName, part1, part2);
            }
            default: {
                final byte[] data = new byte[totalLen];
                System.arraycopy(this._data, this._pos, data, 0, totalLen);
                this._pos += totalLen;
                this._callback.gotBinary(pName, bType, data);
            }
        }
    }
    
    private final boolean decodeElement() {
        final byte type = this._data[this._pos];
        ++this._pos;
        if (type == 0) {
            return false;
        }
        final String name = this.readCstr();
        switch (type) {
            case 10: {
                this._callback.gotNull(name);
                return true;
            }
            case 6: {
                this._callback.gotUndefined(name);
                return true;
            }
            case 8: {
                this._callback.gotBoolean(name, this._data[this._pos] > 0);
                ++this._pos;
                return true;
            }
            case 1: {
                this._callback.gotDouble(name, Double.longBitsToDouble(Bits.readLong(this._data, this._pos)));
                this._pos += 8;
                return true;
            }
            case 16: {
                this._callback.gotInt(name, Bits.readInt(this._data, this._pos));
                this._pos += 4;
                return true;
            }
            case 18: {
                this._callback.gotLong(name, Bits.readLong(this._data, this._pos));
                this._pos += 8;
                return true;
            }
            case 14: {
                this._callback.gotSymbol(name, this.readUtf8Str());
                return true;
            }
            case 2: {
                this._callback.gotString(name, this.readUtf8Str());
                return true;
            }
            case 7: {
                final int p1 = Bits.readIntBE(this._data, this._pos);
                this._pos += 4;
                final int p2 = Bits.readIntBE(this._data, this._pos);
                this._pos += 4;
                final int p3 = Bits.readIntBE(this._data, this._pos);
                this._pos += 4;
                this._callback.gotObjectId(name, new ObjectId(p1, p2, p3));
                return true;
            }
            case 12: {
                this._pos += 4;
                final String ns = this.readCstr();
                final int p4 = Bits.readInt(this._data, this._pos);
                this._pos += 4;
                final int p5 = Bits.readInt(this._data, this._pos);
                this._pos += 4;
                final int p6 = Bits.readInt(this._data, this._pos);
                this._pos += 4;
                this._callback.gotDBRef(name, ns, new ObjectId(p4, p5, p6));
                return true;
            }
            case 9: {
                this._callback.gotDate(name, Bits.readLong(this._data, this._pos));
                this._pos += 8;
                return true;
            }
            case 11: {
                this._callback.gotRegex(name, this.readCstr(), this.readCstr());
                return true;
            }
            case 5: {
                this._binary(name);
                return true;
            }
            case 13: {
                this._callback.gotCode(name, this.readUtf8Str());
                return true;
            }
            case 15: {
                this._pos += 4;
                this._callback.gotCodeWScope(name, this.readUtf8Str(), this._readBasicObject());
                return true;
            }
            case 4: {
                this._pos += 4;
                this._callback.arrayStart(name);
                while (this.decodeElement()) {}
                this._callback.arrayDone();
                return true;
            }
            case 3: {
                this._pos += 4;
                this._callback.objectStart(name);
                while (this.decodeElement()) {}
                this._callback.objectDone();
                return true;
            }
            case 17: {
                final int i = Bits.readInt(this._data, this._pos);
                this._pos += 4;
                final int time = Bits.readInt(this._data, this._pos);
                this._pos += 4;
                this._callback.gotTimestamp(name, time, i);
                return true;
            }
            case -1: {
                this._callback.gotMinKey(name);
                return true;
            }
            case Byte.MAX_VALUE: {
                this._callback.gotMaxKey(name);
                return true;
            }
            default: {
                throw new UnsupportedOperationException("BSONDecoder doesn't understand type : " + type + " name: " + name);
            }
        }
    }
}
