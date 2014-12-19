package net.frozenorb.foxtrot.serialization.serializers;

import net.frozenorb.foxtrot.serialization.*;
import org.bukkit.util.*;
import com.mongodb.*;

public class VectorSerializer implements JSONSerializer<Vector>
{
    @Override
    public Vector deserialize(final BasicDBObject dbobj) {
        return new Vector(dbobj.getDouble("x"), dbobj.getDouble("y"), dbobj.getDouble("z"));
    }
    
    @Override
    public BasicDBObject serialize(final Vector vec) {
        if (vec == null) {
            return new BasicDBObject("empty", true);
        }
        return new BasicDBObject("x", vec.getX()).append("y", vec.getY()).append("z", vec.getZ());
    }
}
