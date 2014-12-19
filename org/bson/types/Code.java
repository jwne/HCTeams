package org.bson.types;

import java.io.*;

public class Code implements Serializable
{
    private static final long serialVersionUID = 475535263314046697L;
    final String _code;
    
    public Code(final String code) {
        super();
        this._code = code;
    }
    
    public String getCode() {
        return this._code;
    }
    
    public boolean equals(final Object o) {
        if (!(o instanceof Code)) {
            return false;
        }
        final Code c = (Code)o;
        return this._code.equals(c._code);
    }
    
    public int hashCode() {
        return this._code.hashCode();
    }
    
    public String toString() {
        return this.getCode();
    }
}
