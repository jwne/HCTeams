package com.mongodb;

import org.bson.util.*;

class ChangeEvent<T>
{
    private final T oldValue;
    private final T newValue;
    
    public ChangeEvent(final T oldValue, final T newValue) {
        super();
        this.oldValue = Assertions.notNull("oldValue", oldValue);
        this.newValue = Assertions.notNull("newValue", newValue);
    }
    
    public T getOldValue() {
        return this.oldValue;
    }
    
    public T getNewValue() {
        return this.newValue;
    }
    
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final ChangeEvent<?> that = (ChangeEvent<?>)o;
        if (!this.newValue.equals(that.newValue)) {
            return false;
        }
        if (this.oldValue != null) {
            if (this.oldValue.equals(that.oldValue)) {
                return true;
            }
        }
        else if (that.oldValue == null) {
            return true;
        }
        return false;
    }
    
    public int hashCode() {
        int result = (this.oldValue != null) ? this.oldValue.hashCode() : 0;
        result = 31 * result + this.newValue.hashCode();
        return result;
    }
    
    public String toString() {
        return "ChangeEvent{oldValue=" + this.oldValue + ", newValue=" + this.newValue + '}';
    }
}
