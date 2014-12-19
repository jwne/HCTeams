package com.mongodb;

import java.util.*;
import java.net.*;

public class MongoURI
{
    public static final String MONGODB_PREFIX = "mongodb://";
    private final MongoClientURI mongoClientURI;
    private final MongoOptions mongoOptions;
    
    public MongoURI(final String uri) {
        super();
        this.mongoClientURI = new MongoClientURI(uri, new MongoClientOptions.Builder().legacyDefaults());
        this.mongoOptions = new MongoOptions(this.mongoClientURI.getOptions());
    }
    
    public MongoURI(final MongoClientURI mongoClientURI) {
        super();
        this.mongoClientURI = mongoClientURI;
        this.mongoOptions = new MongoOptions(mongoClientURI.getOptions());
    }
    
    public String getUsername() {
        return this.mongoClientURI.getUsername();
    }
    
    public char[] getPassword() {
        return this.mongoClientURI.getPassword();
    }
    
    public List<String> getHosts() {
        return this.mongoClientURI.getHosts();
    }
    
    public String getDatabase() {
        return this.mongoClientURI.getDatabase();
    }
    
    public String getCollection() {
        return this.mongoClientURI.getCollection();
    }
    
    public MongoCredential getCredentials() {
        return this.mongoClientURI.getCredentials();
    }
    
    public MongoOptions getOptions() {
        return this.mongoOptions;
    }
    
    public Mongo connect() throws UnknownHostException {
        return new Mongo(this);
    }
    
    public DB connectDB() throws UnknownHostException {
        return this.connect().getDB(this.getDatabase());
    }
    
    public DB connectDB(final Mongo mongo) {
        return mongo.getDB(this.getDatabase());
    }
    
    public DBCollection connectCollection(final DB db) {
        return db.getCollection(this.getCollection());
    }
    
    public DBCollection connectCollection(final Mongo mongo) {
        return this.connectDB(mongo).getCollection(this.getCollection());
    }
    
    public String toString() {
        return this.mongoClientURI.toString();
    }
    
    MongoClientURI toClientURI() {
        return this.mongoClientURI;
    }
}
