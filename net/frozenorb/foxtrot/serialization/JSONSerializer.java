package net.frozenorb.foxtrot.serialization;

import com.mongodb.*;

public interface JSONSerializer<T>
{
    BasicDBObject serialize(T p0);
    
    T deserialize(BasicDBObject p0);
}
