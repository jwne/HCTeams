package com.mongodb;

import org.bson.io.*;
import org.bson.*;
import java.io.*;

public class LazyDBEncoder implements DBEncoder
{
    public int writeObject(final OutputBuffer buf, final BSONObject o) {
        if (!(o instanceof LazyDBObject)) {
            throw new IllegalArgumentException("LazyDBEncoder can only encode BSONObject instances of type LazyDBObject");
        }
        final LazyDBObject lazyDBObject = (LazyDBObject)o;
        try {
            lazyDBObject.pipe(buf);
        }
        catch (IOException e) {
            throw new MongoException("Exception serializing a LazyDBObject", e);
        }
        return lazyDBObject.getBSONSize();
    }
}
