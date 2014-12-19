package com.mongodb;

public class GroupCommand
{
    String input;
    DBObject keys;
    DBObject condition;
    DBObject initial;
    String reduce;
    String finalize;
    
    public GroupCommand(final DBCollection collection, final DBObject keys, final DBObject condition, final DBObject initial, final String reduce, final String finalize) {
        super();
        this.input = collection.getName();
        this.keys = keys;
        this.condition = condition;
        this.initial = initial;
        this.reduce = reduce;
        this.finalize = finalize;
    }
    
    public DBObject toDBObject() {
        final BasicDBObject args = new BasicDBObject();
        args.put("ns", this.input);
        args.put("key", this.keys);
        args.put("cond", this.condition);
        args.put("$reduce", this.reduce);
        args.put("initial", this.initial);
        if (this.finalize != null) {
            args.put("finalize", this.finalize);
        }
        return new BasicDBObject("group", args);
    }
}
