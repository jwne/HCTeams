package org.bson.types;

import java.io.*;

public class MaxKey implements Serializable
{
    private static final long serialVersionUID = 5123414776151687185L;
    
    public boolean equals(final Object o) {
        return o instanceof MaxKey;
    }
    
    public int hashCode() {
        return 0;
    }
    
    public String toString() {
        return "MaxKey";
    }
}
