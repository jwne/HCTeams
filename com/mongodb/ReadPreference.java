package com.mongodb;

import java.util.*;

public abstract class ReadPreference
{
    @Deprecated
    public static final ReadPreference PRIMARY;
    @Deprecated
    public static final ReadPreference SECONDARY;
    private static final ReadPreference _PRIMARY;
    private static final ReadPreference _SECONDARY;
    private static final ReadPreference _SECONDARY_PREFERRED;
    private static final ReadPreference _PRIMARY_PREFERRED;
    private static final ReadPreference _NEAREST;
    
    public abstract boolean isSlaveOk();
    
    @Deprecated
    public abstract DBObject toDBObject();
    
    public abstract String getName();
    
    abstract List<ServerDescription> choose(final ClusterDescription p0);
    
    public static ReadPreference primary() {
        return ReadPreference._PRIMARY;
    }
    
    public static ReadPreference primaryPreferred() {
        return ReadPreference._PRIMARY_PREFERRED;
    }
    
    public static ReadPreference secondary() {
        return ReadPreference._SECONDARY;
    }
    
    public static ReadPreference secondaryPreferred() {
        return ReadPreference._SECONDARY_PREFERRED;
    }
    
    public static ReadPreference nearest() {
        return ReadPreference._NEAREST;
    }
    
    public static TaggableReadPreference primaryPreferred(final TagSet tagSet) {
        return primaryPreferred(Arrays.asList(tagSet));
    }
    
    public static TaggableReadPreference secondary(final TagSet tagSet) {
        return secondary(Arrays.asList(tagSet));
    }
    
    public static TaggableReadPreference secondaryPreferred(final TagSet tagSet) {
        return secondaryPreferred(Arrays.asList(tagSet));
    }
    
    public static TaggableReadPreference nearest(final TagSet tagSet) {
        return nearest(Arrays.asList(tagSet));
    }
    
    public static TaggableReadPreference primaryPreferred(final List<TagSet> tagSetList) {
        return new TaggableReadPreference.PrimaryPreferredReadPreference(tagSetList);
    }
    
    public static TaggableReadPreference secondary(final List<TagSet> tagSetList) {
        return new TaggableReadPreference.SecondaryReadPreference(tagSetList);
    }
    
    public static TaggableReadPreference secondaryPreferred(final List<TagSet> tagSetList) {
        return new TaggableReadPreference.SecondaryPreferredReadPreference(tagSetList);
    }
    
    public static TaggableReadPreference nearest(final List<TagSet> tagSetList) {
        return new TaggableReadPreference.NearestReadPreference(tagSetList);
    }
    
    @Deprecated
    public static TaggableReadPreference primaryPreferred(final DBObject firstTagSet, final DBObject... remainingTagSets) {
        return new TaggableReadPreference.PrimaryPreferredReadPreference(toTagsList(firstTagSet, remainingTagSets));
    }
    
    @Deprecated
    public static TaggableReadPreference secondary(final DBObject firstTagSet, final DBObject... remainingTagSets) {
        return new TaggableReadPreference.SecondaryReadPreference(toTagsList(firstTagSet, remainingTagSets));
    }
    
    @Deprecated
    public static TaggableReadPreference secondaryPreferred(final DBObject firstTagSet, final DBObject... remainingTagSets) {
        return new TaggableReadPreference.SecondaryPreferredReadPreference(toTagsList(firstTagSet, remainingTagSets));
    }
    
    @Deprecated
    public static TaggableReadPreference nearest(final DBObject firstTagSet, final DBObject... remainingTagSets) {
        return new TaggableReadPreference.NearestReadPreference(toTagsList(firstTagSet, remainingTagSets));
    }
    
    public static ReadPreference valueOf(String name) {
        if (name == null) {
            throw new IllegalArgumentException();
        }
        name = name.toLowerCase();
        if (name.equals(ReadPreference._PRIMARY.getName().toLowerCase())) {
            return ReadPreference._PRIMARY;
        }
        if (name.equals(ReadPreference._SECONDARY.getName().toLowerCase())) {
            return ReadPreference._SECONDARY;
        }
        if (name.equals(ReadPreference._SECONDARY_PREFERRED.getName().toLowerCase())) {
            return ReadPreference._SECONDARY_PREFERRED;
        }
        if (name.equals(ReadPreference._PRIMARY_PREFERRED.getName().toLowerCase())) {
            return ReadPreference._PRIMARY_PREFERRED;
        }
        if (name.equals(ReadPreference._NEAREST.getName().toLowerCase())) {
            return ReadPreference._NEAREST;
        }
        throw new IllegalArgumentException("No match for read preference of " + name);
    }
    
    public static TaggableReadPreference valueOf(String name, final List<TagSet> tagSetList) {
        if (name == null) {
            throw new IllegalArgumentException();
        }
        name = name.toLowerCase();
        if (name.equals(ReadPreference.PRIMARY.getName().toLowerCase())) {
            throw new IllegalArgumentException("Primary read preference can not also specify tag sets");
        }
        if (name.equals(ReadPreference._SECONDARY.getName().toLowerCase())) {
            return secondary(tagSetList);
        }
        if (name.equals(ReadPreference._SECONDARY_PREFERRED.getName().toLowerCase())) {
            return secondaryPreferred(tagSetList);
        }
        if (name.equals(ReadPreference._PRIMARY_PREFERRED.getName().toLowerCase())) {
            return primaryPreferred(tagSetList);
        }
        if (name.equals(ReadPreference._NEAREST.getName().toLowerCase())) {
            return nearest(tagSetList);
        }
        throw new IllegalArgumentException("No match for read preference of " + name);
    }
    
    @Deprecated
    public static TaggableReadPreference valueOf(final String name, final DBObject firstTagSet, final DBObject... remainingTagSets) {
        return valueOf(name, toTagsList(firstTagSet, remainingTagSets));
    }
    
    @Deprecated
    public static ReadPreference withTags(final Map<String, String> tags) {
        return new TaggedReadPreference(tags);
    }
    
    @Deprecated
    public static ReadPreference withTags(final DBObject tags) {
        return new TaggedReadPreference(tags);
    }
    
    private static List<TagSet> toTagsList(final DBObject firstTagSet, final DBObject... remainingTagSets) {
        final List<TagSet> tagsList = new ArrayList<TagSet>(remainingTagSets.length + 1);
        tagsList.add(toTags(firstTagSet));
        for (final DBObject cur : remainingTagSets) {
            tagsList.add(toTags(cur));
        }
        return tagsList;
    }
    
    private static TagSet toTags(final DBObject tagsDocument) {
        final List<Tag> tagList = new ArrayList<Tag>();
        for (final String key : tagsDocument.keySet()) {
            tagList.add(new Tag(key, tagsDocument.get(key).toString()));
        }
        return new TagSet(tagList);
    }
    
    static {
        _PRIMARY = new PrimaryReadPreference();
        _SECONDARY = new TaggableReadPreference.SecondaryReadPreference();
        _SECONDARY_PREFERRED = new TaggableReadPreference.SecondaryPreferredReadPreference();
        _PRIMARY_PREFERRED = new TaggableReadPreference.PrimaryPreferredReadPreference();
        _NEAREST = new TaggableReadPreference.NearestReadPreference();
        PRIMARY = ReadPreference._PRIMARY;
        SECONDARY = ReadPreference._SECONDARY_PREFERRED;
    }
    
    private static class PrimaryReadPreference extends ReadPreference
    {
        public boolean isSlaveOk() {
            return false;
        }
        
        public String toString() {
            return this.getName();
        }
        
        public boolean equals(final Object o) {
            return o != null && this.getClass() == o.getClass();
        }
        
        public int hashCode() {
            return this.getName().hashCode();
        }
        
        List<ServerDescription> choose(final ClusterDescription clusterDescription) {
            return clusterDescription.getPrimaries();
        }
        
        public DBObject toDBObject() {
            return new BasicDBObject("mode", this.getName());
        }
        
        public String getName() {
            return "primary";
        }
    }
    
    @Deprecated
    public static class TaggedReadPreference extends ReadPreference
    {
        private final DBObject _tags;
        private final ReadPreference _pref;
        
        public TaggedReadPreference(final Map<String, String> tags) {
            super();
            if (tags == null || tags.size() == 0) {
                throw new IllegalArgumentException("tags can not be null or empty");
            }
            this._tags = new BasicDBObject(tags);
            final List<DBObject> maps = splitMapIntoMultipleMaps(this._tags);
            this._pref = new TaggableReadPreference.SecondaryReadPreference(toTagsList(maps.get(0), this.getRemainingMaps(maps)));
        }
        
        public TaggedReadPreference(final DBObject tags) {
            super();
            if (tags == null || tags.keySet().size() == 0) {
                throw new IllegalArgumentException("tags can not be null or empty");
            }
            this._tags = tags;
            final List<DBObject> maps = splitMapIntoMultipleMaps(this._tags);
            this._pref = new TaggableReadPreference.SecondaryReadPreference(toTagsList(maps.get(0), this.getRemainingMaps(maps)));
        }
        
        public DBObject getTags() {
            final DBObject tags = new BasicDBObject();
            for (final String key : this._tags.keySet()) {
                tags.put(key, this._tags.get(key));
            }
            return tags;
        }
        
        public boolean isSlaveOk() {
            return this._pref.isSlaveOk();
        }
        
        List<ServerDescription> choose(final ClusterDescription clusterDescription) {
            return this._pref.choose(clusterDescription);
        }
        
        public DBObject toDBObject() {
            return this._pref.toDBObject();
        }
        
        public String getName() {
            return this._pref.getName();
        }
        
        private static List<DBObject> splitMapIntoMultipleMaps(final DBObject tags) {
            final List<DBObject> tagList = new ArrayList<DBObject>(tags.keySet().size());
            for (final String key : tags.keySet()) {
                tagList.add(new BasicDBObject(key, tags.get(key).toString()));
            }
            return tagList;
        }
        
        private DBObject[] getRemainingMaps(final List<DBObject> maps) {
            if (maps.size() <= 1) {
                return new DBObject[0];
            }
            return maps.subList(1, maps.size() - 1).toArray(new DBObject[maps.size() - 1]);
        }
    }
}
