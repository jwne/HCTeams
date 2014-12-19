package com.mongodb;

import org.bson.*;
import java.io.*;

public class DefaultDBDecoder extends BasicBSONDecoder implements DBDecoder
{
    public static DBDecoderFactory FACTORY;
    
    public DBCallback getDBCallback(final DBCollection collection) {
        return new DefaultDBCallback(collection);
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
    
    public String toString() {
        return "DefaultDBDecoder";
    }
    
    static {
        DefaultDBDecoder.FACTORY = new DefaultFactory();
    }
    
    static class DefaultFactory implements DBDecoderFactory
    {
        public DBDecoder create() {
            return new DefaultDBDecoder();
        }
        
        public String toString() {
            return "DefaultDBDecoder.DefaultFactory";
        }
    }
}
