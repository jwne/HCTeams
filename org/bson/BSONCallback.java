package org.bson;

import org.bson.types.*;

public interface BSONCallback
{
    void objectStart();
    
    void objectStart(String p0);
    
    @Deprecated
    void objectStart(boolean p0);
    
    Object objectDone();
    
    void reset();
    
    Object get();
    
    BSONCallback createBSONCallback();
    
    void arrayStart();
    
    void arrayStart(String p0);
    
    Object arrayDone();
    
    void gotNull(String p0);
    
    void gotUndefined(String p0);
    
    void gotMinKey(String p0);
    
    void gotMaxKey(String p0);
    
    void gotBoolean(String p0, boolean p1);
    
    void gotDouble(String p0, double p1);
    
    void gotInt(String p0, int p1);
    
    void gotLong(String p0, long p1);
    
    void gotDate(String p0, long p1);
    
    void gotString(String p0, String p1);
    
    void gotSymbol(String p0, String p1);
    
    void gotRegex(String p0, String p1, String p2);
    
    void gotTimestamp(String p0, int p1, int p2);
    
    void gotObjectId(String p0, ObjectId p1);
    
    void gotDBRef(String p0, String p1, ObjectId p2);
    
    @Deprecated
    void gotBinaryArray(String p0, byte[] p1);
    
    void gotBinary(String p0, byte p1, byte[] p2);
    
    void gotUUID(String p0, long p1, long p2);
    
    void gotCode(String p0, String p1);
    
    void gotCodeWScope(String p0, String p1, Object p2);
}
