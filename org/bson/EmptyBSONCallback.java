package org.bson;

import org.bson.types.*;

public class EmptyBSONCallback implements BSONCallback
{
    public void objectStart() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public void objectStart(final String name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Deprecated
    public void objectStart(final boolean array) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public Object objectDone() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public BSONCallback createBSONCallback() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public void arrayStart() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public void arrayStart(final String name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public Object arrayDone() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public void gotNull(final String name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public void gotUndefined(final String name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public void gotMinKey(final String name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public void gotMaxKey(final String name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public void gotBoolean(final String name, final boolean v) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public void gotDouble(final String name, final double v) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public void gotInt(final String name, final int v) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public void gotLong(final String name, final long v) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public void gotDate(final String name, final long millis) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public void gotString(final String name, final String v) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public void gotSymbol(final String name, final String v) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public void gotRegex(final String name, final String pattern, final String flags) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public void gotTimestamp(final String name, final int time, final int inc) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public void gotObjectId(final String name, final ObjectId id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public void gotDBRef(final String name, final String ns, final ObjectId id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Deprecated
    public void gotBinaryArray(final String name, final byte[] data) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public void gotUUID(final String name, final long part1, final long part2) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public void gotCode(final String name, final String code) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public void gotCodeWScope(final String name, final String code, final Object scope) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public void reset() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public Object get() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public void gotBinary(final String name, final byte type, final byte[] data) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
