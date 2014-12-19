package com.mongodb;

import java.util.*;

public class CommandResult extends BasicDBObject
{
    private final ServerAddress _host;
    private static final long serialVersionUID = 1L;
    
    CommandResult(final ServerAddress serverAddress) {
        super();
        if (serverAddress == null) {
            throw new IllegalArgumentException("server address is null");
        }
        this._host = serverAddress;
        this.put("serverUsed", serverAddress.toString());
    }
    
    public boolean ok() {
        final Object okValue = this.get("ok");
        if (okValue instanceof Boolean) {
            return (boolean)okValue;
        }
        return okValue instanceof Number && ((Number)okValue).intValue() == 1;
    }
    
    public String getErrorMessage() {
        final Object errorMessage = this.get("errmsg");
        if (errorMessage == null) {
            return null;
        }
        return errorMessage.toString();
    }
    
    public MongoException getException() {
        if (!this.ok()) {
            if (this.getCode() == 50) {
                return new MongoExecutionTimeoutException(this.getCode(), this.getErrorMessage());
            }
            return new CommandFailureException(this);
        }
        else {
            if (this.hasErr()) {
                return this.getWriteException();
            }
            return null;
        }
    }
    
    private MongoException getWriteException() {
        final int code = this.getCode();
        if (code == 11000 || code == 11001 || code == 12582) {
            return new MongoException.DuplicateKey(this);
        }
        return new WriteConcernException(this);
    }
    
    int getCode() {
        int code = this.getInt("code", -1);
        if (this.get("err") != null && ((String)this.get("err")).contains("E11000 duplicate key error")) {
            return 11000;
        }
        if (code == -1 && this.get("errObjects") != null) {
            for (final BasicDBObject curErrorDocument : (List)this.get("errObjects")) {
                if (this.get("err").equals(curErrorDocument.get("err"))) {
                    code = curErrorDocument.getInt("code", -1);
                    break;
                }
            }
        }
        return code;
    }
    
    boolean hasErr() {
        final String err = this.getString("err");
        return err != null && err.length() > 0;
    }
    
    public void throwOnError() {
        if (!this.ok() || this.hasErr()) {
            throw this.getException();
        }
    }
    
    @Deprecated
    public ServerAddress getServerUsed() {
        return this._host;
    }
}
