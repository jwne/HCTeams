package com.mongodb;

import org.bson.*;
import org.bson.io.*;

public class LazyDBList extends org.bson.LazyDBList
{
    public LazyDBList(final byte[] bytes, final LazyBSONCallback callback) {
        super(bytes, callback);
    }
    
    public LazyDBList(final byte[] bytes, final int offset, final LazyBSONCallback callback) {
        super(bytes, offset, callback);
    }
    
    public LazyDBList(final BSONByteBuffer buffer, final LazyBSONCallback callback) {
        super(buffer, callback);
    }
    
    public LazyDBList(final BSONByteBuffer buffer, final int offset, final LazyBSONCallback callback) {
        super(buffer, offset, callback);
    }
}
