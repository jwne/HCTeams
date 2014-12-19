package com.mongodb;

import org.bson.util.*;
import java.util.*;

class ServerVersion implements Comparable<ServerVersion>
{
    private final List<Integer> versionList;
    
    public ServerVersion() {
        super();
        this.versionList = Collections.unmodifiableList((List<? extends Integer>)Arrays.asList(0, 0, 0));
    }
    
    public ServerVersion(final List<Integer> versionList) {
        super();
        Assertions.notNull("versionList", versionList);
        Assertions.isTrue("version array has three elements", versionList.size() == 3);
        this.versionList = Collections.unmodifiableList((List<? extends Integer>)new ArrayList<Integer>(versionList));
    }
    
    public ServerVersion(final int majorVersion, final int minorVersion) {
        this(Arrays.asList(majorVersion, minorVersion, 0));
    }
    
    public List<Integer> getVersionList() {
        return this.versionList;
    }
    
    public int compareTo(final ServerVersion o) {
        int retVal = 0;
        for (int i = 0; i < this.versionList.size(); ++i) {
            retVal = this.versionList.get(i).compareTo(o.versionList.get(i));
            if (retVal != 0) {
                break;
            }
        }
        return retVal;
    }
    
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final ServerVersion that = (ServerVersion)o;
        return this.versionList.equals(that.versionList);
    }
    
    public int hashCode() {
        return this.versionList.hashCode();
    }
    
    public String toString() {
        return "ServerVersion{versionList=" + this.versionList + '}';
    }
}
