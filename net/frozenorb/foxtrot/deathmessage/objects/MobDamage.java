package net.frozenorb.foxtrot.deathmessage.objects;

import org.bukkit.entity.*;

public abstract class MobDamage extends Damage
{
    private EntityType mobType;
    
    public MobDamage(final String damaged, final double damage, final EntityType mobType) {
        super(damaged, damage);
        this.mobType = mobType;
    }
    
    public EntityType getMobType() {
        return this.mobType;
    }
}
