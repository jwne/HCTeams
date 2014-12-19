package org.bson;

import java.util.*;

public interface BSONObject
{
    Object put(String p0, Object p1);
    
    void putAll(BSONObject p0);
    
    void putAll(Map p0);
    
    Object get(String p0);
    
    Map toMap();
    
    Object removeField(String p0);
    
    @Deprecated
    boolean containsKey(String p0);
    
    boolean containsField(String p0);
    
    Set<String> keySet();
}
