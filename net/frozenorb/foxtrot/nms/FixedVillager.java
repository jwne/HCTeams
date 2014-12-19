package net.frozenorb.foxtrot.nms;

import org.bukkit.craftbukkit.v1_7_R3.util.*;
import java.lang.reflect.*;
import net.minecraft.server.v1_7_R3.*;

public class FixedVillager extends EntityVillager
{
    public FixedVillager(final World w) {
        super(w);
        try {
            final Field gsa = PathfinderGoalSelector.class.getDeclaredField("b");
            gsa.setAccessible(true);
            gsa.set(this.goalSelector, new UnsafeList());
            gsa.set(this.targetSelector, new UnsafeList());
        }
        catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            ex.printStackTrace();
        }
    }
    
    public EntityAgeable createChild(final EntityAgeable entityAgeable) {
        return null;
    }
    
    public void h() {
        this.motX = 0.0;
        this.motY = 0.0;
        this.motZ = 0.0;
        super.h();
    }
    
    public void collide(final Entity arg0) {
    }
}
