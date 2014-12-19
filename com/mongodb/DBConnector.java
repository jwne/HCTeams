package com.mongodb;

@Deprecated
public interface DBConnector
{
    void requestStart();
    
    void requestDone();
    
    void requestEnsureConnection();
    
    WriteResult say(DB p0, OutMessage p1, WriteConcern p2);
    
    WriteResult say(DB p0, OutMessage p1, WriteConcern p2, ServerAddress p3);
    
    Response call(DB p0, DBCollection p1, OutMessage p2, ServerAddress p3, DBDecoder p4);
    
    Response call(DB p0, DBCollection p1, OutMessage p2, ServerAddress p3, int p4);
    
    Response call(DB p0, DBCollection p1, OutMessage p2, ServerAddress p3, int p4, ReadPreference p5, DBDecoder p6);
    
    boolean isOpen();
    
    CommandResult authenticate(MongoCredential p0);
}
