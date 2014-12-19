package org.bson.io;

import java.text.*;
import java.io.*;

class UTF8Encoding
{
    private static final int MIN_2_BYTES = 128;
    private static final int MIN_3_BYTES = 2048;
    private static final int MIN_4_BYTES = 65536;
    private static final int MAX_CODE_POINT = 1114111;
    private char[] decoderArray;
    
    UTF8Encoding() {
        super();
        this.decoderArray = new char[1024];
    }
    
    private static final void checkByte(final int ch, final int pos, final int len) throws IOException {
        if ((ch & 0xC0) != 0x80) {
            throw new IOException(MessageFormat.format("Illegal UTF-8 sequence: byte {0} of {1} byte sequence is not 10xxxxxx: {2}", new Integer(pos), new Integer(len), new Integer(ch)));
        }
    }
    
    private static final void checkMinimal(final int ch, final int minValue) throws IOException {
        if (ch >= minValue) {
            return;
        }
        int actualLen = 0;
        switch (minValue) {
            case 128: {
                actualLen = 2;
                break;
            }
            case 2048: {
                actualLen = 3;
                break;
            }
            case 65536: {
                actualLen = 4;
                break;
            }
            default: {
                throw new IllegalArgumentException("unexpected minValue passed to checkMinimal: " + minValue);
            }
        }
        int expectedLen;
        if (ch < 128) {
            expectedLen = 1;
        }
        else if (ch < 2048) {
            expectedLen = 2;
        }
        else {
            if (ch >= 65536) {
                throw new IllegalArgumentException("unexpected ch passed to checkMinimal: " + ch);
            }
            expectedLen = 3;
        }
        throw new IOException(MessageFormat.format("Illegal UTF-8 sequence: {0} bytes used to encode a {1} byte value: {2}", new Integer(actualLen), new Integer(expectedLen), new Integer(ch)));
    }
    
    public synchronized String decode(final byte[] data, final int offset, final int length) throws IOException {
        char[] cdata = this.decoderArray;
        if (cdata.length < length) {
            final char[] decoderArray = new char[length];
            this.decoderArray = decoderArray;
            cdata = decoderArray;
        }
        int in = offset;
        int out = 0;
        final int end = length + offset;
        try {
            while (in < end) {
                int ch = data[in++] & 0xFF;
                if (ch >= 128) {
                    if (ch < 192) {
                        throw new IOException(MessageFormat.format("Illegal UTF-8 sequence: initial byte is {0}: {1}", "10xxxxxx", new Integer(ch)));
                    }
                    if (ch < 224) {
                        ch = (ch & 0x1F) << 6;
                        checkByte(data[in], 2, 2);
                        ch |= (data[in++] & 0x3F);
                        checkMinimal(ch, 128);
                    }
                    else if (ch < 240) {
                        ch = (ch & 0xF) << 12;
                        checkByte(data[in], 2, 3);
                        ch |= (data[in++] & 0x3F) << 6;
                        checkByte(data[in], 3, 3);
                        ch |= (data[in++] & 0x3F);
                        checkMinimal(ch, 2048);
                    }
                    else {
                        if (ch >= 248) {
                            throw new IOException(MessageFormat.format("Illegal UTF-8 sequence: initial byte is {0}: {1}", "11111xxx", new Integer(ch)));
                        }
                        ch = (ch & 0x7) << 18;
                        checkByte(data[in], 2, 4);
                        ch |= (data[in++] & 0x3F) << 12;
                        checkByte(data[in], 3, 4);
                        ch |= (data[in++] & 0x3F) << 6;
                        checkByte(data[in], 4, 4);
                        ch |= (data[in++] & 0x3F);
                        checkMinimal(ch, 65536);
                    }
                }
                if (ch > 1114111) {
                    throw new IOException(MessageFormat.format("Illegal UTF-8 sequence: final value is out of range: {0}", new Integer(ch)));
                }
                if (ch > 65535) {
                    ch -= 65536;
                    cdata[out++] = (char)(55296 + (ch >> 10));
                    cdata[out++] = (char)(56320 + (ch & 0x3FF));
                }
                else {
                    if (ch >= 55296 && ch < 57344) {
                        throw new IOException(MessageFormat.format("Illegal UTF-8 sequence: final value is a surrogate value: {0}", new Integer(ch)));
                    }
                    cdata[out++] = (char)ch;
                }
            }
        }
        catch (ArrayIndexOutOfBoundsException a) {
            throw new IOException("Illegal UTF-8 sequence: multibyte sequence was truncated");
        }
        if (in > end) {
            throw new IOException("Illegal UTF-8 sequence: multibyte sequence was truncated");
        }
        return new String(cdata, 0, out);
    }
}
