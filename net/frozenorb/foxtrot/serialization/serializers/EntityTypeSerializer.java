package net.frozenorb.foxtrot.serialization.serializers;

import net.frozenorb.foxtrot.serialization.*;
import org.bukkit.entity.*;
import com.mongodb.*;

public class EntityTypeSerializer implements JSONSerializer<EntityType>
{
    @Override
    public BasicDBObject serialize(final EntityType o) {
        return new BasicDBObject("name", o.name());
    }
    
    @Override
    public EntityType deserialize(final BasicDBObject dbobj) {
        return EntityType.valueOf(dbobj.getString("name"));
    }
}
