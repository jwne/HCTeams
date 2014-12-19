package com.mongodb;

import org.bson.*;
import java.io.*;

public interface DBDecoder extends BSONDecoder
{
    DBCallback getDBCallback(DBCollection p0);
    
    DBObject decode(InputStream p0, DBCollection p1) throws IOException;
    
    DBObject decode(byte[] p0, DBCollection p1);
}
