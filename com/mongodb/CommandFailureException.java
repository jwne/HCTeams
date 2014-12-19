package com.mongodb;

import org.bson.*;

public class CommandFailureException extends MongoException
{
    private static final long serialVersionUID = -1180715413196161037L;
    private final CommandResult commandResult;
    
    public CommandFailureException(final CommandResult commandResult) {
        super(ServerError.getCode(commandResult), commandResult.toString());
        this.commandResult = commandResult;
    }
    
    public ServerAddress getServerAddress() {
        return this.commandResult.getServerUsed();
    }
    
    public String getErrorMessage() {
        return this.commandResult.getErrorMessage();
    }
    
    @Deprecated
    public CommandResult getCommandResult() {
        return this.commandResult;
    }
}
