package com.mongodb;

import java.net.*;
import java.util.*;

public class MongoClient extends Mongo
{
    private final MongoClientOptions options;
    
    public MongoClient() throws UnknownHostException {
        this(new ServerAddress());
    }
    
    public MongoClient(final String host) throws UnknownHostException {
        this(new ServerAddress(host));
    }
    
    public MongoClient(final String host, final MongoClientOptions options) throws UnknownHostException {
        this(new ServerAddress(host), options);
    }
    
    public MongoClient(final String host, final int port) throws UnknownHostException {
        this(new ServerAddress(host, port));
    }
    
    public MongoClient(final ServerAddress addr) {
        this(addr, new MongoClientOptions.Builder().build());
    }
    
    public MongoClient(final ServerAddress addr, final List<MongoCredential> credentialsList) {
        this(addr, credentialsList, new MongoClientOptions.Builder().build());
    }
    
    public MongoClient(final ServerAddress addr, final MongoClientOptions options) {
        this(addr, null, options);
    }
    
    public MongoClient(final ServerAddress addr, final List<MongoCredential> credentialsList, final MongoClientOptions options) {
        super(MongoAuthority.direct(addr, new MongoCredentialsStore(credentialsList)), new MongoOptions(options));
        this.options = options;
    }
    
    public MongoClient(final List<ServerAddress> seeds) {
        this(seeds, null, new MongoClientOptions.Builder().build());
    }
    
    public MongoClient(final List<ServerAddress> seeds, final List<MongoCredential> credentialsList) {
        this(seeds, credentialsList, new MongoClientOptions.Builder().build());
    }
    
    public MongoClient(final List<ServerAddress> seeds, final MongoClientOptions options) {
        this(seeds, null, options);
    }
    
    public MongoClient(final List<ServerAddress> seeds, final List<MongoCredential> credentialsList, final MongoClientOptions options) {
        super(MongoAuthority.dynamicSet(seeds, new MongoCredentialsStore(credentialsList)), new MongoOptions(options));
        this.options = options;
    }
    
    public MongoClient(final MongoClientURI uri) throws UnknownHostException {
        super(new MongoURI(uri));
        this.options = uri.getOptions();
    }
    
    public List<MongoCredential> getCredentialsList() {
        return this.getAuthority().getCredentialsStore().asList();
    }
    
    public MongoClientOptions getMongoClientOptions() {
        return this.options;
    }
}
