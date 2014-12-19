package net.frozenorb.foxtrot.deathmessage.objects;

public abstract class PlayerDamage extends Damage
{
    private String damager;
    
    public PlayerDamage(final String damaged, final double damage, final String damager) {
        super(damaged, damage);
        this.damager = damager;
    }
    
    public String getDamager() {
        return this.damager;
    }
}
