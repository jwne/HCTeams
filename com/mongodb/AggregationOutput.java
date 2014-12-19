package com.mongodb;

public class AggregationOutput
{
    @Deprecated
    protected final CommandResult _commandResult;
    @Deprecated
    protected final DBObject _cmd;
    @Deprecated
    protected final Iterable<DBObject> _resultSet;
    
    public Iterable<DBObject> results() {
        return this._resultSet;
    }
    
    @Deprecated
    public CommandResult getCommandResult() {
        return this._commandResult;
    }
    
    @Deprecated
    public DBObject getCommand() {
        return this._cmd;
    }
    
    @Deprecated
    public ServerAddress getServerUsed() {
        return this._commandResult.getServerUsed();
    }
    
    public AggregationOutput(final DBObject command, final CommandResult commandResult) {
        super();
        this._commandResult = commandResult;
        this._cmd = command;
        if (commandResult.containsField("result")) {
            this._resultSet = (Iterable<DBObject>)commandResult.get("result");
            return;
        }
        throw new IllegalArgumentException("result undefined");
    }
}
