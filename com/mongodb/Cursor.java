package com.mongodb;

import java.util.*;
import java.io.*;

public interface Cursor extends Iterator<DBObject>, Closeable
{
    long getCursorId();
    
    ServerAddress getServerAddress();
    
    void close();
}
