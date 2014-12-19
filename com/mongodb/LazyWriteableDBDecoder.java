package com.mongodb;

@Deprecated
public class LazyWriteableDBDecoder extends LazyDBDecoder
{
    public static DBDecoderFactory FACTORY;
    
    public DBCallback getDBCallback(final DBCollection collection) {
        return new LazyWriteableDBCallback(collection);
    }
    
    static {
        LazyWriteableDBDecoder.FACTORY = new LazyDBDecoderFactory();
    }
    
    static class LazyDBDecoderFactory implements DBDecoderFactory
    {
        public DBDecoder create() {
            return new LazyWriteableDBDecoder();
        }
    }
}
