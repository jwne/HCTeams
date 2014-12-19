package net.frozenorb.foxtrot.serialization;

import com.mongodb.*;

public interface JSONSerializable extends Serializable<BasicDBObject>
{
    BasicDBObject serialize();
    
    void deserialize(BasicDBObject p0);
}
