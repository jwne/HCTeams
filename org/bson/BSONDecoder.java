package org.bson;

import java.io.*;

public interface BSONDecoder
{
    BSONObject readObject(byte[] p0);
    
    BSONObject readObject(InputStream p0) throws IOException;
    
    int decode(byte[] p0, BSONCallback p1);
    
    int decode(InputStream p0, BSONCallback p1) throws IOException;
}
