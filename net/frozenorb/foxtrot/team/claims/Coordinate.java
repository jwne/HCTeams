package net.frozenorb.foxtrot.team.claims;

public class Coordinate
{
    int x;
    int z;
    
    public Coordinate(final int x, final int z) {
        super();
        this.x = x;
        this.z = z;
    }
    
    public int getX() {
        return this.x;
    }
    
    public int getZ() {
        return this.z;
    }
    
    public void setX(final int x) {
        this.x = x;
    }
    
    public void setZ(final int z) {
        this.z = z;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Coordinate)) {
            return false;
        }
        final Coordinate other = (Coordinate)o;
        return other.canEqual(this) && this.getX() == other.getX() && this.getZ() == other.getZ();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof Coordinate;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getX();
        result = result * 59 + this.getZ();
        return result;
    }
    
    @Override
    public String toString() {
        return "Coordinate(x=" + this.getX() + ", z=" + this.getZ() + ")";
    }
}
