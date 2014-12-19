package com.mongodb;

import org.bson.*;
import java.io.*;

public class LazyDBDecoder extends LazyBSONDecoder implements DBDecoder
{
    public static DBDecoderFactory FACTORY;
    
    public DBCallback getDBCallback(final DBCollection collection) {
        return new LazyDBCallback(collection);
    }
    
    public DBObject decode(final byte[] b, final DBCollection collection) {
        final DBCallback cbk = this.getDBCallback(collection);
        cbk.reset();
        this.decode(b, cbk);
        return (DBObject)cbk.get();
    }
    
    public DBObject decode(final InputStream in, final DBCollection collection) throws IOException {
        final DBCallback cbk = this.getDBCallback(collection);
        cbk.reset();
        this.decode(in, cbk);
        return (DBObject)cbk.get();
    }
    
    static {
        LazyDBDecoder.FACTORY = new LazyDBDecoderFactory();
    }
    
    static class LazyDBDecoderFactory implements DBDecoderFactory
    {
        public DBDecoder create() {
            return new LazyDBDecoder();
        }
    }
}
