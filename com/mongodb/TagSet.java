package com.mongodb;

import org.bson.util.annotations.*;
import org.bson.util.*;
import java.util.*;

@Immutable
public final class TagSet implements Iterable<Tag>
{
    private final List<Tag> wrapped;
    
    public TagSet() {
        super();
        this.wrapped = Collections.emptyList();
    }
    
    public TagSet(final Tag tag) {
        super();
        Assertions.notNull("tag", tag);
        this.wrapped = Collections.singletonList(tag);
    }
    
    public TagSet(final List<Tag> tagList) {
        super();
        Assertions.notNull("tagList", tagList);
        final Set<String> tagNames = new HashSet<String>();
        for (final Tag tag : tagList) {
            if (tag == null) {
                throw new IllegalArgumentException("Null tags are not allowed");
            }
            if (!tagNames.add(tag.getName())) {
                throw new IllegalArgumentException("Duplicate tag names not allowed in a tag set: " + tag.getName());
            }
        }
        this.wrapped = Collections.unmodifiableList((List<? extends Tag>)new ArrayList<Tag>(tagList));
    }
    
    public Iterator<Tag> iterator() {
        return this.wrapped.iterator();
    }
    
    public boolean containsAll(final TagSet tagSet) {
        return this.wrapped.containsAll(tagSet.wrapped);
    }
    
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final TagSet tags = (TagSet)o;
        return this.wrapped.equals(tags.wrapped);
    }
    
    public int hashCode() {
        return this.wrapped.hashCode();
    }
    
    public String toString() {
        return "TagSet{" + this.wrapped + '}';
    }
}
