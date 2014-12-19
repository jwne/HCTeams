package com.mongodb;

import org.bson.io.*;
import org.bson.*;

public interface DBEncoder
{
    int writeObject(OutputBuffer p0, BSONObject p1);
}
