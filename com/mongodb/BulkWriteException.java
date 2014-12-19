package com.mongodb;

import java.util.*;

public class BulkWriteException extends MongoException
{
    private static final long serialVersionUID = -1505950263354313025L;
    private final BulkWriteResult writeResult;
    private final List<BulkWriteError> writeErrors;
    private final ServerAddress serverAddress;
    private final WriteConcernError writeConcernError;
    
    BulkWriteException(final BulkWriteResult writeResult, final List<BulkWriteError> writeErrors, final WriteConcernError writeConcernError, final ServerAddress serverAddress) {
        super("Bulk write operation error on server " + serverAddress + ". " + (writeErrors.isEmpty() ? "" : ("Write errors: " + writeErrors + ". ")) + ((writeConcernError == null) ? "" : ("Write concern error: " + writeConcernError + ". ")));
        this.writeResult = writeResult;
        this.writeErrors = writeErrors;
        this.writeConcernError = writeConcernError;
        this.serverAddress = serverAddress;
    }
    
    public BulkWriteResult getWriteResult() {
        return this.writeResult;
    }
    
    public List<BulkWriteError> getWriteErrors() {
        return this.writeErrors;
    }
    
    public WriteConcernError getWriteConcernError() {
        return this.writeConcernError;
    }
    
    public ServerAddress getServerAddress() {
        return this.serverAddress;
    }
    
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final BulkWriteException that = (BulkWriteException)o;
        if (!this.writeErrors.equals(that.writeErrors)) {
            return false;
        }
        if (!this.serverAddress.equals(that.serverAddress)) {
            return false;
        }
        if (this.writeConcernError != null) {
            if (this.writeConcernError.equals(that.writeConcernError)) {
                return this.writeResult.equals(that.writeResult);
            }
        }
        else if (that.writeConcernError == null) {
            return this.writeResult.equals(that.writeResult);
        }
        return false;
    }
    
    public int hashCode() {
        int result = this.writeResult.hashCode();
        result = 31 * result + this.writeErrors.hashCode();
        result = 31 * result + this.serverAddress.hashCode();
        result = 31 * result + ((this.writeConcernError != null) ? this.writeConcernError.hashCode() : 0);
        return result;
    }
}
