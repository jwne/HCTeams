package com.mongodb;

public class WriteConcernException extends MongoException
{
    private static final long serialVersionUID = 841056799207039974L;
    private final CommandResult commandResult;
    
    public WriteConcernException(final CommandResult commandResult) {
        this(commandResult.getCode(), commandResult);
    }
    
    WriteConcernException(final int code, final CommandResult commandResult) {
        super(code, commandResult.toString());
        this.commandResult = commandResult;
    }
    
    public ServerAddress getServerAddress() {
        return this.commandResult.getServerUsed();
    }
    
    public String getErrorMessage() {
        return this.commandResult.getString("err");
    }
    
    @Deprecated
    public CommandResult getCommandResult() {
        return this.commandResult;
    }
}
