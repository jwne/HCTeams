package com.mongodb;

import org.bson.util.annotations.*;
import java.util.*;

@Immutable
public final class MongoCredential
{
    public static final String MONGODB_CR_MECHANISM = "MONGODB-CR";
    public static final String GSSAPI_MECHANISM = "GSSAPI";
    public static final String PLAIN_MECHANISM = "PLAIN";
    public static final String SCRAM_SHA_1_MECHANISM = "SCRAM-SHA-1";
    public static final String MONGODB_X509_MECHANISM = "MONGODB-X509";
    private final String mechanism;
    private final String userName;
    private final String source;
    private final char[] password;
    private final Map<String, Object> mechanismProperties;
    
    public static MongoCredential createCredential(final String userName, final String database, final char[] password) {
        return new MongoCredential(null, userName, database, password);
    }
    
    public static MongoCredential createScramSha1Credential(final String userName, final String source, final char[] password) {
        return new MongoCredential("SCRAM-SHA-1", userName, source, password);
    }
    
    public static MongoCredential createMongoCRCredential(final String userName, final String database, final char[] password) {
        return new MongoCredential("MONGODB-CR", userName, database, password);
    }
    
    public static MongoCredential createGSSAPICredential(final String userName) {
        return new MongoCredential("GSSAPI", userName, "$external", null);
    }
    
    public static MongoCredential createMongoX509Credential(final String userName) {
        return new MongoCredential("MONGODB-X509", userName, "$external", null);
    }
    
    public static MongoCredential createPlainCredential(final String userName, final String source, final char[] password) {
        return new MongoCredential("PLAIN", userName, source, password);
    }
    
    public <T> MongoCredential withMechanismProperty(final String key, final T value) {
        return new MongoCredential(this, key, (T)value);
    }
    
    MongoCredential(final String mechanism, final String userName, final String source, final char[] password) {
        super();
        if (userName == null) {
            throw new IllegalArgumentException("username can not be null");
        }
        if (mechanism == null && password == null) {
            throw new IllegalArgumentException("Password can not be null when the authentication mechanism is unspecified");
        }
        if (("MONGODB-CR".equals(mechanism) || "SCRAM-SHA-1".equals(mechanism)) && password == null) {
            throw new IllegalArgumentException("Password can not be null for the " + mechanism + " authentication mechanism");
        }
        if ("GSSAPI".equals(mechanism) && password != null) {
            throw new IllegalArgumentException("Password must be null for the GSSAPI authentication mechanism");
        }
        this.mechanism = mechanism;
        this.userName = userName;
        this.source = source;
        this.password = (char[])((password != null) ? ((char[])password.clone()) : null);
        this.mechanismProperties = Collections.emptyMap();
    }
    
    MongoCredential(final MongoCredential from, final String mechanismPropertyKey, final T mechanismPropertyValue) {
        super();
        this.mechanism = from.mechanism;
        this.userName = from.userName;
        this.source = from.source;
        this.password = from.password;
        (this.mechanismProperties = new HashMap<String, Object>(from.mechanismProperties)).put(mechanismPropertyKey.toLowerCase(), mechanismPropertyValue);
    }
    
    public String getMechanism() {
        return this.mechanism;
    }
    
    public String getUserName() {
        return this.userName;
    }
    
    public String getSource() {
        return this.source;
    }
    
    public char[] getPassword() {
        if (this.password == null) {
            return null;
        }
        return this.password.clone();
    }
    
    public <T> T getMechanismProperty(final String key, final T defaultValue) {
        final T value = (T)this.mechanismProperties.get(key.toLowerCase());
        return (value == null) ? defaultValue : value;
    }
    
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final MongoCredential that = (MongoCredential)o;
        if (this.mechanism != null) {
            if (this.mechanism.equals(that.mechanism)) {
                return this.mechanismProperties.equals(that.mechanismProperties) && Arrays.equals(this.password, that.password) && this.source.equals(that.source) && this.userName.equals(that.userName);
            }
        }
        else if (that.mechanism == null) {
            return this.mechanismProperties.equals(that.mechanismProperties) && Arrays.equals(this.password, that.password) && this.source.equals(that.source) && this.userName.equals(that.userName);
        }
        return false;
    }
    
    public int hashCode() {
        int result = (this.mechanism != null) ? this.mechanism.hashCode() : 0;
        result = 31 * result + this.userName.hashCode();
        result = 31 * result + this.source.hashCode();
        result = 31 * result + ((this.password != null) ? Arrays.hashCode(this.password) : 0);
        result = 31 * result + this.mechanismProperties.hashCode();
        return result;
    }
    
    public String toString() {
        return "MongoCredential{mechanism='" + this.mechanism + '\'' + ", userName='" + this.userName + '\'' + ", source='" + this.source + '\'' + ", password=<hidden>" + ", mechanismProperties=" + this.mechanismProperties + '}';
    }
}
