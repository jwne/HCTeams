package org.bson.types;

import org.bson.*;

public class CodeWScope extends Code
{
    private static final long serialVersionUID = -6284832275113680002L;
    final BSONObject _scope;
    
    public CodeWScope(final String code, final BSONObject scope) {
        super(code);
        this._scope = scope;
    }
    
    public BSONObject getScope() {
        return this._scope;
    }
    
    public boolean equals(final Object o) {
        if (!(o instanceof CodeWScope)) {
            return false;
        }
        final CodeWScope c = (CodeWScope)o;
        return this._code.equals(c._code) && this._scope.equals(c._scope);
    }
    
    public int hashCode() {
        return this._code.hashCode() ^ this._scope.hashCode();
    }
}
