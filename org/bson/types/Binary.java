package org.bson.types;

import java.io.*;
import java.util.*;

public class Binary implements Serializable
{
    private static final long serialVersionUID = 7902997490338209467L;
    final byte _type;
    final byte[] _data;
    
    public Binary(final byte[] data) {
        this((byte)0, data);
    }
    
    public Binary(final byte type, final byte[] data) {
        super();
        this._type = type;
        this._data = data;
    }
    
    public byte getType() {
        return this._type;
    }
    
    public byte[] getData() {
        return this._data;
    }
    
    public int length() {
        return this._data.length;
    }
    
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Binary)) {
            return false;
        }
        final Binary binary = (Binary)o;
        return this._type == binary._type && Arrays.equals(this._data, binary._data);
    }
    
    public int hashCode() {
        int result = this._type;
        result = 31 * result + ((this._data != null) ? Arrays.hashCode(this._data) : 0);
        return result;
    }
}
