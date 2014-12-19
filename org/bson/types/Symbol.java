package org.bson.types;

import java.io.*;

public class Symbol implements Serializable
{
    private static final long serialVersionUID = 1326269319883146072L;
    private final String _symbol;
    
    public Symbol(final String s) {
        super();
        this._symbol = s;
    }
    
    public String getSymbol() {
        return this._symbol;
    }
    
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        String otherSymbol;
        if (o instanceof Symbol) {
            otherSymbol = ((Symbol)o)._symbol;
        }
        else {
            if (!(o instanceof String)) {
                return false;
            }
            otherSymbol = (String)o;
        }
        if (this._symbol != null) {
            if (this._symbol.equals(otherSymbol)) {
                return true;
            }
        }
        else if (otherSymbol == null) {
            return true;
        }
        return false;
    }
    
    public int hashCode() {
        return (this._symbol != null) ? this._symbol.hashCode() : 0;
    }
    
    public String toString() {
        return this._symbol;
    }
}
