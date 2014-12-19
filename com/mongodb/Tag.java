package com.mongodb;

import org.bson.util.annotations.*;
import org.bson.util.*;

@Immutable
public final class Tag
{
    private final String name;
    private final String value;
    
    public Tag(final String name, final String value) {
        super();
        this.name = Assertions.notNull("name", name);
        this.value = Assertions.notNull("value", value);
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getValue() {
        return this.value;
    }
    
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final Tag that = (Tag)o;
        return this.name.equals(that.name) && this.value.equals(that.value);
    }
    
    public int hashCode() {
        int result = this.name.hashCode();
        result = 31 * result + this.value.hashCode();
        return result;
    }
    
    public String toString() {
        return "Tag{name='" + this.name + '\'' + ", value='" + this.value + '\'' + '}';
    }
}
