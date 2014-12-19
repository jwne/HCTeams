package com.mongodb.io;

import java.nio.*;

@Deprecated
public interface ByteBufferFactory
{
    ByteBuffer get();
    
    @Deprecated
    public static class SimpleHeapByteBufferFactory implements ByteBufferFactory
    {
        final int _size;
        
        public SimpleHeapByteBufferFactory(int size) {
            super();
            this._size = size;
        }
        
        public ByteBuffer get() {
            return ByteBuffer.wrap(new byte[this._size]);
        }
    }
}
