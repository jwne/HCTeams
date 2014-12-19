package net.frozenorb.foxtrot.serialization.serializers;

import net.frozenorb.foxtrot.serialization.*;
import com.mongodb.*;
import org.bukkit.*;

public class LocationSerializer implements JSONSerializer<Location>
{
    @Override
    public Location deserialize(final BasicDBObject dbobj) {
        return new Location(Bukkit.getWorld(dbobj.getString("world")), dbobj.getDouble("x"), dbobj.getDouble("y"), dbobj.getDouble("z"), (float)dbobj.getInt("yaw"), (float)dbobj.getInt("pitch"));
    }
    
    @Override
    public BasicDBObject serialize(final Location loc) {
        if (loc == null) {
            return new BasicDBObject("empty", true);
        }
        return new BasicDBObject("world", loc.getWorld().getName()).append("x", loc.getX()).append("y", loc.getY()).append("z", loc.getZ()).append("yaw", loc.getYaw()).append("pitch", loc.getPitch());
    }
}
