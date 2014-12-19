package com.mongodb;

import org.bson.io.*;
import org.bson.*;
import org.bson.types.*;

public class DefaultDBEncoder extends BasicBSONEncoder implements DBEncoder
{
    public static DBEncoderFactory FACTORY;
    
    public int writeObject(final OutputBuffer buf, final BSONObject o) {
        this.set(buf);
        final int x = super.putObject(o);
        this.done();
        return x;
    }
    
    protected boolean putSpecial(final String name, final Object val) {
        if (val instanceof DBRefBase) {
            this.putDBRef(name, (DBRefBase)val);
            return true;
        }
        return false;
    }
    
    @Deprecated
    protected void putDBPointer(final String name, final String ns, final ObjectId oid) {
        this._put((byte)12, name);
        this._putValueString(ns);
        this._buf.writeInt(oid._time());
        this._buf.writeInt(oid._machine());
        this._buf.writeInt(oid._inc());
    }
    
    protected void putDBRef(final String name, final DBRefBase ref) {
        this._put((byte)3, name);
        final int sizePos = this._buf.getPosition();
        this._buf.writeInt(0);
        this._putObjectField("$ref", ref.getRef());
        this._putObjectField("$id", ref.getId());
        this._buf.write(0);
        this._buf.writeInt(sizePos, this._buf.getPosition() - sizePos);
    }
    
    public String toString() {
        return "DefaultDBEncoder";
    }
    
    static {
        DefaultDBEncoder.FACTORY = new DefaultFactory();
    }
    
    static class DefaultFactory implements DBEncoderFactory
    {
        public DBEncoder create() {
            return new DefaultDBEncoder();
        }
        
        public String toString() {
            return "DefaultDBEncoder.DefaultFactory";
        }
    }
}
