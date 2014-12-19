package com.mongodb;

import org.bson.util.annotations.*;
import java.util.*;

@ThreadSafe
class MongoCredentialsStore
{
    private final Map<String, MongoCredential> credentialsMap;
    private volatile Set<String> allDatabasesWithCredentials;
    
    public MongoCredentialsStore() {
        super();
        this.credentialsMap = new HashMap<String, MongoCredential>();
        this.allDatabasesWithCredentials = new HashSet<String>();
    }
    
    public MongoCredentialsStore(final MongoCredential credentials) {
        super();
        this.credentialsMap = new HashMap<String, MongoCredential>();
        this.allDatabasesWithCredentials = new HashSet<String>();
        if (credentials == null) {
            return;
        }
        this.add(credentials);
    }
    
    public MongoCredentialsStore(final Iterable<MongoCredential> credentialsList) {
        super();
        this.credentialsMap = new HashMap<String, MongoCredential>();
        this.allDatabasesWithCredentials = new HashSet<String>();
        if (credentialsList == null) {
            return;
        }
        for (final MongoCredential cur : credentialsList) {
            this.add(cur);
        }
    }
    
    synchronized void add(final MongoCredential credentials) {
        final MongoCredential existingCredentials = this.credentialsMap.get(credentials.getSource());
        if (existingCredentials == null) {
            this.credentialsMap.put(credentials.getSource(), credentials);
            (this.allDatabasesWithCredentials = new HashSet<String>(this.allDatabasesWithCredentials)).add(credentials.getSource());
            return;
        }
        if (existingCredentials.equals(credentials)) {
            return;
        }
        throw new IllegalArgumentException("Can't add more than one credentials for the same database");
    }
    
    public Set<String> getDatabases() {
        return Collections.unmodifiableSet((Set<? extends String>)this.allDatabasesWithCredentials);
    }
    
    public synchronized MongoCredential get(final String database) {
        return this.credentialsMap.get(database);
    }
    
    public synchronized List<MongoCredential> asList() {
        return new ArrayList<MongoCredential>(this.credentialsMap.values());
    }
    
    public synchronized boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final MongoCredentialsStore that = (MongoCredentialsStore)o;
        return this.credentialsMap.equals(that.credentialsMap);
    }
    
    public synchronized int hashCode() {
        return this.credentialsMap.hashCode();
    }
    
    public String toString() {
        return "{credentials=" + this.credentialsMap + '}';
    }
}
