package com.mongodb;

import org.bson.util.*;

public class WriteConcernError
{
    private final int code;
    private final String message;
    private final DBObject details;
    
    public WriteConcernError(final int code, final String message, final DBObject details) {
        super();
        this.code = code;
        this.message = Assertions.notNull("message", message);
        this.details = Assertions.notNull("details", details);
    }
    
    public int getCode() {
        return this.code;
    }
    
    public String getMessage() {
        return this.message;
    }
    
    public DBObject getDetails() {
        return this.details;
    }
    
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final WriteConcernError that = (WriteConcernError)o;
        return this.code == that.code && this.details.equals(that.details) && this.message.equals(that.message);
    }
    
    public int hashCode() {
        int result = this.code;
        result = 31 * result + this.message.hashCode();
        result = 31 * result + this.details.hashCode();
        return result;
    }
    
    public String toString() {
        return "BulkWriteConcernError{code=" + this.code + ", message='" + this.message + '\'' + ", details=" + this.details + '}';
    }
}
