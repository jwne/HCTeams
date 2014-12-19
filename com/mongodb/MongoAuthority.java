package com.mongodb;

import org.bson.util.annotations.*;
import java.util.*;

@Immutable
class MongoAuthority
{
    private final Type type;
    private final List<ServerAddress> serverAddresses;
    private final MongoCredentialsStore credentialsStore;
    
    public static MongoAuthority direct(final ServerAddress serverAddress) {
        return direct(serverAddress, (MongoCredential)null);
    }
    
    public static MongoAuthority direct(final ServerAddress serverAddress, final MongoCredential credentials) {
        return direct(serverAddress, new MongoCredentialsStore(credentials));
    }
    
    public static MongoAuthority direct(final ServerAddress serverAddress, final MongoCredentialsStore credentialsStore) {
        return new MongoAuthority(serverAddress, credentialsStore);
    }
    
    public static MongoAuthority dynamicSet(final List<ServerAddress> serverAddresses) {
        return dynamicSet(serverAddresses, (MongoCredential)null);
    }
    
    public static MongoAuthority dynamicSet(final List<ServerAddress> serverAddresses, final MongoCredential credentials) {
        return dynamicSet(serverAddresses, new MongoCredentialsStore(credentials));
    }
    
    public static MongoAuthority dynamicSet(final List<ServerAddress> serverAddresses, final MongoCredentialsStore credentialsStore) {
        return new MongoAuthority(serverAddresses, Type.Set, credentialsStore);
    }
    
    private MongoAuthority(final ServerAddress serverAddress, final MongoCredentialsStore credentialsStore) {
        super();
        if (serverAddress == null) {
            throw new IllegalArgumentException("serverAddress can not be null");
        }
        if (credentialsStore == null) {
            throw new IllegalArgumentException("credentialsStore can not be null");
        }
        this.serverAddresses = Arrays.asList(serverAddress);
        this.credentialsStore = credentialsStore;
        this.type = Type.Direct;
    }
    
    private MongoAuthority(final List<ServerAddress> serverAddresses, final Type type, final MongoCredentialsStore credentialsStore) {
        super();
        if (serverAddresses == null) {
            throw new IllegalArgumentException("serverAddresses can not be null");
        }
        if (credentialsStore == null) {
            throw new IllegalArgumentException("credentialsStore can not be null");
        }
        if (type == null) {
            throw new IllegalArgumentException("type can not be null");
        }
        if (type == Type.Direct) {
            throw new IllegalArgumentException("type can not be Direct with a list of server addresses");
        }
        this.type = type;
        this.serverAddresses = new ArrayList<ServerAddress>(serverAddresses);
        this.credentialsStore = credentialsStore;
    }
    
    public List<ServerAddress> getServerAddresses() {
        return (this.serverAddresses == null) ? null : Collections.unmodifiableList((List<? extends ServerAddress>)this.serverAddresses);
    }
    
    public MongoCredentialsStore getCredentialsStore() {
        return this.credentialsStore;
    }
    
    public Type getType() {
        return this.type;
    }
    
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final MongoAuthority that = (MongoAuthority)o;
        return this.credentialsStore.equals(that.credentialsStore) && this.serverAddresses.equals(that.serverAddresses) && this.type == that.type;
    }
    
    public int hashCode() {
        int result = this.credentialsStore.hashCode();
        result = 31 * result + this.serverAddresses.hashCode();
        result = 31 * result + this.type.hashCode();
        return result;
    }
    
    public String toString() {
        return "MongoAuthority{type=" + this.type + ", serverAddresses=" + this.serverAddresses + ", credentials=" + this.credentialsStore + '}';
    }
    
    enum Type
    {
        Direct, 
        Set;
    }
}
