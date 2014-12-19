package net.frozenorb.foxtrot.serialization.serializers;

import net.frozenorb.foxtrot.serialization.*;
import com.mongodb.*;
import org.bukkit.potion.*;

public class PotionEffectSerializer implements JSONSerializer<PotionEffect>
{
    @Override
    public PotionEffect deserialize(final BasicDBObject dbobj) {
        return new PotionEffect(PotionEffectType.getByName(dbobj.getString("type")), dbobj.getInt("duration"), dbobj.getInt("amplifier"));
    }
    
    @Override
    public BasicDBObject serialize(final PotionEffect o) {
        return new BasicDBObject("type", o.getType().getName()).append("duration", o.getDuration()).append("amplifier", o.getAmplifier());
    }
}
