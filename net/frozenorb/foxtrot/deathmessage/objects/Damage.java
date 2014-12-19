package net.frozenorb.foxtrot.deathmessage.objects;

public abstract class Damage
{
    private String damaged;
    private double damage;
    private long time;
    private double healthAfter;
    
    public Damage(final String damaged, final double damage) {
        super();
        this.damaged = damaged;
        this.damage = damage;
        this.time = System.currentTimeMillis();
        this.healthAfter = -1.0;
    }
    
    public abstract String getDescription();
    
    public abstract String getDeathMessage();
    
    public String getDamaged() {
        return this.damaged;
    }
    
    public double getDamage() {
        return this.damage;
    }
    
    public long getTime() {
        return this.time;
    }
    
    public double getHealthAfter() {
        return this.healthAfter;
    }
    
    public void setHealthAfter(final double healthAfter) {
        this.healthAfter = healthAfter;
    }
}
