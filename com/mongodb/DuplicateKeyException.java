package com.mongodb;

public class DuplicateKeyException extends WriteConcernException
{
    private static final long serialVersionUID = -4415279469780082174L;
    
    DuplicateKeyException(final CommandResult commandResult) {
        super(commandResult);
    }
    
    DuplicateKeyException(final int code, final CommandResult commandResult) {
        super(code, commandResult);
    }
}
