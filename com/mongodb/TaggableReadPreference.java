package com.mongodb;

import java.util.*;

public abstract class TaggableReadPreference extends ReadPreference
{
    final List<TagSet> tagSetList;
    
    TaggableReadPreference() {
        super();
        this.tagSetList = Collections.emptyList();
    }
    
    TaggableReadPreference(final List<TagSet> tagSetList) {
        super();
        this.tagSetList = Collections.unmodifiableList((List<? extends TagSet>)new ArrayList<TagSet>(tagSetList));
    }
    
    public boolean isSlaveOk() {
        return true;
    }
    
    @Deprecated
    public DBObject toDBObject() {
        final DBObject readPrefObject = new BasicDBObject("mode", this.getName());
        if (!this.tagSetList.isEmpty()) {
            final List<DBObject> tagSetDocumentList = new ArrayList<DBObject>();
            for (final TagSet tagSet : this.tagSetList) {
                final DBObject tagSetDocument = new BasicDBObject();
                for (final Tag tag : tagSet) {
                    tagSetDocument.put(tag.getName(), tag.getValue());
                }
                tagSetDocumentList.add(tagSetDocument);
            }
            readPrefObject.put("tags", tagSetDocumentList);
        }
        return readPrefObject;
    }
    
    public List<TagSet> getTagSetList() {
        return this.tagSetList;
    }
    
    @Deprecated
    public List<DBObject> getTagSets() {
        final List<DBObject> tags = new ArrayList<DBObject>();
        for (final TagSet curTags : this.tagSetList) {
            final BasicDBObject tagsDocument = new BasicDBObject();
            for (final Tag curTag : curTags) {
                tagsDocument.put(curTag.getName(), curTag.getValue());
            }
            tags.add(tagsDocument);
        }
        return tags;
    }
    
    public String toString() {
        return this.getName() + this.printTags();
    }
    
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final TaggableReadPreference that = (TaggableReadPreference)o;
        return this.tagSetList.equals(that.tagSetList);
    }
    
    public int hashCode() {
        int result = this.tagSetList.hashCode();
        result = 31 * result + this.getName().hashCode();
        return result;
    }
    
    List<ServerDescription> choose(final ClusterDescription clusterDescription) {
        if (this.tagSetList.isEmpty()) {
            return this.getServers(clusterDescription);
        }
        for (final TagSet tags : this.tagSetList) {
            final List<ServerDescription> taggedServers = this.getServersForTags(clusterDescription, tags);
            if (!taggedServers.isEmpty()) {
                return taggedServers;
            }
        }
        return Collections.emptyList();
    }
    
    abstract List<ServerDescription> getServers(final ClusterDescription p0);
    
    abstract List<ServerDescription> getServersForTags(final ClusterDescription p0, final TagSet p1);
    
    String printTags() {
        return this.tagSetList.isEmpty() ? "" : (" : " + this.tagSetList);
    }
    
    static class SecondaryReadPreference extends TaggableReadPreference
    {
        SecondaryReadPreference() {
            super();
        }
        
        SecondaryReadPreference(final List<TagSet> tagsList) {
            super(tagsList);
        }
        
        public String getName() {
            return "secondary";
        }
        
        List<ServerDescription> getServers(final ClusterDescription clusterDescription) {
            return clusterDescription.getSecondaries();
        }
        
        List<ServerDescription> getServersForTags(final ClusterDescription clusterDescription, final TagSet tags) {
            return clusterDescription.getSecondaries(tags);
        }
    }
    
    static class SecondaryPreferredReadPreference extends SecondaryReadPreference
    {
        SecondaryPreferredReadPreference() {
            super();
        }
        
        SecondaryPreferredReadPreference(final List<TagSet> tagsList) {
            super(tagsList);
        }
        
        public String getName() {
            return "secondaryPreferred";
        }
        
        List<ServerDescription> choose(final ClusterDescription clusterDescription) {
            final List<ServerDescription> servers = super.choose(clusterDescription);
            return servers.isEmpty() ? clusterDescription.getPrimaries() : servers;
        }
    }
    
    static class NearestReadPreference extends TaggableReadPreference
    {
        NearestReadPreference() {
            super();
        }
        
        NearestReadPreference(final List<TagSet> tagsList) {
            super(tagsList);
        }
        
        public String getName() {
            return "nearest";
        }
        
        List<ServerDescription> getServers(final ClusterDescription clusterDescription) {
            return clusterDescription.getAnyPrimaryOrSecondary();
        }
        
        List<ServerDescription> getServersForTags(final ClusterDescription clusterDescription, final TagSet tags) {
            return clusterDescription.getAnyPrimaryOrSecondary(tags);
        }
    }
    
    static class PrimaryPreferredReadPreference extends SecondaryReadPreference
    {
        PrimaryPreferredReadPreference() {
            super();
        }
        
        PrimaryPreferredReadPreference(final List<TagSet> tagsList) {
            super(tagsList);
        }
        
        public String getName() {
            return "primaryPreferred";
        }
        
        List<ServerDescription> choose(final ClusterDescription clusterDescription) {
            final List<ServerDescription> servers = clusterDescription.getPrimaries();
            return servers.isEmpty() ? super.choose(clusterDescription) : servers;
        }
    }
}
