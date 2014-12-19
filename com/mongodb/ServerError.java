package com.mongodb;

import org.bson.*;

@Deprecated
public class ServerError
{
    final String _err;
    final int _code;
    
    ServerError(final DBObject o) {
        super();
        this._err = getMsg(o, null);
        if (this._err == null) {
            throw new IllegalArgumentException("need to have $err");
        }
        this._code = getCode(o);
    }
    
    static String getMsg(final BSONObject o, final String def) {
        Object e = o.get("$err");
        if (e == null) {
            e = o.get("err");
        }
        if (e == null) {
            e = o.get("errmsg");
        }
        if (e == null) {
            return def;
        }
        return e.toString();
    }
    
    static int getCode(final BSONObject o) {
        Object c = o.get("code");
        if (c == null) {
            c = o.get("$code");
        }
        if (c == null) {
            c = o.get("assertionCode");
        }
        if (c == null) {
            return -5;
        }
        return ((Number)c).intValue();
    }
    
    public String getError() {
        return this._err;
    }
    
    public int getCode() {
        return this._code;
    }
    
    public boolean isNotMasterError() {
        switch (this._code) {
            case 10054:
            case 10056:
            case 10058:
            case 10107:
            case 13435:
            case 13436: {
                return true;
            }
            default: {
                return this._err.startsWith("not master");
            }
        }
    }
    
    public String toString() {
        if (this._code > 0) {
            return this._code + " " + this._err;
        }
        return this._err;
    }
}
