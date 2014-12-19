package com.mongodb;

import org.bson.util.annotations.*;

@Immutable
final class MongoNamespace
{
    private static final String NAMESPACE_TEMPLATE = "%s.%s";
    public static final String COMMAND_COLLECTION_NAME = "$cmd";
    private final String databaseName;
    private final String collectionName;
    
    public MongoNamespace(final String databaseName, final String collectionName) {
        super();
        if (databaseName == null) {
            throw new IllegalArgumentException("database name can not be null");
        }
        if (collectionName == null) {
            throw new IllegalArgumentException("Collection name can not be null");
        }
        this.databaseName = databaseName;
        this.collectionName = collectionName;
    }
    
    public String getDatabaseName() {
        return this.databaseName;
    }
    
    public String getCollectionName() {
        return this.collectionName;
    }
    
    public String getFullName() {
        return this.getDatabaseName() + "." + this.getCollectionName();
    }
    
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final MongoNamespace that = (MongoNamespace)o;
        Label_0062: {
            if (this.collectionName != null) {
                if (this.collectionName.equals(that.collectionName)) {
                    break Label_0062;
                }
            }
            else if (that.collectionName == null) {
                break Label_0062;
            }
            return false;
        }
        if (this.databaseName != null) {
            if (this.databaseName.equals(that.databaseName)) {
                return true;
            }
        }
        else if (that.databaseName == null) {
            return true;
        }
        return false;
    }
    
    public String toString() {
        return this.databaseName + "." + this.collectionName;
    }
    
    public int hashCode() {
        int result = (this.databaseName != null) ? this.databaseName.hashCode() : 0;
        result = 31 * result + ((this.collectionName != null) ? this.collectionName.hashCode() : 0);
        return result;
    }
    
    public static String asNamespaceString(final String databaseName, final String collectionName) {
        return String.format("%s.%s", databaseName, collectionName);
    }
}
