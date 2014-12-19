package org.bson;

import org.bson.io.*;

public interface BSONEncoder
{
    byte[] encode(BSONObject p0);
    
    int putObject(BSONObject p0);
    
    void done();
    
    void set(OutputBuffer p0);
}
