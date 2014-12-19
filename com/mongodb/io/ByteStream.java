package com.mongodb.io;

import java.nio.*;

@Deprecated
public interface ByteStream
{
    boolean hasMore();
    
    int write(ByteBuffer p0);
}
