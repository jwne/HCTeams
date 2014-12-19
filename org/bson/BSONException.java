package org.bson;

public class BSONException extends RuntimeException
{
    private static final long serialVersionUID = -4415279469780082174L;
    private Integer _errorCode;
    
    public BSONException(final String msg) {
        super(msg);
        this._errorCode = null;
    }
    
    public BSONException(final int errorCode, final String msg) {
        super(msg);
        this._errorCode = null;
        this._errorCode = errorCode;
    }
    
    public BSONException(final String msg, final Throwable t) {
        super(msg, t);
        this._errorCode = null;
    }
    
    public BSONException(final int errorCode, final String msg, final Throwable t) {
        super(msg, t);
        this._errorCode = null;
        this._errorCode = errorCode;
    }
    
    public Integer getErrorCode() {
        return this._errorCode;
    }
    
    public boolean hasErrorCode() {
        return this._errorCode != null;
    }
}
