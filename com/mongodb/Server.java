package com.mongodb;

import org.bson.util.annotations.*;
import java.util.concurrent.*;

@ThreadSafe
interface Server
{
    ServerDescription getDescription();
    
    Connection getConnection(long p0, TimeUnit p1);
    
    void invalidate();
}
