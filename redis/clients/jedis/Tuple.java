package redis.clients.jedis;

import java.util.*;
import redis.clients.util.*;

public class Tuple implements Comparable<Tuple>
{
    private byte[] element;
    private Double score;
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result *= 31;
        if (null != this.element) {
            for (final byte b : this.element) {
                result = 31 * result + b;
            }
        }
        final long temp = Double.doubleToLongBits(this.score);
        result = 31 * result + (int)(temp ^ temp >>> 32);
        return result;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final Tuple other = (Tuple)obj;
        if (this.element == null) {
            if (other.element != null) {
                return false;
            }
        }
        else if (!Arrays.equals(this.element, other.element)) {
            return false;
        }
        return true;
    }
    
    @Override
    public int compareTo(final Tuple other) {
        if (Arrays.equals(this.element, other.element)) {
            return 0;
        }
        return (this.score < other.getScore()) ? -1 : 1;
    }
    
    public Tuple(final String element, final Double score) {
        super();
        this.element = SafeEncoder.encode(element);
        this.score = score;
    }
    
    public Tuple(final byte[] element, final Double score) {
        super();
        this.element = element;
        this.score = score;
    }
    
    public String getElement() {
        if (null != this.element) {
            return SafeEncoder.encode(this.element);
        }
        return null;
    }
    
    public byte[] getBinaryElement() {
        return this.element;
    }
    
    public double getScore() {
        return this.score;
    }
    
    @Override
    public String toString() {
        return '[' + Arrays.toString(this.element) + ',' + this.score + ']';
    }
}
