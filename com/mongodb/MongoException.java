package com.mongodb;

import org.bson.*;
import java.io.*;

public class MongoException extends RuntimeException
{
    private static final long serialVersionUID = -4415279469780082174L;
    final int _code;
    
    public MongoException(final String msg) {
        super(msg);
        this._code = -3;
    }
    
    public MongoException(final int code, final String msg) {
        super(msg);
        this._code = code;
    }
    
    public MongoException(final String msg, final Throwable t) {
        super(msg, t);
        this._code = -4;
    }
    
    public MongoException(final int code, final String msg, final Throwable t) {
        super(msg, t);
        this._code = code;
    }
    
    public MongoException(final BSONObject o) {
        this(ServerError.getCode(o), ServerError.getMsg(o, "UNKNOWN"));
    }
    
    static MongoException parse(final BSONObject o) {
        final String s = ServerError.getMsg(o, null);
        if (s == null) {
            return null;
        }
        return new MongoException(ServerError.getCode(o), s);
    }
    
    public int getCode() {
        return this._code;
    }
    
    @Deprecated
    public static class Network extends MongoSocketException
    {
        private static final long serialVersionUID = 8364298902504372967L;
        
        public Network(final String msg, final IOException ioe) {
            super(msg, ioe);
        }
        
        public Network(final IOException ioe) {
            super(ioe);
        }
    }
    
    @Deprecated
    public static class DuplicateKey extends DuplicateKeyException
    {
        private static final long serialVersionUID = 6557680785576001838L;
        
        public DuplicateKey(final CommandResult commandResult) {
            super(commandResult);
        }
        
        DuplicateKey(final int code, final CommandResult commandResult) {
            super(code, commandResult);
        }
    }
    
    @Deprecated
    public static class CursorNotFound extends MongoCursorNotFoundException
    {
        private static final long serialVersionUID = -3759595395830412426L;
        
        public CursorNotFound(final long cursorId, final ServerAddress serverAddress) {
            super(cursorId, serverAddress);
        }
    }
}
