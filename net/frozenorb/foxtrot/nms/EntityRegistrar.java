package net.frozenorb.foxtrot.nms;

import net.minecraft.server.v1_7_R3.*;
import net.frozenorb.foxtrot.util.*;

public class EntityRegistrar
{
    public static void registerCustomEntities() throws Exception {
        registerCustomEntity(FixedVillager.class, "Villager", 120);
    }
    
    public static void registerCustomEntity(final Class entityClass, final String name, final int id) throws Exception {
        ReflectionUtils.putInPrivateStaticMap(EntityTypes.class, "d", entityClass, name);
        ReflectionUtils.putInPrivateStaticMap(EntityTypes.class, "f", entityClass, id);
    }
}
